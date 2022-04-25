import java.awt.Color;
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
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

public class MyRobot extends Agent {
    LightSensor left,right;
    DifferentialKinematic diff;
    
    public MyRobot (Vector3d position, String name) 
    {
        super(position,name);   
        diff = RobotFactory.setDifferentialDriveKinematicModel(this);
        left = RobotFactory.addLightSensorLeft(this);
        right = RobotFactory.addLightSensorRight(this);        
    }
    public void initBehavior() {
    }
    public void performBehavior(){
        double l = Math.pow(left.getLux(),0.1);
        double r = Math.pow(right.getLux(),0.1);
        diff.setLeftVelocity(3*(1-l));
        diff.setRightVelocity(3*(1-r));        
    }    
}
