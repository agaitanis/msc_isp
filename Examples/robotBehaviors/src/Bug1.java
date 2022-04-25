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
public class Bug1 {
    public enum robotState {
        MoveToGoal, CircumNavigate, MoveToClosestPoint
    }    
    robotState state;    
    static double ZERO = 1;
    static double SAFETY =0.8;    
    Agent rob;
    RangeSensorBelt sonars;
    Point3d goal;
    boolean CLOCKWISE;
    Point3d startingPoint;
    Point3d closestPoint;
    boolean circleCompleted=false;
    
    public Bug1(Agent rob,RangeSensorBelt sonars,Point3d goal,boolean CLOCKWISE){
        this.rob=rob;
        this.goal=goal;
        this.sonars=sonars;
        this.CLOCKWISE=CLOCKWISE;
        state = robotState.MoveToGoal;
    }
    public void step() {
        double minDist=Double.POSITIVE_INFINITY,f2g;
        Point3d lg  = Tools.getLocalCoords(rob,goal);                               
        f2g = Math.atan2(lg.z, lg.x);
        for (int i=0;i<sonars.getNumSensors();i++) // Find closest obstacle
        {
            double ph = Tools.wrapToPi(sonars.getSensorAngle(i));            
            if (Math.abs(ph-f2g)<=Math.PI/2)
                minDist=Tools.min(minDist,sonars.getMeasurement(i));
        }
        if (state==robotState.MoveToGoal)
        {
            if (lg.distance(new Point3d(0,0,0))<ZERO)
                SimpleBehaviors.stop(rob);
//                SimpleBehaviors.stop(rob);
            if (minDist>SAFETY) // MOVE TO GOAL
                SimpleBehaviors.moveToGoal(rob,goal);
            else //obstacle encountered
            {
                state=robotState.CircumNavigate;
                startingPoint = null;
                closestPoint=null;
                circleCompleted=false;
            }
        }
        if (state==robotState.CircumNavigate)
        {
            if (startingPoint==null)
            {
                startingPoint=new Point3d();
                rob.getCoords(startingPoint);
                closestPoint=startingPoint;
                SimpleBehaviors.circumNavigate(rob,sonars,CLOCKWISE);
            }
            else
            {
                Point3d currentPoint = new Point3d();
                rob.getCoords(currentPoint);
                if (currentPoint.distance(goal)<closestPoint.distance(goal))
                    closestPoint=currentPoint;
                if (currentPoint.distance(startingPoint)>ZERO && !circleCompleted)
                    circleCompleted=true;
                if (currentPoint.distance(startingPoint)<ZERO && circleCompleted)
                    state=robotState.MoveToClosestPoint;
                else                
                    SimpleBehaviors.circumNavigate(rob,sonars,CLOCKWISE);                
            }
        }
        if (state==robotState.MoveToClosestPoint)
        {
                Point3d currentPoint = new Point3d();
                rob.getCoords(currentPoint);
                if (currentPoint.distance(closestPoint)<ZERO)
                {
                    if (minDist>SAFETY)
                        state = robotState.MoveToGoal;
                    else
                        SimpleBehaviors.stop(rob); //Problem Unsolvable
                }
                else
                    SimpleBehaviors.circumNavigate(rob,sonars,CLOCKWISE);                
        }
    }        
}
