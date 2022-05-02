package project;

import simbad.sim.*;
import javax.vecmath.*;

enum RobotStatus {
    FOLLOW_LINE_STRAIGHT,
    FOLLOW_LINE_LEFT,
    FOLLOW_LINE_RIGHT,
    MOVE_FWD,
    REORIENT,
    CIRVUMNAVIGATE,
    STOP,
}

public class MyRobot extends Agent {
    LineSensor line;
    LightSensor leftLight;
    LightSensor rightLight;
    RangeSensorBelt sonars;
    RangeSensorBelt bumpers;
    RobotStatus status;
    double leftLuxSum;
    double rightLuxSum;
    double leftLux;
    double rightLux;
    double prevLeftLux;
    double prevRightLux;
    double prevPrevLux;
    double iL;
    double iH;
    int cnt;
    boolean init = false;
    
    static double K_ROT = 10;
    static double K_ROT_2 = 0.03;
    static double K_TRANSL = 35;
    static double K_TRANSL_2 = 0.01;
    static double GOAL_LUX = 0.053;
    
    static double SAFETY = 0.8;
    static double K1 = 2;
    static double K2 = 1;
    static double K3 = 3;
    static boolean CLOCKWISE = false;
    
    public MyRobot(Vector3d position, String name) {
        super(position, name);
        leftLight = RobotFactory.addLightSensorLeft(this);
        rightLight = RobotFactory.addLightSensorRight(this);
        sonars = RobotFactory.addSonarBeltSensor(this, 12, 1.5f);
        bumpers = RobotFactory.addBumperBeltSensor(this, 8);
        line = RobotFactory.addLineSensor(this, 11);
        status = RobotStatus.FOLLOW_LINE_STRAIGHT;
        leftLuxSum = 0;
        rightLuxSum = 0;
        leftLux = 0;
        rightLux = 0;
        prevLeftLux = 0;
        prevRightLux = 0;
        prevPrevLux = 0;
        iL = 0;
        iH = 0;
        cnt = 0;
    }
    
    private double getIntensity() {
        return 0.5*(leftLux + rightLux);
    }
    
    private void updateLux() {
        double curLeftLux = leftLight.getLux();
        double curRightLux = rightLight.getLux();
        
        cnt++;
        
        if (cnt == 10) {
            leftLux = leftLuxSum/cnt;
            rightLux = rightLuxSum/cnt;
            leftLuxSum = 0;
            rightLuxSum = 0;
            cnt = 0;
        } else {
            leftLuxSum += curLeftLux;
            rightLuxSum += curRightLux;
        }
    }
    
    private void updatePrevLux() {
        if (cnt == 0) {
            prevPrevLux = 0.5*(prevLeftLux + prevRightLux);
            prevLeftLux = leftLux;
            prevRightLux = rightLux;
        }
    }
    
    private boolean foundLocalMax() {
        double lux = 0.5*(leftLux + rightLux);
        double prevLux = 0.5*(prevLeftLux + prevRightLux);
        
        return prevLux > prevPrevLux && prevLux > lux;
    }
    
    private boolean reachedLight() {
        double lux = 0.5*(leftLux + rightLux);
        
        Point3d pos = new Point3d();
        
        getCoords(pos);
        
        return lux > GOAL_LUX;
    }
    
    private void stop() {
        status = RobotStatus.STOP;
        setTranslationalVelocity(0);
        setRotationalVelocity(0);
    }
    
    private boolean shouldReorient() {
        double lux = 0.5*(leftLux + rightLux);
        double prevLux = 0.5*(prevLeftLux + prevRightLux);
        double diff = lux - prevLux;

        return diff < 0;
    }
    
    private void reorient() {
        status = RobotStatus.REORIENT;
        
        double diff = leftLux - rightLux;
        double lux = 0.5*(leftLux + rightLux);
        double goalDiff = GOAL_LUX - lux;
        
        setTranslationalVelocity(0);
        setRotationalVelocity(K_ROT * Math.signum(diff) * (Math.log10(Math.abs(goalDiff) + 1) + K_ROT_2));
    }
    
    private boolean isAlignedWithLight() {
        double diff = leftLux - rightLux;
        double prevDiff = prevLeftLux - prevRightLux;

        return Math.signum(diff) != Math.signum(prevDiff);
    }
    
    private void moveFwd() {
        status = RobotStatus.MOVE_FWD;
        
        double lux = 0.5*(leftLux + rightLux);
        double goalDiff = GOAL_LUX - lux;
        
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
        status = RobotStatus.CIRVUMNAVIGATE;
        
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
    
    private boolean bumperWasHit() {
        return bumpers.oneHasHit();
    }
    
    private boolean lineWasDetected() {
        for (int i = 0; i < line.getNumSensors(); i++) {
            if (line.hasHit(i)) return true;
        }
        
        return false;
    }
    
    private void followLine() {
        int left = 0;
        int right = 0;
        int k = 0;
        
        for (int i = 0; i < line.getNumSensors()/2; i++) {
            left += line.hasHit(i) ? 1 : 0;
            right += line.hasHit(line.getNumSensors() - i - 1) ? 1 : 0;
            k++;
        }
        
        if (left == 1 && right == 1) {
            status = RobotStatus.FOLLOW_LINE_STRAIGHT;
        }
        
        if (status == RobotStatus.FOLLOW_LINE_LEFT) {
            right = 0;
        } else if (status == RobotStatus.FOLLOW_LINE_RIGHT) {
            left = 0;
        } else {
            if (left >= 3 && right >= 3) {
                if (leftLux > rightLux) {
                    status = RobotStatus.FOLLOW_LINE_LEFT;
                    right = 0;
                } else {
                    status = RobotStatus.FOLLOW_LINE_RIGHT;
                    left = 0;
                } 
            } else {
                status = RobotStatus.FOLLOW_LINE_STRAIGHT;
            }
        }
             
        double diff = (left - right)/(float)k;
               
        setRotationalVelocity(diff);
        setTranslationalVelocity(1 - Math.abs(diff));
    }
    
    @Override
    public void initBehavior() {

    }
    
    @Override
    public void performBehavior() {       
        if (status == RobotStatus.STOP) {
            return;
        }
        
        updateLux();
        
        if (reachedLight()) {
            stop();
            return;
        }
        
        switch (status) {
            case FOLLOW_LINE_STRAIGHT:
            case FOLLOW_LINE_LEFT:
            case FOLLOW_LINE_RIGHT:
                if (bumperWasHit()) {
                    if (iL != getIntensity()) iH = getIntensity();
                    circumNavigate();
                } else if (!lineWasDetected()) {
                    iL = getIntensity();
                    moveFwd();
                } else {
                    followLine();
                }
                break;
            case MOVE_FWD:
                if (bumperWasHit()) {
                    if (iL != getIntensity()) iH = getIntensity();
                    circumNavigate();
                } else if (lineWasDetected()) {
                    followLine();
                } else if (shouldReorient()) {
                    if (iL != getIntensity()) iH = getIntensity();
                    reorient();
                } else {
                    moveFwd();
                }
                break;
            case REORIENT:
                if (lineWasDetected()) {
                    followLine();
                } else if (isAlignedWithLight()) {
                    iL = getIntensity();
                    moveFwd();
                } else {
                    reorient();
                }
                break;
            case CIRVUMNAVIGATE:
                if (lineWasDetected() && !bumperWasHit()) {
                    followLine();
                } else if (foundLocalMax() && getIntensity() > iH) {
                    reorient();
                } else {
                    circumNavigate();
                }
                break;
            default:
                break;
        }

        updatePrevLux();
    }
}
