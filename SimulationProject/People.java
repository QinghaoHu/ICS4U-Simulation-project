import greenfoot.*;

public abstract class People extends Entity{
    protected int speed;
    protected int centerDist = 20;
    private boolean hasStatBar;
    private boolean statBarEnabled = true;
    private SuperStatBar statBar;
    
    
    // people is a subclass of entity and will all be able to move
    
    public People(Team team, int maxHealth, int speed) {
        super(team);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.speed = speed;
    }

    public void act(){
        if (isAtEdge()) { // if the person is outside the border, the person will be deleted
            // Drifted off-map, so just remove it.
            getWorld().removeObject(this);
            return;
        }
    
        if (!hasStatBar && getWorld() != null) {
            addStatBar(getWorld());
        }
    
        updateStatBar();
    
        super.act();
    }

    public void addStatBar(World world) {
        if (world != null && statBarEnabled && !hasStatBar) {
            // Only spawn the bar once.
            statBar = new SuperStatBar(maxHealth, health, this, 50, 6, -65);
            world.addObject(statBar, getX(), getY());
            hasStatBar = true;
        }
    }
    
    public void setStatBarEnabled(boolean statBarEnabled) {
        this.statBarEnabled = statBarEnabled;
    }
    
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getSpeed() {
        return speed;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }
    
    public void updateStatBar() {
        if (statBar != null) {
            statBar.setMaxVal(maxHealth);
            statBar.update(health);
        }
    }
    
    protected void moveTowards(int x, int y) {
        // Straight-line move, capped by speed.
        int dx = x - getX();
        int dy = y - getY();

        double dist = Math.sqrt(dx * dx + dy * dy);
        
        updateDirection(dx, dy);
        
        if (dist < speed) {
            setLocation(x, y);
            return;
        }

        if (dist > 0) {
            double vx = (dx / dist) * speed;
            double vy = (dy / dist) * speed;

            setLocation(getX() + (int) vx, getY() + (int) vy);
        }
    }
    
    protected void updateDirection(int dx, int dy) {
        if (dx != 0 || dy != 0) {
            setRotation((int) Math.toDegrees(Math.atan2(dy, dx)));
        }
    }
}
