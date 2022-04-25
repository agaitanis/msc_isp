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
    LightSensor center;
    
    public MyRobot (Vector3d position, String name) 
    {
        super(position,name);        
        center = RobotFactory.addLightSensor(this);     
    }
    public void initBehavior() {
    }
    public void performBehavior(){
        double stimulus = center.getLux();
        this.setTranslationalVelocity(10*stimulus);
    }    
}
