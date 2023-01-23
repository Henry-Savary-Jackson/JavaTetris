
package henry.savaryjackson.javatetris.utils;

import java.util.Random;

public class Utils {
    
    public static boolean notOutOfBounds(byte[][] grid, int xIndex ,int yIndex){
	return ((-1<xIndex) &&( xIndex< grid.length) && (-1 < yIndex)&&(yIndex < grid[0].length));
    }
    
    public static Piece.tetrominoes randTetrominoe(){
	Random r = new Random();
	int rIndex = r.nextInt(0, Piece.tetrominoes.values().length);
	return Piece.tetrominoes.values()[rIndex];
	
    }
    
}
