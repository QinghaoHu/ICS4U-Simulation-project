import greenfoot.*;

public class GameTimer extends Actor
{
    private int actCount = 0;
    private int seconds = 0;

    public GameTimer()
    {
        // Start with a visible clock.
        updateImage();
    }

    public void act()
    {
        // Count one real second every 60 frames.
        actCount++;

        if (actCount >= 60) {
            seconds++;
            actCount = 0;
            updateImage();
        }
    }

    private void updateImage()
    {
        // Keep the on-screen timer easy to read.
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        String time = String.format("%02d:%02d", minutes, remainingSeconds);

        GreenfootImage img = new GreenfootImage("Time: " + time, 30, Color.WHITE, new Color(0,0,0,0));
        setImage(img);
    }

    public int getSeconds()
    {
        return seconds;
    }
}
