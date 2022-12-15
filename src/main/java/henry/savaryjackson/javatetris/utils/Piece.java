/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils;

public class Piece {
    public static enum tetrominoes{
	I, J, L, O, S ,T , Z
    }
    
    private tetrominoes tetr;
    
    
    public Piece(){
	
    }

    public tetrominoes getTetr() {
	return tetr;
    }
    
}
