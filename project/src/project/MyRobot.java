package project;

import simbad.sim.*;
import javax.vecmath.*;

enum RobotStatus {
    MOVE_FWD,
    REORIENT,
    STOP,
}

public class MyRobot extends Agent {
    LightSensor leftLight;
    LightSensor rightLight;
    RangeSensorBelt sonars;
    RobotStatus status;
    double leftLux;
    double rightLux;
    double prevLeftLux;
    double prevRightLux;
    int cnt;
    
    static double K_ROT = 10;
    static double K_ROT_2 = 0.03;
    static double K_TRANSL = 40;
    static double K_TRANSL_2 = 0.01;
    static double GOAL_LUX = 0.053;
    
    static double SAFETY = 0.8;
    static double K1 = 5;
    static double K2 = 0.9;
    static double K3 = 3;
    static boolean CLOCKWISE = true;
    
    public MyRobot (Vector3d position, String name) {
        super(position, name);
        status = RobotStatus.MOVE_FWD;
        leftLight = RobotFactory.addLightSensorLeft(this);
        rightLight = RobotFactory.addLightSensorRight(this);
        sonars = RobotFactory.addSonarBeltSensor(this, 12, 1.5f);
        status = RobotStatus.MOVE_FWD;
        leftLux = 0;
        rightLux = 0;
        prevLeftLux = 1;
        prevRightLux = 1;
        cnt = 0;
    }
    
    private boolean shouldStop() {
        double avgLux = 0.5*(leftLux + rightLux);
        
        Point3d pos = new Point3d();
        
        getCoords(pos);
        
        return avgLux > GOAL_LUX;
    }
    
    private void stop() {
        setTranslationalVelocity(0);
        setRotationalVelocity(0);
    }
    
    private boolean shouldReorient() {
        double avgLux = 0.5*(leftLux + rightLux);
        double prevAvgLux = 0.5*(prevLeftLux + prevRightLux);
        double diff = avgLux - prevAvgLux;

        return diff < 0;
    }
    
    private void reorient() {
        double diff = leftLux - rightLux;
        double avgLux = 0.5*(leftLux + rightLux);
        double goalDiff = GOAL_LUX - avgLux;
        
        setTranslationalVelocity(0);
        setRotationalVelocity(K_ROT * Math.signum(diff) * (Math.log10(Math.abs(goalDiff) + 1) + K_ROT_2));
    }
    
    private boolean shouldMoveFwd() {
        double diff = leftLux - rightLux;
        double prevDiff = prevLeftLux - prevRightLux;

        return Math.signum(diff) != Math.signum(prevDiff);
    }
    
    private void moveFwd() {
        double avgLux = 0.5*(leftLux + rightLux);
        double goalDiff = GOAL_LUX - avgLux;
        
        setTranslationalVelocity(K_TRANSL*(Math.log10(Math.abs(goalDiff) + 1) + K_TRANSL_2));
        setRotationalVelocity(0);
    }
        
    private double wrapToPi(double a) {
        while (a > Math.PI) {
            a -= Math.PI*2;
        }
        while (a <= -Math.PI) {
            a += Math.PI*2;
        }
        return a;
    }
        
    private Point3d getSensedPoint(int sonar){        
        double v = radius + sonars.getMeasurement(sonar);
        double x = v*Math.cos(sonars.getSensorAngle(sonar));
        double z = v*Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x, 0, z);
    }
    
    private void circumNavigate() {
        int min = 0;
        
        for (int i = 1; i < sonars.getNumSensors(); i++) {
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min)) {
                min = i;
            }
        }
        
        Point3d p = getSensedPoint(min);
        double d = p.distance(new Point3d(0, 0, 0));  
        Vector3d v = CLOCKWISE ? new Vector3d(-p.z, 0, p.x) : new Vector3d(p.z, 0, -p.x);
        
        double phLin = Math.atan2(v.z, v.x);
        double phRot = Math.atan(K3*(d - SAFETY));
        
        if (CLOCKWISE) {
            phRot = -phRot;
        }
        
        double phRef = wrapToPi(phLin + phRot); 
        
        setRotationalVelocity(K1*phRef);
        setTranslationalVelocity(K2*Math.cos(phRef));            
    }
    
    @Override
    public void initBehavior() {
    }
    
    @Override
    public void performBehavior() {       
        if (status == RobotStatus.STOP) {
            return;
        }
         
        double curLeftLux = leftLight.getLux();
        double curRightLux = rightLight.getLux();
        
        cnt++;
        if (cnt == 1) {
            leftLux = 0;
            rightLux = 0;
        }
        if (cnt < 10) {
            leftLux += curLeftLux;
            rightLux += curRightLux;
            return;
        }
        leftLux /= cnt;
        rightLux /= cnt;
        cnt = 0;

        if (shouldStop()) {
            stop();
            status = RobotStatus.STOP;
            return;
        }
        
        if (status == RobotStatus.MOVE_FWD) {
            if (shouldReorient()) {
                reorient();
                status = RobotStatus.REORIENT;
            } else {
                moveFwd();
            }
        } else if (status == RobotStatus.REORIENT) {
            if (shouldMoveFwd()) {
                moveFwd();
                status = RobotStatus.MOVE_FWD;
            } else {
                reorient();
            }
        }

        prevLeftLux = leftLux;
        prevRightLux = rightLux;
    }
}
