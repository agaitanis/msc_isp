/*    
  	Algernon is an implementation of a subsumption architecture for Simbad.
  	
    Copyright (C) 2007  Paul Reiners

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    You may reach the author, Paul Reiners, by email at <paul.reiners@gmail.com>
    or at the following postal address:
    
    	503 15th Avenue SW
    	Rochester, MN  55902
 */

package Subsumption;

import javax.vecmath.Vector3d;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

/**
 * @author Paul Reiners
 * 
 */
public class BehaviorBasedAgent extends Agent {

   private Behavior[] behaviors;
   private boolean suppresses[][];
   private int currentBehaviorIndex;
   private Sensors sensors;

   public BehaviorBasedAgent(Vector3d position, String name, int sensorCount,
         boolean addLightSensors) {
      super(position, name);
      // Add sensors
      RangeSensorBelt bumpers = RobotFactory.addBumperBeltSensor(this,
            sensorCount);
      RangeSensorBelt sonars = RobotFactory.addSonarBeltSensor(this,
            sensorCount);
      if (addLightSensors) {
         LightSensor lightSensorLeft = RobotFactory.addLightSensorLeft(this);
         LightSensor lightSensorRight = RobotFactory.addLightSensorRight(this);
         sensors = new Sensors(sonars, bumpers, lightSensorLeft,
               lightSensorRight);
      } else {
         sensors = new Sensors(sonars, bumpers);
      }
      CameraSensor camera = RobotFactory.addCameraSensor(this);
      System.out.println("camera: " + camera);
   }

   public void initBehaviors(Behavior[] behaviors,
         boolean subsumptionRelationships[][]) {
      this.behaviors = behaviors;
      this.suppresses = subsumptionRelationships;
   }

   /*
    * (non-Javadoc)
    * 
    * @see simbad.sim.Agent#initBehavior()
    */
   @Override
   protected void initBehavior() {
      currentBehaviorIndex = 0;
   }

   /*
    * (non-Javadoc)
    * 
    * @see simbad.sim.Agent#performBehavior()
    */
   @Override
   protected void performBehavior() {
      boolean isActive[] = new boolean[behaviors.length];
      for (int i = 0; i < isActive.length; i++) {
         isActive[i] = behaviors[i].isActive();
      }
      boolean ranABehavior = false;
      while (!ranABehavior) {
         boolean runCurrentBehavior = isActive[currentBehaviorIndex];
         if (runCurrentBehavior) {
            for (int i = 0; i < suppresses.length; i++) {
               if (isActive[i] && suppresses[i][currentBehaviorIndex]) {
                  runCurrentBehavior = false;

                  break;
               }
            }
         }

         if (runCurrentBehavior) {
            if (currentBehaviorIndex < behaviors.length) {
               Velocities newVelocities = behaviors[currentBehaviorIndex].act();
               this.setTranslationalVelocity(newVelocities
                     .getTranslationalVelocity());
               this
                     .setRotationalVelocity(newVelocities
                           .getRotationalVelocity());
            }
            ranABehavior = true;
         }

         if (behaviors.length > 0) {
            currentBehaviorIndex = (currentBehaviorIndex + 1)
                  % behaviors.length;
         }
      }
   }

   /**
    * @return the sensors
    */
   public Sensors getSensors() {
      return sensors;
   }

   /*
    * Returns a description of this behavior-based agent.
    */
   @Override
   public String toString() {
      return "[BehaviorBasedAgent: behaviors=" + behaviors + ", suppresses="
            + suppresses + ", currentBehaviorIndex=" + currentBehaviorIndex
            + ", sensors=" + sensors + ", " + super.toString() + "]";
   }
}
