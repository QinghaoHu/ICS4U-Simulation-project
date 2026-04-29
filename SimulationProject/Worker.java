import greenfoot.*;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class Worker extends People {
    protected int carryAmount = 0;
    protected int maxCarry = 17;
    protected int minRate = 5;

    private GreenfootImage emptyImg;
    private GreenfootImage miningImg;

    private Resources targetResource;
    private Resources assignedResource;
    private Base homeBase;

    private Queue<String> states = new LinkedList<>(); // keeps track of what the bot will do
    private Queue<int[]> targetPositions = new LinkedList<>(); // keeps tracks of where the bot will go
    private Queue<Buildings> buildings = new LinkedList<>(); // keeps track of what buildings the bot will create

    private int timer = 0;

    private static int redIndex = 0;
    private static int blueIndex = 0;
    private static final int cost = 75;

    private static int maxWorkerCoolDown = 120;
    

    /*
     * The worker bot main functions are to create buildings and to collect resources
     * It is able to do this by keeping a todo list of what to do through the states variable
     * It alternatives between preforming a task a moving to a task to preform it
     */
    
    public Worker(Team team, Base base) {
        super(team, 450, 5);
        setStatBarEnabled(false);
        this.homeBase = base;

        if (team != null) {
            team.addUnit(this);
            team.addWorker(this);
        }

        states.add("move"); // Start by walking out to a node.
        states.add("mining");
        states.add("move");

        // initials states will be moving the resources and mining it then moving some where else
        
        setupImage();
    }

    public static int getCost() {
        return cost;
    }

    @Override
    protected void addedToWorld(World w) {
        // First stop: assigned resource node.
        targetPositions.add(resourceLocation());
    }

    public void act() {
        if (states.isEmpty()) {
            super.act();
            return;
        }

        timer--; // Shared timer for current task.
        
        String state = states.peek();
        // Simple state machine: move, mine, deposit, build.
        if (state.equals("move")) {
            stopMusic();
            move();
        } else if (state.equals("mining")) {
            mine();
        } else if (state.equals("depositing")) {
            deposit();
        } else if (state.equals("building")) {
            build(buildings.peek()); // grabs the building it will build
        }

        updateImage();
        super.act();
    }

    private void mine() {
        if (timer <= 0) {
            // Fill the carry slot, then head home.
            carryAmount = maxCarry;
            states.remove();
            states.add("depositing");
            states.add("move");
            targetPositions.add(goToBase());
        }
        if (timer % 50 == 0) {
            ResourceCache.playSound("mining.mp3", 15);
        }
    }

    public int available() {
        return states.size();
    }

    public void build(Buildings building) {
        // Drop building, then return to mining loop.
        
        if (timer <= 0) {
            if (getWorld() == null || building == null) {
                return;
            }

            getWorld().addObject(building, getX(), getY());
            team.correctBuildingList(building);

            // Tell the HUD the build landed.
            UI.reportUpgrade(team, building.getClass().getSimpleName(), 1);
            states.remove();
            states.add("mining");
            states.add("move");

            buildings.poll();
            targetPositions.add(resourceLocation());
            
            ResourceCache.playSound("building.mp3", 30);
        }
    }

    public void prepBuild(Buildings building, int x, int y) {
        // Queue build order for this worker.
        states.add("building");
        states.add("move");
        buildings.add(building);
        targetPositions.add(new int[]{x, y});
    }

    private void move() {
        // Walk to the front target in the queue.
        if (targetPositions.isEmpty()) {
            return;
        }

        int[] targetPosition = targetPositions.peek();

        moveTowards(targetPosition[0], targetPosition[1]);

        double dist = Math.hypot(
                getX() - targetPosition[0],
                getY() - targetPosition[1]
        );

        if (dist < 40) {
            timer = 60;

            String state = states.peek();
            // Building takes longer than mine/deposit.
            if (state.equals("building")) {
                timer = 1200;
            }

            states.remove();
            targetPositions.remove();
            
            // Move tasks hand off to the next state immediately.
            ResourceCache.playSound(states.peek() + ".mp3", getSoundVolume(states.peek()));
            System.out.println(states.peek());
        }
    }

    private void setupImage() {
        if (team == null) return;

        if (team.getTeamId() == Team.RED) {
            emptyImg = ResourceCache.getImage("RedWorkerRegular.png");
            miningImg = ResourceCache.getImage("RedWorkerMining.png");
        } else if (team.getTeamId() == Team.BLUE) {
            emptyImg = ResourceCache.getImage("BlueWorkerRegular.png");
            miningImg = ResourceCache.getImage("BlueWorkerMining.png");
        }

        if (emptyImg != null) emptyImg.scale(35, 35);
        if (miningImg != null) miningImg.scale(35, 35);
    }

    private int[] resourceLocation() {
        // Cache one resource node per worker when possible.
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
        // Fallback safe coord if base is gone.
        if (homeBase == null || homeBase.getWorld() == null) {
            return new int[]{-1, -1};
        }

        return new int[]{homeBase.getX(), homeBase.getY()};
    }

    private void deposit() {
        // Drop cargo, then head back out.
        if (timer <= 0) {
            team.addMoney(carryAmount);
            carryAmount = 0;

            states.remove();
            states.add("mining");
            states.add("move");
            targetPositions.add(resourceLocation());
            
            ResourceCache.playSound("depositing.mp3", 20);
        }
    }

    public int statesLeft() {
        // Handy for worker load balancing.
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

    private int getSoundVolume(String state) {
        if (state.equals("mining")) {
            return 15;
        }
        return 20;
    }

    public static int getMaxWorkerCoolDown(Base base) {
        if (base == null) {
            return maxWorkerCoolDown;
        }
    
        int level = base.getLevel();
        int reduced = maxWorkerCoolDown - ((level - 1) * 15);
    
        if (reduced < 30) {
            reduced = 30;
        }
    
        return reduced;
    }

    public static void modifyMaxWorkerCoolDown(int workerCoolDown) {
        maxWorkerCoolDown = workerCoolDown;
    }
}
