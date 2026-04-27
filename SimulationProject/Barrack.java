import greenfoot.*;

public class Barrack extends Buildings{
    
    private GreenfootImage img;
    private final int cost = 100; 
    private int marineCoolDown = 0; //individually manages marine spawning cooldown

    public Barrack (Team team){
        super(team, 500);
        setupImage();
        setImage(img);
    }

    public void act(){
        super.act();
        
        if (marineCoolDown > 0) marineCoolDown--;
    }
    
    public int getCost(){
        return 100; 
    }

    public boolean addPeople() { // adds a marine into the world
        if (getWorld() == null) {
            return false;
        }

        if (marineCoolDown > 0) {
            return false;
        }

        Marine marine = new Marine(team);
        if (!team.spendMoney(marine.getCost())){
            return false;
        }
        
        getWorld().addObject(marine, getX(), getY());
        UI.reportUpgrade(team, "Marine", 1);

        marineCoolDown = Marine.getMaxMarineCoolDown();

        return true;
    }

    private void setupImage(){
        if (team == null) {
            return;
        }
        
        img = ResourceCache.getImage(team.getName() + getClass().getName() +  ".png");
        
        img.scale(100, 100);

        if (img != null) {
            setImage(img);
        }
    }
}
