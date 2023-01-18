package henry.savaryjackson.javatetris.Main;

import javax.swing.SwingUtilities;
import henry.savaryjackson.javatetris.GUI.Screen;

public class Main {
    public static void main(String[] args) {
	// TODO code application logic here
	SwingUtilities.invokeLater(() -> {
	    Screen screen = new Screen();
	    screen.setVisible(true);});   
    }
    
}
