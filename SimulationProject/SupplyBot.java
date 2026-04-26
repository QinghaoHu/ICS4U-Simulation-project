import greenfoot.*;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class SupplyBot extends People{
    private static int MAX_HEALTH = 150;
    private static double SPEED = 4; 
    private static int cost = 85;
    private static int maxSupplyBotCoolDown = 200;

    private Supply targetSupply;
    private GreenfootImage img;

    private int suppliesCollected = 0;
    private final int MAX_SUPPLIES = 1;
    
    private static GreenfootSound collectSupplyCrateSound = new GreenfootSound("supply crate.mp3");

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
            return;
        }

        collectSupply();
    }

    private void collectSupply(){

        if (targetSupply == null || targetSupply.getWorld() == null){
            targetSupply = findTargetSupply();
        }

        moveTowardsSupply();

        if (targetSupply != null && getWorld() != null && isTouching(Supply.class)) {
            targetSupply = null;
            suppliesCollected++;
            
            collectSupplyCrateSound.play();
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

    private void setupImage(){
        img = new GreenfootImage ("SupplyBot.png");
        img.scale(30, 30);
        setImage(img);
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
}