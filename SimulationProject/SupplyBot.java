import greenfoot.GreenfootImage;

import java.util.ArrayList;

public class SupplyBot extends People{
    private static int MAX_HEALTH = 100;
    private static double SPEED = 0.6; 
    private static int cost = 85;
    private static int maxSupplyBotCoolDown = 200;

    private Supply targetSupply;
    private GreenfootImage img;

    private int suppliesCollected = 0;
    private final int MAX_SUPPLIES = 2;

    public SupplyBot(Team team) {
        super(team, MAX_HEALTH, 1); 

        if (team != null) {
            team.addUnit(this);
            team.addSupplyBot(this);
        }

        targetSupply = null;
        setupImage();
    }

    public void act (){
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

        if (targetSupply != null && isTouching(Supply.class)) {
            removeTouching(Supply.class);
            targetSupply = null;
            suppliesCollected++;
        }
    }

    private Supply findTargetSupply(){
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
        if (targetSupply == null || targetSupply.getWorld() == null)
        {
            return;
        }

        turnTowards(targetSupply.getX(), targetSupply.getY());


       move(1);
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