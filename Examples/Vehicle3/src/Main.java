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
    
    

    public Main() {
    }
public static void main(String[] args) throws IOException {
        EnvironmentDescription environment = new EnvironmentDescription();    
        
        environment.light1IsOn=true;
        environment.light2IsOn=true;
        
        environment.light1SetPosition(7, 0.2, 7);
        environment.light2SetPosition(7, 0.2, 7);
        environment.ambientLightColor=environment.white;
        
        environment.add(new MyRobot(new Vector3d(0,0,0),"Turtle"));

        Simbad frame = new Simbad(environment, false);
    }
}
