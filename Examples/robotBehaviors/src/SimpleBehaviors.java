/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

/**
 *
 * @author dvrakas
 */
public class SimpleBehaviors {
    static double K1 = 5;
    static double K2 = 0.8;
    static double K3 = 1;  
    static double MT =2;
    static double SAFETY =0.8;
    
    public static void stop(Agent rob) {
        rob.setTranslationalVelocity(0);
        rob.setRotationalVelocity(0);
    }
    public static void moveToGoal(Agent rob, Point3d goal){
        Point3d lg  = Tools.getLocalCoords(rob,goal);                       
        double dist = Math.sqrt(lg.x*lg.x + lg.z*lg.z);            
        double phRef = Math.atan2(lg.z,lg.x);            
        rob.setTranslationalVelocity (Tools.min(MT,dist/MT*Math.abs(Math.cos(phRef))));
        rob.setRotationalVelocity(3*phRef);   
    }
    public static void circumNavigate(Agent rob,RangeSensorBelt sonars,boolean CLOCKWISE){
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
        
        rob.setRotationalVelocity(K1*phRef);
        rob.setTranslationalVelocity(K2*Math.cos(phRef));       
    }



}
