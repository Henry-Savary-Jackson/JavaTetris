
package henry.savaryjackson.javatetris.GUI;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import henry.savaryjackson.javatetris.utils.Piece;
import henry.savaryjackson.javatetris.utils.Utils;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
 
/**
 *
 * @author hsavaryjackson
 */
public class TetrisScreen extends TetrisGrid implements KeyListener {

    boolean paused ;
    Timer clock;
    Piece p;
    
    public Piece.tetrominoes nextTetrominoe;
    
    
    int delay;
    
    List<int[]> surfaceBlocks = new ArrayList<>();
    
    private long lastKeyPress = 0;
    private int highestBlock = 0;
    public TetrisScreen(int w, int h) {
	super(w,h);
	paused = false;
	p = new Piece(Piece.tetrominoes.I,4, 24);
	nextTetrominoe = Piece.tetrominoes.I;
	updateSurface(0, w-1);
	//this timer iswhere all of the game logic is running for the screen
	clock = new Timer(300, 
	    (evt)->{		
		if (!paused){
		    if (checkBottom()){
			if(checkTop()){
			    clock.stop();
			    paused = true;
			    System.out.println("game over");
			}
			//sets the piece block positions to solid tiles
			//and updates the highest block y pos
			fixPiece();
			//updates the surface on which falling onto causes block to sbecome solid
			updateSurface(p.getBottomSpan()[0] + p.cX, p.getBottomSpan()[1] + p.cX);
			//generate new piece and next randomly
			p = new Piece(nextTetrominoe,(int)(w/2), 24);
			nextTetrominoe = Piece.tetrominoes.I;
			
			int [] linesCleared = checkLines();
			try {
			clearLines(linesCleared[0], linesCleared[1]);
			}catch (IOException ioe){};
			
	
		    } else {
			movePiece(0, -1);
			
		    }
		    
		    
		}
	    }
	);
	clock.setInitialDelay(0);
	clock.start();
	initComponents();
	
	//keyListener needs this to work
	setFocusable(true);
	
	addKeyListener(this);
	
	setVisible(true);
    }
    
    private void fixPiece(){
	int iHighest = 0;
	for (int[] block : p.getBlocks()){
	    if (block[1] + p.cY > iHighest)
		iHighest = block[1] + p.cY;
	    
	    grid[block[0] + p.cX][block[1] + p.cY] = 1;
	}
	
	if (iHighest > highestBlock)
	    highestBlock = iHighest;
    }
    
    private boolean checkBottom(){
	for (int[] block : p.getBlocks()){
	    for (int[] surfaceBlock : surfaceBlocks){
		if (surfaceBlock[0] == block[0]+ p.cX && surfaceBlock[1] == block[1] + p.cY)
		    return true;
	    }
	}
	return false;
    }
    
    private void movePiece(int xDiff, int yDiff){
	clearPiece();
	p.moveTo(grid, p.cX + xDiff, p.cY+yDiff);
	drawPiece();
    }
    
    public void updateSurface(int xBegin, int xEnd){
	Iterator<int[]> it = surfaceBlocks.iterator();
	while (it.hasNext()){
	    int[] next = it.next();
	    if (next[0] >= xBegin && next[0] <= xEnd){
		it.remove();
	    }
	}
	
	for (int x = xBegin; x <= xEnd; x++){
	    for (int y =0; y <= highestBlock; y++ ){
		if (grid[x][y+1]==0 ){
		    if ((y == 0 && grid[x][y]==0) || (y!=0 && grid[x][y] ==1) ){
			
			surfaceBlocks.add(new int[]{x,y +1});
		    } 
		}
	    }
	}
    }
    
    public int[] checkLines(){
	byte numFull = 0;
	    
	for (int y = 1; y < grid[0].length-4; y++){
	    int j = 0;
	    for (; j<4; j++){
		for (int x = 0; x <grid.length; x++){
		    numFull += grid[x][y+j];
		}
		if (numFull < grid.length){
		    numFull = 0;
		    break;
		}
		numFull = 0;
	    }
	    if (j > 0){
		return new int[]{y, y+j};
	    }
	    
	}
	return new int[]{0, 0};
    }
    
    //bottom incl top excl
    public void clearLines( int bottom, int top) throws IOException{
	if (bottom == top)
	    return;
	
	//copy drawn pixels
	BufferedImage ImageCopy = 
		new BufferedImage(
			getBuffer().getWidth(), 
			(highestBlock-top+1)*tileSize, 
			BufferedImage.TYPE_INT_RGB
		);
	Raster raster = getBuffer().getData(
		    new Rectangle(
			    0, 
			    (h-highestBlock)*tileSize,
			    getBuffer().getWidth(), 
			    (highestBlock-top+1)*tileSize)
		);
	ImageCopy.setData(raster);
	ImageIO.write(ImageCopy,"bmp", new File("copy.bmp"));
	for (int y = bottom; y < top; y++){
	    for (int x= 0; x < grid.length; x++){
		grid[x][y] = 0;
	    }
	}
	
	
	//move all lines above numLines below and clear lines above
	int numLines = top -bottom;
	for (int y = top; y<= highestBlock; y++){
	    for (int x= 0; x< grid.length; x++){
		if (grid[x][y] == 0)
		    continue;
		grid[x][y-numLines] = grid[x][y];
		grid[x][y] = 0;
		//clear tile
		drawTile(x, y, emptyColour);
	    }
	}
	
	updateSurface(0, w-1);
	
	//redraw Image
	getBuffer().getGraphics().drawImage(ImageCopy, 0, (top-1) * tileSize, this);
	repaint();
	int u = 0;
    }
    
    public void clearPiece(){
	for (int[] block : p.getBlocks()){
	    drawTile(block[0] + p.cX, block[1] + p.cY, emptyColour);
	}
    }
    
    public void drawPiece(){
	for (int[] block : p.getBlocks()){
	    drawTile(block[0] + p.cX, block[1] + p.cY, p.getColour());
	}
    }
    
    public void rotatePiece(Piece.ROT_DIR direction){
	clearPiece();
	p.rotate(grid, direction);
	drawPiece();
    }
    
    public boolean checkTop(){
	for (int[] block : p.getBlocks()){
	    if (block[1] == h-1){
		return true;
	    }
	}
	return false;
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
	if (lastKeyPress != 0){
	    long time = System.currentTimeMillis();
	    long diff = time - lastKeyPress;
	    if (diff  >= 100){
		lastKeyPress = time;
		switch (e.getKeyCode()){
		    case KeyEvent.VK_UP:
			rotatePiece(Piece.ROT_DIR.Clockwise);
			break;
		    case KeyEvent.VK_DOWN:
			rotatePiece(Piece.ROT_DIR.CounterClockwise);
			break;
		    case KeyEvent.VK_LEFT:
			movePiece(-1,0);
			break;
		    case KeyEvent.VK_RIGHT:
			movePiece(1,0);
			break;
		    case KeyEvent.VK_SPACE:
			//quick drop logic
			break;
		}
	    }
	} else{
	    lastKeyPress = System.currentTimeMillis();
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    @Override
    public void paintComponent(Graphics g){
	super.paintComponent(g);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
