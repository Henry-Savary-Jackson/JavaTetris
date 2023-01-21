/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Piece {
    public static enum tetrominoes{
	I, J, L, O, S ,T , Z
    }
    
    public static final HashMap<tetrominoes, Color> tetrColour = createColourMap();
    
    public static final HashMap<tetrominoes, HashSet<int[]>> tetrStructure = createStructureMap();
   
    
    public  static HashSet<int[]> setI ;
    public   static HashSet<int[]> setJ;
    public  static HashSet<int[]> setL;
    public  static HashSet<int[]> setO;
    public  static HashSet<int[]> setS;
	    
    public  static HashSet<int[]> setT ;
    public  static HashSet<int[]> setZ; 
    
    
    private tetrominoes tetr;
    
    public int cX, cY;
    
    private static HashMap<tetrominoes, Color>  createColourMap(){
	HashMap<tetrominoes, Color> output = new HashMap<>();
	output.put(tetrominoes.I, Color.CYAN);
	output.put(tetrominoes.J, Color.BLUE);
	output.put(tetrominoes.L, Color.ORANGE);
	output.put(tetrominoes.O, Color.YELLOW);
	output.put(tetrominoes.S, Color.GREEN);
	output.put(tetrominoes.T, Color.PINK);
	output.put(tetrominoes.Z, Color.RED);
	
	return output;
    }
    
    private static HashMap<tetrominoes, HashSet<int[]>>  createStructureMap(){
	
	setI = new HashSet<>(Arrays.asList(
		new int[]{0,3}, 
		new int[]{0,2}, 
		new int[]{0,1},
		new int[]{0,0}
	));
	
	setJ = new HashSet<>(Arrays.asList( 
		new int[]{-1,0}, 
	    new int[]{0,0}, 
	    new int[]{0,1},
	    new int[]{0,2})
	);
	setL = new HashSet<>(Arrays.asList( new int[]{1,0}, 
	    new int[]{0,0}, 
	    new int[]{0,1},
	    new int[]{0,2})
	);
	setO = new HashSet<>(Arrays.asList(new int[]{0,0}, 
	    new int[]{0,-1}, 
	    new int[]{1,0},
	    new int[]{1,-1})
	);
	setS = new HashSet<>(Arrays.asList(new int[]{0,0}, 
	    new int[]{0,-1}, 
	    new int[]{-1,0},
	    new int[]{-1,1})
	);
	setT = new HashSet<>(Arrays.asList( new int[]{-1,0}, 
	    new int[]{0,0}, 
	    new int[]{1,0},
	    new int[]{-1,0})
	);
	setZ = new HashSet<>(Arrays.asList(new int[]{0,0}, 
	    new int[]{-1,0}, 
	    new int[]{-1,-1},
	    new int[]{0,1})
	);
	
	HashMap<tetrominoes, HashSet<int[]>> output = new HashMap<>();
	output.put(tetrominoes.I, setI);
	output.put(tetrominoes.J,setJ);
	output.put(tetrominoes.L, setL);
	output.put(tetrominoes.O, setO);
	output.put(tetrominoes.S, setS);
	output.put(tetrominoes.T, setT);
	output.put(tetrominoes.Z, setZ);
	return output;
    }
    
    public static void rotatePointCounterClockwise(int[] point){
	if (!(point[0] == 0 && point[1] == 0)){
	    int iTemp = point[0];
	    point[0] = -point[1];
	    point[1] = iTemp;
	}
    }
    
    public static void rotatePointClockwise(int[] point){
	if (!(point[0] == 0 && point[1] == 0)){
	    int iTemp = point[0];
	    point[0] = point[1];
	    point[1] = -iTemp;
	}
    }
    
    
    public Piece(tetrominoes t,int  cX , int cY){
	tetr = t;
	this.cX = cX;
	this.cY = cY;
    }

    public tetrominoes getTetr() {
	return tetr;
    }
    
}
