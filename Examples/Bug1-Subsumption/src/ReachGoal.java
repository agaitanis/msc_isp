
import Subsumption.Behavior;
import Subsumption.Sensors;
import Subsumption.Velocities;
import javax.vecmath.Point3d;
import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

public class ReachGoal extends Behavior {
    Point3d goal;
    Agent rob;
    public ReachGoal(Sensors sensors,Agent rob, Point3d goal) {
       super(sensors);
       this.rob = rob;
       this.goal=goal;
    }
    public Velocities act() {
       return new Velocities(0.0, 0.0);
    }
    public boolean isActive() {
        Point3d p = new Point3d();
        rob.getCoords(p);
        if (p.distance(goal)<0.1)
            return true;
        return false;
    }
}