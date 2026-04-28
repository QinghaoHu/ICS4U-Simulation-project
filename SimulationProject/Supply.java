import greenfoot.*;

import java.util.ArrayList;
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

    private boolean isChaosMode;
    private int targetX;
    private int targetY;
    private int dropSpeed;
    private boolean hasLanded;
    private GreenfootImage img;

    public Supply(boolean isChaosMode) {
        dropSpeed = Greenfoot.getRandomNumber(MAX_DROP_SPEED - MIN_DROP_SPEED + 1) + MIN_DROP_SPEED;
        this.isChaosMode = isChaosMode;
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
                String reward = applyBuff(bot.getTeam(), bot.getOpponentTeam(), isChaosMode);
                
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

    private String applyBuff(Team team, Team opponentTeam, boolean isChaosMode) {
        if (team == null || opponentTeam == null) {
            return "Nothing";
        }
    
        int roll = Greenfoot.getRandomNumber(100);
        int chance = Greenfoot.getRandomNumber(2);
        if(isChaosMode && chance == 0){
            if (roll < COMMON_CHANCE) {
                removeDrop(opponentTeam, COMMON_RESOURCE_DROP, COMMON_WORKER_DROP, 0);
                return "-" + COMMON_RESOURCE_DROP + " RESOURCES, -" + COMMON_WORKER_DROP + " WORKER";
            }
            else if (roll < COMMON_CHANCE + RARE_CHANCE) {
                removeDrop(opponentTeam, RARE_RESOURCE_DROP, RARE_WORKER_DROP, 0);
                return "-" + RARE_RESOURCE_DROP + " RESOURCES, -" + RARE_WORKER_DROP + " WORKERS";
            }
            else {
                int marineDrop = 0;

                if (opponentTeam.hasBuilding(Barrack.class)) {
                    marineDrop = ULTRA_RARE_MARINE_DROP;
                }

                removeDrop(opponentTeam, ULTRA_RARE_RESOURCE_DROP, ULTRA_RARE_WORKER_DROP, marineDrop * 2);

                return "-" + ULTRA_RARE_RESOURCE_DROP + " RESOURCES, -"
                        + ULTRA_RARE_WORKER_DROP + " WORKERS, -"
                        + marineDrop + " MARINE";
            }
        }else if(!isChaosMode || chance != 1){
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
        return null;
    }

    private void applyDrop(Team team, int resources, int workerCount, int marineCount) {
        team.addResources(resources);
        spawnWorkers(team, workerCount);
        spawnMarines(team, marineCount);
    }

    private void removeDrop(Team team, int resources, int workerCount, int marineCount) {
        team.addResources(-resources);
        removeWorkers(team, workerCount);
        removeMarines(team, marineCount);
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

    private void removeWorkers (Team team, int workerCount){
        ArrayList<Worker> workers = (ArrayList<Worker>) getWorld().getObjects(Worker.class);
        if (workers.size() >= 2) {
            for (int i = 0; i < workerCount; i++){
                getWorld().removeObject(workers.get(i));
            }
        }
    }

    private void removeMarines (Team team, int marineCount){
        ArrayList<Marine> marines = (ArrayList<Marine>) getWorld().getObjects(Marine.class);
        if (marines.size() >= marineCount) {
            for (int i = 0; i < marineCount; i++){
                getWorld().removeObject(marines.get(i));
            }
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
        img = ResourceCache.getImage("upgrades.png");
        img.scale(40,40);
        setImage(img);
    }
}
