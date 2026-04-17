import greenfoot.*;

public class Barrack extends Buildings{
    private GreenfootImage img;
    public Barrack (Team team){
        super(team);
        setupImage();
        setImage(img);
    }

    public void act(){

    }

    public People addPeople(){
        return super.addPeople("Soldier");
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
}
