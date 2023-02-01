/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils;

import henry.savaryjackson.javatetris.GUI.TetrisScreen;

/**
 *
 * @author hsavaryjackson
 */
public class MoveAction extends TetrisAction {
    
    byte dir;
    
    public MoveAction(KEY_STATE state,  byte dir, int delay,TetrisScreen screen){
	super(state, delay, screen);
	this.dir = dir;
    }

    @Override
    public void action() {
	screen.movePiece(dir, 0);
    }
    
}
