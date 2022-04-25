import java.util.Vector;
import  simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.media.j3d.*;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

public class MyRobot extends Agent {
   
    static double ZERO =0.01;
    static double K1 =0.3;
    static double K2 =0.4;
    static double K3 =5;
    
    Vector<Point3d> path;
    int index;
    
    
    
    public MyRobot (Vector3d position, String name, Vector<Point3d> path) 
    {
        super(position,name);        
        this.path = path;        
        index =0;
    }
    public void initBehavior() 
    {
    }
    public void performBehavior() {
        double u,dN,phLin,phRot,phRef;            
        Point3d p1,p2;
        Vector3d v,r,vN;            
        
        if (index<path.size()-1)        {
            p1 = getLocalCoords(path.get(index));
            p2 = getLocalCoords(path.get(index+1));            
            v = new Vector3d(p2.x-p1.x,0,p2.z-p1.z);
            r = new Vector3d(-p1.x,0,-p1.z);            
            u = v.dot(r)/v.dot(v);            
            if (u>1)            {
                if (++index < (path.size()-1))                {
                    p1 = getLocalCoords(path.get(index));
                    p2 = getLocalCoords(path.get(index+1));            
                    r = new Vector3d(-p1.x,0,-p1.z);                    
                }
                else             {
                    this.setRotationalVelocity(0);
                    this.setTranslationalVelocity(0);
                    return;
                }
            }            
            vN = new Vector3d(p2.z-p1.z,0,p1.x-p2.x);            
            dN = vN.dot(r)/vN.dot(vN);
            phLin = Math.atan2(p2.z-p1.z,p2.x-p1.x);
            phRot = Math.atan(K3*dN);            
            phRef = wrapToPi(phLin+phRot);            
 //           this.followLine(p1, p2);
            this.setRotationalVelocity(K1*phRef);
            this.setTranslationalVelocity(K2*Math.cos(phRef));            
        }
    }
    public void followLine(Point3d a, Point3d b){
        
        Point3d p1 = a;//this.getLocalCoords(a);
        Point3d p2 = b;//this.getLocalCoords(b);
        
        
 //       System.out.println("p1 is "+p1);
 //       System.out.println("p2 is "+p2);
        
        Vector3d v = new Vector3d(p2.x-p1.x,0,p2.z-p1.z);
        Vector3d r = new Vector3d(-p1.x,0,-p1.z);            
        Vector3d vN = new Vector3d(p2.z-p1.z,0,p1.x-p2.x); 
        
        double  dN = vN.dot(r)/vN.dot(vN);
        double d = vN.dot(r)/Math.sqrt(vN.dot(vN));

        double phLin = Math.atan2(p2.z-p1.z,p2.x-p1.x);
        double phRot = Math.atan(K3*dN);            
        double phRef = wrapToPi(phLin+phRot);           
       
        setRotationalVelocity(3*phRef);
        setTranslationalVelocity(0.4*Math.cos(phRef)); 
    }

    public double wrapToPi(double a)
    {
        if (a>Math.PI)
            return a-Math.PI*2;
        if (a<=-Math.PI)
            return a+Math.PI*2;
        return a;
    }
    
    public double getAngletoPoint(Vector3d p)
    {
        double f;
        if ((p.x==0) && (p.z==0))
            f=0;
        else
        {
            if (p.x==0)
            {
               if (p.z>0) 
                   f = Math.PI/2;
               else
                   f = -Math.PI/2;
            }
            else
            {
                f = Math.atan(p.z/p.x);
                if (p.x<0)
                    f+=p.z>0?Math.PI:-Math.PI;
            }
        }
        return f;    
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
    
}
