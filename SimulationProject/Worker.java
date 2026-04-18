import greenfoot.*;
import java.util.List;
/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Worker extends People {
    protected int carryAmount;
    protected int maxCarry;
    protected int minRate;

    private GreenfootImage img;
    private Resources targetResource;
    private Base homeBase;

    private String state = "toResource";
    private int mineTimer = 0;
    private int depositTimer = 0;
    private int speed = 2;//set to 2, may change speed to even slower in the future

    public Worker(Team team, Base base) {
        super(team);
        health = 100;

        this.homeBase = base;

        if (team != null) {
            team.addUnit(this);
        }

        carryAmount = 0;
        maxCarry = 10;
        minRate = 5;

        setupImage(); //forgot to add before
    }

    public void act() {
        super.act();

        if (state.equals("toResource")) {
            goToResource();
        } else if (state.equals("mining")) {
            mine();
        } else if (state.equals("toBase")) {
            goToBase();
        } else if (state.equals("depositing")) {
            deposit();
        }
    }

    private void mine() {
        mineTimer--;

        if (mineTimer <= 0) {
            carryAmount = maxCarry;
            state = "toBase";
        }
    }

    private void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            img = new GreenfootImage("RedWorkerRegular.png");
        } else if (team.getTeamId() == Team.BLUE) {
            img = new GreenfootImage("BlueWorkerRegular.png");
        }

        if (img != null) {
            img.scale(40, 40);
            setImage(img);
        }
    }

    private void goToResource() {
        if (getWorld() == null) return;

        if (targetResource == null || targetResource.getWorld() == null) {
            List<Resources> resources = getWorld().getObjects(Resources.class);

            if (!resources.isEmpty()) {
                targetResource = resources.get(0);
            } else {
                return;
            }
        }

        if (targetResource == null) return;

        moveTowards(targetResource.getX(), targetResource.getY());

        double dist = Math.hypot(
            getX() - targetResource.getX(),
            getY() - targetResource.getY()
        );

        if (dist < 30) { //this is for hitbox as worker hitbox is originally too big
            state = "mining";
            mineTimer = 120;
        }
    }

    public void buildBarrack() {
        Barrack bar = new Barrack(super.team);
        getWorld().addObject(bar, getX(), getY());
    }

    private void goToBase() {
        moveTowards(homeBase.getX(), homeBase.getY());

        double dist = Math.hypot(
            getX() - homeBase.getX(),
            getY() - homeBase.getY()
        );

        if (dist < 45) { //THIS IS FOR HITBOX AS WORKER HITBOX IS ORIGINALLY TOO BIG
            state = "depositing";
            depositTimer = 120;
        }
    }

    private void deposit() {
        depositTimer--;

        if (depositTimer <= 0) {
            team.addMoney(carryAmount);
            carryAmount = 0;

            state = "toResource";
            targetResource = null;
        }
    }

    private void moveTowards(int x, int y) {
        int dx = x - getX();
        int dy = y - getY();

        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > 0) {
            double vx = (dx / dist) * speed;
            double vy = (dy / dist) * speed;

            setLocation(getX() + (int) vx, getY() + (int) vy);

            if (img != null) {
                setRotation((int) Math.toDegrees(Math.atan2(dy, dx)));
            }
        }
    }
}