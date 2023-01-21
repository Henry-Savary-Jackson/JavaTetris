package henry.savaryjackson.javatetris.Main;

import javax.swing.SwingUtilities;
import henry.savaryjackson.javatetris.GUI.Screen;
import henry.savaryjackson.javatetris.GUI.TetrisGrid;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
	// TODO code application logic here
	SwingUtilities.invokeLater(() -> {
	    //Screen screen = new Screen();
	    //screen.setVisible(true);
	    JFrame frame = new JFrame("lol") ;
	    frame.setSize(800, 800);
	    TetrisGrid grid = new TetrisGrid(10, 10);
	    grid.setVisible(true);
	    frame.add(grid);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    frame.setVisible(true);
	});   
    }
    
}
