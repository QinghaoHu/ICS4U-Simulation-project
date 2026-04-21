import greenfoot.*;

import java.util.List;

public class Supply extends SuperSmoothMover {
    private static final int MIN_DROP_SPEED = 3;
    private static final int MAX_DROP_SPEED = 8;
    private static final int COMMON_CHANCE = 70;
    private static final int RARE_CHANCE = 25;
    private static final int COMMON_RESOURCE_DROP = 75;
    private static final int RARE_RESOURCE_DROP = 150;
    private static final int ULTRA_RARE_RESOURCE_DROP = 250;
    private static final int COMMON_WORKER_DROP = 1;
    private static final int RARE_WORKER_DROP = 2;
    private static final int ULTRA_RARE_WORKER_DROP = 3;
    private static final int ULTRA_RARE_MARINE_DROP = 1;

    private int targetX;
    private int targetY;
    private int dropSpeed;
    private boolean hasLanded;
    private GreenfootImage img;

    public Supply() {
        dropSpeed = Greenfoot.getRandomNumber(MAX_DROP_SPEED - MIN_DROP_SPEED + 1) + MIN_DROP_SPEED;
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

        int roll = Greenfoot.getRandomNumber(100);

        if (roll < COMMON_CHANCE) {
            applyDrop(team, COMMON_RESOURCE_DROP, COMMON_WORKER_DROP, 0);
        } else if (roll < COMMON_CHANCE + RARE_CHANCE) {
            applyDrop(team, RARE_RESOURCE_DROP, RARE_WORKER_DROP, 0);
        } else {
            int marineDrop = 0;
            if(team.hasBuilding(Barrack.class)){
                marineDrop = ULTRA_RARE_MARINE_DROP;
            }
            applyDrop(team, ULTRA_RARE_RESOURCE_DROP, ULTRA_RARE_WORKER_DROP, marineDrop);
        }
    }

    private void applyDrop(Team team, int resources, int workerCount, int marineCount) {
        team.addResources(resources);
        spawnWorkers(team, workerCount);
        spawnMarines(team, marineCount);
    }

    private void spawnWorkers(Team team, int workerCount) {
        Base base = findBase(team);
        World world = getWorld();
        if (base == null || world == null) {
            return;
        }

        for (int i = 0; i < workerCount; i++) {
            Worker worker = new Worker(team, base);
            world.addObject(worker, base.getX(), base.getY());
        }
    }

    private void spawnMarines(Team team, int marineCount) {
        Barrack barrack = findBarrack(team);
        World world = getWorld();
        if (world == null || barrack == null) {
            return;
        }

        for (int i = 0; i < marineCount; i++) {
            Soldier soldier = new Soldier(team);
            world.addObject(soldier, barrack.getX(), barrack.getY());
        }
    }

    private Base findBase(Team team) {
        for (Buildings building : team.getBuildings()) {
            if (building instanceof Base && building.getWorld() != null) {
                return (Base) building;
            }
        }

        return null;
    }

    private Barrack findBarrack (Team team) {
        for (Buildings building : team.getBuildings()) {
            if (building instanceof Barrack && building.getWorld() != null) {
                return (Barrack) building;
            }
        }

        return null;
    }

    private void setupImage(){
        img = new GreenfootImage("Supply.png");
        setImage(img);
    }

}
