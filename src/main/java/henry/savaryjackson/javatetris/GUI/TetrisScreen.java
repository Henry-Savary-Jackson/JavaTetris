
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

    boolean paused;
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
    
    private long lastKeyPress = 0;
    private int highestBlock = 1;
    
    


    public TetrisScreen(int w, int h , TetrisGrid nextgrid, Screen OuterFrame) {
	super(w, h);
	paused = true;
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
			//updates the surface on which falling onto causes block to sbecome solid
			updateSurface(p.getBottomSpan()[0] + p.cX, p.getBottomSpan()[1] + p.cX);
			//generate new piece and next randomly
			createPiece();
			drawPiece();
			//if piece spawns on taken block, then game over
			if (!Utils.notTaken(grid, p))
			    gameOver();
			
			
			
			//update any lines that have cleared by user
			int [] lines = checkLines();
			linesCleared += lines[1]-lines[0];
			incrScore(lines[1]-lines[0]);
			screen.updateUI();
			clearLines(lines[0], lines[1]);
			
	
		    } else {
			movePiece(0, -1);
			
		    }
		    
		    
		}
	    }
	);
	clock.setInitialDelay(0);
	
	initComponents();
	
	//keyListener needs this to work
	setFocusable(true);
	
	addKeyListener(this);
	
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
    private void movePiece(int xDiff, int yDiff){
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
	int yHighest = 1;
	List<Integer> highests = new ArrayList<>();
	int xHighest = span[0] + p.cX;
	//within the x-range of the piece that faces the bottom, find what the highest 
	//y position of a solid tile is and its corresponding x-value
	for (int x = span[0] + p.cX; x <=span[1] + p.cX; x++){
	    int y =1;
	    int yHighestinCol = y;
	    for (; y <= highestBlock; y ++){
		if (grid[x][y] == 1){
		    if (y+1 > yHighestinCol){
			yHighestinCol = y+1;
		    }
		}
	    }
	    if (yHighestinCol >= yHighest){
		xHighest = x;
		yHighest = yHighestinCol;
		highests.add(yHighestinCol);
	    }
	}
	clearPiece();
	//a list to see whether all the tiles are level withing the piece's bottom-facing range
	highests.removeIf( (i)-> {return i.equals(highests.get(0));});
	if (highests.isEmpty()){
	    //ground is level: place the piece's lowest block to the surface
	    int yLowest = Integer.MAX_VALUE;
	    for ( int[] b : p.getBottomBlocks()){
		int bY = b[1];
		if (bY < yLowest){
		    yLowest = bY;
		}
		
	    }
	    p.cY = yHighest - yLowest;

	} else{
	    //ground is not level: place the ground's highest tile against the piece
	    int yLowest = Integer.MAX_VALUE ;
	    for (int[] block : p.getBottomBlocks()){
		if (block[0] + p.cX == xHighest){
		    if (block[1] < yLowest){
			yLowest = block[1];
		    }
		}
	    }
	    p.cY = yHighest-yLowest;

	}
	
	//redraw piece
	drawPiece();
    }
    
    //gives the range of lines to be cleared, if any, once a piece hits the grounds
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
		 //bottom is inclusive top is exclusive
		return new int[]{y, y+j};
	    }
	    
	}
	return new int[]{0, 0};
    }
    
    //clears all the lines that must be cleared on a piece is placed
    //bottom is inclusive top is exclusive
    public void clearLines( int bottom, int top){
	if (bottom == top)
	    return;
	
	//clears all lines in the range that is full
	for (int y = bottom; y < top; y++){
	    for (int x= 0; x < grid.length; x++){
		grid[x][y] = 0;
		drawTile(x, y, emptyColour);
	    }
	}
	
	//move all lines above that range below and clear lines above
	int numLines = top -bottom;
	for (int y = top; y<= highestBlock; y++){
	    for (int x= 0; x< grid.length; x++){
		if (grid[x][y] == 0)
		    continue;
		//move tile to new pos below
		
		//pick the color that must drawn in the new tile position
		grid[x][y-numLines] = grid[x][y];
		int rgb = getBuffer().getRGB((int)(x)*tileSize + 12, (int)(h-y)*tileSize + 12);
		drawTile(x,y-numLines, new Color(rgb));
		//clear tile
		grid[x][y] = 0;
		drawTile(x, y, emptyColour);
	    }
	}
	
	updateSurface(0, w-1);
	
	
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
    
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    //input handling 
    @Override
    public void keyPressed(KeyEvent e) {
	if (lastKeyPress != 0){
	    long time = System.currentTimeMillis();
	    long diff = time - lastKeyPress;
	    if (diff  >= 40){
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
			quickDrop();
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
