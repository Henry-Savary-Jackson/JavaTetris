
package henry.savaryjackson.javatetris.GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import henry.savaryjackson.javatetris.utils.Piece;
import henry.savaryjackson.javatetris.utils.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author hsavaryjackson
 */
public class TetrisScreen extends TetrisGrid implements KeyListener {
    
    Timer leftTimer; 
    Timer rightTimer;
    Timer upTimer;
    Timer downTimer;
    
    public final static int KEY_DELAY = 150;

    public boolean paused;
    boolean inSession; 
    Timer clock;
    Piece p;
    public Piece.tetrominoes nextTetrominoe;
    int delay;
    
    int points = 0;
    int linesCleared = 0;
    
    public TetrisGrid nextPieceGrid;
    public Screen screen;
    
    List<int[]> surfaceBlocks = new ArrayList<>();
    private int highestBlock = 1;
    
    public byte isSidewaysPressed = 0;
    
    public byte isUpPressed = 0;


    public TetrisScreen(int w, int h , TetrisGrid nextgrid, Screen OuterFrame)  {
	//
	super(w, h);
	paused = true;
	
	leftTimer= new Timer(KEY_DELAY, (evt)->{movePiece(-1, 0);});
	leftTimer.setInitialDelay(0);
	rightTimer= new Timer(KEY_DELAY, (evt)->{movePiece(1, 0);});
	rightTimer.setInitialDelay(0);
	upTimer= new Timer(KEY_DELAY, (evt)->{rotatePiece(Piece.ROT_DIR.Clockwise);});
	upTimer.setInitialDelay(0);
	downTimer= new Timer(KEY_DELAY, (evt)->{rotatePiece(Piece.ROT_DIR.CounterClockwise);});
	downTimer.setInitialDelay(0);

	inSession = false;
	screen = OuterFrame;
	nextPieceGrid = nextgrid;
	createPiece();
	updateSurface(0, w-1);
	//this timer iswhere all of the game logic is running for the screen
	clock = new Timer(200, 
	    (evt)->{		
		if (!paused){
		    //if the block hits the bottom
		    if (checkBottom()){
			if(checkTop()){
			    //if hits top then gameover
			    gameOver();
			}
			
			fixPiece();
			//updates the surface on which falling onto causes block to become solid
			updateSurface(p.getBottomSpan()[0] + p.cX, p.getBottomSpan()[1] + p.cX);
			//generate new piece and next one randomly
			createPiece();
			drawPiece();
			//if piece spawns on taken block, then game over
			if (!Utils.notTaken(grid, p))
			    gameOver();
			
			//update any lines that have been cleared by user
			int lines = clearAllFullLines();
			linesCleared += lines;
			incrScore(lines);
			screen.updateUI();
			
	
		    } else {
			movePiece(0, -1);
			
		    }
		}
	    }
	);
	clock.setInitialDelay(0);
	
	initComponents();
	
	addKeyListener(this);
	setFocusable(true);
	
	setVisible(true);
	
    }
    
    //initialises the current falling piece
    public void createPiece(){
	p = inSession?  p = new Piece(nextTetrominoe,(int)(w/2), 24):new Piece(Utils.randTetrominoe(),(int)(w/2), 24);
	updateNextTetr();
    }
    
    //code for a game over once it occurs
    public void gameOver(){
	clock.stop();
	
	//the current session is now over and the game is paused
	inSession = false;
	paused = true;
	System.out.println("game over");			    
    }
    
    
    //updates the next piece the game will spawn and displays it on the preview grid
    public void updateNextTetr(){
	nextTetrominoe = Utils.randTetrominoe();
	if (!paused){
	    nextPieceGrid.initTiles();
	    nextPieceGrid.drawTetrominoe(nextTetrominoe, Math.floorDiv(nextPieceGrid.w,2),Math.floorDiv(nextPieceGrid.h,2));
	}
	
    }
    
    //sets the piece block positions to solid tiles
    //and updates the highest block y pos
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
    
    
    
    //checks to see whether the current piece sits on the bottom
    private boolean checkBottom(){
	for (int[] block : p.getBlocks()){
	    for (int[] surfaceBlock : surfaceBlocks){
		if (surfaceBlock[0] == block[0]+ p.cX && surfaceBlock[1] == block[1] + p.cY)
		    return true;
	    }
	}
	return false;
    }
    
    //code that handles moving the piece and rendering that change
    public void movePiece(int xDiff, int yDiff){
	clearPiece();
	p.moveTo(grid, p.cX + xDiff, p.cY+yDiff);
	drawPiece();
    }
    
    //increases the games score given how many lines were cleared
    private void incrScore(int linesCleared){
	switch (linesCleared){
	    case 0 ->{
		return;
	    }
	    case 1 -> points += 40;
	    case 2 -> points += 100;
	    case 3 -> points += 400;
	    case 4 -> points += 1200;
	}
    }
    
    // updates the list of blocks on the surface withing a range
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
    
    //code for handling a quick drop
    public void quickDrop(){
	int[] span = p.getBottomSpan();
	int yLowestHeightDiff = Integer.MAX_VALUE;
	//within the x-range of the piece that faces the bottom
	//find what is the lowest amount height needed to be lost 
	//in order to touch the surface
	for (int x = span[0] + p.cX; x <=span[1] + p.cX; x++){
	    int y =1;
	    int yHighestinCol = y;
	    for (; y <= p.cY; y ++){
		if (grid[x][y] == 1){
		    if (y+1 > yHighestinCol){
			yHighestinCol = y+1;
		    }
		}
	    }
	    int yOfPiece = p.getBottomBlocks().get(x-p.cX);
	    if ( (p.cY+ yOfPiece) - yHighestinCol < yLowestHeightDiff){
		yLowestHeightDiff = (p.cY+ yOfPiece) - yHighestinCol;
	    }
	}
	clearPiece();
	//lower the piece by the necessary height
	p.cY -= (yLowestHeightDiff);
	
	//redraw piece
	drawPiece();
	
    }
    
    //clears all the lines that are full once a piece is placed
    public int clearAllFullLines(){
	int numLines = 0;

	for (int y = 1; y <= highestBlock; y++){
	    int x = 0;
	    for (; x <= grid.length; x++){
		//this prevetns arraindexoutofbounds 
		if (x == grid.length)
		    break;
		//stop if it finds an empty block
		if (grid[x][y] == 0)
		    break;
	    }
	    //use final x value to determine if line is full
	    if (x == grid.length){
		//line is full
		numLines ++;
		//clear full line
		clearLine(y);
	    } else if ( numLines != 0){
		//lines is not full and must be moved down
		for (int xPos = 0; xPos < grid.length; xPos++){
		    if (grid[xPos][y] == 0)
			continue;
		    //to move it down, pick the color that must drawn in its new tile position
		    grid[xPos][y-numLines] = grid[xPos][y];
		    int rgb = getBuffer().getRGB((int)(xPos)*tileSize + 12, (int)(h-y)*tileSize + 12);
		    drawTile(xPos,y-numLines, new Color(rgb));
		    //clear the original tile
		    grid[xPos][y] = 0;
		    drawTile(xPos, y, emptyColour);
		}
		
	    }
	}
	
	updateSurface(0, w-1);
	
	return numLines;
    }
    
    //clear one entrie row
    public void clearLine(int y){
	for (int x= 0; x < grid.length; x++){
	    grid[x][y] = 0;
	    drawTile(x, y, emptyColour);
	}
    }
    
    

    
    //hides the piece 
    public void clearPiece(){
	for (int[] block : p.getBlocks()){
	    drawTile(block[0] + p.cX, block[1] + p.cY, emptyColour);
	}
    }
    
    //displays the piece onto the screen
    public void drawPiece(){
	for (int[] block : p.getBlocks()){
	    drawTile(block[0] + p.cX, block[1] + p.cY, p.getColour());
	}
    }
    
    //rotates a piece
    public void rotatePiece(Piece.ROT_DIR direction){
	clearPiece();
	p.rotate(grid, direction);
	drawPiece();
    }
    
    //code to check if the block, once static goes over the y-range
    public boolean checkTop(){
	for (int[] block : p.getBlocks()){
	    if (block[1] == h-1){
		return true;
	    }
	}
	return false;
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
	
	switch (e.getKeyCode()){
	    case KeyEvent.VK_UP:
		if (!upTimer.isRunning() ){
		    upTimer.start();
		    
		    if (downTimer.isRunning())
			downTimer.stop();
		}

		break;
	    case KeyEvent.VK_DOWN:

		if (!downTimer.isRunning() ){
		    
		    downTimer.start();
		    
		    if (upTimer.isRunning())
			upTimer.stop();
		}
		break;
	    case KeyEvent.VK_LEFT:

		if (!leftTimer.isRunning() ){
		    
		    leftTimer.start();
		    
		    if (rightTimer.isRunning())
			rightTimer.stop();
		}
		break;
	    case KeyEvent.VK_RIGHT:

		if (!rightTimer.isRunning() ){
		    
		    rightTimer.start();
		    
		    if (leftTimer.isRunning())
			leftTimer.stop();
		}
		break;
	    case KeyEvent.VK_SPACE:
		quickDrop();
		break;
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	switch (e.getKeyCode()){
	    case KeyEvent.VK_UP:
		if (upTimer.isRunning()){
		    upTimer.stop();
		}
		break;
	    case KeyEvent.VK_DOWN:
		if (downTimer.isRunning()){
		    downTimer.stop();
		}
		break;
	    case KeyEvent.VK_LEFT:
		if (leftTimer.isRunning() ){
		    leftTimer.stop();
		} 
		break;
	    case KeyEvent.VK_RIGHT:
		if (rightTimer.isRunning() ){
		    rightTimer.stop();
		}
		break;
	}
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
