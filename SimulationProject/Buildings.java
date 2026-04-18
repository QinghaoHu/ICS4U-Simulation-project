import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Buildings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Buildings extends Entity
{
    public Buildings() {
        super();
    }

    public Buildings(Team team) {
        super(team);
        if (team != null) {
            team.addBuilding(this);
        }
    }

    /**
     * Act - do whatever the Buildings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }

    public People addPeople(String type){
        if (getWorld() == null || team == null) {
            return null;
        }

        People newPerson = null;

        /*if ("Worker".equals(type)) {
            newPerson = new Worker(team, this);
        } else if ("Soldier".equals(type)) {
            newPerson = new Soldier(team);
        }
        */
        if (newPerson != null) {
            getWorld().addObject(newPerson, getX(), getY());
        }

        return newPerson;
    }
}
