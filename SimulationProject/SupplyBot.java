import greenfoot.*;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class SupplyBot extends People{
    private static int MAX_HEALTH = 60;
    private static double SPEED = 4; 
    private static int cost = 100;
    private static int maxSupplyBotCoolDown = 200;
    private static int BASE_HEALTH_GAIN = 2;
    private static int BASE_MAX_HEALTH_GAIN = 2;

    private int growthTimer = 0;
    private int currentSize = 50;
    
    
    private Supply targetSupply;
    private GreenfootImage img;

    private int suppliesCollected = 0;
    private final int MAX_SUPPLIES = 1;
    
    private static final String SUPPLY_CRATE_SOUND_FILE = "supply crate.mp3";

    public SupplyBot(Team team) {
        super(team, MAX_HEALTH, 4); 

        if (team != null) {
            team.addUnit(this);
            team.addSupplyBot(this);
        }
        
        targetSupply = null;
        setupImage();
    }

    public void act (){
        if(getWorld() == null) return;
        super.act();

        if (suppliesCollected >= MAX_SUPPLIES) {
            growTank();
            return;
        }
        
        collectSupply();
        updateStatBar();
    }

    private void collectSupply(){

        if (targetSupply == null || targetSupply.getWorld() == null){
            targetSupply = findTargetSupply();
        }

        moveTowardsSupply();

        if (targetSupply != null && getWorld() != null && isTouching(Supply.class)) {
            targetSupply = null;
            suppliesCollected++;
            
            ResourceCache.playSound(SUPPLY_CRATE_SOUND_FILE, 20);
        }
    }

    private Supply findTargetSupply(){
        if (getWorld() == null){    
            return null;
        }
        ArrayList<Supply> supplies = (ArrayList<Supply>)getWorld().getObjects(Supply.class);

        Supply closest = null;
        double shortestDist = Double.MAX_VALUE;

        for (Supply s : supplies) {

            double distX = s.getX() - this.getX();
            double distY = s.getY() - this.getY();
            double distance = Math.sqrt(distX * distX + distY * distY);

            if (distance < shortestDist) {
                shortestDist = distance;
                closest = s;
            }
        }

        return closest;
    }

    private void moveTowardsSupply(){
        if (targetSupply == null || targetSupply.getWorld() == null || getWorld() == null)
        {
            return;
        }

        turnTowards(targetSupply.getX(), targetSupply.getY());


       move(SPEED);
    }

    private void setupImage() {
        if (team == null) {
            return;
        }

        img = ResourceCache.getImage(team.getName() + getClass().getName() +  ".png");
        img.scale(50, 50);

        if (img != null) {
            setImage(img);
        }
    }

    public static int getCost() {
        return cost;
    }

    public static int getMaxSupplyBotCoolDown() {
        return maxSupplyBotCoolDown;
    }

    public static void modifyMaxSupplyBotCoolDown(int supplyBotCoolDown) {
        maxSupplyBotCoolDown = supplyBotCoolDown;
    }
    
    private void growTank() {
        growthTimer++;
    
        if (growthTimer >= 60) {
            maxHealth += BASE_MAX_HEALTH_GAIN;
            health += BASE_MAX_HEALTH_GAIN;
    
            currentSize += 1;
    
            GreenfootImage newImg = ResourceCache.getImage(team.getName() + getClass().getName() + ".png");
            newImg.scale(currentSize, currentSize);
            setImage(newImg);
    
            updateStatBar();
            
            growthTimer = 0;
        }
    }
    
    public void upgradeCurrentBot() {
        maxHealth += 20;
        health += 20;
        updateStatBar();
    }
    
    public static void upgradeSupplyBot() {
        MAX_HEALTH += 25;
        
    }
}
