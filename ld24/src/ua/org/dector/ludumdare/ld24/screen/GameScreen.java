package ua.org.dector.ludumdare.ld24.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import ua.org.dector.ludumdare.ld24.Level;
import ua.org.dector.ludumdare.ld24.Player;
import ua.org.dector.ludumdare.ld24.Renderer;

/**
 * @author dector
 */
public class GameScreen implements Screen {
    public static final String LEVEL_FILE = "ld24/data/level0.png";
    
    Level level;
    Renderer renderer;
    
    public void render(float v) {
        renderer.render(v);
    }

    public void resize(int i, int i1) {
    }

    public void show() {
        level = new Level();
        level.load(LEVEL_FILE);
        
        renderer = new Renderer(level);
    }

    public void hide() {
        dispose();
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }
}
