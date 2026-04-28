import greenfoot.GreenfootSound;

/**
 * Button sound that can be called in all worlds
 */
public class SoundManager {
    private static GreenfootSound[] buttonSounds;
    
    private static final int SOUND_POOL_SIZE = 9;
    private static final int SOUND_VOLUME = 90;
    
    private static int buttonSoundIndex = 0;
    
    public static void playButtonSound() {
        if (buttonSounds != null && buttonSounds.length > 0) {
            buttonSounds[buttonSoundIndex].play();
            buttonSoundIndex = (buttonSoundIndex + 1) % buttonSounds.length;
        }
    }

    public static void buttonSoundSetup() {
        buttonSounds = new GreenfootSound[SOUND_POOL_SIZE];
        for (int i = 0; i < SOUND_POOL_SIZE; i++) {
            buttonSounds[i] = new GreenfootSound("ButtonSound.wav");
            buttonSounds[i].setVolume(SOUND_VOLUME);
        }
    }
}
