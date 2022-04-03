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
import javax.vecmath.Point3d;
import  simbad.sim.*;
import javax.vecmath.Vector3d;

public class MyRobot extends Agent {
    LineSensor line;
    CameraSensor camera;        
    public MyRobot (Vector3d position, String name) {     
        super(position,name);
        line=RobotFactory.addLineSensor(this,11);        
        camera = RobotFactory.addCameraSensor(this);
        camera.rotateZ(-Math.PI/4);
    }
    public void initBehavior() {
        setTranslationalVelocity(0.5);
    }    
    public void performBehavior() {
        int left=0, right=0;
        float k=0;
        for (int i=0;i<line.getNumSensors()/2;i++)
        {
            left+=line.hasHit(i)?1:0;
            right+=line.hasHit(line.getNumSensors()-i-1)?1:0;            
            k++;
        }
        if (left==0 && right==0)
        {
            setRotationalVelocity(0);
            setTranslationalVelocity(0);
        }
        else
            this.setRotationalVelocity((left-right)/k*5);
    }
}
