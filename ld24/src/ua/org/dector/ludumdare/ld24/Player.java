package ua.org.dector.ludumdare.ld24;

/**
 * @author dector
 */
public class Player {
    Direction direction = Direction.RIGHT;
    
    float x;
    float y;

    public Player(float y, float x) {
        this.y = y;
        this.x = x;
    }
}

enum Direction {
    RIGHT, LEFT
}
