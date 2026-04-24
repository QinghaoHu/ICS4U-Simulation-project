import greenfoot.*;

public class HealthBar extends SuperStatBar
{
    public HealthBar(Entity target, int yOffset)
    {
        super(target.getMaxHealth(), target.getHealth(), target, 50, 6, yOffset);
    }
}
