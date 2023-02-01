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
public class RotateAction extends TetrisAction {
    
    Piece.ROT_DIR dir;

    public RotateAction( KEY_STATE state,  Piece.ROT_DIR dir, int delay, TetrisScreen screen ){
	super(state, delay, screen);
	this.dir = dir;
    }
    
    @Override
    public void action() {
	screen.rotatePiece(dir);
    }
    
}
