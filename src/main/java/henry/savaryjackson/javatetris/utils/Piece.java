/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Piece {
    public static enum tetrominoes{
	I, J, L, O, S ,T , Z;

    }
    
    public static enum ROT_DIR{
	Clockwise, CounterClockwise
    }
    
    public static final HashMap<tetrominoes, Color> tetrColour = createColourMap();
    
    public static final HashMap<tetrominoes, List<int[]>> tetrStructure = createStructureMap();
    
    public static List<int[]> setI;
    public static List<int[]> setJ;
    public static List<int[]> setL;
    public static List<int[]> setO;
    public static List<int[]> setS;
    public static List<int[]> setT ;
    public static List<int[]> setZ; 
    
    private int[] bottomSpan = new int[2]; 
    private final Color colour;
    
    //a map that stores the x position(key) and y position(value) of all the piece's blocks
    //facing the bottom. This is useful for performing quick drops, as this can let us see 
    //where the piece will land at quick drop
    private List<int[]> bottomBlocks = new ArrayList<>();
    
    //a list of the all the positions of its blocks relative to its center
    private List<int[]> blocks = new ArrayList<>();
    
    private tetrominoes tetr;
    
    //center coordinates
    public int cX, cY;
    
    public Piece(tetrominoes t,int  cX , int cY){
	tetr = t;
	colour = tetrColour.get(tetr);
	blocks.addAll(tetrStructure.get(tetr));
	
	this.cX = cX;
	this.cY = cY;
	UpdateBottomBlocks();
    }
    
    //initialises the link between a piece and its colour
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
    
    //updates the list of blocks that face the bottom
    // as well as the soan of the piece that face the bottom
    private void UpdateBottomBlocks(){
	int xHighest = 0;
	int xLowest = Integer.MAX_VALUE;
	if (!bottomBlocks.isEmpty())
	    bottomBlocks.clear();
	for (int[] block : blocks){
	    if (xHighest < block[0] + cX){
		xHighest = block[0] + cX;
		
	    }
	    if (xLowest > block[0] + cX){
		xLowest = block[0] + cX;
	    }
	    if (!blocks.stream().filter((p) -> p.equals(new int[]{block[0], block[1]-1})).findFirst().isPresent()){
		bottomBlocks.add( new int[]{block[0], block[1]});
	    }
	}
	bottomSpan = new int[]{xLowest -cX , xHighest -cX};
    }
    
    //initialises the link between a piece and the blocks it contains
    private static HashMap<tetrominoes, List<int[]>>  createStructureMap(){
	
	setI =(List<int[]>) Arrays.asList(
		new int[]{0,3}, 
		new int[]{0,2}, 
		new int[]{0,1},
		new int[]{0,0}
	);
	
	setJ = (List<int[]>) Arrays.asList( 
		new int[]{-1,0}, 
		new int[]{0,0}, 
		new int[]{0,1},
		new int[]{0,2}
	);
	setL = (List<int[]>) Arrays.asList(
	    new int[]{1,0}, 
	    new int[]{0,0}, 
	    new int[]{0,1},
	    new int[]{0,2}
	);
	setO = (List<int[]>) Arrays.asList(
	    new int[]{0,0}, 
	    new int[]{0,-1}, 
	    new int[]{1,0},
	    new int[]{1,-1}
	);
	setS = (List<int[]>) Arrays.asList(
	    new int[]{0,0}, 
	    new int[]{0,-1}, 
	    new int[]{-1,0},
	    new int[]{-1,1}
	);
	setT = (List<int[]>) Arrays.asList( 
	    new int[]{0,-1}, 
	    new int[]{0,0}, 
	    new int[]{1,0},
	    new int[]{-1,0}
	);
	setZ = (List<int[]>) Arrays.asList(
	    new int[]{0,0}, 
	    new int[]{-1,0}, 
	    new int[]{-1,-1},
	    new int[]{0,1}
	);
	
	HashMap<tetrominoes, List<int[]>> output = new HashMap<>();
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
    
    //moves the piece to a position, checking that it is a valid position
    public void moveTo( byte[][] grid, int newX, int newY){
	for (int[] block: blocks){
	    if (!Utils.pieceNotOutOfBounds(grid, block[0] + newX, block[1] + newY)){
		System.out.println(String.format("out of bounds at x:%d y:%d",block[0]+ newX, block[1] + newY ));
		return;
	    }
	    if (!Utils.pieceNotTaken(grid, block[0] + newX,  block[1] + newY)){
		System.out.println("taken");
		return;
	    }
	}
	cX = newX;
	cY = newY;
    }
    
    //rotates a block, checking that the final position is valid
    public void rotate(byte[][] grid, ROT_DIR dir){
	if (tetr ==tetrominoes.O){
	    return;
	}
	
	List<int[]> blocksCopy  = new ArrayList<>();
	for (int[] block : getBlocks()){
	    blocksCopy.add(block.clone());
	}
	
	for (int[] block : blocksCopy){
	    
		switch (dir){
		    case Clockwise -> rotatePointClockwise( block);
		    case CounterClockwise -> rotatePointCounterClockwise(block);
		}
		if (!Utils.pieceNotOutOfBounds(grid, block[0] + cX, block[1]+ cY)){
		    System.out.println("out of bounds rot");
		    return;
		}
		    
		if (grid[ block[0] + cX][block[1]+ cY]== 1)
		    return;
	}
	blocks = blocksCopy;
	UpdateBottomBlocks();
	System.out.println(Arrays.toString(bottomSpan));
    }
    
    public tetrominoes getTetr() {
	return tetr;
    }

    /**
     * @return the blocks
     */
    public List<int[]> getBlocks() {
	return blocks;
    }

    /**
     * @return the bottomBlocks
     */
    public List<int[]> getBottomBlocks() {
	return bottomBlocks;
    }

    /**
     * @return the colour
     */
    public Color getColour() {
	return colour;
    }

    /**
     * @return the bottomSpan
     */
    public int[] getBottomSpan() {
	return bottomSpan;
    }
    
}
