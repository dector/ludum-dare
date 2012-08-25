package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

import static com.badlogic.gdx.Input.*;

/**
 * @author dector
 */
public class GameScreen implements Screen, InputProcessor {
    public static final String LEVEL_FILE = "ld24/data/level2.png";
    
    Level level;
    Renderer renderer;
    
    public void render(float v) {
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();

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
            case Keys.LEFT: {
                level.player.direction = Direction.LEFT;

                if (level.player.state == State.RUNNING)
                    level.player.ax -= Player.RUNNING;
                else if (level.player.state == State.SWIM)
                    level.player.ax -= 10 * Player.SWIMMING;
            } break;
            case Keys.RIGHT: {
                level.player.direction = Direction.RIGHT;

                if (level.player.state == State.RUNNING)
                    level.player.ax += Player.RUNNING;
                else if (level.player.state == State.SWIM)
                    level.player.ax += 10 * Player.SWIMMING;
            } break;
            case Keys.UP: {
                if (level.player.state == State.RUNNING)
                    level.player.jump();
                else if (level.player.state == State.SWIM)
                    level.player.vy += Player.SWIMMING;
            } break;
            case Keys.DOWN: {
                if (level.player.state == State.SWIM)
                    level.player.vy -= Player.SWIMMING;
                else if (! level.player.gravityAffection) {
                    level.player.clearSlick();
                    level.wasCollided = false;
                }
            } break;
            case Keys.R: {
                renderer.restart();
            } break;
        }
        
        return true;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.LEFT: {
                level.player.ax = 0;
            } break;
            case Keys.RIGHT: {
                level.player.ax = 0;
            } break;
            case Keys.UP: {
                if (level.player.state == State.SWIM)
                    level.player.ay = 0;
            } break;
            case Keys.DOWN: {
                if (level.player.state == State.SWIM)
                    level.player.ay = 0;
            } break;
        }

        return true;
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
