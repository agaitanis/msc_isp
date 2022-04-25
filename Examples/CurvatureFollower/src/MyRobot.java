import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;
import static javafx.application.Platform.exit;
import  simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

public class MyRobot extends Agent {
    static boolean CLOCKWISE=true;
    static double K1 = 5;
    static double K2 = 0.9;
    static double K3 = 3;
    
    final double SAFETY =0.8;
    RangeSensorBelt sonars;    
    CameraSensor camera;

    int counter;
    int lap;
    Point3d check[];
    

    
    public MyRobot (Vector3d position, String name) 
    {
        super(position,name);
        
        
        //sonars = RobotFactory.addSonarBeltSensor(this, 8);
        
        sonars = RobotFactory.addSonarBeltSensor(this, 24, 5f);
        
        counter =0;
        check = new Point3d[2];
        check[0] = new Point3d(0,0,0);
        check[1] = new Point3d(0,0,6);
        lap=0;        
    }
    public void initBehavior() {
        
    }
    public Point3d getSensedPoint(int sonar){        
        double v =radius+sonars.getMeasurement(sonar);
        double x = v*Math.cos(sonars.getSensorAngle(sonar));
        double z = v*Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x,0,z);
    }
    
    public void circumNavigate(){
        int min;
        min=0;
        for (int i=1;i<sonars.getNumSensors();i++)
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min))
                min=i;
        Point3d p = getSensedPoint(min);
        double d = p.distance(new Point3d(0,0,0));  
        Vector3d v;
        v = CLOCKWISE? new Vector3d(-p.z,0,p.x): new Vector3d(p.z,0,-p.x);
        double phLin = Math.atan2(v.z,v.x);
        double phRot =Math.atan(K3*(d-SAFETY));
        if (CLOCKWISE)
            phRot=-phRot;
        double phRef = wrapToPi(phLin+phRot); 
        
        setRotationalVelocity(K1*phRef);
        setTranslationalVelocity(K2*Math.cos(phRef));            
    }

    public void performBehavior()     {
        this.circumNavigate();
    }        
    public double wrapToPi(double a)
    {
        if (a>Math.PI)
            return a-Math.PI*2;
        if (a<=-Math.PI)
            return a+Math.PI*2;
        return a;
    }
}
