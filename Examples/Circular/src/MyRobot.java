import  simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.media.j3d.*;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

public class MyRobot extends Agent {
    DifferentialKinematic Kinematic;
    Point3d goal;
    double t;
    double dist;    
    static double VELOCITY= 1.5;
    
    public MyRobot (Vector3d position, String name, Point3d goal) 
    {
        super(position,name);
        this.goal = goal;
        Kinematic = RobotFactory.setDifferentialDriveKinematicModel(this);                 
    }
    public void initBehavior() 
    {       
       Point3d lg=getLocalCoords(goal); //Στόχος σε Ρομποκ. Συντετ.                
       double L = (lg.x*lg.x + lg.z*lg.z)/(2*lg.z); // απόσταση Ο-R
       double r=L - this.getRadius()/2; //r: απόσταση O με αριστερό τροχό
       double th,vl,vr; 
       
       vl = VELOCITY*Math.signum(lg.x);
       vr=vl*(r+this.getRadius())/r; //VR = VL*(r+b)/r       
       Kinematic.setLeftVelocity(vl);        
       Kinematic.setRightVelocity(vr); //Θέτουμε τις ταχύτητες VL και VR
       th = wrapToPi(2* Math.atan2(lg.z,lg.x));//θ=2φ        
       dist = r*th; //Η απόσταση που θα διανύσει ο αριστερός τροχός SL=rθ
       t=dist/vl;   //Ο χρόνος που θα διαρκέσει η κίνηση t=SL/VL
    }
    public Point3d getLocalCoords(Point3d p)
    {
        Point3d a = new Point3d();
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
    public void performBehavior() 
    {
        double lt=this.getLifeTime(); // Ο χρόνος που πέρασε
        if (lt>=t) // Αν έχει εκπνεύσει ο χρόνος
        {
            Kinematic.setLeftVelocity(0);
            Kinematic.setRightVelocity(0);
        }
    }
    public double wrapToPi(double a)
    {
        while (a>Math.PI)
            a -= Math.PI*2;
        while (a<=-Math.PI)
            a += Math.PI*2;
        return a;
    }
}
