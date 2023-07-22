/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.GUI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import henry.savaryjackson.javatetrisproject.utils.AudioUtils.MP3Player;
import henry.savaryjackson.javatetrisproject.utils.AudioUtils.MusicPlayer;
import henry.savaryjackson.javatetrisproject.utils.WebUtils.APIUtils;
import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.CompletableFuture;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class Screen extends JFrame {

    private JButton btnPlay = new JButton("Play");
    private JButton btnStop = new JButton("Stop");

    private JButton btnUserDetails = new JButton("Your Account");

    private JPanel pnlUI = new JPanel();
    private JPanel pnlHold = new JPanel();
    private JLabel lblLevel = new JLabel("Level:");
    private JLabel lblLines = new JLabel("Lines:");
    private JLabel lblNextPiece = new JLabel("Next Piece:");
    private JLabel lblScore = new JLabel("Score:");
    private JLabel lblHold = new JLabel("Hold:");
    private JLabel lblHighscore = new JLabel("High Score:");

    private TetrisGrid grid;
    private TetrisScreen screen;
    private TetrisGrid hold;

    private GroupLayout layout;

    private int highScore = 0;

    public Screen() {
	super("Tetris");

	MusicPlayer.setUp();

	//init tetris grids
	grid = new TetrisGrid(6, 6);
	hold = new TetrisGrid(6, 6);
	screen = new TetrisScreen(10, 24);

	// all this messy code to retrieve high score for server
	// due to all the error handling with joptionpanes
	try {
	    JsonObject userData = APIUtils.getInfo(ApplicationContext.getToken());

	    if (userData.has("high_score")) {
		highScore = userData.getAsJsonPrimitive("high_score").getAsInt();
		screen.setHighScore(highScore);

	    }
	} catch (NullPointerException | WebClientRequestException ex) {
	    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	} catch (WebClientResponseException ex) {
	    JsonObject body = ex.getResponseBodyAs(JsonObject.class);
	    if (body == null) {
		JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    if (body.has("Status")) {
		JOptionPane.showMessageDialog(this, body.getAsJsonPrimitive("Status").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);

	    } else {
		JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

	    }

	}

	initUI();

	//change some other settings
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(false);
	setVisible(true);
    }

    public void sendScore(int newScore) {
	//update high score on server
	final Screen thisScreen = this;
	new Thread(() -> {
	    try {
		
		JsonObject responseJson = APIUtils.updateHighScore(ApplicationContext.getToken(), newScore);
		if (responseJson.has("high_score")) {
		    screen.setHighScore(responseJson.getAsJsonPrimitive("high_score").getAsInt());
		    thisScreen.updateUI();
		}
	    } catch (NullPointerException | WebClientRequestException e) {
		JOptionPane.showMessageDialog(thisScreen, e.getMessage(),
			"Error", JOptionPane.ERROR_MESSAGE);
	    } catch (WebClientResponseException ex) {
		JsonObject body = (JsonObject) JsonParser.parseString(ex.getResponseBodyAsString());
		if (body == null) {
		    JOptionPane.showMessageDialog(thisScreen, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		if (body.has("Status")) {
		    String status = body.getAsJsonPrimitive("Status").getAsString();
		    JOptionPane.showMessageDialog(thisScreen, status, "Error", JOptionPane.ERROR_MESSAGE);
		    ApplicationContext.getLoginScreen().setVisible(true);
		    ApplicationContext.disposeMainScreen();
		} else {
		    JOptionPane.showMessageDialog(thisScreen, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    
		}
	    }
	}).start();
		
    }

    private void initUI() {
	//set borders
	screen.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	getNextPieceGrid().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
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
		.addGap(70)
		.addComponent(btnUserDetails)
	);

	layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
		addComponent(pnlHold).addGap(125).
		addComponent(screen).addGap(125).
		addComponent(pnlUI).addGap(125)
		.addComponent(btnUserDetails));

	//pnlHold layout
	GroupLayout pnlHoldLayout = new GroupLayout(pnlHold);
	pnlHoldLayout.setAutoCreateGaps(true);

	pnlHoldLayout.setVerticalGroup(pnlHoldLayout.createSequentialGroup()
		.addComponent(lblHold)
		.addComponent(getHold(), getHold().getPreferredSize().height, GroupLayout.PREFERRED_SIZE, getHeight())
	);

	pnlHoldLayout.setHorizontalGroup(pnlHoldLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
		.addComponent(lblHold)
		.addComponent(getHold(), 100, GroupLayout.PREFERRED_SIZE, 150)
	);
	pnlHold.setLayout(pnlHoldLayout);

	getContentPane().setLayout(layout);

	//pnlUi layout
	GroupLayout pnlUILayout = new GroupLayout(pnlUI);
	pnlUILayout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	pnlUILayout.setVerticalGroup(pnlUILayout.createSequentialGroup()
		.addComponent(lblNextPiece)
		.addComponent(getNextPieceGrid(), getNextPieceGrid().getPreferredSize().height, GroupLayout.PREFERRED_SIZE, getHeight())
		.addGap(100)
		.addComponent(lblLevel)
		.addComponent(lblLines)
		.addComponent(lblScore)
		.addComponent(lblHighscore)
		.addGap(100)
		.addComponent(btnPlay)
		.addComponent(btnStop)
	);

	pnlUILayout.setHorizontalGroup(pnlUILayout.createParallelGroup(GroupLayout.Alignment.CENTER)
		.addComponent(lblNextPiece)
		.addComponent(getNextPieceGrid(), 100, GroupLayout.PREFERRED_SIZE, 150)
		.addGap(100)
		.addComponent(lblLevel)
		.addComponent(lblLines)
		.addComponent(lblScore)
		.addComponent(lblHighscore)
		.addGap(100)
		.addComponent(btnPlay)
		.addComponent(btnStop)
	);
	pnlUI.setLayout(pnlUILayout);

	//button action listeners
	btnPlay.addActionListener((e) -> {
	    btnPlayActionPerformed(e);
	});
	btnStop.addActionListener((e) -> {
	    btnStopActionPerformed(e);
	});
	btnUserDetails.addActionListener((e) -> {
	    btnUserDetailsActionPerformed(e);
	});

	//setting fonts
	lblNextPiece.setFont(new Font("Futura", Font.BOLD, 18));
	lblHighscore.setFont(new Font("Futura", Font.BOLD, 18));
	lblLevel.setFont(new Font("Futura", Font.BOLD, 18));
	lblLines.setFont(new Font("Futura", Font.BOLD, 18));
	lblScore.setFont(new Font("Futura", Font.BOLD, 18));
	lblHold.setFont(new Font("Futura", Font.BOLD, 18));
	btnPlay.setFont(new Font("Futura", Font.BOLD, 18));
	btnStop.setFont(new Font("Futura", Font.BOLD, 18));
	btnUserDetails.setFont(new Font("Futura", Font.BOLD, 18));

	pack();
	updateUI();
    }

    private void btnUserDetailsActionPerformed(java.awt.event.ActionEvent evt) {
	// pause game
	MusicPlayer.pauseMusic();
	screen.pause();
	btnStop.setText("Quit");

	// hide this screen
	this.setVisible(false);

	// load user details screen
	if (ApplicationContext.getUserDetails() == null) {
	    ApplicationContext.generateUserDetailsScreen();
	}
	ApplicationContext.getUserDetails().setVisible(true);
    }

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {

	// when pause/quit button clicked
	if (screen.isPaused()) {
	    // if you click it when the game isnt playing dispose game
	    if (ApplicationContext.getLoginScreen() != null) {
		ApplicationContext.getLoginScreen().setVisible(true);
	    }
	    ApplicationContext.disposeMainScreen();
	    // release resources from audio inputstream
	    MusicPlayer.endMusic();
	} else {
	    // if click it during play, just pause action
	    MusicPlayer.pauseMusic();
	    btnStop.setText("Quit");
	    screen.pause();
	}
    }

    public void updateUI() {
	// update lablels
	lblScore.setText(String.format("Score: %d", screen.getPoints()));
	lblLines.setText(String.format("Lines: %d", screen.getLinesCleared()));
	lblLevel.setText(String.format("Level: %d", screen.getLevel()));
	lblHighscore.setText(String.format("High Score: %d", screen.getHighScore()));

	// update the puase/quit button
	if (screen.isPaused()) {
	    btnStop.setText("Quit");
	}
	// repaint components individually
	// instead of repainting the whole panel, which would use unnecessary resources
	lblLevel.repaint();
	lblScore.repaint();
	lblLines.repaint();
	lblHighscore.repaint();
	btnStop.repaint();

    }

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {
	// when play button clicked
	if( screen.isPaused() && screen.isInSession()){
	    MusicPlayer.resumeMusic();
	}
	
	btnStop.setText("Pause");
	// make the screen start playing
	screen.requestFocus();
	screen.resume();
	// restart game if it is not in progress
	if (!screen.isInSession()) {
	    screen.restart();
	    MusicPlayer.playMusic();
	    screen.setInSession(true);
	}
	
    }

    /**
     * @return the grid
     */
    public TetrisGrid getNextPieceGrid() {
	return grid;
    }

    /**
     * @return the hold
     */
    public TetrisGrid getHold() {
	return hold;
    }

}
