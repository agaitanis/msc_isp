
import Subsumption.Behavior;
import Subsumption.Sensors;
import Subsumption.Velocities;
import javax.vecmath.Point3d;
import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dvrakas
 */
public class MoveToGoal extends Behavior {
    Point3d goal;
    Agent rob;
    public MoveToGoal(Sensors sensors,Agent rob, Point3d goal) {
       super(sensors);
       this.rob = rob;
       this.goal=goal;
    }
    public Velocities act() {
        Point3d lg  = Tools.getLocalCoords(rob,goal);                       
        double dist = lg.distance(new Point3d (0,0,0));
        double phRef = Math.atan2(lg.z,lg.x);            
        
        return new Velocities(Tools.min(0.5,2*dist*Math.abs(Math.cos(phRef))), 3*phRef);
    }
    public boolean isActive() {
            return true;
    }    
}
