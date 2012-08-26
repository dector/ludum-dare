package ua.org.dector.ludumdare.ld24;

import java.util.Random;

/**
 * @author dector
 */
public class Levelset {
    public static final int EDU_LEVELS = 10;
    public static final String dataDir = "ld24/data/";
    public static final String[] levels    = { "level1.png", "level2.png", "level3.png",
            "level4.png", "level5.png", "level6.png", "level7.png", "level8.png", "level9.png", "level10.png",
            // PLAYS
            "level11.png" };
    public static final String[] levelInfo = { "level1i.png", "level2i.png","level3i.png",
            "level4i.png", "level5i.png", "level6i.png", "level7i.png", "level8i.png", "level9i.png", "level10i.png",
            null, null, null, null, null };

    public static final String[] avalBacks = { "autumn.png", "club.png", "ludum.png", "sea.png", "sky.png", "stars.png" };

    public static final String[] levelBack = { "autumn.png", "autumn.png", "club.png",
            "club.png",
            "ludum.png", "ludum.png", "sea.png", "sea.png","sky.png", "sky.png", "stars.png",
            "stars.png"};

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

    public static String getBack() {
        if (currentNum <= EDU_LEVELS)
            return dataDir + levelBack[currentNum];
        else
            return dataDir + levelBack[new Random().nextInt(avalBacks.length)];
    }

    public static void restart() {
        currentNum = 0;
    }
}
