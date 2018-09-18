package launcher;

import controller.MainController;
import view.MainFrame;

/**
 * Created by vasil_000 on 01.04.2018.
 */
public class AppLauncher {

    public static void main(String[] args) {
        MainController controller = new MainController();
        MainFrame view = new MainFrame(controller);
        controller.setView(view);
    }
}

