public abstract class People extends Entity{
    protected int health;
    protected int damage;
    protected int speed;

    public People (){
        super();
    }

    public People(Team team) {
        super(team);
    }

    public void act(){
        move(speed);
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }

        if (health <= 0) {
            decrease();
        }
    }

    private void decrease() {
        getWorld().removeObject(this);
    }

    public int getHealth() {
        return health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
