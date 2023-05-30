/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.utils;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Piece {

    public static enum tetrominoes {
	I, J, L, O, S, T, Z;

    }

    public static enum ROT_DIR {
	Clockwise, CounterClockwise
    }

    public static final HashMap<tetrominoes, Color> tetrColour = createColourMap();

    public static final HashMap<tetrominoes, HashSet<Tile>> tetrStructure = createStructureMap();

    public static HashSet<Tile> setI;
    public static HashSet<Tile> setJ;
    public static HashSet<Tile> setL;
    public static HashSet<Tile> setO;
    public static HashSet<Tile> setS;
    public static HashSet<Tile> setT;
    public static HashSet<Tile> setZ;

    private int[] bottomSpan = new int[2];
    private final Color colour;

    //a map that stores the x position(key) and y position(value) of all the piece's blocks
    //facing the bottom. This is useful for performing quick drops, as this can let us see 
    //where the piece will land at quick drop
    private HashSet<Tile> bottomBlocks = new HashSet<>();

    //a list of the all the positions of its blocks relative to its center
    private HashSet<Tile> blocks = new HashSet<>();

    private tetrominoes tetr;

    //center coordinates
    public int cX, cY;

    public Piece(tetrominoes t, int cX, int cY) {
	tetr = t;
	colour = tetrColour.get(tetr);
	blocks.addAll(tetrStructure.get(tetr));

	this.cX = cX;
	this.cY = cY;
	UpdateBottomBlocks();
    }

    //initialises the link between a piece and its colour
    private static HashMap<tetrominoes, Color> createColourMap() {
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

    //updates the list of blocks that face the bottom of a tetris block 
    // as well as the side of the piece that face the bottom
    private void UpdateBottomBlocks() {
	int xHighest = 0;
	int xLowest = Integer.MAX_VALUE;
	if (!bottomBlocks.isEmpty()) {
	    bottomBlocks.clear();
	}
	for (Tile block : blocks) {
	    // find the highest xposition
	    if (xHighest < block.getX() + cX) {
		xHighest = block.getX() + cX;

	    }
	    // update the lowest xposition
	    if (xLowest > block.getY() + cX) {
		xLowest = block.getY() + cX;
	    }
	    // for each block in a tetrominoe's blocks, check to see if there is any block below it
	    // if not, it is part of the blocks that would touch the bottom if dropped
	    if (blocks.contains(new Tile(block.getX(), block.getY()-1))){
		bottomBlocks.add(block);
	    }
	}
	// update the piece's span
	bottomSpan = new int[]{xLowest - cX, xHighest - cX};
    }

    //initialises the link between a piece and the blocks it contains
    private static HashMap<tetrominoes, HashSet<Tile>> createStructureMap() {

	setI = new HashSet<>(Arrays.asList(
		new Tile(0,  3),
		new Tile(0,  2),
		new Tile(0,  1),
		new Tile(0,  0)
	));

	setJ =  new HashSet<>(Arrays.asList(
		new Tile(-1,  0),
		new Tile(0,  0),
		new Tile(0,  1),
		new Tile(0,  2)
	));
	setL =  new HashSet<>(Arrays.asList(
		new Tile(1,  0),
		new Tile(0,  0),
		new Tile(0,  1),
		new Tile(0,  2)
	));
	setO =  new HashSet<>(Arrays.asList(
		new Tile(0,  0),
		new Tile(0,  -1),
		new Tile(1,  0),
		new Tile(1,  -1)
	));
	setS =  new HashSet<>(Arrays.asList(
		new Tile(0,  0),
		new Tile(0,  -1),
		new Tile(-1,  0),
		new Tile(-1,  1)
	));
	setT =  new HashSet<>(Arrays.asList(
		new Tile(0,  -1),
		new Tile(0,  0),
		new Tile(1,  0),
		new Tile(-1,  0)
	));
	setZ =  new HashSet<>(Arrays.asList(
		new Tile(0,  0),
		new Tile(-1,  0),
		new Tile(-1,  -1),
		new Tile(0,  1)
	));

	HashMap<tetrominoes, HashSet<Tile>> output = new HashMap<>();
	output.put(tetrominoes.I, setI);
	output.put(tetrominoes.J, setJ);
	output.put(tetrominoes.L, setL);
	output.put(tetrominoes.O, setO);
	output.put(tetrominoes.S, setS);
	output.put(tetrominoes.T, setT);
	output.put(tetrominoes.Z, setZ);
	return output;
    }

    // rotate a given point anti-clockwise 
    // notes that the point supplied should have position as relative to centre
    public static void rotatePointCounterClockwise(Tile point) {
	if (!(point.getX() == 0 && point.getY() == 0)) {
	    int iTemp = point.getX();
	    point.setX(-point.getY());
	    point.setY(iTemp);
	}
    }

    // rotate a given point clockwise 
    // notes that the point supplied should have position as relative to centre
    public static void rotatePointClockwise(Tile point) {
	if (!(point.getX() == 0 && point.getY() == 0)) {

	    int iTemp = point.getX();
	    point.setX(point.getY());
	    point.setY(-iTemp);
	}
    }

    //moves the piece to a position, checking that it is a valid position
    public void moveTo(byte[][] grid, int newX, int newY) {
	for (Tile block : blocks) {
	    if (!Utils.pieceNotOutOfBounds(grid, block.getX() + newX, block.getY()+ newY)) {
		return;
	    }
	    if (!Utils.pieceNotTaken(grid, block.getX() + newX, block.getY() + newY)) {
		return;
	    }
	}
	cX = newX;
	cY = newY;
    }

    //rotates a block, checking that the final position is valid
    public void rotate(byte[][] grid, ROT_DIR dir) {
	if (tetr == tetrominoes.O) {
	    // no rotation occurs with square
	    return;
	}

	// copy the tile position of the og block and rotate them
	HashSet<Tile> blocksCopy = blocks.stream().map(Tile::new).collect(Collectors.toSet());

	for (Tile block : blocksCopy) {

	    switch (dir) {
		case Clockwise ->
		    rotatePointClockwise(block);
		case CounterClockwise ->
		    rotatePointCounterClockwise(block);
	    }
	    // if there is a problem, such as one of the tiles going out of bounds 
	    // cancel rotation
	    if (!Utils.pieceNotOutOfBounds(grid, block.getX()+ cX, block.getY() + cY)) {
		return;
	    }

	    if (grid[block.getX()+ cX][block.getY() + cY] == 1) {
		return;
	    }
	}
	blocks = blocksCopy;

	UpdateBottomBlocks();
    }

    public tetrominoes getTetr() {
	return tetr;
    }

    /**
     * @return the blocks
     */
    public HashSet<Tile> getBlocks() {
	return blocks;
    }

    /**
     * @return the bottomBlocks
     */
    public HashSet<Tile> getBottomBlocks() {
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
