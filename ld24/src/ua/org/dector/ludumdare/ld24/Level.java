package ua.org.dector.ludumdare.ld24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.Rectangle;

import static ua.org.dector.ludumdare.ld24.Renderer.BLOCK_SIZE;

/**
 * @author dector
 */
public class Level {
    public static final int NOTHING = 0;
    public static final int BLOCK   = 0x000000ff;
    public static final int SPAWN   = 0x00ff00ff;
    public static final int EXIT    = 0xff00ffff;
    public static final int WATER   = 0x0000ffff;
    public static final int DEATH   = 0xff0000ff;
    
    public static final int AB_SWIM = 0x009900ff;
    public static final int AB_GAS  = 0x66ccffff;

    int width;
    int height;
    Tile[][] map;
    
    int spawnX;
    int spawnY;
    
    Player player;
    
    String filename;

    public void load(String file) {
        this.filename = file;
        
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
                        player = new Player(BLOCK_SIZE * x, BLOCK_SIZE * y);
                        map[x][y] = Tile.SPAWN;
                        spawnX = x * BLOCK_SIZE;
                        spawnY = y * BLOCK_SIZE;
                    } break;
                    case EXIT: {
                        map[x][y] = Tile.EXIT;
                    } break;
                    case DEATH: {
                        map[x][y] = Tile.DEATH;
                    } break;
                    case WATER: {
                        map[x][y] = Tile.WATER;
                    } break;
                    
                    case AB_SWIM: map[x][y] = Tile.AB_SWIM; break;
                    case AB_GAS:  map[x][y] = Tile.AB_GAS; break;
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
            int nextBlock = (player.gravityDirection < 0) ? -1 : 2;

            boolean onTheGround = player.state != State.SWIM
                    && (map[(int)player.x / BLOCK_SIZE][(int)player.y / BLOCK_SIZE + nextBlock] == Tile.BLOCK
                    || map[(int)player.x / BLOCK_SIZE + 1][(int)player.y / BLOCK_SIZE + nextBlock] == Tile.BLOCK);

            if (! player.isJumping && onTheGround) {
                player.vy -= player.gravityDirection * Player.JUMPING;
                player.isJumping = true;
            }

            player.jumpCommand = false;
        }

        Rectangle pr = new Rectangle((int)player.x / BLOCK_SIZE, (int)Math.floor(player.y / BLOCK_SIZE), 1, 1);
        Rectangle[] rs;

        boolean collided = false;

        player.x += player.vx;
        pr.setX((int)player.x / BLOCK_SIZE);
        rs = checkCollisions();
        for (Rectangle r : rs) {
            if (pr.overlaps(r)) {
                if (player.vx < 0)
                    player.x = (r.x + 1) * BLOCK_SIZE + 0.01f;
                else
                    player.x = (r.x - 1) * BLOCK_SIZE - 0.01f;

                collided = true;
            }
        }

        if (collided) { player.vx = 0; /*player.ax = 0;*/ }
        collided = false;

        player.y += player.vy;
        pr.setX((int)player.x / BLOCK_SIZE);
        pr.setY((int)Math.floor(player.y / BLOCK_SIZE));
        rs = checkCollisions();
        for (Rectangle r : rs) {
            if (pr.overlaps(r)) {
                if (player.vy < 0)
                    player.y = (r.y + 1) * BLOCK_SIZE + 0.01f;
                else
                    player.y = (r.y - 1) * BLOCK_SIZE - 0.01f;

                collided = true;
            }
        }

        if (collided) {
            if (player.isJumping && player.vy * player.gravityDirection > 0)
                player.isJumping = false;

            if (player.gravityDirection > 0 && player.vy > 0)
                player.gravityDirection = -1;

            player.vy = 0;
            player.ay = 0;
        }
    }

    private Rectangle[] checkCollisions() {
        int px = (int)player.x / BLOCK_SIZE;
        int py = (int)Math.floor(player.y / BLOCK_SIZE);

        int[] x = { px, px + 1, px + 1, px };
        int[] y = { py, py, py + 1, py + 1 };

        Tile[] tiles = { map[x[0]][y[0]], map[x[1]][y[1]], map[x[2]][y[2]], map[x[3]][y[3]] };
        
        Rectangle[] r = { new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle() };

        boolean inWater = false;
        
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null)
                switch (tiles[i]) {
                    case BLOCK: r[i].set(x[i], y[i], 1, 1); break;
                    case EXIT: player.win = true; break;
                    case DEATH: die(); break;
                    case WATER: {
                        if (! inWater) inWater = true;
                    } break;
                    case AB_SWIM: {
                        player.abilities.add(Ability.SWIM);
                        removeTile(x[i], y[i]);
                    } break;
                    case AB_GAS: {
//                        player.state = State.GAS;
                        player.gravityDirection = 1;
                        removeTile(x[i], y[i]);
                    } break;
                    default: r[i].set(-1, -1, 1, 1); break;
                }
        }
        
        if (player.state == State.SWIM) {
            if (! inWater)
                player.state = State.RUNNING;
        } else if (inWater) {
            if (! player.abilities.contains(Ability.SWIM))
                die();
            else
                player.state = State.SWIM;
        }
        
        return r;
    }

    private void removeTile(int x, int y) {
        map[x][y] = null;
    }

    private void die() {
        restart();
    }

    void restart() {
        player.restart(spawnX, spawnY);
    }


}

