import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Base here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Base extends Buildings
{
    private GreenfootImage img;
    
    public Base (int team){
        setImage(team);
        
    }
    
    /**
     * Act - do whatever the Base wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
    
    private void setImage(int team){
        if(team == Team.RED){
            img = new GreenfootImage("RedHomeBase.png");
        }else if( team == Team.BLUE){
            img = new GreenfootImage("BlueHomeBase.png");
        }
    }
    
    public void addPeople(String type){
        if(type.equals("Worker")){
            getWorld().addObject(new Worker(), getX(), getY());
        } else if (type.equals("Soldier")){
            getWorld().addObject(new Soldier(), getX(), getY());
        }
    }
}
