import greenfoot.*;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class Worker extends People {

    protected int carryAmount = 0;
    protected int maxCarry = 13;
    protected int minRate = 5;

    private GreenfootImage emptyImg;
    private GreenfootImage miningImg;

    private Resources targetResource;
    private Resources assignedResource;
    private Base homeBase;

    private Queue<String> states = new LinkedList<>();
    private Queue<int[]> targetPositions = new LinkedList<>();
    private Queue<Buildings> buildings = new LinkedList<>();

    private int timer = 0;

    private static int redIndex = 0;
    private static int blueIndex = 0;
    private static final int cost = 75;

    private static int maxWorkerCoolDown = 240;
    
    private static GreenfootSound constructionSound = new GreenfootSound("building construction.mp3");
    private static GreenfootSound miningSound = new GreenfootSound("mining.mp3");
    private static GreenfootSound depositSound = new GreenfootSound("resource gain.mp3");

    public Worker(Team team, Base base) {
        super(team, 60, 2);

        this.homeBase = base;

        if (team != null) {
            team.addUnit(this);
            team.addWorker(this);
        }

        states.add("move");
        states.add("mining");
        states.add("move");
        
        miningSound.setVolume(20);
        depositSound.setVolume(30);

        setupImage();
    }

    public static int getCost() {
        return cost;
    }

    @Override
    protected void addedToWorld(World w) {
        targetPositions.add(resourceLocation());
    }

    public void act() {
        if (states.isEmpty()) {
            super.act();
            return;
        }

        timer--;

        String state = states.peek();

        if (state.equals("move")) {
            move();
        } else if (state.equals("mining")) {
            mine();
        } else if (state.equals("depositing")) {
            deposit();
        } else if (state.equals("building")) {
            build(buildings.peek());
        }

        updateImage();
        super.act();
    }

    private void mine() {
        if (timer <= 0) {
            carryAmount = maxCarry;
            states.remove();
            states.add("depositing");
            states.add("move");
            targetPositions.add(goToBase());
        }
        if (timer % 50 == 0) {
            miningSound.play();
        }
    }

    public int available() {
        return states.size();
    }

    public void build(Buildings building) {
        if (timer <= 0) {
            if (getWorld() == null || building == null) {
                return;
            }

            getWorld().addObject(building, getX(), getY());
            team.correctBuildingList(building);

            states.remove();
            states.add("mining");
            states.add("move");

            buildings.poll();
            targetPositions.add(resourceLocation());
            
            constructionSound.play();
        }
    }

    public void prepBuild(Buildings building, int x, int y) {
        states.add("building");
        states.add("move");
        buildings.add(building);
        targetPositions.add(new int[]{x, y});
    }

    private void move() {
        if (targetPositions.isEmpty()) {
            return;
        }

        int[] targetPosition = targetPositions.peek();

        moveTowards(targetPosition[0], targetPosition[1]);

        double dist = Math.hypot(
                getX() - targetPosition[0],
                getY() - targetPosition[1]
        );

        if (dist < 55) {
            timer = 120;

            String state = states.peek();

            if (state.equals("building")) {
                timer = 1200;
            }

            states.remove();
            targetPositions.remove();
        }
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

        if (emptyImg != null) emptyImg.scale(35, 35);
        if (miningImg != null) miningImg.scale(35, 35);
    }

    private int[] resourceLocation() {
        if (getWorld() == null) return new int[]{-1, -1};

        if (assignedResource == null || assignedResource.getWorld() == null) {
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

        if (targetResource == null || targetResource.getWorld() == null) {
            return new int[]{-1, -1};
        }

        return new int[]{targetResource.getX(), targetResource.getY()};
    }

    private int[] goToBase() {
        if (homeBase == null || homeBase.getWorld() == null) {
            return new int[]{-1, -1};
        }

        return new int[]{homeBase.getX(), homeBase.getY()};
    }

    private void deposit() {
        if (timer <= 0) {
            team.addMoney(carryAmount);
            carryAmount = 0;

            states.remove();
            states.add("mining");
            states.add("move");
            targetPositions.add(resourceLocation());
            
            depositSound.play();
        }
    }

    public int statesLeft() {
        return states.size();
    }
    
    public int getPendingBarracks() {
        int count = 0;
    
        for (Buildings building : buildings) {
            if (building instanceof Barrack) {
                count++;
            }
        }
    
        return count;
    }

    public int getPendingTurrets() {
        int count = 0;
    
        for (Buildings building : buildings) {
            if (building instanceof Turret) {
                count++;
            }
        }
    
        return count;
    }
    
    private void updateImage() {
        if (carryAmount > 0) {
            setImage(miningImg);
        } else {
            setImage(emptyImg);
        }
    }

    public static int getMaxWorkerCoolDown() {
        return maxWorkerCoolDown;
    }

    public static void modifyMaxWorkerCoolDown(int workerCoolDown) {
        maxWorkerCoolDown = workerCoolDown;
    }
}