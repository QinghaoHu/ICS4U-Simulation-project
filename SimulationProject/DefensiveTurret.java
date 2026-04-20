import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DefensiveTurret here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DefensiveTurret extends Buildings
{
    /**
     * Act - do whatever the DefensiveTurret wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage img;

    public DefensiveTurret(Team team) {
        super(team, 1000);
        setupImage();
        setImage(img);
    }
    
    private void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            //img = new GreenfootImage("RedHomeBase.png");
        } else if (team.getTeamId() == Team.BLUE) {
            //img = new GreenfootImage("BlueHomeBase.png");
        }
        //img.scale(110, 110);

        if (img != null) {
            //setImage(img);
        }
    }
    
    public void act()
    {
        Entity target = findTarget();
        if (target != null) {
            turnTowards(target.getX(), target.getY());
            if (target instanceof Buildings) {
                ((Buildings) target).takeDamage(damage);
            }
            else if (target instanceof People) {
                ((People) target).setHealth(((People) target).getHealth() - damage);
            }
        }
    }
}
