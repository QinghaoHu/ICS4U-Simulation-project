import greenfoot.*;

import java.util.List;

public class Supply extends SuperSmoothMover {
    private static final int MIN_DROP_SPEED = 1;
    private static final int MAX_DROP_SPEED = 3;
    private static final int COMMON_CHANCE = 70;
    private static final int RARE_CHANCE = 25;
    private static final int COMMON_RESOURCE_DROP = 175;
    private static final int RARE_RESOURCE_DROP = 250;
    private static final int ULTRA_RARE_RESOURCE_DROP = 350;
    private static final int COMMON_WORKER_DROP = 2;
    private static final int RARE_WORKER_DROP = 3;
    private static final int ULTRA_RARE_WORKER_DROP = 4;
    private static final int ULTRA_RARE_MARINE_DROP = 1;
    private static final int MIN_LANDING_Y = 60;

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
        int imageHalfHeight = getImage().getHeight() / 2;
        int maxLandingY = UI.PLAY_AREA_BOTTOM_Y - imageHalfHeight;
        if (maxLandingY <= MIN_LANDING_Y) {
            targetY = maxLandingY;
        } else {
            targetY = MIN_LANDING_Y + Greenfoot.getRandomNumber(maxLandingY - MIN_LANDING_Y + 1);
        }
    }

    private void moveToTargetPosition() {
        double distance = MyWorld.getDistance(this, targetX, targetY);
        if (distance < dropSpeed) {
            setLocation(targetX, targetY);
            hasLanded = true;
            return;
        }

        setLocation(targetX, getY() + dropSpeed);
    }

    private void checkForPickup() {
        List<SupplyBot> bots = getIntersectingObjects(SupplyBot.class);

        for (SupplyBot bot : bots) {
            if (bot != null && bot.getTeam() != null) {
                String reward = applyBuff(bot.getTeam());
                
                String message = reward;

                System.out.println(message);
                
                Color teamColor = Color.WHITE;
                
                if (bot.getTeam().getTeamId() == Team.RED) {
                    teamColor = Color.RED;
                } else if (bot.getTeam().getTeamId() == Team.BLUE) {
                    teamColor = Color.BLUE;
                }
                
                if (getWorld() != null) {
                    getWorld().addObject(new SupplyText(message, teamColor), getX(), getY() - 30);
                }
                                
                if (getWorld() != null) {
                    getWorld().removeObject(this);
                }
                return;
            }
        }
    }

    private String applyBuff(Team team) {
        if (team == null) {
            return "Nothing";
        }
    
        int roll = Greenfoot.getRandomNumber(100);
    
        if (roll < COMMON_CHANCE) {
            applyDrop(team, COMMON_RESOURCE_DROP, COMMON_WORKER_DROP, 0);
            return "+" + COMMON_RESOURCE_DROP + " RESOURCES, +" + COMMON_WORKER_DROP + " WORKER";
        } 
        else if (roll < COMMON_CHANCE + RARE_CHANCE) {
            applyDrop(team, RARE_RESOURCE_DROP, RARE_WORKER_DROP, 0);
            return "+" + RARE_RESOURCE_DROP + " RESOURCES, +" + RARE_WORKER_DROP + " WORKERS";
        } 
        else {
            int marineDrop = 0;
    
            if (team.hasBuilding(Barrack.class)) {
                marineDrop = ULTRA_RARE_MARINE_DROP;
            }
    
            applyDrop(team, ULTRA_RARE_RESOURCE_DROP, ULTRA_RARE_WORKER_DROP, marineDrop);
    
            return "+" + ULTRA_RARE_RESOURCE_DROP + " RESOURCES, +"
                    + ULTRA_RARE_WORKER_DROP + " WORKERS, +"
                    + marineDrop + " MARINE";
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

        UI.reportUpgrade(team, "Worker", workerCount);
    }

    private void spawnMarines(Team team, int marineCount) {
        Barrack barrack = findBarrack(team);
        World world = getWorld();
        if (world == null || barrack == null) {
            return;
        }

        for (int i = 0; i < marineCount; i++) {
            Marine marine = new Marine(team);
            world.addObject(marine, barrack.getX(), barrack.getY());
        }

        UI.reportUpgrade(team, "Marine", marineCount);
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
        img = new GreenfootImage("upgrades.png");
        img.scale(40,40);
        setImage(img);
    }
}
