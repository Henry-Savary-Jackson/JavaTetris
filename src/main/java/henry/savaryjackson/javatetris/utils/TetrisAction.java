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
    
    public TetrisAction( int delay,TetrisScreen screen){
	this.screen = screen;
	actionTimer = new Timer(delay, ()-> {
	    action();
	});
	actionTimer.setInitialDelay(0);
    }
    
    public abstract void action();

    @Override
    public void actionPerformed(ActionEvent e) {
	if (active){
	    actionTimer.stop();
	} else {
	    active = true;
	    actionTimer.start();
	}
    }
    
}
