/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.utils.WebUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class APIUtils {
    private static final Base64.Encoder b64encoder = Base64.getEncoder().withoutPadding();

    private static WebClient webClient = WebConfig.getWebClient();

    private static final String domain = "https://localhost:8079";
    private static final String changeUsernameEndoint = domain + "/change";
    private static final String loginEndoint = domain + "/login";
    private static final String signUpEndpoint = domain + "/signup";
    private static final String userInfoEndpoint = domain + "/userinfo";
    private static final String updateScoreEndpoint = domain + "/score";
    private static final String deleteUserEndpoint = domain + "/delete";
    private static final String changePasswordUserEndpoint = domain + "/changePassword";
    
    static String encode(byte[] hash, Argon2Parameters parameters) throws IllegalArgumentException {
		StringBuilder stringBuilder = new StringBuilder();
		switch (parameters.getType()) {
		case Argon2Parameters.ARGON2_d:
			stringBuilder.append("$argon2d");
			break;
		case Argon2Parameters.ARGON2_i:
			stringBuilder.append("$argon2i");
			break;
		case Argon2Parameters.ARGON2_id:
			stringBuilder.append("$argon2id");
			break;
		default:
			throw new IllegalArgumentException("Invalid algorithm type: " + parameters.getType());
		}
		stringBuilder.append("$v=").append(parameters.getVersion()).append("$m=").append(parameters.getMemory())
				.append(",t=").append(parameters.getIterations()).append(",p=").append(parameters.getLanes());
		if (parameters.getSalt() != null) {
			stringBuilder.append("$").append(b64encoder.encodeToString(parameters.getSalt()));
		}
		stringBuilder.append("$").append(b64encoder.encodeToString(hash));
		return stringBuilder.toString();
    }

    public static String hashPassword(String rawPassword) {

	int iterations = 2;
	int memLimit = 15 * 1024;
	int hashLength = 64;
	int parallelism = 1;

	Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
		.withIterations(iterations)
		.withMemoryAsKB(memLimit)
		.withParallelism(parallelism)
		.withSalt("123456789".getBytes());

	Argon2BytesGenerator generate = new Argon2BytesGenerator();
	generate.init(builder.build());
	byte[] result = new byte[hashLength];
	generate.generateBytes(rawPassword.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
	
	String output = encode(result, builder.build());
	
	return output;
    }

    public static String login(String username, String password) throws WebClientResponseException,
	    NullPointerException, JsonSyntaxException {
	String hash = hashPassword(password);

	// create a map of values
	JsonObject bodyJson = new JsonObject();

	bodyJson.addProperty("username", username);
	bodyJson.addProperty("hash", hash);

	// create requests
	ResponseSpec request = null;
	request = webClient.post().uri(loginEndoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve();

	if (request == null) {
	    throw new NullPointerException("Request entity is null");
	}

	// send request
	ResponseEntity<String> response;
	try {
	    response = request.toEntity(String.class).block();
	} catch (WebClientResponseException e) {
	    // handle error with unsuccessful acess code
	    throw e;
	}

	// handle null response
	if (response == null) {
	    throw new NullPointerException("Response entity is null");
	}

	// parse response body
	JsonObject responseBody = (JsonObject) JsonParser.parseString(response.getBody());

	// return id for use
	return responseBody.getAsJsonPrimitive("token").getAsString();
    }

    public static String SignUp(String username, String password) throws WebClientResponseException,
	    NullPointerException, JsonSyntaxException {

	// ecnrypted password without salt
	// this is just to add another layer of encryption along with TLS (which is not implemented yet but wait)
	String hash = hashPassword(password);

	// create the key value pairs for your request body
	JsonObject bodyJson = new JsonObject();

	bodyJson.addProperty("username", username);
	bodyJson.addProperty("hash", hash);

	// create and send request to endpoint
	ResponseSpec request = webClient.post().uri(signUpEndpoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString()).retrieve();

	ResponseEntity<String> response = request.toEntity(String.class).block();

	// throw exception if null response
	if (response == null) {
	    throw new NullPointerException("Response entity is null");
	}

	// if successful so far, extract body from response
	JsonObject responseBody = (JsonObject) JsonParser.parseString(response.getBody());

	// check status values in body if the server has indicated a problem , raise exception with info
	return responseBody.getAsJsonPrimitive("token").getAsString();
    }

    public static void changeUsername(String token, String newUsername) throws JsonSyntaxException, WebClientResponseException, NullPointerException {

	// ecnrypted password without salt
	// this is just to add another layer of encryption along with TLS (which is not implemented yet but wait)
	// create the key value pairs for your request body
	JsonObject bodyJson = new JsonObject();

	bodyJson.addProperty("token", token);
	bodyJson.addProperty("new_username", newUsername);

	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().put().uri(changeUsernameEndoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve();

	ResponseEntity<String> response = request.toEntity(String.class).block();

	// hanlde null response
	if (response == null) {
	    throw new NullPointerException("Response entity is null");
	}
	// if successful so far, extract body from response
	JsonObject responseBody = (JsonObject) JsonParser.parseString(response.getBody());

	Logger.getGlobal().info(response.getBody());
    }

    public static JsonObject getInfo(String token) throws JsonSyntaxException, WebClientResponseException, NullPointerException {
	ResponseSpec request = WebClient.builder().build().get().uri(userInfoEndpoint)
		.accept(MediaType.APPLICATION_JSON)
		.header("tetris_token", token).retrieve();

	ResponseEntity<String> response = request.toEntity(String.class).block();

	if (response == null) {
	    throw new NullPointerException();
	}

	JsonObject jsonUserInfo = (JsonObject) JsonParser.parseString(response.getBody());

	if (!jsonUserInfo.has("username") || !jsonUserInfo.has("high_score")) {
	    throw new NullPointerException("missing attibutes from response");
	}

	return jsonUserInfo;
    }

    public static JsonObject updateHighScore(String token, int score) throws JsonSyntaxException, WebClientResponseException, NullPointerException {
	JsonObject bodyJson = new JsonObject();

	bodyJson.addProperty("token", token);
	bodyJson.addProperty("new_score", score);

	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().put().uri(updateScoreEndpoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve();

	ResponseEntity<String> response = request.toEntity(String.class).block();

	// hanlde null response
	if (response == null) {
	    throw new NullPointerException("Response entity is null");
	}
	// if successful so far, extract body from response
	JsonObject responseBody = (JsonObject) JsonParser.parseString(response.getBody());
	Logger.getGlobal().info(response.getBody());

	return responseBody;
    }
    
    public static void changePassword(String token, String oldPassword, String newPassword){
	
	String expectedHashOld = hashPassword(oldPassword);
		
	String hashNew = hashPassword(newPassword);
	
	JsonObject bodyJson = new JsonObject();
	
	bodyJson.addProperty("token", token);
	bodyJson.addProperty("old_password", expectedHashOld);
	bodyJson.addProperty("new_password", hashNew);
	
	ResponseSpec request = WebClient.builder().build()
		.post()
		.uri(changePasswordUserEndpoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve();
	
	ResponseEntity<JsonObject> response  = request.toEntity(JsonObject.class).block();
	
	JsonObject responseBody = response.getBody();
	
	Logger.getGlobal().info(response.toString());
	Logger.getGlobal().info(response.getStatusCode().toString());
    }

    public static void deleteUser(String token) {

	// create the key value pairs for your request body
	JsonObject bodyJson = new JsonObject();

	bodyJson.addProperty("token", token);

	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().method(HttpMethod.DELETE)
		.uri(deleteUserEndpoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve();

	ResponseEntity<String> response = request.toEntity(String.class).block();

	// hanlde null response
	if (response == null) {
	    throw new NullPointerException("Response entity is null");
	}
	// if successful so far, extract body from response
	JsonObject responseBody = (JsonObject) JsonParser.parseString(response.getBody());

	Logger.getGlobal().info(response.getBody());
    }

}
