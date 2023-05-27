/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils.WebUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


import org.springframework.web.server.ResponseStatusException;
public class APIUtils {
    
    private static WebClient webClient;
    
    private static final String domain = "http://localhost:8080";
    private static final String changeUsernameEndoint = domain+"/change" ;
    private static final String loginEndoint = domain+"/login" ;
    private static final String signUpEndpoint = domain+"/signup" ;
    
    public static String login(String username, String password ) throws NullPointerException,ResponseStatusException, StatusException{
	
	// hashs using argon2id
	Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(0,64,1,15*1024,2);
	String hash = encoder.encode(password);
	
	// create a map of values
	
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("username", username);
	bodyJson.addProperty("hash", hash);
	
	Logger.getGlobal().log(Level.INFO, "Hash: {0}", hash);
	
	// create requests
	ResponseSpec request = WebClient.builder().build().post().uri(loginEndoint)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	// send request
	ResponseEntity<String> response ;
	try {
	    response = request.toEntity(String.class).block();
	} catch (ResponseStatusException e){
	    // handle error with unsuccessful acess code
	    throw e;
	}
	
	// handle null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	
	// parse response body
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	if (!responseBody.get("Status").getAsString().equals("Success")){
	    throw new StatusException(responseBody.get("Status").getAsString());
	}
	
	// return id for use
	
	return responseBody.get("id").getAsString();
    }
        
    public static String SignUp(String username, String password)  throws NullPointerException,ResponseStatusException, StatusException{
	
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
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	ResponseEntity<String> response = request.toEntity(String.class).block();
	
	// throw exception if null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	// check status code
	if (!response.getStatusCode().is2xxSuccessful()){
	    throw new ResponseStatusException(response.getStatusCode());
	}
	
	// if successful so far, extract body from response
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	// check status values in body if the server has indicated a problem , raise exception with info
	if (!responseBody.get("Status").getAsString().equals("Success")){
	    throw new StatusException(responseBody.get("Status").getAsString());
	}
	
	return responseBody.get("id").getAsString();
    }
    
    public static void changeUsername(String userID, String password, String newUsername)  throws NullPointerException,ResponseStatusException, StatusException{
	
	// ecnrypted password without salt
	// this is just to add another layer of encryption along with TLS (which is not implemented yet but wait)
	
	Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(0,64,1,15*1024,2);
	
	String hash = encoder.encode(password);
	
	// create the key value pairs for your request body
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("hash", hash);
	bodyJson.addProperty("new_username", newUsername);
	
	// create and send request to endpoint
	ResponseSpec request = WebClient.builder().build().put().uri(String.format("%s/%s",changeUsernameEndoint,userID))
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	ResponseEntity<String> response = request.toEntity(String.class).block();
	
	// hanlde null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	// check status code
	if (!response.getStatusCode().is2xxSuccessful()){
	    throw new ResponseStatusException(response.getStatusCode());
	}
	
	// if successful so far, extract body from response
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	// check status values in body if the server has indicated a problem , raise exception with info
	if (!responseBody.get("Status").getAsString().equals("Success")){
	    throw new StatusException(responseBody.get("Status").getAsString());
	}
    }
    
    
    public int getHighScore(String userID){
	
	
	return 0;
    }
    
    public void updateHighSchore(String userID, int score){
	
    }
    
}
