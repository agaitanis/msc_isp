import javax.vecmath.*;
import simbad.gui.*;
import simbad.sim.*;

public class Main {

    public Main() {
    }

    public static void main(String[] args) 
    {
        EnvironmentDescription environment = new EnvironmentDescription();        
        Point3d p = new Point3d(-7, 0, 4);
        CherryAgent ca = new CherryAgent(new Vector3d(p),"goal",0.1f);
        environment.add(ca);        
        MyRobot a = new MyRobot(new Vector3d(6, 0, 1),"my robot",p);                
        environment.add(a);                
        Simbad frame = new Simbad(environment, false);
    }

}
