package ua.org.dector.ludumdare.ld24;

/**
 * @author dector
 */
public class Utils {
    public static int toPowerOfTwo(int number) {
        double log = Math.log(number) / Math.log(2);

        if (log == (int)log) {
            return number;
        } else {
            return (int)Math.pow(2, (int)log + 1);
        }
    }
}
