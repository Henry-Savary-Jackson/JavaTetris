/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

package henry.savaryjackson.javatetrisproject.GUI;

import henry.savaryjackson.javatetris.utils.Piece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import henry.savaryjackson.javatetris.utils.Piece.tetrominoes;
import java.awt.Dimension;
import java.util.List;

/**
 *
 * @author hsavaryjackson
 */
public class TetrisGrid extends javax.swing.JPanel {

    /** Creates new form TetrisGrid */
    protected int w,h;
    protected byte[][] grid;
    
    protected final int tileSize = 24;
    protected final int innerTileSize =20;
    
    protected final Color borderColour = Color.DARK_GRAY;
    protected final Color emptyColour = Color.BLACK;
    
    protected BufferedImage buffer;

    public TetrisGrid(int w ,int h) {
	this.h = h;
	this.w = w;
	grid = new byte[w][h+10];
	setPreferredSize(new Dimension((w)*tileSize, (h)*tileSize));
        initComponents();
	buffer = initBuffer();
	initTiles();
	setVisible(true);
	
    }
    
    public void drawTetrominoe(tetrominoes t, int cX, int cY){
	List<int[]> blocks = Piece.tetrStructure.get(t);
	Color color = Piece.tetrColour.get(t);

	blocks.forEach((b)->{
	    int xIndex = cX + b[0];
	    int yIndex = cY + b[1];
	    drawTile(xIndex, yIndex , color);
	});
    }
    
    @Override
    public void paintComponent(Graphics g){
	super.paintComponent(g);
	if (getBuffer() != null)
	    g.drawImage(getBuffer(), 0, 0, this);
    }
    
    protected void drawTile(int x, int  y , Color c){
	Graphics2D g2 = getBuffer().createGraphics();
	
	//draw main
	int height = getH() * tileSize;
	
	g2.setColor(c);
	g2.fillRect((x*tileSize)+2, (height -(y*tileSize)+2), innerTileSize, innerTileSize);
	
	g2.dispose();
	repaint((x*tileSize)+2, (height -(y*tileSize)+2), innerTileSize, innerTileSize);
    }
    
    
    protected BufferedImage initBuffer(){
	
	BufferedImage output = new BufferedImage(getW()*tileSize, getH()*tileSize, BufferedImage.TYPE_INT_RGB);
	Graphics2D g2 = output.createGraphics();
	g2.setColor(borderColour);
	g2.fillRect(0,0, output.getWidth(), output.getHeight());
	g2.dispose();
	return output;
    }
    
    protected void initTiles(){
	for (int x = 0; x < getGrid().length; x++){
	    for (int y = 1; y < getGrid()[0].length; y++){
		grid[x][y] = 0;
		drawTile(x, y, emptyColour);
	    }
	}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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

    /**
     * @return the buffer
     */
    public BufferedImage getBuffer() {
	return buffer;
    }

    /**
     * @return the w
     */
    public int getW() {
	return w;
    }

    /**
     * @return the h
     */
    public int getH() {
	return h;
    }

    /**
     * @return the grid
     */
    public byte[][] getGrid() {
	return grid;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
