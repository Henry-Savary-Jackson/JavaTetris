/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils.WebUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.logging.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class APIUtils {
    
    private static WebClient webClient;
    
    private static final String domain = "http://localhost:8080";
    private static final String changeUsernameEndoint = domain+"/change" ;
    private static final String loginEndoint = domain+"/login" ;
    private static final String signUpEndpoint = domain+"/signup" ;
    private static final String userInfoEndpoint = domain+"/userinfo" ;
    private static final String updateScoreEndpoint = domain+"/score";
    private static final String deleteUserEndpoint = domain+"/delete";
    
    public static String login(String username, String password ) throws WebClientResponseException,
	    NullPointerException, JsonSyntaxException{
	
	// hashs using argon2id
	Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(0,64,1,15*1024,2);
	String hash = encoder.encode(password);
	
	// create a map of values
	
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("username", username);
	bodyJson.addProperty("hash", hash);
	
	// create requests
	ResponseSpec request = WebClient.builder().build().post().uri(loginEndoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	// send request
	ResponseEntity<String> response ;
	try {
	    response = request.toEntity(String.class).block();
	} catch (WebClientResponseException e){
	    // handle error with unsuccessful acess code
	    throw e;
	}
	
	// handle null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	
	// parse response body
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());

	
	// return id for use
	
	return responseBody.getAsJsonPrimitive("token").getAsString();
    }
        
    public static String SignUp(String username, String password)  throws WebClientResponseException,
	    NullPointerException,JsonSyntaxException{
	
	// ecnrypted password without salt
	// this is just to add another layer of encryption along with TLS (which is not implemented yet but wait)
	Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(0,64,1,15*1024,2);
	
	String hash = encoder.encode(password);
	
	
	// create the key value pairs for your request body
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("username", username);
	bodyJson.addProperty("hash", hash);
	
	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().post().uri(signUpEndpoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString()).retrieve();

	ResponseEntity<String> response = request.toEntity(String.class).block();

	
	// throw exception if null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	
	// if successful so far, extract body from response
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	// check status values in body if the server has indicated a problem , raise exception with info

	return responseBody.getAsJsonPrimitive("token").getAsString();
    }
	
 
    
    public static void changeUsername(String token, String newUsername) throws JsonSyntaxException, WebClientResponseException, NullPointerException {
	
	// ecnrypted password without salt
	// this is just to add another layer of encryption along with TLS (which is not implemented yet but wait)
	
	// create the key value pairs for your request body
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("token", token);
	bodyJson.addProperty("new_username", newUsername);
	
	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().put().uri(changeUsernameEndoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	ResponseEntity<String> response = request.toEntity(String.class).block();
	
	// hanlde null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	// if successful so far, extract body from response
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	Logger.getGlobal().info(response.getBody());
    }
    
    
    public static JsonObject getInfo(String token) throws JsonSyntaxException , WebClientResponseException, NullPointerException{
	ResponseSpec request = WebClient.builder().build().get().uri(userInfoEndpoint)
		.accept(MediaType.APPLICATION_JSON)
		.header("tetris_token", token).retrieve() ;
	
	ResponseEntity<String> response = request.toEntity(String.class).block();
	
	if (response == null){
	    throw new NullPointerException();
	}
	
	JsonObject jsonUserInfo = (JsonObject)JsonParser.parseString(response.getBody());
	
	if (!jsonUserInfo.has("username") || !jsonUserInfo.has("high_score")){
	    throw new NullPointerException("missing attibutes from response"); 
	}
	
	return jsonUserInfo;
    }
    
    public static JsonObject updateHighScore(String token,  int score) throws JsonSyntaxException, WebClientResponseException, NullPointerException{
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("token", token);
	bodyJson.addProperty("new_score", score);
	
	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().put().uri(updateScoreEndpoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	ResponseEntity<String> response = request.toEntity(String.class).block();
	
	// hanlde null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	// if successful so far, extract body from response
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	Logger.getGlobal().info(response.getBody());
	
	return responseBody;
    }
    
    public static void deleteUser(String token){
	
	// create the key value pairs for your request body
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("token", token);
	
	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().method(HttpMethod.DELETE)
		.uri(deleteUserEndpoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	ResponseEntity<String> response = request.toEntity(String.class).block();
	
	// hanlde null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	// if successful so far, extract body from response
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	Logger.getGlobal().info(response.getBody());
    }
    
}
