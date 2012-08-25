package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Game;
import ua.org.dector.ludumdare.ld24.screen.GameScreen;

/**
 * @author dector
 */
public class MyGame extends Game {
    public void create() {
        setScreen(new GameScreen());
    }
}

