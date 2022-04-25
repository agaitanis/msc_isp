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

        Goal goal =   new Goal(2,0,-4,Math.PI/6);

        CherryAgent c = new CherryAgent(goal.getPoint(),"goal",0.2f);

        Vector3d intermediate = new Vector3d(goal.getX()-MyRobot.r*Math.cos(goal.getPose()),0, goal.getZ()-MyRobot.r*Math.sin(goal.getPose()));
        CherryAgent c2 = new CherryAgent(intermediate,"intermediate",0.1f);
        c2.setColor(new Color3f(0,1,0));
        
        Vector3d theta = new Vector3d(goal.getX()+MyRobot.r*Math.cos(goal.getPose()),0, goal.getZ()+MyRobot.r*Math.sin(goal.getPose()));
        CherryAgent c3 = new CherryAgent(theta,"orientation",0.1f);
        c3.setColor(new Color3f(0,0,1));

        
        MyRobot r = new MyRobot(new Vector3d(0,0,0),"my robot",goal);                

        environment.add(r);
        environment.add(c);
        environment.add(c2);
        environment.add(c3);
        
        Simbad frame = new Simbad(environment, false);
    }

}
