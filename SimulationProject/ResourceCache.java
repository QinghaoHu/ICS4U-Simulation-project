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
            "building.mp3",
            "depositing.mp3",
            "MarineShoot.mp3",
            "mining.mp3",
            "OfficerShoot.mp3",
            "supply crate.mp3",
            "TurretExplosion.mp3",
            "TurretShoot.mp3"
    };

    private static final Map<String, GreenfootImage> images = new HashMap<>();
    private static final Map<String, GreenfootSound> sounds = new HashMap<>();
    private static final Map<String, GreenfootSound[]> soundPools = new HashMap<>();
    private static final Map<String, Integer> soundPoolIndexes = new HashMap<>();
    private static boolean allResourcesLoaded;

    private ResourceCache() {
    }

    public static void loadAllResources() {
        if (allResourcesLoaded) {
            return;
        }

        for (String imageFile : IMAGE_FILES) {
            getImage(imageFile);
        }

        for (String soundFile : SOUND_FILES) {
            getSound(soundFile);
        }

        allResourcesLoaded = true;
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
