import greenfoot.*;

import java.util.List;

public class Supply extends SuperSmoothMover {
    private static final int MIN_DROP_SPEED = 3;
    private static final int MAX_DROP_SPEED = 8;
    private static final int MULTIPLY_RESOURCES_BUFF = 0;
    private static final int ADD_RESOURCES_BUFF = 1;
    private static final double MIN_RESOURCES_MULTIPLIER = 1.2;
    private static final double MAX_RESOURCES_MULTIPLIER = 2.0;
    private static final int MIN_RESOURCES = 200;
    private static final int MAX_RESOURCES = 500;

    private int targetX;
    private int targetY;
    private int dropSpeed;
    private boolean hasLanded;
    private int buffType;
    private GreenfootImage img;

    public Supply() {
        dropSpeed = Greenfoot.getRandomNumber(MAX_DROP_SPEED - MIN_DROP_SPEED + 1) + MIN_DROP_SPEED;
        buffType = Greenfoot.getRandomNumber(2);
        setupImage();
    }

    public void act() {
        if (!hasLanded) {
            moveToTargetPosition();
            return;
        }

        checkForPickup();
    }

    protected void addedToWorld(World world) {
        targetX = getX();
        targetY = (int) (Math.random()*500) + 300;
    }

    private void moveToTargetPosition() {
        double distance = MyWorld.getDistance(this, targetX, targetY);
        if (distance < dropSpeed) {
            setLocation(targetX, targetY);
            hasLanded = true;
            return;
        }

        setLocation(targetX, getY()-dropSpeed);
    }

    private void checkForPickup() {
        List<Entity> entities = getIntersectingObjects(Entity.class);
        for (Entity entity : entities) {
            if (entity != null && entity.getTeam() != null) {
                applyBuff(entity.getTeam());
                if (getWorld() != null) {
                    getWorld().removeObject(this);
                }
                return;
            }
        }
    }

    private void applyBuff(Team team) {
        if (team == null) {
            return;
        }

        if (buffType == MULTIPLY_RESOURCES_BUFF) {
            multiplyResources(team);
        } else if (buffType == ADD_RESOURCES_BUFF) {
            addResources(team);
        }
    }

    private void multiplyResources(Team team) {
        double multiplier = Greenfoot.getRandomNumber((int)((MAX_RESOURCES_MULTIPLIER - MIN_RESOURCES_MULTIPLIER) * 10) + 1) / 10.0 + MIN_RESOURCES_MULTIPLIER;
        int currentMoney = team.getResources();
        int newMoneyAmount = (int) (currentMoney * multiplier);
        int addedMoney = newMoneyAmount - currentMoney;
        team.addResources(addedMoney);
    }

    private void addResources(Team team) {
        int amount = Greenfoot.getRandomNumber(MAX_RESOURCES - MIN_RESOURCES + 1) + MIN_RESOURCES;
        team.addResources(amount);
    }

    private void setupImage(){
        img = new GreenfootImage("Supply.png");
        setImage(img);
    }

}
