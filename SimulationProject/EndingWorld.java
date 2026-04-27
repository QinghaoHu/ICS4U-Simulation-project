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
    private Button startButton;
    private static final int BUTTON_Y_POSITION = 100;
    private static final int START_BUTTON_X = 1000;

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
        setupButtons();
    
    }
    
    public void act(){
        checkClickButton();

    }
     private void setupButtons(){
        startButton = new Button("Restart", 150);
        addObject(startButton, START_BUTTON_X, BUTTON_Y_POSITION);
    }

    private void checkClickButton(){
        if (Greenfoot.mouseClicked(startButton)) {
            Greenfoot.setWorld(new StartingWorld());
        }
    }
}
