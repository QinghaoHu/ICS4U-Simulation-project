import greenfoot.*;

public class Barrack extends Buildings{
    
    private GreenfootImage img;
    private int counter;
    
    public Barrack (Team team){
        super(team, 500);
        setupImage();
        setImage(img);
        this.cost = 150; 
    }

    public void act(){
        counter++;
        super.act();
        
        if (counter % 300 == 0) {
        getWorld().addObject(new Marine(team), getX() + getImage().getWidth()/2, getY());
        }
    }

    public People addPeople(){
        return super.addPeople("Officer");
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
