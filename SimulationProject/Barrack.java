import greenfoot.*;

public class Barrack extends Buildings{
    
    private GreenfootImage img;
    
    public Barrack (Team team){
        super(team, 500);
        setupImage();
        setImage(img);
        this.cost = 150; 
    }

    public void act(){
        super.act();
    }

    public People addPeople(){
        return super.addPeople("Marine");
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
