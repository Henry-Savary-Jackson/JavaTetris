package henry.savaryjackson.javatetris.Main;

import henry.savaryjackson.javatetris.GUI.MainScreen;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
	// TODO code application logic here
	SwingUtilities.invokeLater(() -> {
	    MainScreen screen = new MainScreen();
	});   
    }
    
}
