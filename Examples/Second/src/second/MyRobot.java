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
    Point3d start=new Point3d();
    boolean collided;
    double collidedOdometer;
    public MyRobot (Vector3d position, String name) {     
        super(position,name);
        getCoords(start);
    }
    public void initBehavior() {
        collided = false;
        collidedOdometer=0;
    }    
    public void performBehavior() {
        if (collided){
            if (getOdometer()-collidedOdometer>=2)
                collided=false;
        }
        else{        
            setTranslationalVelocity(2);
            if (collisionDetected()) {
                setTranslationalVelocity(-2);
                setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
                collided=true;
                collidedOdometer=getOdometer();
            } else {
                Point3d p=new Point3d();
                getCoords(p);            
                if (p.distance(start)>15)
                    this.moveToStartPosition();            
                if ((getCounter() % 10)==0) 
                   setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
            }
        }            
    }
}
