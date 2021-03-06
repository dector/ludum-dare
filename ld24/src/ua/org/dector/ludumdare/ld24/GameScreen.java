package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

import static com.badlogic.gdx.Input.*;

/**
 * @author dector
 */
public class GameScreen implements Screen, InputProcessor {
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
        level.load(Levelset.getLevel());
        level.started = true;
        
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
                if (! level.paused) {
                    level.player.direction = Direction.LEFT;

                    if (level.player.state == State.RUNNING)
                        level.player.ax -= Player.RUNNING;
                    else if (level.player.state == State.SWIM)
                        level.player.ax -= 5 * Player.SWIMMING;
                }
            } break;
            case Keys.RIGHT: {
                if (! level.paused) {
                    level.player.direction = Direction.RIGHT;

                    if (level.player.state == State.RUNNING)
                        level.player.ax += Player.RUNNING;
                    else if (level.player.state == State.SWIM)
                        level.player.ax += 5 * Player.SWIMMING;
                }
            } break;
            case Keys.UP: {
                if (! level.paused) {
                    if (level.player.state == State.RUNNING)
                        level.player.jump();
                    else if (level.player.state == State.SWIM)
                        level.player.vy += Player.SWIMMING / 2;
                }
            } break;
            case Keys.DOWN: {
                if (! level.paused) {
                    if (level.player.state == State.SWIM)
                        level.player.vy -= Player.SWIMMING / 2;
                    else if (! level.player.gravityAffection) {
                        level.player.clearSlick();
//                        level.wasCollided = false;
                    }
                }
            } break;
            case Keys.R: {
                renderer.restart();
            } break;
            case Keys.ENTER: {
                level.paused = ! level.paused;
            } break;
            case Keys.M: {
                Sounds.get().mutedOff = ! Sounds.get().mutedOff;
            } break;
            case Keys.SPACE: {
                if (level.player.win || Debug.DEBUG) {
                    if (Levelset.isLast()) {
                        Levelset.restart();
                    } else {
                        Levelset.next();
                    }
                    
                    level.started = true;
                    level.restart();
                } else if (level.started) {
                    level.started = false;
                }
            } break;
            case Keys.F2: {
//                Debug.DEBUG = ! Debug.DEBUG;
            } break;
            case Keys.NUM_1: if (Debug.DEBUG) level.getAbility(Ability.SWIM); break;
            case Keys.NUM_2: if (Debug.DEBUG) level.getAbility(Ability.GAS); break;
            case Keys.NUM_3: if (Debug.DEBUG) level.getAbility(Ability.SLICK); break;
            case Keys.NUM_4: if (Debug.DEBUG) level.getAbility(Ability.SOLID); break;
            case Keys.NUM_5: if (Debug.DEBUG) level.getAbility(Ability.LIQUID); break;
            case Keys.NUM_6: if (Debug.DEBUG) level.getAbility(Ability.NORMAL); break;
            case Keys.F5: if (Debug.DEBUG) {
                Debug.savedX = (int)level.player.x;
                Debug.savedY = (int)level.player.y;
                Debug.abilities.clear();
                Debug.abilities.addAll(level.player.abilities);
            } break;
            case Keys.F9: if (Debug.DEBUG) {
                level.player.x = Debug.savedX;
                level.player.y = Debug.savedY;
                level.player.abilities.clear();
                level.player.abilities.addAll(Debug.abilities);
            } break;
            case Keys.P: if (Debug.DEBUG) { Levelset.prev(); level.restart(); } break;
        }
        
        return true;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.LEFT: {
                if (! level.paused) {
                    level.player.ax = 0;
                }
            } break;
            case Keys.RIGHT: {
                if (! level.paused) {
                    level.player.ax = 0;
                }
            } break;
            case Keys.UP: {
                if (! level.paused) {
                    if (level.player.state == State.SWIM)
                        level.player.ay = 0;
                }
            } break;
            case Keys.DOWN: {
                if (! level.paused) {
                    if (level.player.state == State.SWIM)
                        level.player.ay = 0;
                }
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
