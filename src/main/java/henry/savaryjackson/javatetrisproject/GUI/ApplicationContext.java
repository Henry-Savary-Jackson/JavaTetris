/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.GUI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import henry.savaryjackson.javatetrisproject.Main.Login;
import henry.savaryjackson.javatetrisproject.utils.WebUtils.APIUtils;
import javax.swing.JOptionPane;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 *
 * @author hsavaryjackson
 */
// this class acts a global context to access all the different screens in the application
// this makes any screen accessible to any other screen
// this allos for ivnversion of Control
public class ApplicationContext {

    private static Screen mainScreen;
    private static UserDetails userDetails;
    private static Login loginScreen;

    private static String token;

    // use this exclusively to create a nuew login screen
    public static Login generateLoginScreen() {
	loginScreen = new Login();
	return loginScreen;
    }

    public static Screen generateMainScreen() {
	mainScreen = new Screen();
	return mainScreen;
    }
    
    public static void disposeMainScreen(){
	getMainScreen().dispose();
	mainScreen = null;
    }
    public static void disposeLoginScreen(){
	getLoginScreen().dispose();
	loginScreen = null;
    }
    
    public static void disposeUserDetailsScreen(){
	getUserDetails().dispose();
	userDetails = null;
    }

    public static UserDetails generateUserDetailsScreen() {
	try {
	    // retrieve data from user with this token
	    JsonObject userData = APIUtils.getInfo(getToken());

	    // create screen
	    userDetails = new UserDetails(userData);

	    return userDetails;

	} catch (NullPointerException | WebClientRequestException ex) {
	    JOptionPane.showMessageDialog(getMainScreen(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    return null;

	} catch (WebClientResponseException ex) {
	    JsonObject body = (JsonObject) JsonParser.parseString(ex.getResponseBodyAsString());
	    if (body == null) {
		JOptionPane.showMessageDialog(getMainScreen(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    } else if (body.has("Status")) {
		String status = body.getAsJsonPrimitive("Status").getAsString();
		JOptionPane.showMessageDialog(getMainScreen(), status, "Error", JOptionPane.ERROR_MESSAGE);
		if (status.equals("Invalid Token")) {
		    getMainScreen().dispose();
		    getLoginScreen().setVisible(true);
		}

	    } else {
		JOptionPane.showMessageDialog(getMainScreen(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

	    }
	    return null;

	}
    }

    /**
     * @return the mainScreen
     */
    // the accepted way to access the main screen
    public static Screen getMainScreen() {
	return mainScreen;
    }

    /**
     * @return the userDetails
     */
    // the accepted way to access the user details screen
    public static UserDetails getUserDetails() {
	return userDetails;
    }

    /**
     * @return the loginScreen
     */
    // the accepted way to access the login screen
    public static Login getLoginScreen() {
	return loginScreen;
    }

    /**
     * @return the token
     */
    public static String getToken() {
	return token;
    }

    /**
     * @param aToken the token to set
     */
    public static void setToken(String aToken) {
	token = aToken;
    }

}
