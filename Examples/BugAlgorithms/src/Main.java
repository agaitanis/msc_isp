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
        spiral(environment);
        //spheres(environment);
        //bottle(environment);
        //box(environment);
        Simbad frame = new Simbad(environment, false);
    }
    static void spheres(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,3), new Vector3f(5,1,5),environment));
        environment.add(new Box(new Vector3d(2.75,0,3), new Vector3f(0.5f,1,4),environment));
        environment.add(new Box(new Vector3d(3.25,0,3), new Vector3f(0.5f,1,3),environment));
        environment.add(new Box(new Vector3d(-2.75,0,3), new Vector3f(0.5f,1,4),environment));
        environment.add(new Box(new Vector3d(-3.25,0,3), new Vector3f(0.5f,1,3),environment));

        environment.add(new Box(new Vector3d(0,0,-4), new Vector3f(2,1,2),environment));

        Point3d goal = new Point3d(0,0,-9);
        environment.add(new MyRobot(new Vector3d(0,0,8),"my robot",goal));
        environment.add(new CherryAgent(new Vector3d(goal),"goal",0.2f));
    }      
    static void box(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,0), new Vector3f(5,1,5),environment));

        Point3d goal = new Point3d(0,0,-9);
        environment.add(new MyRobot(new Vector3d(0,0,8),"my robot",goal));
        environment.add(new CherryAgent(new Vector3d(goal),"goal",0.2f));
    }      
    static void bottle(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,3), new Vector3f(5,1,5),environment));
        environment.add(new Box(new Vector3d(0,0,-0.5), new Vector3f(2,1,2),environment));

        Point3d goal = new Point3d(0,0,-9);
        environment.add(new MyRobot(new Vector3d(0,0,8),"my robot",goal));
        environment.add(new CherryAgent(new Vector3d(goal),"goal",0.2f));
    }      

    static void spiral(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,7), new Vector3f(14,1,1),environment));
        environment.add(new Box(new Vector3d(-2,0,3), new Vector3f(12,1,1),environment));
        environment.add(new Box(new Vector3d(0,0,-9), new Vector3f(16,1,1),environment));
        environment.add(new Box(new Vector3d(-7.5,0,-2.5), new Vector3f(1,1,12),environment));
        environment.add(new Box(new Vector3d(7.5,0,-0.5), new Vector3f(1,1,16),environment));
        environment.add(new Box(new Vector3d(3.5,0,-1), new Vector3f(1,1,7),environment));
        environment.add(new Box(new Vector3d(0,0,-5), new Vector3f(7,1,1),environment));
        environment.add(new Box(new Vector3d(-3.5,0,-2.5), new Vector3f(1,1,4),environment));
        environment.add(new Box(new Vector3d(-1.5,0,-1), new Vector3f(3,1,1),environment));
        Point3d goal = new Point3d(-1,0,-3);
        environment.add(new MyRobot(new Vector3d(0,0,8),"my robot",goal));
        environment.add(new CherryAgent(new Vector3d(goal),"goal",0.2f));
    }

}
