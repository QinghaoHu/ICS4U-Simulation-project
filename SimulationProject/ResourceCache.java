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
    private static final int soundPoolSize = 4;

    private static final String[] IMAGE_FILES = {
            "Background.png",
            "BlueBarrack.png",
            "BlueBase.png",
            "BlueMarine.png",
            "BlueMarineRecoil.png",
            "BlueTurret.png",
            "BlueWorkerMining.png",
            "BlueWorkerRegular.png",
            "Counter.png",
            "placeholder.png",
            "redwins.png",
            "bluewins.png",
            "RedBarrack.png",
            "RedBase.png",
            "RedMarine.png",
            "RedMarineRecoil.png",
            "RedOfficer.png",
            "RedOfficerRecoil.png",
            "RedTurret.png",
            "RedWorkerMining.png",
            "RedWorkerRegular.png",
            "Resources1.png",
            "Resources2.png",
            "SoldierBullet.png",
            "Supply.png",
            "SupplyBot.png",
            "Ui.png",
            "upgrades.png"
    };

    private static final String[] SOUND_FILES = {
            "BarrackExplosion.mp3",
            "BaseExplosion.mp3",
            "BackgroundMusic.mp3",
    };

    private static final String[] SOUND_POOL_FILES = {
            "building.mp3",
            "depositing.mp3",
            "MarineShoot.mp3",
            "mining.mp3",
            "OfficerShoot.mp3",
            "supply crate.mp3",
            "TurretShoot.mp3",
            "MarineDeath0.mp3",
            "MarineDeath1.mp3",
            "MarineDeath2.mp3"
    };

    private static final Map<String, GreenfootImage> images = new HashMap<>();
    private static final Map<String, GreenfootSound> sounds = new HashMap<>();
    private static final Map<String, GreenfootSound[]> soundPools = new HashMap<>();
    private static final Map<String, Integer> soundPoolIndexes = new HashMap<>();

    private static volatile boolean loadingStarted = false;
    private static volatile boolean allResourcesLoaded = false;
    private static volatile int loadedCount = 0;
    private static volatile int totalCount = IMAGE_FILES.length + SOUND_FILES.length + SOUND_POOL_FILES.length;

    private ResourceCache() {
    }

    public static void startLoadingAsync() {
        if (loadingStarted || allResourcesLoaded) {
            return;
        }

        loadingStarted = true;

        // Keep load time off the main thread.
        // Greenfoot can stall if this runs too early on the main loop.
        Thread loadingThread = new Thread(() -> {
            loadAllResources();
        });

        loadingThread.start();
    }

    public static void loadAllResources() {
        if (allResourcesLoaded) {
            return;
        }

        // Warm up images first, then sounds.
        // This keeps the first in-game use from hitching as much.
        loadedCount = 0;

        for (String imageFile : IMAGE_FILES) {
            getImage(imageFile);
            loadedCount++;
        }

        for (String soundFile : SOUND_FILES) {
            getSound(soundFile);
            loadedCount++;
        }

        for (String soundFile : SOUND_POOL_FILES) {
            GreenfootSound[] pool = new GreenfootSound[soundPoolSize];
            for (int i = 0; i < soundPoolSize; i++) {
                pool[i] = new GreenfootSound(soundFile);
            }
            soundPools.put(soundFile, pool);
            soundPoolIndexes.put(soundFile, 0);
            loadedCount++;
        }

        allResourcesLoaded = true;
    }

    public static GreenfootImage getImage(String fileName) {
        GreenfootImage image = images.get(fileName);
        if (image == null) {
            // Cache the original, return a fresh copy.
            image = new GreenfootImage(fileName);
            images.put(fileName, image);

        }
        return new GreenfootImage(image);
    }

    public static GreenfootSound getSound(String fileName) {
        GreenfootSound sound = sounds.get(fileName);
        if (sound == null) {
            // Shared sound handle for long clips.
            sound = new GreenfootSound(fileName);
            sounds.put(fileName, sound);
        }
        return sound;
    }

    public static void playSound(String fileName) {
        GreenfootSound[] pool = soundPools.get(fileName);
        if (pool == null) {
            // Small pool avoids clobbering the same sound.
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
            // Same idea here, just with a volume tweak.
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

    public static int getLoadingPercent() {
        if (loadedCount == 0) {
            return 0;
        }
        return loadedCount * 100 / totalCount;
    }

    public static boolean isAllResourcesLoaded() {
        return allResourcesLoaded;
    }

}
