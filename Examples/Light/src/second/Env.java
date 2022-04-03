/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package second;

/**
 *
 * @author dvrakas
 */
import simbad.sim.*;
import javax.vecmath.Vector3d;
public class Env extends EnvironmentDescription {
    public Env(){
        add(new Arch(new Vector3d(0,0,-5),this));
        add(new Arch(new Vector3d(0,0,5),this));
        add(new MyRobot(new Vector3d(0, 0, 0),"my robot"));        
        Arch a = new Arch(new Vector3d(5,0,0),this);
        a.rotate90(1);
        add(a);
        Arch b = new Arch(new Vector3d(-5,0,0),this);
        b.rotate90(1);
        add(b);        
    }
}