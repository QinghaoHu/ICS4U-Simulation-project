import greenfoot.*;

public abstract class People extends Entity{
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int speed;

    public People(Team team, int maxHealth) {
        super(team);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public void act(){
        move(speed);

        if (isAtEdge()) {
            getWorld().removeObject(this);
            return;
        }

        if (health <= 0 && getWorld() != null) {
            decrease();
            return;
        }
    }

    protected void addedToWorld(World world) {
        if (world != null) {
            world.addObject(new HealthBar(this, -35), getX(), getY());
        }
    }

    private void decrease() {
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
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

    public void setDamage(int damage) {
        this.damage = damage;
    }
}