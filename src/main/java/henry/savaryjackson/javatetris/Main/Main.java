package henry.savaryjackson.javatetris.Main;

import henry.savaryjackson.javatetris.GUI.TetrisGrid;
import javax.swing.SwingUtilities;
import henry.savaryjackson.javatetris.GUI.TetrisScreen;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
	// TODO code application logic here
	SwingUtilities.invokeLater(() -> {
	    //Screen screen = new Screen();
	    //screen.setVisible(true);
	    JFrame frame = new JFrame("lol") ;
	    frame.setSize(800, 800);
	    TetrisScreen grid = new TetrisScreen(10, 24);
	    
	    frame.add(grid);
	    
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    frame.setVisible(true);
	});   
    }
    
}
