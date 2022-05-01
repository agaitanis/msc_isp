package project;

import javax.vecmath.*;
import simbad.sim.*;

public class Env extends EnvironmentDescription {
    public Env() {
        Point3d light = new Point3d(7, 2, -5);
        light1SetPosition(light.x, light.y, light.z);
        light2SetPosition(light.x, light.y, light.z);
        add(new CherryAgent(new Vector3d(light.x, 0, light.z), "goal", 0.1f));
        
        add(new MyRobot(new Vector3d(2, 0, 8), "my robot"));
        
        Line line = new Line(new Vector3d(-6, 0, 8), 10, this);
        line.rotate90(1);
        add(line);
        
        add(new Line(new Vector3d(-6, 0, -9), 17, this));
        
        Line line2 = new Line(new Vector3d(-6, 0, -9), 13, this);
        line2.rotate90(1);
        add(line2);
        
        Wall wall = new Wall(new Vector3d(-4, 0, 1), 10, 1, this);
        wall.rotate90(1);
        add(wall);
        
        add(new Wall(new Vector3d(3, 0, 6), 14, 1, this));
        
        add(new Box(new Vector3d(5, 0, 0), new Vector3f(5, 1, 2), this));
        add(new Box(new Vector3d(0, 0, -5), new Vector3f(2, 1, 5), this));
        
        Arch arch = new Arch(new Vector3d(5, 0, -5), this);
        arch.rotate90(1);
        add(arch);
    }
}
