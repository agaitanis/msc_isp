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
    Point3d goal;
    RangeSensorBelt sonars;    
    Bug0 algo0;
    Bug1 algo1;
    Bug2 algo2;

    public MyRobot (Vector3d position, String name, Point3d goal) 
    {
        super(position,name);
        this.goal=goal;   
        sonars = RobotFactory.addSonarBeltSensor(this, 12);        
        algo0 = new Bug0(this,sonars,goal,false);
        algo1 = new Bug1(this,sonars,goal,false);
        algo2 = new Bug2(this,sonars,goal,false);

    }
    public void initBehavior() {
        
    }
    
    public void performBehavior(){
        algo2.step();
    }
}
