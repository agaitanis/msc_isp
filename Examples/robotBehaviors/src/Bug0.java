/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

/**
 *
 * @author dvrakas
 */
public class Bug0 {
    boolean CLOCKWISE;
    static double K1 = 5;
    static double K2 = 0.8;
    static double K3 = 1;    
    static double ZERO = 0.1;    
    static double SAFETY =0.75;
    Agent rob;
    RangeSensorBelt sonars;
    Point3d goal;
    
    
    public Bug0(Agent rob,RangeSensorBelt sonars,Point3d goal,boolean CLOCKWISE){
        this.rob=rob;
        this.goal=goal;
        this.sonars=sonars;
        this.CLOCKWISE=CLOCKWISE;
    }
    
    public void step()     {
        Point3d lg= Tools.getLocalCoords(rob,goal);
        double f2g=Math.atan2(lg.z,lg.x), minDist=Double.POSITIVE_INFINITY, phRef;
        double dist = lg.distance(new Point3d(0,0,0));        
        if (dist<ZERO)        
            SimpleBehaviors.stop(rob);
        else
        {
            // CHECK IF THE ROBOT CAN MOVE TO GOAL        
            for (int i=0;i<sonars.getNumSensors();i++)        {
                double ph = Tools.wrapToPi(sonars.getSensorAngle(i));            
                if (Math.abs(ph-f2g)<=Math.PI/2)
                    minDist=Tools.min(minDist,sonars.getMeasurement(i));
            }        
            if (minDist>1)    // MOVE TO GOAL        
                SimpleBehaviors.moveToGoal(rob, goal);            
            else    
                SimpleBehaviors.circumNavigate(rob, sonars, CLOCKWISE);
        }
    }        
    
}
