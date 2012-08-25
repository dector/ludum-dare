package ua.org.dector.ludumdare.ld24;

/**
 * @author dector
 */
public class Player {
    public static final float ACCELERATION = 3.5f;
    public static final float JUMPING = 3.5f;
    public static final float GRAVITY = 0.8f;
    public static final float FRICTION = 0.8f;
    public static final float AIR_FRICTION = 1 - 0.1f;
    public static final float MAX_SPEED_X = 2.5f;
    public static final float MAX_SPEED_Y = 4f;

    Direction direction = Direction.RIGHT;
    int gravityDirection = -1;

    boolean isJumping;
    boolean finished;

    float x;
    float y;
    
    float vx;
    float vy;
    float ax;
    float ay;

    public Player(float y, float x) {
        this.y = y;
        this.x = x;
    }

    public void update(float dt) {
        if (ax != 0 && (vx > 0 && ax < 0 || vx < 0 && ax > 0))
            vx = ax * dt;
        else
            vx += ax * dt;

        ay += gravityDirection * GRAVITY * dt;
        vy += ay;

        if (vx > MAX_SPEED_X) vx = MAX_SPEED_X;
        if (vx < -MAX_SPEED_X) vx = -MAX_SPEED_X;
        if (vy > MAX_SPEED_Y) vy = MAX_SPEED_Y;
        if (vy < -MAX_SPEED_Y) vy = -MAX_SPEED_Y;

        if (ax == 0) {
            if (isJumping)
                vx *= AIR_FRICTION;
            else
                vx *= FRICTION;
        }
    }

    public void tryToJump() {
        if (! isJumping) {
            isJumping = true;
            vy += JUMPING;
        }
    }
}

enum Direction {
    RIGHT, LEFT
}
