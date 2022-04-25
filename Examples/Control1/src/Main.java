import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
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
    
        Vector3d goal =   new Vector3d(-8,0,4.5);
        CherryAgent c = new CherryAgent(goal,"goal",0.2f);
        MyRobot r = new MyRobot(new Vector3d(3,0,-1),"my robot",goal);                

        environment.add(r);
        environment.add(c);
        Simbad frame = new Simbad(environment, false);
    }

}
