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
    RobotStatus status;
    double leftLux;
    double rightLux;
    double prevLeftLux;
    double prevRightLux;
    int cnt;
    Point3d goal;
    
    static double K_ROT = 10;
    static double K_ROT_2 = 0.03;
    static double K_TRANSL = 40;
    static double K_TRANSL_2 = 0.01;
    static double GOAL_LUX = 0.053;
    
    public MyRobot (Vector3d position, String name, Point3d goalPos) {
        super(position, name);
        status = RobotStatus.REORIENT;
        leftLight = RobotFactory.addLightSensorLeft(this);
        rightLight = RobotFactory.addLightSensorRight(this);
        leftLux = 0;
        rightLux = 0;
        prevLeftLux = 1;
        prevRightLux = 1;
        cnt = 0;
        goal = goalPos;
    }
    
    @Override
    public void initBehavior() {
    }
    
    private boolean shouldStop() {
        double avgLux = 0.5*(leftLux + rightLux);
        
        Point3d pos = new Point3d();
        
        getCoords(pos);
        
        System.out.println("dist = " + pos.distance(goal));
        System.out.println("avgLux = " + avgLux);
        
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
        
        setTranslationalVelocity(K_TRANSL * (Math.log10(Math.abs(goalDiff) + 1) + K_TRANSL_2));
        setRotationalVelocity(0);
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
