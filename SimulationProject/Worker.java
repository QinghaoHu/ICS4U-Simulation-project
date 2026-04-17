public class Worker extends People{
    protected int carryAmount;
    protected int maxCarry;
    protected int minRate;

    public Worker(){
        super();
    }

    public Worker(Team team) {
        super(team);
        if (team != null) {
            team.addUnit(this);
        }

        carryAmount = 0;
        maxCarry = 10;
        minRate = 5;
    }

    public void act(){
        super.act();
        mineResources();
    }

    private void mineResources(){
        Resources resource;
        resource = (Resources) getOneIntersectingObject(Resources.class);
        if (resource != null && carryAmount < maxCarry) {
            mine(resource);
        }
    }

    private void mine(Resources resource) {
        getWorld().removeObject(resource);
        carryAmount++;
    }

    public void buildBarrack() {
        Barrack bar = new Barrack(super.team);
        getWorld().addObject(bar, getX(), getY());
    }
}
