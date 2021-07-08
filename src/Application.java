import ui.ApplicationStarter;

import javax.swing.*;

/**
 * The Twitter viewer application
 * Derived from a JMapViewer demo program written by Jan Peter Stotz
 * This class will be used to instantiate an ApplicationStarter object
 * The ApplicationStarter object will be responsible for everything related to the UI
 */
public class Application extends JFrame {

    /**
     * @param args Application program arguments (which are ignored)
     */
    public static void main(String[] args) {
        new ApplicationStarter().setVisible(true);
    }


}
