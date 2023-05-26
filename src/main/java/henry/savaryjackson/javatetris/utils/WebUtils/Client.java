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
public class Client {
    
    private WebClient webClient;
    
    public String login(String username, String password ) throws NullPointerException,ResponseStatusException, StatusException{
	
	// hashs using argon2id
	Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(0,64,1,15*1024,2);
	String hash = encoder.encode(password);
	
	// create a map of values
	
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("username", username);
	bodyJson.addProperty("hash", hash);
	
	Logger.getGlobal().log(Level.INFO, "Hash: {0}", hash);
	
	// create requests
	ResponseSpec request = WebClient.builder().build().post().uri("http://localhost:8080/login")
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
	
	// hanlde null response
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	
	// parse response body
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	if (!responseBody.get("Status").getAsString().equals("Success")){
	    throw new StatusException(responseBody.get("Status").getAsString());
	}
	
	
	
	return responseBody.get("id").getAsString();
    }
        
    public String SignUp(String username, String password)  throws NullPointerException,ResponseStatusException, StatusException{
	
	Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(0,64,1,15*1024,2);
	
	String hash = encoder.encode(password);
	
	
	JsonObject bodyJson  = new JsonObject();
	
	bodyJson.addProperty("username", username);
	bodyJson.addProperty("hash", hash);
	
	Logger.getGlobal().log(Level.INFO, "Hash: {0}", hash);
	
	ResponseSpec request = WebClient.builder().build().post().uri("http://localhost:8080/signup")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(bodyJson.toString())
		.retrieve() ;
	
	ResponseEntity<String> response = request.toEntity(String.class).block();
	
	if (response == null){
	    throw new NullPointerException("Response entity is null");
	}
	// check status code
	if (!response.getStatusCode().is2xxSuccessful()){
	    throw new ResponseStatusException(response.getStatusCode());
	}
	
	
	JsonObject responseBody =  (JsonObject)JsonParser.parseString( response.getBody());
	
	if (!responseBody.get("Status").getAsString().equals("Success")){
	    throw new StatusException(responseBody.get("Status").getAsString());
	}
	
	return responseBody.get("id").getAsString();
    }
    
    
    public int getHighScore(String userID){
	return 0;
    }
    
    public void updateHighSchore(String userID, int score){
	
    }
    
}
