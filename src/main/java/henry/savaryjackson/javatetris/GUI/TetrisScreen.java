
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
    
        
    private Timer leftTimer; 
    private Timer rightTimer;
    private Timer upTimer;
    private Timer downTimer;
    
    public final static int KEY_DELAY = 90;
    
    private Piece.tetrominoes hold;
    private boolean switchable = true;

    private boolean paused;
    private boolean inSession; 
    private Timer clock;
    private Piece p;
    private Piece.tetrominoes nextTetrominoe;
    private int delay = 400;
    
    private int level = 1;
    private int points = 0;
    private int linesCleared = 0;
    
    private List<int[]> surfaceBlocks = new ArrayList<>();
    private int highestBlock = 1;
    
    


    public TetrisScreen(int w, int h )  {
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
	createPiece(Utils.randTetrominoe());
	updateSurface(0, w-1);
	
	//this timer iswhere all of the game logic is running for the screen
	clock = new Timer(delay, 
	    (evt)->{		
		if (!isPaused()){
		    //if the block hits the bottom
		    if (checkBottom()){
			if(checkTop()){
			    //if hits top then gameover
			    gameOver();
			}
			
			fixPiece();
			//updates the surface on which falling onto causes block to become solid
			updateSurface(getPiece().getBottomSpan()[0] + getPiece().cX, getPiece().getBottomSpan()[1] + getPiece().cX);
			//generate new piece and next one randomly
			createPiece(getNextTetrominoe());
			drawPiece();
			//if piece spawns on taken block, then game over
			if (!Utils.notTaken(grid, p))
			    gameOver();
			
			//update any lines that have been cleared by user
			int lines = clearAllFullLines();
			updateDelay(level);
			linesCleared += lines;
			//level = 1+ Math.divideExact(linesCleared, 5);
			// TODO: fix this shit
			level = 2;
			updateDelay(level);
			incrScore(lines);
			
			if (!switchable)
			    switchable = true;
	
		    } else {
			movePiece(0, -1);
			
		    }
		}
	    }
	);
	updateDelay(level);
	clock.setInitialDelay(0);
	clock.start();
	
	
	
	initComponents();
	
	addKeyListener(this);
	setFocusable(true);
	
	setVisible(true);
	
    }
    
    
    private void holdPiece(){

	if (hold == null){
	    clearPiece();
	    hold = p.getTetr();
	    createPiece(getNextTetrominoe());
	    drawPiece();
	}else if (switchable) {
	    clearPiece();
	    Piece.tetrominoes tTemp = p.getTetr();
	    createPiece(hold);
	    drawPiece();
	    hold =tTemp;
	    switchable = false;

	}
	
    }    

    
    public void restart(){
	initTiles();
        updateSurface(0, w-1);
        createPiece(Utils.randTetrominoe());
        clock.start();
        inSession = true;
	paused = false;
	
    }
    
    //generates the delay between falling updates depending on the level the user is on.
    //using a logistic equation to do so
    private void updateDelay(int lvl){
	delay =  (int)( 429.5- ( 379.5/ (1+ Math.exp( (-00.15)*(lvl - 16.3) )) ) ) ;
	clock.setDelay(delay);
    }
    
    //initialises the current falling piece
    private void createPiece(Piece.tetrominoes tetr){
	p = isInSession()?  p = new Piece(tetr,(int)(w/2), 24):new Piece(Utils.randTetrominoe(),(int)(w/2), 24);
	updateNextTetr();
    }
    
    //code for a game over once it occurs
    public void gameOver(){
	clock.stop();
	//the current session is now over and the game is paused
	setInSession(false);
	setPaused(true);
	System.out.println("game over");			    
    }
    
    
    //updates the next piece the game will spawn and displays it on the preview grid
    private void updateNextTetr(){
	nextTetrominoe = Utils.randTetrominoe();
    }
    
    //sets the piece block positions to solid tiles
    //and updates the highest block y pos
    private void fixPiece(){
	int iHighest = 0;
	for (int[] block : getPiece().getBlocks()){
	    if (block[1] + getPiece().cY > iHighest)
		iHighest = block[1] + getPiece().cY;
	    
	    grid[block[0] + getPiece().cX][block[1] + getPiece().cY] = 1;
	}
	
	if (iHighest > highestBlock)
	    highestBlock = iHighest;
    }
    
    
    
    //checks to see whether the current piece sits on the bottom
    private boolean checkBottom(){
	for (int[] block : getPiece().getBlocks()){
	    for (int[] surfaceBlock : surfaceBlocks){
		if (surfaceBlock[0] == block[0]+ getPiece().cX && surfaceBlock[1] == block[1] + getPiece().cY)
		    return true;
	    }
	}
	return false;
    }
    
    //code that handles moving the piece and rendering that change
    private void movePiece(int xDiff, int yDiff){
	clearPiece();
	getPiece().moveTo(grid, getPiece().cX + xDiff, getPiece().cY+yDiff);
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
    private void updateSurface(int xBegin, int xEnd){
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
    private void quickDrop(){
	int[] span = getPiece().getBottomSpan();
	int yLowestHeightDiff = Integer.MAX_VALUE;
	//within the x-range of the piece that faces the bottom
	//find what is the lowest amount height needed to be lost 
	//in order to touch the surface
	for (int x = span[0] + getPiece().cX; x <=span[1] + getPiece().cX; x++){
	    int y =1;
	    int yHighestinCol = y;
	    for (; y <= getPiece().cY; y ++){
		if (grid[x][y] == 1){
		    if (y+1 > yHighestinCol){
			yHighestinCol = y+1;
		    }
		}
	    }
	    int yOfPiece = getPiece().getBottomBlocks().get(x-getPiece().cX);
	    if ( (getPiece().cY+ yOfPiece) - yHighestinCol < yLowestHeightDiff){
		yLowestHeightDiff = (getPiece().cY+ yOfPiece) - yHighestinCol;
	    }
	}
	clearPiece();
	//lower the piece by the necessary height
	p.cY -= (yLowestHeightDiff);
	
	//redraw piece
	drawPiece();
	
    }
    
    //clears all the lines that are full once a piece is placed
    private int clearAllFullLines(){
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
		    int rgb = buffer.getRGB((int)(xPos)*tileSize + 12, (int)(h-y)*tileSize + 12);
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
    private void clearLine(int y){
	for (int x= 0; x < grid.length; x++){
	    grid[x][y] = 0;
	    drawTile(x, y, emptyColour);
	}
    }
    
    

    
    //hides the piece 
    private void clearPiece(){
	for (int[] block : getPiece().getBlocks()){
	    drawTile(block[0] + getPiece().cX, block[1] + getPiece().cY, emptyColour);
	}
    }
    
    //displays the piece onto the screen
    private void drawPiece(){
	for (int[] block : getPiece().getBlocks()){
	    drawTile(block[0] + getPiece().cX, block[1] + getPiece().cY, getPiece().getColour());
	}
    }
    
    //rotates a piece
    private void rotatePiece(Piece.ROT_DIR direction){
	clearPiece();
	getPiece().rotate(grid, direction);
	drawPiece();
    }
    
    //code to check if the block, once static goes over the y-range
    private boolean checkTop(){
	for (int[] block : getPiece().getBlocks()){
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
	    case KeyEvent.VK_C:
		holdPiece();
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

    /**
     * @return the delay
     */
    public int getDelay() {
	return delay;
    }

    /**
     * @param delay the delay to set
     */
    public void setDelay(int delay) {
	this.delay = delay;
    }

    /**
     * @return the points
     */
    public int getPoints() {
	return points;
    }

    /**
     * @return the linesCleared
     */
    public int getLinesCleared() {
	return linesCleared;
    }

    /**
     * @return the p
     */
    public Piece getPiece() {
	return p;
    }

    /**
     * @return the nextTetrominoe
     */
    public Piece.tetrominoes getNextTetrominoe() {
	return nextTetrominoe;
    }

    /**
     * @return the paused
     */
    public boolean isPaused() {
	return paused;
    }

    /**
     * @param paused the paused to set
     */
    public void setPaused(boolean paused) {
	this.paused = paused;
    }

    /**
     * @return the inSession
     */
    public boolean isInSession() {
	return inSession;
    }

    /**
     * @param inSession the inSession to set
     */
    public void setInSession(boolean inSession) {
	this.inSession = inSession;
    }

    /**
     * @return the level
     */
    public int getLevel() {
	return level;
    }

    /**
     * @return the hold
     */
    public Piece.tetrominoes getHold() {
	return hold;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
