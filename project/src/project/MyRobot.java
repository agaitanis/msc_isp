/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;
import simbad.sim.*;
import javax.vecmath.*;

/**
 *
 * @author alexa
 */
public class MyRobot extends Agent {
    LightSensor lightLeft;
    LightSensor lightRight;
    Point3d goal;
    
    public MyRobot (Vector3d position, String name, Point3d goalPos) {     
        super(position, name);
        lightLeft = RobotFactory.addLightSensorLeft(this);
        lightRight = RobotFactory.addLightSensorRight(this);
        goal = goalPos;
    }
    
    @Override
    public void initBehavior() {
        setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        setTranslationalVelocity(2);
    }
    
    @Override
    public void performBehavior() {
        Point3d robotPos = new Point3d();
        
        this.getCoords(robotPos);
        
        System.out.println("dist = " + robotPos.distance(goal) +
                ", lux = (" + lightLeft.getLux() +
                ", " + lightRight.getLux() + ")");
    }
}
