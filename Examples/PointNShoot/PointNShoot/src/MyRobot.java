import  simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.media.j3d.*;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

public class MyRobot extends Agent {
    Point3d goal;
    double t;
    boolean rotate;
    double dist;
    static double ROTATIONAL = 0.5;
    public MyRobot (Vector3d position, String name, Point3d goal) 
    {
        super(position,name);
        this.goal = goal;
    }
    public void initBehavior()     {
        Point3d lg;
        lg = getLocalCoords(goal);
        double ph = Math.atan2(lg.z, lg.x);
        double rv=ROTATIONAL*Math.signum(ph);        
        t= ph/rv;
        rotate=true;
        Vector3d v = new Vector3d();
        dist = lg.distance(new Point3d(0,0,0));
        this.setRotationalVelocity(rv);                
    }
    public Point3d getLocalCoords(Point3d p)
    {
        Point3d a = new Point3d();
        Point3d r = new Point3d();
        double th = getAngle();        
        double x,z;
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
    public void performBehavior() 
    {
        double lt=this.getLifeTime();
        if (lt>=t)
        {
            if (rotate)
            {
                this.setRotationalVelocity(0);
                rotate=false;
                double tv = 1.5;
                t = dist/tv +lt;
                this.setTranslationalVelocity(tv);
            }
            else
            {
                this.setTranslationalVelocity(0);            
            }
        }
    }
}
