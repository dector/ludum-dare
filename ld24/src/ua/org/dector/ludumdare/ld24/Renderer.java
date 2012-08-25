package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

/**
 * @author dector
 */
public class Renderer {
    public static final int SPRITE_SIZE = 16;
    public static final int BLOCK_SIZE = 16;
    public static final int CAM_WIDTH = 640;
    public static final int CAM_HEIGHT = 480;
    
    public static final int AB_JELLY = 0;
    public static final int AB_LIQUID = 1;
    public static final int AB_GAS = 2;
    public static final int AB_SLICK = 3;
    public static final int AB_SWIM = 4;
    public static final int AB_NORMAL = 5;

    static final int PLAYER_RIGHT = 0;
    static final int PLAYER_LEFT = 1;

    public static final String GRAPHICS_FILE = "ld24/data/graphics.png";
    
    Level level;

    SpriteBatch sb;
    SpriteBatch uiSb;
    BitmapFont font;
    OrthographicCamera cam;

    TextureRegion[] playerTex;
    TextureRegion[] abilTex;
    TextureRegion blockTex;
    TextureRegion waterTex;
    TextureRegion deathTex;
    TextureRegion exitTex;
    TextureRegion levelTex;

    public Renderer(Level level) {
        this.level = level;

        sb = new SpriteBatch();
        uiSb = new SpriteBatch();
        font = new BitmapFont();

        loadResources();
        createMap();

        setupCamera();
    }

    private void setupCamera() {
        cam = new OrthographicCamera(CAM_WIDTH, CAM_HEIGHT);
        cam.position.set(level.player.x, level.player.y, 0);
    }

    private void createMap() {
        int levelTexWidth = BLOCK_SIZE * level.width;
        int levelTexHeight = BLOCK_SIZE * level.height;

        Pixmap p = new Pixmap(levelTexWidth, levelTexHeight, Pixmap.Format.RGBA8888);

        Color c = new Color();
        for (int x = 0; x < level.width; x++) {
            for (int y = level.height - 1; y >= 0; y--) {
                Tile tile = level.map[x][level.height - y - 1];

                if (tile != null) {
                    switch (tile) {
                        case BLOCK: {
                            c.set(Color.GRAY);
                        } break;
                        case WATER: {
                            Color.rgba8888ToColor(c, Level.WATER);
                        } break;
                        case EXIT: {
                            Color.rgba8888ToColor(c, Level.EXIT);
                        } break;
                        case DEATH: {
                            Color.rgba8888ToColor(c, Level.DEATH);
                        } break;
                        case SPAWN: {
                            Color.rgba8888ToColor(c, Level.SPAWN);
                        } break;
                    }
                
                    p.setColor(c);
                    p.fillRectangle(BLOCK_SIZE * x, BLOCK_SIZE * y, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        Pixmap p2 = new Pixmap(
                Utils.toPowerOfTwo(levelTexWidth),
                Utils.toPowerOfTwo(levelTexHeight),
                Pixmap.Format.RGBA8888
        );
        p2.drawPixmap(p, 0, 0);
        Texture t = new Texture(p2);
        p.dispose();
        p2.dispose();

        levelTex = new TextureRegion(t, levelTexWidth, levelTexHeight);
    }

    private void loadResources() {
        FileHandle graphicsFile = Gdx.files.internal(GRAPHICS_FILE);
        Texture graphicsTexture = new Texture(graphicsFile);
        
        TextureRegion[][] textureRegions = TextureRegion.split(graphicsTexture, SPRITE_SIZE, SPRITE_SIZE);
        playerTex = new TextureRegion[2];
        playerTex[PLAYER_RIGHT] = textureRegions[0][0];
        playerTex[PLAYER_LEFT] = new TextureRegion(playerTex[PLAYER_RIGHT]);
        playerTex[PLAYER_LEFT].flip(true, false);
        
        blockTex = textureRegions[0][1];
        waterTex = textureRegions[2][0];
        deathTex = textureRegions[2][1];
        exitTex  = textureRegions[2][2];

        abilTex = new TextureRegion[6];
        abilTex[AB_JELLY]   = textureRegions[0][2];
        abilTex[AB_LIQUID]  = textureRegions[0][3];
        abilTex[AB_GAS]     = textureRegions[1][0];
        abilTex[AB_SLICK]   = textureRegions[1][1];
        abilTex[AB_SWIM]    = textureRegions[1][2];
        abilTex[AB_NORMAL]  = textureRegions[1][3];
    }

    public void render(float dt) {
        if (! level.player.win)
            level.update(dt);
        
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        cam.position.lerp(new Vector3(level.player.x, level.player.y, 0), 2 * dt);
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        // Render level
//        sb.draw(levelTex, 0, 0);
        renderLevel();

        // Render player
        if (level.player.direction == Direction.RIGHT)
            sb.draw(playerTex[PLAYER_RIGHT], level.player.x, level.player.y);
        else
            sb.draw(playerTex[PLAYER_LEFT], level.player.x, level.player.y);

        sb.end();

        uiSb.begin();
        if (level.player.win) {
            String wonStr = "You won!";
            BitmapFont.TextBounds bounds = font.getBounds(wonStr);
            font.draw(uiSb, wonStr,
                    (App.SCREEN_WIDTH - bounds.width) / 2,
                    (App.SCREEN_HEIGHT - bounds.height) / 2
            );
        }
        
        if (Debug.DEBUG) {
            String debugInfo = String.format(
                    "Player: %.0f:%.0f\nVx: %.2f\nVy: %.2f\nAx: %.2f\nAy: %.2f\nState: %s\nAbilities: %s",
                    level.player.x,
                    level.player.y,
                    level.player.vx,
                    level.player.vy,
                    level.player.ax,
                    level.player.ay,
                    level.player.state,
                    level.player.abilities
            );
            font.drawMultiLine(uiSb, debugInfo, 10, App.SCREEN_HEIGHT - 10);
        }
        uiSb.end();
    }

    private void renderLevel() {
        TextureRegion t = null;
        
        for (int x = 0; x < level.width; x++) {
            for (int y = 0; y < level.height; y++) {
                Tile tile = level.map[x][y];

                if (tile != null) {
                    switch (tile) {
                        case BLOCK: {
                            t = blockTex;
                        } break;
                        case WATER: {
                            t = waterTex;
                        } break;
                        case EXIT: {
                            t = exitTex;
                        } break;
                        case DEATH: {
                            t = deathTex;
                        } break;

                        case AB_SWIM:
                            t = abilTex[AB_SWIM]; break;
                        
                        default: t = null; break;
                    }
                    
                    if (t != null)
                        sb.draw(t, x * BLOCK_SIZE, y * BLOCK_SIZE);
                }
            }
        }
    }

    public void restart() {
        level.load(level.filename);
        createMap();
        level.restart();
    }
}
