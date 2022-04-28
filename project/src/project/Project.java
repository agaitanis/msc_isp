package project;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.gui.Simbad;

public class Project {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Point3d light = new Point3d(-7, 2, -7);
        Point3d goal = new Point3d(light.x, 0, light.z);
    
        Simbad frame = new Simbad(new Env(light, goal), false);
    }
    
}
