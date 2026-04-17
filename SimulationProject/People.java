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
    }
}
