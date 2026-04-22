import greenfoot.*;
import java.util.List;

public class Worker extends People {

    protected int carryAmount;
    protected int maxCarry;
    protected int minRate;

    private GreenfootImage emptyImg;
    private GreenfootImage miningImg;

    private Resources targetResource;
    private Resources assignedResource;
    private Base homeBase;

    private String state = "move";
    private String nextState = "mining";

    private int timer = 0;

    private static int redIndex = 0;
    private static int blueIndex = 0;

    public Worker(Team team, Base base) {
        super(team, 30, 2);

        this.homeBase = base;

        if (team != null) {
            team.addUnit(this);
        }

        this.cost = 50;

        carryAmount = 0;
        maxCarry = 15;
        minRate = 5;

        setupImage();
    }

    public void addedToWorld(World w) {
        targetPosition = resourceLocation();
    }

    public void act() {
        if (state.equals("move")) {
            move();
        } else if (state.equals("mining")) {
            mine();
        } else if (state.equals("depositing")) {
            deposit();
        } else if (state.equals("building")) {
            build();
        }

        updateImage();
        super.act();
    }

    private void mine() {
        timer--;

        if (timer <= 0) {
            carryAmount = maxCarry;
            state = "move";
            targetPosition = goToBase();
            nextState = "depositing";
        }
    }

    private void build() {
        if (timer <= 0){
            getWorld().addObject(new Turret(team), getX(), getY());
        }
    }

    private void move() {
        moveTowards(targetPosition[0], targetPosition[1]);

        double dist = Math.hypot(
                getX() - targetPosition[0],
                getY() - targetPosition[1]
        );

        if (dist < 55) {
            timer = 120;
            if (state.equals("build")){
                timer = 1200;
            }

            state = nextState;
            nextState = "move"; 
        }
    }
    
    public boolean isAreaFree(int x, int y, List<Buildings> buildings) {
        int size = 50;

        for (Buildings b : buildings) {
            int bx = b.getX();
            int by = b.getY();

            if (Math.abs(x - bx) < size &&
                Math.abs(y - by) < size) {
                return false;
            }
        }

        return true;
    }

    public int[] findValidLocation(World world) {
        int width = world.getWidth();
        int height = world.getHeight();

        List<Buildings> buildings = world.getObjects(Buildings.class);

        for (int i = 0; i < 1000; i++) {
            int x = Greenfoot.getRandomNumber(width) - 25;
            int y = Greenfoot.getRandomNumber(height);

            if (isAreaFree(x, y, buildings)) {
                return new int[]{x, y};
            }
        }

        return new int[]{-1, -1}; // no space found
    }

    private void setupImage() {
        if (team == null) return;

        if (team.getTeamId() == Team.RED) {
            emptyImg = new GreenfootImage("RedWorkerRegular.png");
            miningImg = new GreenfootImage("RedWorkerMining.png");
        } else if (team.getTeamId() == Team.BLUE) {
            emptyImg = new GreenfootImage("BlueWorkerRegular.png");
            miningImg = new GreenfootImage("BlueWorkerMining.png");
        }

        if (emptyImg != null) emptyImg.scale(50, 50);
        if (miningImg != null) miningImg.scale(50, 50);
    }

    private int[] resourceLocation() {
        if (getWorld() == null) return new int[]{-1, -1};

        if (assignedResource == null) {
            List<Resources> all = getWorld().getObjects(Resources.class);
            List<Resources> valid = new java.util.ArrayList<>();

            for (Resources r : all) {
                if (r.getTeamSide() == team.getTeamId()) {
                    valid.add(r);
                }
            }

            if (valid.isEmpty()) return new int[]{-1, -1};

            if (team.getTeamId() == Team.RED) {
                assignedResource = valid.get(redIndex % valid.size());
                redIndex++;
            } else {
                assignedResource = valid.get(blueIndex % valid.size());
                blueIndex++;
            }
        }

        targetResource = assignedResource;

        if (targetResource == null) return new int[]{-1, -1};

        return new int[]{targetResource.getX(), targetResource.getY()};
    }

    private int[] goToBase() {
        if (homeBase == null || homeBase.getWorld() == null) {
            return new int[]{-1, -1};
        }
        return new int[]{homeBase.getX(), homeBase.getY()};
    }

    private void deposit() {
        timer--;

        if (timer <= 0) {
            team.addMoney(carryAmount);
            carryAmount = 0;

            state = "move";
            targetPosition = resourceLocation();
        }
    }

    private void updateImage() {
        if (carryAmount > 0) {
            setImage(miningImg);
        } else {
            setImage(emptyImg);
        }
    }
}