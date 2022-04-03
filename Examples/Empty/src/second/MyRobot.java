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
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import  simbad.sim.*;
import javax.vecmath.Vector3d;

public class MyRobot extends Agent {
    LineSensor line;
    CameraSensor camera;    
    DifferentialKinematic d;
    LampActuator l;
    
    public MyRobot (Vector3d position, String name) {     
        super(position,name);
        d=RobotFactory.setDifferentialDriveKinematicModel(this);
        d.setLeftVelocity(1);
        d.setRightVelocity(1.5);
        LampActuator l=RobotFactory.addLamp(this);
        l.setOn(true);
        l.setBlink(true);
    }
    public void initBehavior() {
    }    
    public void performBehavior() {
    }
}
