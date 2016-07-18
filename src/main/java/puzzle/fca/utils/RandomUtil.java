package puzzle.fca.utils;

import java.util.Random;

public class RandomUtil {

    public static Integer nextInt(int length){
        int min = (int)Math.pow(10, length - 1);
        int max = (int)Math.pow(10, length);
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(max - min + 1) + min;
    }

}
