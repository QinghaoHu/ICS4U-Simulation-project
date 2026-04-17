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
    
    public Base(Team team){
        super(team);
        setupImage();
        setImage(img);
    }
    
    /**
     * Act - do whatever the Base wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {

    }
    
    private void setupImage(){
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            img = new GreenfootImage("RedHomeBase.png");
        } else if (team.getTeamId() == Team.BLUE) {
            img = new GreenfootImage("BlueHomeBase.png");
        }

        if (img != null) {
            setImage(img);
        }
    }
    
    public People addPeople(){
        return super.addPeople("Worker");
    }
}
