package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * @author dector
 */
public class Level {
    public static final int NOTHING = 0;
    public static final int BLOCK   = 0x000000ff;
    public static final int SPAWN   = 0x00ff00ff;

    int width;
    int height;
    Tile[][] map;
    
    Player player;

    public void load(String file) {
        Pixmap p = new Pixmap(Gdx.files.internal(file));
        
        width = p.getWidth();
        height = p.getHeight();
        map = new Tile[width][height];

        int t;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                t = p.getPixel(x, y);
//                System.out.printf("%d:%d %s%n", x, y, Integer.toHexString(t));
                
                switch (t) {
                    case BLOCK: map[x][y] = Tile.BLOCK; break;
                    case SPAWN: {
                        player = new Player(x, y);
                        map[x][y] = Tile.SPAWN;
                    } break;
                }
            }
        }
    }
}
