/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils.WebUtils;

/**
 *
 * @author hsavaryjackson
 */

public class StatusException extends Exception {
    
    private String message;
    
    public StatusException(String message){
	this.message = message;
    }
    
    public String getMessage(){
	return message;
    }
    
}
