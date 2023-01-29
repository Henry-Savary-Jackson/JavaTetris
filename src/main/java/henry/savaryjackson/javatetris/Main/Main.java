package henry.savaryjackson.javatetris.Main;

import henry.savaryjackson.javatetris.GUI.Screen;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
	// TODO code application logic here
	SwingUtilities.invokeLater(() -> {
	    Screen screen = new Screen();
	});   
    }
    
}
