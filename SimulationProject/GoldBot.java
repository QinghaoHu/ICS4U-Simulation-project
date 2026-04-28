import greenfoot.*;

public class GoldBot extends People
{
    private Base homeBase;
    private GoldMineral goldMineral;

    private int timer = 0;
    private int carryAmount = 0;

    private boolean goingToGold = true;

    public GoldBot(Team team, Base base) {
        super(team, 300, 5);
        this.homeBase = base;

        if (team != null) {
            team.addUnit(this);
        }
    }

    protected void addedToWorld(World w) {
        if (!w.getObjects(GoldMineral.class).isEmpty()) {
            goldMineral = w.getObjects(GoldMineral.class).get(0);
        }
    }

    public void act() {
        if (goldMineral == null || homeBase == null) {
            return;
        }

        if (timer > 0) {
            timer--;
            return;
        }

        if (goingToGold) {
            moveTowards(goldMineral.getX(), goldMineral.getY());

            double dist = Math.hypot(
                getX() - goldMineral.getX(),
                getY() - goldMineral.getY()
            );

            if (dist < 35) {
                timer = 60;
                carryAmount = 75;
                goingToGold = false;
            }
        } else {
            moveTowards(homeBase.getX(), homeBase.getY());

            double dist = Math.hypot(
                getX() - homeBase.getX(),
                getY() - homeBase.getY()
            );

            if (dist < 35) {
                team.addMoney(carryAmount);
                carryAmount = 0;
                timer = 60;
                goingToGold = true;
            }
        }

        super.act();
    }
}