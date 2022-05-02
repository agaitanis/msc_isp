package project;

import javax.vecmath.*;
import simbad.sim.*;

public class Env extends EnvironmentDescription {
    public Env() {
        Point3d light = new Point3d(-5, 2, 3);
        light1SetPosition(light.x, light.y, light.z);
        light2SetPosition(light.x, light.y, light.z);
        
        add(new MyRobot(new Vector3d(-2, 0, 8), "my robot"));
        add(new CherryAgent(new Vector3d(light.x, 0, light.z), "goal", 0.1f));

        Wall wall = new Wall(new Vector3d(6, 0, 0), 12, 1, this);
        wall.rotate90(1);
        add(wall);
        
        add(new Wall(new Vector3d(-2, 0, 6), 16, 1, this));
        
        add(new Box(new Vector3d(2, 0, 0), new Vector3f(2, 1, 7), this));
        add(new Box(new Vector3d(-2, 0, -1), new Vector3f(2, 1, 10), this));
        
        add(new Arch(new Vector3d(-5, 0, 0), this));
              
        Line line = new Line(new Vector3d(-4, 0, 8), 12, this);
        line.rotate90(1);
        add(line);
        
        add(new Line(new Vector3d(8, 0, -8), 16, this));
        
        Line line2 = new Line(new Vector3d(-8, 0, -8), 16, this);
        line2.rotate90(1);
        add(line2);
        
        Line line3 = new Line(new Vector3d(6, 0, -1), 2, this);
        line3.rotate90(1);
        add(line3);
        
        add(new Line(new Vector3d(-5, 0, -8), 8, this));
        add(new Line(new Vector3d(2, 0, -8), 5, this));
        add(new Line(new Vector3d(-7, 0, -2), 2, this));
        
        Line line4 = new Line(new Vector3d(-7, 0, -2), 2, this);
        line4.rotate90(1);
        add(line4);
        
        Line line5 = new Line(new Vector3d(-5, 0, -4), 2, this);
        line5.rotate90(1);
        add(line5);
    }
}
