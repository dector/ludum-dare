package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sun.servicetag.SystemEnvironment;

/**
 * @author dector
 */
public class Renderer {
    public static final int SPRITE_SIZE = 16;
    public static final int DEFAULT_BLOCK_SIZE = 16;
    public static final float MULT = 1;
    public static final int BLOCK_SIZE = (int)(DEFAULT_BLOCK_SIZE * MULT);
    public static final int CAM_WIDTH = 640;
    public static final int CAM_HEIGHT = 480;
    
    public static final int AB_SOLID = 0;
    public static final int AB_LIQUID = 1;
    public static final int AB_GAS = 2;
    public static final int AB_SLICK = 3;
    public static final int AB_SWIM = 4;
    public static final int AB_NORMAL = 5;

    static final int PLAYER_RIGHT = 0;
    static final int PLAYER_LEFT = 1;
    
    public static final int PAUSE_WIDTH     = CAM_WIDTH / 5;
    public static final int PAUSE_HEIGHT    = CAM_HEIGHT / 5;
    public static final int SOUND_WIDTH     = (int)(BLOCK_SIZE * 1.5);
    public static final int SOUND_HEIGHT    = (int)(BLOCK_SIZE * 1.5);
    
    public static final int SOUND_ON = 0;
    public static final int SOUND_OFF = 1;

    public static final int TUBE_UP = 0;
    public static final int TUBE_RIGHT = 1;
    public static final int TUBE_DOWN = 2;
    public static final int TUBE_LEFT = 3;

    public static final int REACHED_AB_RUN = 0;
    public static final int REACHED_AB_SOLID = 1;
    public static final int REACHED_AB_LIQUID = 2;
    public static final int REACHED_AB_GAS = 3;
    public static final int REACHED_AB_SLICK = 4;
    public static final int REACHED_AB_SWIM = 5;

    public static final int REACHED_AB_WIDTH = BLOCK_SIZE * 2;
    public static final int REACHED_AB_HEIGHT = BLOCK_SIZE * 2;
    public static final int REACHED_AB_SPACE = REACHED_AB_WIDTH / 2;
    public static final int REACHED_AB_BOTTOM_PAD = REACHED_AB_HEIGHT / 2;

    public static final int BACK_WIDTH  = CAM_WIDTH + 100;
    public static final int BACK_HEIGHT = CAM_HEIGHT + 100;

    public static final int SIGN_WIDTH = Renderer.CAM_WIDTH / 2;
    public static final int SIGN_HEIGHT = Renderer.CAM_HEIGHT / 2;
    
    public static final String GRAPHICS_FILE = "ld24/data/graphics.png";
    public static final String SIGNS_FILE   = "ld24/data/signs.png";

    public static final int LEVEL_COMPLETED = 0;
    public static final int GAME_COMPLETED = 1;

    Level level;

    SpriteBatch sb;
    SpriteBatch uiSb;
    BitmapFont font;
    OrthographicCamera cam;

    TextureRegion[] playerTex;
    TextureRegion[] abilTex;
    TextureRegion[] tube;
    TextureRegion blockTex;
    TextureRegion waterTex;
    TextureRegion deathTex;
    TextureRegion exitTex;
    TextureRegion glassTex;

    TextureRegion pauseTex;
    TextureRegion darkTex;
    
    TextureRegion[] soundTex;
    TextureRegion[] reachedAbilTex;

    TextureRegion[] winSignsTex;
    
    TextureRegion levelTex;
    
    TextureRegion backTex;
    float backC1 = 0;
    float backC2 = 0;
    TextureRegion infoTex;

    public Renderer(Level level) {
        this.level = level;

        sb = new SpriteBatch();
        uiSb = new SpriteBatch();
        font = new BitmapFont();
        
        level.renderer = this;

        loadResources();
        loadLevelTexs();
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
        glassTex = textureRegions[2][3];

        abilTex = new TextureRegion[6];
        abilTex[AB_SOLID]   = textureRegions[0][2];
        abilTex[AB_LIQUID]  = textureRegions[0][3];
        abilTex[AB_GAS]     = textureRegions[1][0];
        abilTex[AB_SLICK]   = textureRegions[1][1];
        abilTex[AB_SWIM]    = textureRegions[1][2];
        abilTex[AB_NORMAL]  = textureRegions[1][3];

        tube = new TextureRegion[4];
        tube[TUBE_UP]   = textureRegions[3][0];
        tube[TUBE_RIGHT] = textureRegions[3][1];
        tube[TUBE_DOWN] = textureRegions[3][2];
        tube[TUBE_LEFT] = textureRegions[3][3];

        Pixmap darkP = new Pixmap(
                Utils.toPowerOfTwo(CAM_WIDTH),
                Utils.toPowerOfTwo(CAM_HEIGHT),
                Pixmap.Format.RGBA8888
        );
        darkP.setColor(Color.rgba8888(0, 0, 0, 0.7f));
        darkP.fill();
        darkTex = new TextureRegion(new Texture(darkP));
        darkP.dispose();
        
        pauseTex = textureRegions[4][1];

        soundTex = new TextureRegion[2];
        soundTex[SOUND_ON] = textureRegions[6][0];
        soundTex[SOUND_OFF] = textureRegions[6][1];

        reachedAbilTex = new TextureRegion[6];
        reachedAbilTex[REACHED_AB_RUN] = textureRegions[4][0];
        reachedAbilTex[REACHED_AB_SOLID] = textureRegions[4][2];
        reachedAbilTex[REACHED_AB_LIQUID] = textureRegions[4][3];
        reachedAbilTex[REACHED_AB_GAS] = textureRegions[5][0];
        reachedAbilTex[REACHED_AB_SLICK] = textureRegions[5][1];
        reachedAbilTex[REACHED_AB_SWIM] = textureRegions[5][2];
        
        TextureRegion[][] winSigns = TextureRegion.split(new Texture(Gdx.files.internal(SIGNS_FILE)), SIGN_WIDTH, SIGN_HEIGHT);
        winSignsTex = new TextureRegion[2];
        winSignsTex[LEVEL_COMPLETED] = winSigns[0][0];
        winSignsTex[GAME_COMPLETED] = winSigns[1][0];
    }

    public void render(float dt) {
        if (! level.player.win && ! level.paused && ! level.started)
            level.update(dt);
        
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

//        System.out.println(level.player.x, level.player.y);
        cam.position.lerp(new Vector3(level.player.x, level.player.y, 0), 2 * dt);
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        
        uiSb.begin();
        uiSb.draw(backTex,
                - (cam.position.x * backC1),
                - (cam.position.y * backC2)
        );
        uiSb.end();
        
        sb.begin();

        // Render level
//        sb.draw(levelTex, 0, 0);
        renderLevel();

        // Render player
        if (level.player.direction == Direction.RIGHT)
            sb.draw(playerTex[PLAYER_RIGHT], level.player.x, level.player.y, BLOCK_SIZE, BLOCK_SIZE);
        else
            sb.draw(playerTex[PLAYER_LEFT], level.player.x, level.player.y, BLOCK_SIZE, BLOCK_SIZE);

        sb.end();

        // Render UI

        uiSb.begin();
        if (level.player.win) {
            TextureRegion tWin;
            
            if (Levelset.isLast()) {
                tWin = winSignsTex[GAME_COMPLETED];
            } else {
                tWin = winSignsTex[LEVEL_COMPLETED];
            }
            
            uiSb.draw(tWin, (CAM_WIDTH - SIGN_WIDTH) / 2, (CAM_HEIGHT - SIGN_HEIGHT) / 2);
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
        
        uiSb.draw(
                ((Sounds.get().mutedOff) ? soundTex[SOUND_ON] : soundTex[SOUND_OFF]),
                App.SCREEN_WIDTH - 2 * SOUND_WIDTH,
                App.SCREEN_HEIGHT - 2 * SOUND_HEIGHT,
                SOUND_WIDTH,
                SOUND_HEIGHT
        );

        drawReachedAbilities();
        
        if (level.paused) {
            uiSb.draw(darkTex, 0, 0, CAM_WIDTH, CAM_HEIGHT);
            uiSb.draw(pauseTex, (CAM_WIDTH - PAUSE_WIDTH) / 2, (CAM_HEIGHT - PAUSE_HEIGHT) / 2, PAUSE_WIDTH, PAUSE_HEIGHT);
        }
        
        if (level.started && ! level.player.win) {
            if (infoTex != null)
                uiSb.draw(infoTex, (CAM_WIDTH - SIGN_WIDTH) / 2, (CAM_HEIGHT - SIGN_HEIGHT) / 2);
            else
                level.started = false;
        }

        uiSb.end();
    }

    private void drawReachedAbilities() {
        int abils = level.player.abilities.size() + 1;
        int sumWidth = abils * (REACHED_AB_WIDTH + REACHED_AB_SPACE) - REACHED_AB_SPACE;

        int x = (CAM_WIDTH - sumWidth) / 2;
        int y = REACHED_AB_BOTTOM_PAD;

        if (level.player.state == State.RUNNING)
            uiSb.draw(reachedAbilTex[REACHED_AB_RUN], x, y, REACHED_AB_WIDTH, REACHED_AB_HEIGHT);
//        else if (level.player.state == State.SWIM)
//            uiSb.draw(reachedAbilTex[REACHED_AB_SWIM], x, y, REACHED_AB_WIDTH, REACHED_AB_HEIGHT);

        x += REACHED_AB_WIDTH + REACHED_AB_SPACE;

        TextureRegion t = null;
        for (Ability ab : level.player.abilities) {
            switch (ab) {
                case SWIM: t = reachedAbilTex[REACHED_AB_SWIM];  break;
                case SLICK: t = reachedAbilTex[REACHED_AB_SLICK]; break;
                case SOLID: t = reachedAbilTex[REACHED_AB_SOLID]; break;
                case GAS: t = reachedAbilTex[REACHED_AB_GAS]; break;
                case LIQUID: t = reachedAbilTex[REACHED_AB_LIQUID]; break;
            }

            uiSb.draw(t, x, y, REACHED_AB_WIDTH, REACHED_AB_HEIGHT);
            x += REACHED_AB_WIDTH + REACHED_AB_SPACE;
        }
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
                        case GLASS: {
                            t = glassTex;
                        } break;

                        case AB_SWIM:
                            t = abilTex[AB_SWIM]; break;
                        case AB_GAS:
                            t = abilTex[AB_GAS]; break;
                        case AB_SLICK:
                            t = abilTex[AB_SLICK]; break;
                        case AB_NORMAL:
                            t = abilTex[AB_NORMAL]; break;
                        case AB_SOLID:
                            t = abilTex[AB_SOLID]; break;
                        case AB_LIQUID:
                            t = abilTex[AB_LIQUID]; break;

                        case TUBE_UP:
                            t = tube[TUBE_UP]; break;
                        case TUBE_RIGHT:
                            t = tube[TUBE_RIGHT]; break;
                        case TUBE_DOWN:
                            t = tube[TUBE_DOWN]; break;
                        case TUBE_LEFT:
                            t = tube[TUBE_LEFT]; break;
                        
                        default: t = null; break;
                    }
                    
                    if (t != null)
                        sb.draw(t, x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    public void restart() {
        level.restart();
    }

    public void loadLevelTexs() {
        if (backTex != null) backTex.getTexture().dispose();
        
        Texture tex = new Texture(Gdx.files.internal(Levelset.getBack()));
        backTex = new TextureRegion(tex, BACK_WIDTH, BACK_HEIGHT);

        backC1 = (float)(BACK_WIDTH - CAM_WIDTH) / (level.width * (BLOCK_SIZE - 1));
        backC2 = (float)(BACK_HEIGHT - CAM_HEIGHT) / (level.height * (BLOCK_SIZE - 1));
        
        if (infoTex != null) infoTex.getTexture().dispose();

        String infoFile = Levelset.getInfo();
//        System.out.println(infoFile);
        if (infoFile != null) {
            Pixmap ip = new Pixmap(Gdx.files.internal(infoFile));
            Texture iTex = new Texture(
                    Utils.toPowerOfTwo(ip.getWidth()),
                    Utils.toPowerOfTwo(ip.getHeight()),
                    Pixmap.Format.RGBA8888
            );
            iTex.draw(ip, 0, 0);
            infoTex = new TextureRegion(iTex, SIGN_WIDTH, SIGN_HEIGHT);
            ip.dispose();
        } else
            infoTex = null;
    }
}
