/**
 * Write a description of class Animation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import greenfoot.*;
import java.lang.Math; 

public class Animation  
{
    
    private GreenfootImage images[];
    private int frame = 0;
    private int curDelay;
    private int maxDelay;
    private boolean loop = true;
    
    public Animation(String path, int length, int delay)
    {// get the path of animation, the amount of pngs in the animation, and the wait between each png
        maxDelay = delay; 
        curDelay = delay;
        images = new GreenfootImage[length]; // sets an appropriate length to the animation
        for (int i = 0; i< length; i++){
            GreenfootImage curImg = ResourceCache.getImage(path + "/" + i + ".png"); // sets each image to respective space in the list
            if (curImg != null){
                images[i] = curImg; // 
            }
        }
    }
    
    public Animation(String path, int length, int delay, boolean looped)
    { // same as above but check if you want to loop the animation
        maxDelay = delay;
        curDelay = delay;
        loop = false;
        images = new GreenfootImage[length];
        for (int i = 0; i< length; i++){  
            GreenfootImage curImg = ResourceCache.getImage(path + "/" + i + ".png");
            if (curImg != null){
                images[i] = curImg; 
            }
        }
    }
    
    public GreenfootImage img(boolean flipped){ // return the respective image and flip it if it is flipped
        if (curDelay == 0){
            frame += 1;
            if (loop){
                frame %= images.length; // loop the animation 
            }else{
                frame = Math.min(images.length-1, frame); // if not looped then don't loop
            }
            curDelay = maxDelay; // gets a cycle of delaying it until it time to go to the next image
        }else{
            curDelay -= 1;
        }
        GreenfootImage image = images[frame];
        if (flipped){
            image.mirrorHorizontally(); //flips the image if you need to
        }
        return images[frame];   
    }
    
    public boolean isFinished(){
        return frame == images.length-1; // checks if the animation is at the end
    }
}
