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

        
        Goal g = new Goal(7,0,6,Math.PI/2);
        environment.add(new CherryAgent(g.getPoint(),"goal",0.2f));
        
        
        MyRobot r = new MyRobot(new Vector3d(0,0,0),"my robot",g);      
        
        
        Vector3d theta = new Vector3d(g.getX()-MyRobot.r*Math.cos(g.getPose()),0, g.getZ()-MyRobot.r*Math.sin(g.getPose()));
        CherryAgent c3 = new CherryAgent(theta,"orientation",0.1f);
        c3.setColor(new Color3f(0,0,1));

        environment.add(r);
        environment.add(c3);
        
        Simbad frame = new Simbad(environment, false);
    }

}
