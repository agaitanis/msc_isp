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
        this.wallColor = this.green;
        light1IsOn = true;
        light2IsOn = false;
        Wall w1 = new Wall(new Vector3d(9, 0, 0), 19, 1, this);
        w1.rotate90(1);
        add(w1);
        Wall w2 = new Wall(new Vector3d(-9, 0, 0), 19, 2, this);
        w2.rotate90(1);
        add(w2);
        Wall w3 = new Wall(new Vector3d(0, 0, 9), 19, 1, this);
        add(w3);
        Wall w4 = new Wall(new Vector3d(0, 0, -9), 19, 2, this);
        add(w4);
        Line l1 = new Line(new Vector3d(0, 0, 0), 2, this);
        l1.rotate90(1);
        add(l1);
        Line l2 = new Line(new Vector3d(2, 0, 0), 2, this);
        add(l2);
        Line l3 = new Line(new Vector3d(-2, 0, 2), 4, this);
        l3.rotate90(1);
        add(l3);
        Line l4 = new Line(new Vector3d(-2, 0, -2), 4, this);
        add(l4);
        Line l5 = new Line(new Vector3d(-2, 0, -2), 6, this);
        l5.rotate90(1);
        add(l5);
        Line l6 = new Line(new Vector3d(4, 0, -2), 6, this);
        add(l6);
        Line l7 = new Line(new Vector3d(-4, 0, 4), 8, this);
        l7.rotate90(1);
        add(l7);
        Line l8 = new Line(new Vector3d(-4, 0, -4), 8, this);
        add(l8);
        Line l9 = new Line(new Vector3d(-4, 0, -4), 10, this);
        l9.rotate90(1);
        add(l9);
        Line l10 = new Line(new Vector3d(6, 0, -4), 10, this);
        add(l10);
        Line l11 = new Line(new Vector3d(-6, 0, 6), 12, this);
        l11.rotate90(1);
        add(l11);
        Line l12 = new Line(new Vector3d(-6, 0, -6), 12, this);
        add(l12);
        Line l13 = new Line(new Vector3d(-6, 0, -6), 13, this);
        l13.rotate90(1);
        add(l13);

        add(new MyRobot(new Vector3d(0, 0, 0), "robot 1"));
    }
}