import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndingWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndingWorld extends World
{

    /**
     * Constructor for objects of class EndingWorld.
     * 
     */
    int loser;
    
    GreenfootImage winner;

    public EndingWorld(int l)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        loser = l;
        if(loser == 1){
            winner = ResourceCache.getImage("redwins.png");
        }
        else if(loser == 0){
            winner = ResourceCache.getImage("bluewins.png");
        }
        
        setBackground(winner);
    
    }
}
