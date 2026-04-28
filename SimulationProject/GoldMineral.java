import greenfoot.*;

public class GoldMineral extends Actor
{
    private GreenfootImage img;

    public GoldMineral() {
        img = ResourceCache.getImage("GoldMinerals.png");

        if (img != null) {
            img.scale(80, 80);
            setImage(img);
        }
    }
}