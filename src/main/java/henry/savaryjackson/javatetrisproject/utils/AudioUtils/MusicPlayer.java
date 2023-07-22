/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.utils.AudioUtils;

/**
 *
 * @author hsavaryjackson
 */
public class MusicPlayer {
    private static MP3Player player;
    
    public static void setUp(){

	player = new MP3Player("/theme.mp3");
	
    }
    
    public static void playMusic(){
	
	new Thread( () -> getPlayer().play() ).start();
	
    }
    
    public static void resumeMusic(){
	getPlayer().resume();
    }
   
    public static void endMusic(){
	getPlayer().stop();
    }
    
    public static void pauseMusic(){
	getPlayer().pause();
    }

    /**
     * @return the player
     */
    public static MP3Player getPlayer() {
	return player;
    }
    
}
