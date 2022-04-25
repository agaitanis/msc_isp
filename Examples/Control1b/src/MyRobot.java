import  simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.media.j3d.*;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

public class MyRobot extends Agent {

    final double ZERO =0.01;
    final double K1 =5;
    final double K2 =10;
    Vector3d goal;

    public MyRobot (Vector3d position, String name, Vector3d goal) 
    {
        super(position,name);        
        this.goal = goal;        
    }
    public void initBehavior() {
    }
    public void performBehavior() 
    {
        Vector3d lg;
        lg = getLocalCoords(goal);
        double dist = lg.length();
        double ph = Math.atan2(lg.z,lg.x);
        if (dist<ZERO)
        {
            this.setRotationalVelocity(0);
            this.setTranslationalVelocity(0);            
        }
        else
        {           
            this.setRotationalVelocity(K1*Math.atan(Math.tan(ph)));
            this.setTranslationalVelocity(K2*Math.log10(dist+1)*Math.signum(lg.x));
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
