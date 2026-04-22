import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Base here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Base extends Buildings {
    private GreenfootImage img;

    public Base(Team team) {
        super(team, 1000);
        setupImage();
        setImage(img);
    }

    /**
     * Act - do whatever the Base wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        super.act();
    }

    private void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            img = new GreenfootImage("RedHomeBase.png");
        } else if (team.getTeamId() == Team.BLUE) {
            img = new GreenfootImage("BlueHomeBase.png");
        }
        img.scale(110, 110);

        if (img != null) {
            setImage(img);
        }
    }

    public void addPeople() {
        Worker worker = new Worker(team, this);
        getWorld().addObject(worker, getX(), getY());
    }
}
