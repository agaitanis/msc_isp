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
    RangeSensorBelt sonars;
    public MyRobot (Vector3d position, String name) {     
        super(position,name);
        sonars=RobotFactory.addSonarBeltSensor(this, 12);
    }
    public void initBehavior() {
        setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        setTranslationalVelocity(6*(0.5 - Math.random()));
    }    
    public void performBehavior() {
        
        for (int i=0;i<sonars.getNumSensors();i++)
            if (sonars.hasHit(i))
                System.out.println("Sonar "+i+" measures "+ sonars.getMeasurement(i));
            else
                System.out.println("Sonar's "+i+" reports nothing for "+sonars.getMaxRange()+" meters");
        if (sonars.getFrontQuadrantMeasurement()<1)
            setTranslationalVelocity(-1);
        else
            if (sonars.getBackQuadrantMeasurement()<1)
                setTranslationalVelocity(1);            
    }
}
