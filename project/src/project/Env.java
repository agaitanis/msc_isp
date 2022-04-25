/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import javax.vecmath.Vector3d;
import simbad.sim.*;

/**
 *
 * @author alexa
 */
public class Env extends EnvironmentDescription {
    public Env() {
        add(new MyRobot(new Vector3d(0, 0, 0), "my robot"));
        light1SetPosition(-7, 2, -7);
        light2SetPosition(-7, 2, -7);
    }
}
