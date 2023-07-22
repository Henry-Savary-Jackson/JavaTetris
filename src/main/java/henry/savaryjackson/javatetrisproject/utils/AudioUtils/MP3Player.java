/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.utils.AudioUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author hsavaryjackson
 */
public class MP3Player {
    
    public static enum STATE {
	PAUSED, PLAYING, STOPPED
    }

    private STATE currentState;
    
    private Player player;
    
    private String currentFile ;
    
    private int currentPosition;
    
    private final Object lock = new Object();
    
    
    public MP3Player(String filepath){
	try {
	    currentFile = filepath;
	    
	    player = new Player(MP3Player.class.getResourceAsStream(currentFile));
	    
	    currentState = STATE.STOPPED;
	    
	} catch (JavaLayerException ex) {
	    
	    Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    public void play() {
	currentState = STATE.PLAYING;
	while ( true){
	   
	    try {
		if (getCurrentState() == STATE.PLAYING)
		    getPlayer().play(1);
	    } catch (JavaLayerException ex) {
		Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    synchronized (lock) {
		switch (getCurrentState()){
		    case PAUSED :
			break;
		    case STOPPED:
			return;
		}
	    }

	}
    }
    
    public void resume(){
	synchronized (lock) {
	    if ( getCurrentState() == STATE.PAUSED){
		currentState = STATE.PLAYING;
	    }
	}
	
    }
    
    public void pause(){
	synchronized (lock) {
	    currentState = STATE.PAUSED;
	}
	
    }
    
    public void stop(){
	synchronized (this) {
	
	    currentState = STATE.STOPPED;
	    getPlayer().close();
	}
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
	return player;
    }

    /**
     * @return the currentState
     */
    public STATE getCurrentState() {
	return currentState;
    }
}
