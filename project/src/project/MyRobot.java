/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;
import  simbad.sim.*;
import javax.vecmath.Vector3d;

/**
 *
 * @author alexa
 */
public class MyRobot extends Agent {
    LightSensor lightLeft;
    LightSensor lightRight;
    
    public MyRobot (Vector3d position, String name) {     
        super(position, name);
        lightLeft = RobotFactory.addLightSensorLeft(this);
        lightRight = RobotFactory.addLightSensorRight(this);
    }
    
    @Override
    public void initBehavior() {
        setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        setTranslationalVelocity(2);
    }
    
    @Override
    public void performBehavior() {
    }
}
