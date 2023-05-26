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
import javax.swing.Timer;
import javax.swing.border.BevelBorder;


public class Screen extends JFrame{

    
    private JButton btnPlay = new JButton("Play");
    private JButton btnStop  = new JButton("Stop");
    private JPanel pnlUI = new JPanel();
    private JPanel pnlHold = new JPanel();
    private JLabel lblLevel = new JLabel("Level:");
    private JLabel lblLines = new JLabel("Lines:");
    private JLabel lblNextPiece = new JLabel("Next Piece:");
    private JLabel lblScore = new JLabel("Score:");
    private JLabel lblHold = new JLabel("Hold:");
    
    private TetrisGrid grid;
    private TetrisScreen screen;
    private TetrisGrid hold;
    
    private GroupLayout layout;
    
    private Timer UIupdateClock;
    
    private String userID;
    
    public Screen(String userID){
	super("Tetris");
	
	//setAccount
	this.userID = userID;
	
	//init tetris grids
	grid = new TetrisGrid(6,6);
	hold = new TetrisGrid(6,6);
	screen = new TetrisScreen(10, 24);
	
	
	initUI();
	
	//set clock to update the score info
	UIupdateClock = new Timer(15, (evt)-> {updateUI();});
	UIupdateClock.start();
	
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
	pnlHold.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	getContentPane().setBackground(Color.LIGHT_GRAY);
	
	//frame layout
	layout = new GroupLayout(getContentPane());
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	layout.setHorizontalGroup(layout.createSequentialGroup()
		.addComponent(pnlHold)
		.addGap(70)
		.addComponent(screen)
		.addGap(70)
		.addComponent(pnlUI)
	);
	
	layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pnlHold).addGap(125).addComponent(screen).addGap(125).addComponent(pnlUI));
	
	//pnlHold layout
	
	GroupLayout pnlHoldLayout = new GroupLayout(pnlHold);
	pnlHoldLayout.setAutoCreateGaps(true);
	
	pnlHoldLayout.setVerticalGroup(
		pnlHoldLayout.createSequentialGroup()
		.addComponent(lblHold)
		.addComponent(hold,hold.getPreferredSize().height, GroupLayout.PREFERRED_SIZE, getHeight())
	);
	
	pnlHoldLayout.setHorizontalGroup(
		pnlHoldLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
		.addComponent(lblHold)
		.addComponent(hold,100, GroupLayout.PREFERRED_SIZE, 150)
	);
	pnlHold.setLayout(pnlHoldLayout);
	
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
	lblHold.setFont(new Font("Futura", Font.BOLD, 20));
	btnPlay.setFont(new Font("Futura", Font.BOLD, 20));
	btnStop.setFont(new Font("Futura", Font.BOLD, 20));
	
	pack();
	updateUI();
    }
    
    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {                                        

        if (screen.isPaused()){
            dispose();
            System.exit(0);
        }else {
            btnStop.setText("Quit");
            screen.setPaused(true);
        }
    }                                       
    private void updateUI(){
	lblScore.setText(String.format("Score: %d", screen.getPoints()));
	lblLines.setText(String.format("Lines: %d" , screen.getLinesCleared()));
	lblLevel.setText(String.format("Level: %d" , screen.getLevel()));
	if (screen.isPaused()){
	    btnStop.setText("Quit");
	}
	if (!screen.isPaused()){
	    grid.initTiles();
	    grid.drawTetrominoe(screen.getNextTetrominoe(), Math.floorDiv(grid.getW(),2),Math.floorDiv(grid.getH(),2));
	    if (screen.getHold() != null){
		hold.initTiles();
		hold.drawTetrominoe(screen.getHold(),  Math.floorDiv(hold.getW(),2),Math.floorDiv(hold.getH(),2));
	    }
	    
	}
	lblLevel.repaint();
	lblScore.repaint();
	lblLines.repaint();
	btnStop.repaint();
	
    }
    
    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) { 
	
	screen.setPaused(false);
	btnStop.setText("Pause");
	screen.requestFocus();
	if (!screen.isInSession()){ 
	   screen.restart();
	}
	screen.setInSession(true);

    }   


}
