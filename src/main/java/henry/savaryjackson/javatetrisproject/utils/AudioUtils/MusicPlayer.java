/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.utils.AudioUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author hsavaryjackson
 */
public class MusicPlayer {
    private static Clip clip;
    private static AudioInputStream audioIs;
    
    public static void setUpClip(){
	try {
	    audioIs =AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("/theme.wav"));
	    clip = AudioSystem.getClip();
	} catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
	    Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
	}
	
    }
    
    public static void playMusic(){
	
	if (!clip.isOpen()){
	    try {
		clip.open(audioIs);
	    } catch (LineUnavailableException | IOException ex) {
		Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	clip.start();
	clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public static void releaseMusic(){
	clip.close();
    }
    
    public static void endMusic(){
	clip.setMicrosecondPosition(0);
	clip.stop();
    }
    
    public static void pauseMusic(){
	clip.stop();
    }
    
}
