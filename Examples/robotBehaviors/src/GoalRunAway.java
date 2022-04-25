
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
public class GoalRunAway {
    Agent rob;
    RangeSensorBelt sonars;
    Point3d goal;
    double  MAX_TRANSLATIONAL = 2;
    double  MAX_ROTATIONAL = 4;
    double ZERO =0.01;
    public double GOALFACTOR=0.43;
    double maxSensor;
    
    public GoalRunAway(Agent rob,RangeSensorBelt sonars,Point3d goal){
        this.rob=rob;
        this.goal=goal;
        this.sonars=sonars;
        double d,r,y;
        maxSensor=0;
        for (int i=0;i<=sonars.getNumSensors()/2;i++)
        {            
            d = sonars.getMaxRange();
            r = sonars.getSensorAngle(i);                        
            maxSensor+= d*Math.sin(r);            
        }               
        
    }
    public void step(){
        double sx=0,sz=0,x=0,z=0;
        double d,r;
        Double min=Double.POSITIVE_INFINITY;
        for (int i=0;i<sonars.getNumSensors();i++)
        {            
            if (sonars.hasHit(i)){
                d =sonars.getMeasurement(i);
                if (d<min)
                    min=d;
            }
            else
                d = sonars.getMaxRange();
            r = sonars.getSensorAngle(i);                        
            sx+= d*Math.cos(r);
            sz+= d*Math.sin(r);
        }                       
        sx /=maxSensor;
        sz /=maxSensor;                


        double safe=1;
        
        if (min<=1)
            safe=0.1;
        if (min>=3)
            safe=4;
        
        
        Point3d lg;
        lg = Tools.getLocalCoords(rob,goal);        
        double dist = lg.distance(new Point3d(0,0,0));
    //    System.out.println("dist is "+dist);
        if ( dist>ZERO)
        {        
            lg.x/=5;
            lg.z/=5; 
            x = safe*GOALFACTOR*lg.x + (1-GOALFACTOR*safe)*sx;
            z = safe*GOALFACTOR*lg.z + (1-GOALFACTOR*safe)*sz;
            ActivateActuators(rob,x,z);
        }  
        else
            SimpleBehaviors.stop(rob);
    }    
    public void ActivateActuators(Agent rob,double x, double z)
    {
        if (x<0)
        {
            z *=2;
            if (Math.abs(z)<ZERO)                
                z=1;
            x*=-1;
        }
        rob.setRotationalVelocity(z*MAX_ROTATIONAL);
        rob.setTranslationalVelocity(Tools.max(0.5,x*MAX_TRANSLATIONAL));        
    }    
}
