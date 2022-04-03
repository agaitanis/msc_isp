/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package second;

/**
 *
 * @author dvrakas
 */
import javax.vecmath.Point3d;
import  simbad.sim.*;
import javax.vecmath.Vector3d;

public class MyRobot extends Agent {
    LightSensor light;
    public MyRobot (Vector3d position, String name) {     
        super(position,name);
        light=RobotFactory.addLightSensor(this);
    }
    public void initBehavior() {
        setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        setTranslationalVelocity(2);
    }    
    public void performBehavior() {
        System.out.println("Average luminance is: "+light.getAverageLuminance());
    }
}
