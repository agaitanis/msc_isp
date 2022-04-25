
import Subsumption.Behavior;
import Subsumption.Sensors;
import Subsumption.Velocities;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
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
public class CircumNavigate extends Behavior{
    Point3d goal;
    Agent rob;
    boolean CLOCKWISE;
    static double K1 = 5;
    static double K2 = 0.8;
    static double K3 = 1;  
    static double SAFETY =0.8;
    
    public CircumNavigate(Sensors sensors,Agent rob, Point3d goal, boolean CLOCKWISE) {
       super(sensors);
       this.rob = rob;
       this.goal=goal;
       this.CLOCKWISE=CLOCKWISE;
    }
    public Velocities act() {
        RangeSensorBelt sonars = getSensors().getSonars();
        
        int min;
        min=0;
        for (int i=1;i<sonars.getNumSensors();i++)
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min))
                min=i;
        Point3d p = Tools.getSensedPoint(rob,sonars,min);
        double d = p.distance(new Point3d(0,0,0));  
        Vector3d v;
        v = CLOCKWISE? new Vector3d(-p.z,0,p.x): new Vector3d(p.z,0,-p.x);
        double phLin = Math.atan2(v.z,v.x);
        double phRot =Math.atan(K3*(d-SAFETY));
        if (CLOCKWISE)
            phRot=-phRot;
        double phRef = Tools.wrapToPi(phLin+phRot); 
                
        return new Velocities(K2*Math.cos(phRef),K1*phRef);
    }
    public boolean isActive() {
        RangeSensorBelt sonars = getSensors().getSonars();
        return sonars.oneHasHit();            
    }     
    
}
