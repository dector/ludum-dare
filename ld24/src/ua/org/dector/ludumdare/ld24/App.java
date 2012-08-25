package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * @author dector
 */
public class App {
    public static final int SCREEN_WIDTH    = 640;
    public static final int SCREEN_HEIGHT   = 480;
    public static final String APP_TITLE    = "Ludum Dare #24";

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width    = SCREEN_WIDTH;
        config.height   = SCREEN_HEIGHT;
        config.title    = APP_TITLE;
        config.resizable = false;

        new LwjglApplication(new MyGame(), config);
    }
}
