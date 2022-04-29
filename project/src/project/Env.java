package project;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.*;

public class Env extends EnvironmentDescription {
    public Env(Point3d light, Point3d goal) {
        add(new MyRobot(new Vector3d(0, 0, 0), "my robot"));
        add(new CherryAgent(new Vector3d(goal), "goal", 0.1f));
        light1SetPosition(light.x, light.y, light.z);
        light2SetPosition(light.x, light.y, light.z);
        
        Wall wall1 = new Wall(new Vector3d(-3, 0, 0), 10, 1, this);
        wall1.rotate90(1);
        add(wall1);
    }
}
