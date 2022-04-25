/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

/**
 *
 * @author dvrakas
 */
public class Tools {
    public static Point3d getSensedPoint(Agent rob,RangeSensorBelt sonars,int sonar){
        
        double v;
        if (sonars.hasHit(sonar))
            v=rob.getRadius()+sonars.getMeasurement(sonar);
        else
            v=rob.getRadius()+sonars.getMaxRange();
        double x = v*Math.cos(sonars.getSensorAngle(sonar));
        double z = v*Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x,0,z);
    }
    public static Point3d getGlobalSensedPoint(Agent rob,RangeSensorBelt sonars,int sonar){
        Point3d r=new Point3d();
        rob.getCoords(r);
        double v;
        if (sonars.hasHit(sonar))
            v=rob.getRadius()+sonars.getMeasurement(sonar);
        else
            v=rob.getRadius()+sonars.getMaxRange();
        
        double th = getAngle(rob);
        
        
        double a = /*wrapToPi*/(th+sonars.getSensorAngle(sonar));
       // System.out.println("v = "+v + " radius = "+rob.getRadius());
        double x = v*Math.cos(a);
        double z = v*Math.sin(a);
        return new Point3d(r.x+x,0,r.z-z);

    }
    public static Point3d getGlobalCoords(Agent rob,Point3d p){
        Point3d a = new Point3d();
        Point3d r = new Point3d();
        
        double th = getAngle(rob);     
        double x,y,z;
        rob.getCoords(r);

        x=p.x;
        z=p.z;
        
        a.setX(-x*Math.cos(th) - z*Math.sin(th));
        a.setZ(-z*Math.cos(th) + x*Math.sin(th));
        a.setY(p.y);

        a.x =r.x-a.x;
        a.z=r.z-a.z;
    //    x=p.getX() + r.x;
      //  z=-p.getZ()- r.z;        

        return a;
    }

    public static Point3d getLocalCoords(Agent rob,Point3d p){
        Point3d a = new Point3d();
        Point3d r = new Point3d();
        double th = getAngle(rob);        
        double x,y,z;
        rob.getCoords(r);
        x=p.getX() - r.x;
        z=-p.getZ()+ r.z;        
        a.setX(x*Math.cos(th) + z*Math.sin(th));
        a.setZ(z*Math.cos(th) - x*Math.sin(th));
        a.setY(p.y);
        return a;
    }  
    public static double getAngle(Agent rob){
        double angle=0;
        double msin; 
        double mcos;              
        Transform3D m_Transform3D=new Transform3D();
        rob.getRotationTransform(m_Transform3D);        
        Matrix3d m1 = new Matrix3d();
        m_Transform3D.get( m1 );                
        msin=m1.getElement( 2, 0 );
        mcos=m1.getElement( 0, 0 );        
        if (msin<0)
        {
            angle = Math.acos(mcos);
        }
        else            
        {
            if (mcos<0)
            {
                angle = 2*Math.PI-Math.acos(mcos);
            }
            else
            {            
                angle = -Math.asin(msin);
            }
        }
        while (angle<0)
            angle+=Math.PI*2;
        return angle;
    }    
    public static double min (double a, double b){
        return a<b?a:b;
    }
    public static double max (double a, double b){
        return a>b?a:b;
    }
    public static double wrapToPi(double a){
        if (a>Math.PI)
            return a-Math.PI*2;
        if (a<=-Math.PI)
            return a+Math.PI*2;
        return a;
    }
    public static double getGlobalAngleToGoal(Agent rob, Point3d goal){
        Point3d r = new Point3d();
        rob.getCoords(r);
        return Math.atan2(goal.z-r.z,goal.x-r.x);
    }
    
}
