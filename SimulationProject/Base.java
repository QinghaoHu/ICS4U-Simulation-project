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
        setImage();
    }
    
    /**
     * Act - do whatever the Base wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {

    }
    
    private void setImage(){
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
    
    public People addPeople(String type){
        if (getWorld() == null || team == null) {
            return null;
        }

        People newPerson = null;

        if ("Worker".equals(type)) {
            newPerson = new Worker(team);
        } else if ("Soldier".equals(type)) {
            newPerson = new Soldier(team);
        }

        if (newPerson != null) {
            getWorld().addObject(newPerson, getX(), getY());
        }

        return newPerson;
    }
}
