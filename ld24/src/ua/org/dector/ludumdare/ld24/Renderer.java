package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author dector
 */
public class Renderer {
    public static final int SPRITE_SIZE = 16;
    public static final int BLOCK_SIZE = 16;
    public static final int CAM_WIDTH = 640;
    public static final int CAM_HEIGHT = 480;
    
    static final int PLAYER_RIGHT = 0;
    static final int PLAYER_LEFT = 1;

    public static final String GRAPHICS_FILE = "ld24/data/graphics.png";
    
    Level level;

    SpriteBatch sb;
    OrthographicCamera cam;

    TextureRegion[] playerTex;
    TextureRegion levelTex;

    public Renderer(Level level) {
        this.level = level;

        sb = new SpriteBatch();

        createMap();
        loadResources();

        setupCamera();
    }

    private void setupCamera() {
        cam = new OrthographicCamera(CAM_WIDTH, CAM_HEIGHT);
        cam.position.set(level.player.x, level.player.y, 0);
    }

    private void createMap() {

    }

    private void loadResources() {
        FileHandle graphicsFile = Gdx.files.internal(GRAPHICS_FILE);
        Texture graphicsTexture = new Texture(graphicsFile);
        
        TextureRegion[][] textureRegions = TextureRegion.split(graphicsTexture, SPRITE_SIZE, SPRITE_SIZE);
        playerTex = new TextureRegion[2];
        playerTex[PLAYER_RIGHT] = textureRegions[0][0];
        playerTex[PLAYER_LEFT] = textureRegions[0][1];
        
    }

    public void render(float dt) {
        sb.begin();

        sb.setProjectionMatrix(cam.combined);

        // Render player
        
        if (level.player.direction == Direction.RIGHT)
            sb.draw(playerTex[PLAYER_RIGHT], level.player.x, level.player.y);
        else
            sb.draw(playerTex[PLAYER_LEFT], level.player.x, level.player.y);
        
        // Render level
        
        for (int x = 0; x < level.width; x++) {
            for (int y = 0; y < level.height; y++) {
                
            }
        }

        sb.end();
    }
}
