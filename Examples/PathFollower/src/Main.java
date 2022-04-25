import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.Vector;
import javax.vecmath.*;
import simbad.gui.*;
import simbad.sim.*;

/**
 *
 * @author Dimitrs
 */
public class Main {
    
    private static State initial;

    public Main() {
    }

    public static void main(String[] args) throws IOException {
        EnvironmentDescription environment = new EnvironmentDescription();     

        
        Vector<Point3d> path = new Vector<Point3d>();
        
        
        
        path.add(new Point3d(-7,0,-3));
        path.add(new Point3d(-4,0,-3));
        path.add(new Point3d(-1,0,-2));
        path.add(new Point3d(-1,0,1));
        path.add(new Point3d(3,0,4));
        path.add(new Point3d(3.5,0,-2));
        path.add(new Point3d(3,0,-5));
        path.add(new Point3d(4,0,-8));
        
        
        
        for (int i=0;i<path.size();i++)
        {
            environment.add(new CherryAgent(new Vector3d(path.get(i)),"t",0.2f));
        }
        
        
        MyRobot r = new MyRobot(new Vector3d(-8,0,2),"my robot",path);                

        environment.add(r);
        
        Simbad frame = new Simbad(environment, false);
    }

}
