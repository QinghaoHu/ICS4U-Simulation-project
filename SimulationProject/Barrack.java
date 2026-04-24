import greenfoot.*;

public class Barrack extends Buildings{
    
    private GreenfootImage img;
    private final int cost = 100; 
    
    public Barrack (Team team){
        super(team, 500);
        setupImage();
        setImage(img);
    }

    public void act(){
        super.act();
    }
    
    public int getCost(){
        return 100; 
    }

    public boolean addPeople() {
        if (getWorld() == null) {
            return false;
        }
        Marine marine = new Marine(team);
        if (!team.spendMoney(marine.getCost())){
            return false;
        }
        
        getWorld().addObject(marine, getX(), getY());
        return true;
    }

    private void setupImage(){
        if (team == null) {
            return;
        }
        
        img = new GreenfootImage(team.getName() + getClass().getName() +  ".png");
        
        img.scale(100, 100);

        if (img != null) {
            setImage(img);
        }
    }
}
