package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import ua.org.dector.ludumdare.ld24.Level;
import ua.org.dector.ludumdare.ld24.Player;
import ua.org.dector.ludumdare.ld24.Renderer;

/**
 * @author dector
 */
public class GameScreen implements Screen, InputProcessor {
    public static final String LEVEL_FILE = "ld24/data/level0.png";
    
    Level level;
    Renderer renderer;
    
    public void render(float v) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        
        renderer.render(v);
    }

    public void resize(int i, int i1) {
    }

    public void show() {
        level = new Level();
        level.load(LEVEL_FILE);
        
        renderer = new Renderer(level);
        
        Gdx.input.setInputProcessor(this);
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

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT: {
                level.player.direction = Direction.LEFT;
            } break;
            case Input.Keys.RIGHT: {
                level.player.direction = Direction.RIGHT;
            } break;
        }
        
        return true;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved(int x, int y) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
}
