public class Worker extends People{
    public Worker(){
        super();
    }

    public Worker(Team team) {
        super(team);
        if (team != null) {
            team.addUnit(this);
        }
    }

    public void act(){

    }
}
