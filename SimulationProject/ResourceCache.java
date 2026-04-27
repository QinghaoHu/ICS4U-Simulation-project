import greenfoot.*;
import java.util.HashMap;
import java.util.Map;
/**
 * Write a description of class ResourceCache here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ResourceCache {
    private static final int soundPoolSize = 6;

    private static final Map<String, GreenfootImage> images = new HashMap<>();
    private static final Map<String, GreenfootSound> sounds = new HashMap<>();
    private static final Map<String, GreenfootSound[]> soundPools = new HashMap<>();
    private static final Map<String, Integer> soundPoolIndexes = new HashMap<>();

    private ResourceCache() {
    }

    public static GreenfootImage getImage(String fileName) {
        GreenfootImage image = images.get(fileName);
        if (image == null) {
            image = new GreenfootImage(fileName);
            images.put(fileName, image);

        }
        return new GreenfootImage(image);
    }

    public static GreenfootSound getSound(String fileName) {
        GreenfootSound sound = sounds.get(fileName);
        if (sound == null) {
            sound = new GreenfootSound(fileName);
            sounds.put(fileName, sound);
        }
        return sound;
    }

    public static void playSound(String fileName) {
        GreenfootSound[] pool = soundPools.get(fileName);
        if (pool == null) {
            pool = new GreenfootSound[soundPoolSize];
            for (int i = 0; i < pool.length; i++) {
                pool[i] = new GreenfootSound(fileName);
            }
            soundPools.put(fileName, pool);
            soundPoolIndexes.put(fileName, 0);
        }
        int index = soundPoolIndexes.get(fileName);
        pool[index].play();
        soundPoolIndexes.put(fileName, (index + 1) % pool.length);
    }

    public static void playSound(String fileName, int volume) {
        GreenfootSound[] pool = soundPools.get(fileName);
        if (pool == null) {
            pool = new GreenfootSound[soundPoolSize];
            for (int i = 0; i < pool.length; i++) {
                pool[i] = new GreenfootSound(fileName);
            }
            soundPools.put(fileName, pool);
            soundPoolIndexes.put(fileName, 0);
        }
        int index = soundPoolIndexes.get(fileName);
        pool[index].setVolume(volume);
        pool[index].play();
        soundPoolIndexes.put(fileName, (index + 1) % pool.length);
    }
}
