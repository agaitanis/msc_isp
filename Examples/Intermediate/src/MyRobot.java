import  simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.media.j3d.*;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

public class MyRobot extends Agent {

    static double ZERO =0.01;
    static double K1 =5;
    static double K2 =0.8;
    static double r = 5;
    Vector3d intermediate;
    Goal goal;
    boolean phase1;
    
    public MyRobot (Vector3d position, String name, Goal goal) 
    {
        super(position,name);        
        this.goal = goal;        
    }
    public void initBehavior() 
    {
        intermediate = new Vector3d(goal.getX()-r*Math.cos(goal.getPose()),0, goal.getZ()-r*Math.sin(goal.getPose()));        
        phase1=true;
    }
    public void performBehavior() 
    {
        Vector3d lg;         
        if (phase1)
            lg = getLocalCoords(intermediate);
        else
            lg = getLocalCoords(goal.getPoint());        
        double dist = lg.length();
        double ph = Math.atan2(lg.z,lg.x);
        if (dist<ZERO)
        {
            if (phase1)
                phase1=false;
            else
            {
                this.setRotationalVelocity(0);
                this.setTranslationalVelocity(0);            
            }
        }
        else
        {           
            this.setRotationalVelocity(K1*ph);
            if (!phase1)
                this.setTranslationalVelocity(K2*dist);
            else
                this.setTranslationalVelocity(K2);
        }
    }

    public Vector3d getLocalCoords(Vector3d p)
    {
        Vector3d a = new Vector3d();
        Point3d r = new Point3d();
        double th = getAngle();        
        double x,y,z;
        getCoords(r);
        x=p.getX() - r.x;
        z=-p.getZ()+ r.z;        
        a.setX(x*Math.cos(th) + z*Math.sin(th));
        a.setZ(z*Math.cos(th) - x*Math.sin(th));
        a.setY(p.y);
        return a;
    }    
    public double getAngle()
    {
        double angle=0;
        double msin; 
        double mcos;              
        Transform3D m_Transform3D=new Transform3D();
        this.getRotationTransform(m_Transform3D);        
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
    
}
