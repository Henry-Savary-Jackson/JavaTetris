/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;


public class Screen extends JFrame {
    
    private JButton btnPlay = new JButton("Play");
    private JButton btnStop  = new JButton("Stop");
    private JPanel pnlUI = new JPanel();
    private JLabel lblLevel = new JLabel("Level:");
    private JLabel lblLines = new JLabel("Lines:");
    private JLabel lblNextPiece = new JLabel("Next Piece:");
    private JLabel lblScore = new JLabel("Score:");
    
    private TetrisGrid grid;
    private TetrisScreen screen;
    
    private GroupLayout layout;
    
    public Screen(){
	super("Tetris");
	//init tetris grids
	grid = new TetrisGrid(6,6);
	screen = new TetrisScreen(10, 24, grid, this);
	
	initUI();
	
	//change some other settings
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(false);
	setVisible(true);
    }
    
    private void initUI(){
	//set borders
	screen.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	grid.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	pnlUI.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	getContentPane().setBackground(Color.LIGHT_GRAY);
	
	//frame layout
	layout = new GroupLayout(getContentPane());
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	layout.setHorizontalGroup(layout.createSequentialGroup()
		.addComponent(screen)
		.addGap(70)
		.addComponent(pnlUI)
	);
	
	layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(screen).addGap(125).addComponent(pnlUI));
	
	
	
	getContentPane().setLayout(layout);
	
	
	//pnlUi layout
	GroupLayout pnlUILayout = new GroupLayout(pnlUI);
	pnlUILayout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	pnlUILayout.setVerticalGroup(pnlUILayout.createSequentialGroup()
		.addComponent(lblNextPiece)
		.addComponent(grid,grid.getPreferredSize().height, GroupLayout.PREFERRED_SIZE, getHeight())
		.addGap(100)
		.addComponent(lblLevel)
		.addComponent(lblLines)
		.addComponent(lblScore)
		.addGap(100)
		.addComponent(btnPlay)
		.addComponent(btnStop)
	);
	
	pnlUILayout.setHorizontalGroup(pnlUILayout.createParallelGroup(GroupLayout.Alignment.CENTER)
		.addComponent(lblNextPiece)
		.addComponent(grid, 100, GroupLayout.PREFERRED_SIZE, 150)
		.addGap(100)
		.addComponent(lblLevel)
		.addComponent(lblLines)
		.addComponent(lblScore)
		.addGap(100)
		.addComponent(btnPlay)
		.addComponent(btnStop)
	);
	pnlUI.setLayout(pnlUILayout);
	
	//button action listeners
	btnPlay.addActionListener((e)-> {btnPlayActionPerformed(e);});
	btnStop.addActionListener((e)-> {btnStopActionPerformed(e);});
	
	
	//setting fonts
	lblNextPiece.setFont(new Font("Futura", Font.BOLD, 20));
	lblLevel.setFont(new Font("Futura", Font.BOLD, 20));
	lblLines.setFont(new Font("Futura", Font.BOLD, 20));
	lblScore.setFont(new Font("Futura", Font.BOLD, 20));
	btnPlay.setFont(new Font("Futura", Font.BOLD, 20));
	btnStop.setFont(new Font("Futura", Font.BOLD, 20));
	pack();
	updateUI();
    }
    
    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {                                        

        if (screen.paused){
            dispose();
            System.exit(0);
        }else {
            btnStop.setText("Quit");
            screen.paused = true;
        }
    }                                       
 public void updateUI(){
	lblScore.setText(String.format("Score: %d", screen.points));
	lblLines.setText(String.format("Lines: %d" , screen.linesCleared));
	if (screen.paused){
	    btnStop.setText("Quit");
	}
	repaint();
	
    }
    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) { 
	
	
       //switch the whether the screen is paused
        screen.paused = !screen.paused;

        //if the game is paused, and the game has not yet started or is over
        if (!screen.inSession){
            screen.initTiles();
            screen.updateSurface(0, screen.w-1);
            screen.createPiece();
            screen.clock.start();
            screen.inSession = true;

        }
        if (!screen.paused)
        btnStop.setText("Pause");

       screen.requestFocus();

    }      
}
