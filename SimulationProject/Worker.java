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
    private GreenfootImage emptyImg;
    private GreenfootImage miningImg;
    private Resources targetResource;
    private Resources assignedResource;
    private Base homeBase;

    private String state = "toResource";
    private int mineTimer = 0;
    private int depositTimer = 0;
    private int speed = 2;//set to 2, may change speed to even slower in the future
    
    
    //sets the side in which resources it goes to
    private static int redIndex = 0;
    private static int blueIndex = 0;

    public Worker(Team team, Base base) {
        super(team, 30, 2);
        health = 100;

        this.homeBase = base;

        if (team != null) {
            team.addUnit(this);
        }

        carryAmount = 0;
        maxCarry = 15;
        minRate = 5;

        setupImage(); //forgot to add before
    }

    public void act() {
        if (state.equals("toResource")) {
            goToResource();
        } else if (state.equals("mining")) {
            mine();
        } else if (state.equals("toBase")) {
            goToBase();
        } else if (state.equals("depositing")) {
            deposit();
        }
        
        updateImage();
        super.act();
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
            emptyImg = new GreenfootImage("RedWorkerRegular.png");
            miningImg = new GreenfootImage("RedWorkerMining.png");
        } else if (team.getTeamId() == Team.BLUE) {
            emptyImg = new GreenfootImage("BlueWorkerRegular.png");
            miningImg = new GreenfootImage("BlueWorkerMining.png");
        }

        if (emptyImg != null) {
            emptyImg.scale(50, 50);
            setImage(img);
        }
        
        if (miningImg != null) {
            miningImg.scale(50, 50);
            setImage(img);
        }
        
        setImage(emptyImg);
    }

    private void goToResource() {
        if (getWorld() == null) return;

        if (assignedResource == null) {
            List<Resources> allResources = getWorld().getObjects(Resources.class);
            List<Resources> validResources = new java.util.ArrayList<>();

            // filters the resouces of the team so that it doesnt grab from the other team
            for (Resources r : allResources) {
                if (r.getTeamSide() == team.getTeamId()) {
                    validResources.add(r);
                }
            }
            
            // cycle through only valid resources
            if (!validResources.isEmpty()) {
                if (team.getTeamId() == Team.RED) {
                    assignedResource = validResources.get(redIndex % validResources.size());
                    redIndex++;
                } else {
                    assignedResource = validResources.get(blueIndex % validResources.size());
                    blueIndex++;
                }
            } else {
                return;
            }
        }

        targetResource = assignedResource;

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
        if (homeBase.getWorld() == null){
            return;
        }
        moveTowards(homeBase.getX(), homeBase.getY());

        double dist = Math.hypot(
                getX() - homeBase.getX(),
                getY() - homeBase.getY()
        );

        if (dist < 55) { //THIS IS FOR HITBOX AS WORKER HITBOX IS ORIGINALLY TOO BIG
            state = "depositing";
            depositTimer = 120;
        }
    }

    private void deposit() {
        depositTimer--;

        if (depositTimer <= 0) {
            team.addResources(carryAmount);
            carryAmount = 0;

            state = "toResource";
        }
    }
    
    private void updateImage(){
        if(carryAmount > 0){
            setImage(miningImg);
        } else{
            setImage(emptyImg);
        }
    }
}
