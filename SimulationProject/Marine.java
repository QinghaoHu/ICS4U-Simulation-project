import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Basic front-line soldier.
 */
public class Marine extends Soldier
{
    private static final int cost = 100;
    private static int maxMarineCoolDown = 240;
    private int bonusDamage;
    
    public Marine(Team team) {
        super(team);
        if (team != null) {
            team.addUnit(this);
    
            Base base = team.getBase();
            if (base != null) {
                int level = base.getLevel();
                bonusDamage = (level - 1);
                maxHealth += (level - 1) * 3;
                health = maxHealth;
            }
        }
    }
    
    public static int getCost(){
        return 75; 
    }
    
    public void act()
    {
        super.act();
        
        if (health <= 0) {
            // Death sound gets picked at random.
            int random = Greenfoot.getRandomNumber(2);
            ResourceCache.playSound("MarineDeath" + random + ".mp3", 15);
        }
    }

    protected void shoot(Entity target){ // will create a bullet at the tip of it's gun going towards the entity it wants to shoot at
        // Marine shots are simple and direct.
        turnTowards(target.getX(), target.getY());
        double angle = shootAngle(target);
        int X = getX() + (int)(centerDist * Math.cos(Math.toRadians(angle)));
        int Y = getY() + (int)(centerDist * Math.sin(Math.toRadians(angle)));
        getWorld().addObject(new SoldierBullet(this, angle, 8, 3 + bonusDamage), X, Y); // adds bullet
    }

    public static int getMaxMarineCoolDown(Base base) {
        if (base == null) {
            return maxMarineCoolDown;
        }

        // Base level trims the unit wait time.
        int level = base.getLevel();
        int reduced = maxMarineCoolDown - ((level - 1) * 7);
    
        if (reduced < 7) {
            reduced = 7;
        }
    
        return reduced;
    }

    public static void modifyMaxMarineCoolDown(int marineCoolDown) {
        maxMarineCoolDown = marineCoolDown;
    }
    
    public int getTotalDamage() {
        return 3 + bonusDamage;
    }
}
