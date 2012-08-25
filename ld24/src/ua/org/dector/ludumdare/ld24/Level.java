package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author dector
 */
public class Level {
    public static final int NOTHING = 0;
    public static final int BLOCK   = 0x000000ff;
    public static final int SPAWN   = 0x00ff00ff;
    public static final int EXIT    = 0x0000ffff;
    public static final int DEATH   = 0xff0000ff;

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
                t = p.getPixel(x, height - y - 1);
//                System.out.printf("%d:%d %s%n", x, y, Integer.toHexString(t));
                
                switch (t) {
                    case BLOCK:
                        map[x][y] = Tile.BLOCK; break;
                    case SPAWN: {
                        player = new Player(Renderer.BLOCK_SIZE * x, Renderer.BLOCK_SIZE * y);
                        map[x][y] = Tile.SPAWN;
                    } break;
                    case EXIT: {
                        map[x][y] = Tile.EXIT;
                    } break;
                    case DEATH: {
                        map[x][y] = Tile.DEATH;
                    } break;
                }
            }
        }
    }

    public void update(float dt) {
        player.update(dt);

        tryToMovePlayer();
    }

    private void tryToMovePlayer() {
        if (player.jumpCommand) {
            boolean onTheGround =
                    map[(int)player.x / Renderer.BLOCK_SIZE][(int)player.y / Renderer.BLOCK_SIZE - 1] == Tile.BLOCK
                    || map[(int)player.x / Renderer.BLOCK_SIZE + 1][(int)player.y / Renderer.BLOCK_SIZE - 1] == Tile.BLOCK;

            if (! player.isJumping && onTheGround) {
                player.vy += player.JUMPING;
                player.isJumping = true;
            }

            player.jumpCommand = false;
        }

        Rectangle pr = new Rectangle((int)player.x / Renderer.BLOCK_SIZE, (int)Math.floor(player.y / Renderer.BLOCK_SIZE), 1, 1);
        Rectangle[] rs;

        boolean collided = false;

        player.x += player.vx;
        pr.setX((int)player.x / Renderer.BLOCK_SIZE);
        rs = checkCollisions();
        for (int i = 0; i < rs.length; i++) {
            if (pr.overlaps(rs[i])) {
                if (player.vx < 0)
                    player.x = (rs[i].x + 1) * Renderer.BLOCK_SIZE + 0.01f;
                else
                    player.x = (rs[i].x - 1) * Renderer.BLOCK_SIZE - 0.01f;

                collided = true;
            }
        }

        if (collided) { player.vx = 0; /*player.ax = 0;*/ }
        collided = false;

        player.y += player.vy;
        pr.setX((int)player.x / Renderer.BLOCK_SIZE);
        pr.setY((int)Math.floor(player.y / Renderer.BLOCK_SIZE));
        rs = checkCollisions();
        for (int i = 0; i < rs.length; i++) {
            if (pr.overlaps(rs[i])) {
                if (player.vy < 0)
                    player.y = (rs[i].y + 1) * Renderer.BLOCK_SIZE + 0.01f;
                else
                    player.y = (rs[i].y - 1) * Renderer.BLOCK_SIZE - 0.01f;

                collided = true;
            }
        }

        if (collided) {
            if (player.isJumping && player.vy < 0)
                player.isJumping = false;

            player.vy = 0;
            player.ay = 0;
        }
    }

    private Rectangle[] checkCollisions() {
        int px = (int)player.x / Renderer.BLOCK_SIZE;
        int py = (int)Math.floor(player.y / Renderer.BLOCK_SIZE);

        int[] x = { px, px + 1, px + 1, px };
        int[] y = { py, py, py + 1, py + 1 };

        Tile[] tiles = { map[x[0]][y[0]], map[x[1]][y[1]], map[x[2]][y[2]], map[x[3]][y[3]] };
        
        Rectangle[] r = { new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle() };

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null)
                switch (tiles[i]) {
                    case BLOCK: r[i].set(x[i], y[i], 1, 1); break;
                    case EXIT: if (i == 0) player.win = true; break;
                    case DEATH: if (i == 0) player.dead = true; break;
                    default: r[i].set(-1, -1, 1, 1); break;
                }
        }
        
        return r;
    }
}

