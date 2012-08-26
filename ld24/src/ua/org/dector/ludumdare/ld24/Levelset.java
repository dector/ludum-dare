package ua.org.dector.ludumdare.ld24;

/**
 * @author dector
 */
public class Levelset {
    public static final String dataDir = "ld24/data/";
    public static final String[] levels    = { "level0.png", "level1.png", "level2.png", "level3.png" };
    public static final String[] levelInfo = { "level0i.png", "level1i.png", "level2i.png", "level3i.png" };
    
    public static final int INFO_WIDTH = Renderer.CAM_WIDTH / 2;
    public static final int INFO_HEIGHT = Renderer.CAM_HEIGHT / 2;
    
    private static int currentNum = 0;
    
    public static String next() {
        if (! isLast()) {
            currentNum++;
            return getLevel();
        } else {
            return null;
        }
    }
    
    public static String getLevel() {
        return dataDir + levels[currentNum];
    }

    public static boolean isLast() {
        return currentNum >= levels.length - 1;
    }

    public static String getInfo() {
        return dataDir + levelInfo[currentNum];
    }

    public static void restart() {
        currentNum = 0;
    }
}
