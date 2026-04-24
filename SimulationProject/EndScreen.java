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
    int winner;
    
    GreenfootImage redWin = new GreenfootImage("RedBase.png");
    GreenfootImage blueWin = new GreenfootImage("BlueBase.png");
    public EndScreen(int w){
        winner = w;
    }

    public void act()
    {
        if(winner == 1){
            setImage(redWin);
            System.out.println("red wins");
        }
        else if(winner == 0){
            setImage(blueWin);
            System.out.println("blue wins");
        }
        Greenfoot.stop();
    }
}
