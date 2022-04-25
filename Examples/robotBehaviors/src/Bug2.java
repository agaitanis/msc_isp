/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.vecmath.Point3d;
import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

/**
 *
 * @author dvrakas
 */
public class Bug2 {
    public enum robotState {
        MoveToGoal, CircumNavigate
    }    
    private robotState state;
    Agent rob;
    RangeSensorBelt sonars;
    Point3d goal;
    boolean CLOCKWISE;
    static Point3d sp;
    static boolean circle=false;
    static double AngleToGoal;
    static double ZERO =1;
    static double SAFETY=0.7;

    public Bug2(Agent rob,RangeSensorBelt sonars,Point3d goal,boolean CLOCKWISE){
        this.rob=rob;
        this.goal=goal;
        this.sonars=sonars;
        this.CLOCKWISE=CLOCKWISE;
        state=robotState.MoveToGoal;
        AngleToGoal=Tools.getGlobalAngleToGoal(rob,goal);        
    }
    
    public void step()     {
        Point3d lg= Tools.getLocalCoords(rob,goal),cp=new Point3d();
        double f2g= Math.atan2(lg.z, lg.x),minDist=2*SAFETY;
        rob.getCoords(cp);        
        if (state==robotState.MoveToGoal)        {
            for (int i=0;i<sonars.getNumSensors();i++)       {
                double ph = Tools.wrapToPi(sonars.getSensorAngle(i));            
                if (Math.abs(ph-f2g)<=Math.PI/2)
                    minDist=Tools.min(minDist,sonars.getMeasurement(i));
            }
            if (minDist>SAFETY) 
                SimpleBehaviors.moveToGoal(rob,goal);
            else {
                state=robotState.CircumNavigate;
                sp = null;
                circle=false;
            }
        }
        if (state==robotState.CircumNavigate)        {
            if (sp==null)            {
                sp=new Point3d();
                rob.getCoords(sp);
                SimpleBehaviors.circumNavigate(rob,sonars,CLOCKWISE);
            }
            else            {
                if (cp.distance(sp)>ZERO && !circle)
                    circle=true;                         
                if (cp.distance(sp)<=ZERO && circle)
                    SimpleBehaviors.stop(rob);
                else{
                    double f= Tools.getGlobalAngleToGoal(rob,goal);
                    if (Math.abs(f-AngleToGoal)<0.05 && sp.distance(goal)>cp.distance(goal))  
                        state = robotState.MoveToGoal;
                    else
                        SimpleBehaviors.circumNavigate(rob,sonars,CLOCKWISE);
                }
            }
        }
    }        

}
