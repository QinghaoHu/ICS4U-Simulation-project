import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndScreen extends Actor
{
    /**
     * Act - do whatever the EndScreen wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    int loser;
    
    GreenfootImage winner;
    public EndScreen(int l){
        loser = l;
        if(loser == 1){
            winner = new GreenfootImage("redwins.png");
        }
        else if(loser == 0){
            winner = new GreenfootImage("bluewins.png");
        }
    }

    public void act()
    {
        setImage(winner);
        Greenfoot.stop();
    }
}
