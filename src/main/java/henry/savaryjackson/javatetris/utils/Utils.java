
package henry.savaryjackson.javatetris.utils;

import java.util.Random;

public class Utils {
    
    public static boolean pieceNotOutOfBounds(byte[][] grid, int xIndex ,int yIndex){
	return ((-1<xIndex) &&( xIndex< grid.length) && (0 < yIndex)&&(yIndex < grid[0].length));
    }
    
    public static boolean pieceNotTaken(byte[][] grid, int xIndex ,int yIndex){
	return grid[xIndex][yIndex] == 0 ;
    }
    
    public static boolean notTaken(byte[][] grid, Piece p ){
	return p.getBlocks().stream().allMatch((block)-> { return pieceNotTaken(grid, block[0]+ p.cX, block[1]+ p.cY);});
    }
    
    public static Piece.tetrominoes randTetrominoe(){
	Random r = new Random();
	int rIndex = r.nextInt(0, Piece.tetrominoes.values().length);
	return Piece.tetrominoes.values()[rIndex];
	
    }
    
}
