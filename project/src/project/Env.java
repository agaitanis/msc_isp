/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.*;

/**
 *
 * @author alexa
 */
public class Env extends EnvironmentDescription {
    public Env(Point3d light, Point3d goal) {
        add(new MyRobot(new Vector3d(0, 0, 0), "my robot", goal));
        light1SetPosition(light.x, light.y, light.z);
        light2SetPosition(light.x, light.y, light.z);
    }
}
