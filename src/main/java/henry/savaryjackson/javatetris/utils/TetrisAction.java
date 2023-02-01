/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils;

import henry.savaryjackson.javatetris.GUI.TetrisScreen;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Timer;

/**
 *
 * @author hsavaryjackson
 */
public abstract class TetrisAction extends AbstractAction {
    
    Timer actionTimer;
    TetrisScreen screen;
    
    boolean active ;
    
    KEY_STATE state;
    
    public static enum KEY_STATE{

	KEY_RELEASED,KEY_PRESSED;
	
    }
    
    
    public TetrisAction( KEY_STATE state, int delay,TetrisScreen screen){
	this.screen = screen;
	this.state = state;
	actionTimer = new Timer(delay, (evt)-> {
	    System.out.println("Still going");
	    action();
	});
	actionTimer.setInitialDelay(delay);
    }
    
    public abstract void action();

    @Override
    public void actionPerformed(ActionEvent e) {
	if (screen.paused)
	    actionTimer.stop();
	
	switch (state){
	    case KEY_PRESSED: 
		if (!active){
		    active = true;
		    System.out.println("Start timer");
		    actionTimer.start();
		}
		
	    break;
	    case KEY_RELEASED:
		 System.out.println("Stop timer");
		if (active){
		   
		    actionTimer.stop();
		}
	    break;
	}
	    //TODO: fix as it doesn't handle key releases
	    //TODO: make it so that being paused stops the timer
	    //TODO: make it so that opp actions work nicely
	}
    
}
