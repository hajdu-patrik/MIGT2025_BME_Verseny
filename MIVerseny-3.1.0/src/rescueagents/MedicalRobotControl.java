/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescueagents;

import interfaces.RobotPerception;
import rescueframework.AbstractRobotControl;
import rescueframework.Action;
import world.Injured;
import world.Robot;


/**
 * RobotControl class to implement custom robot control strategies for medical robots
 * The main aim of medical robots is to discover injured people and keep them alive until they are rescued.
 */
public class MedicalRobotControl extends AbstractRobotControl{
    RescueRobotControl fallbackRobotControl;
    private AMSService amsService;
    private RobotPerception internalWorldMap;

    /**
     * Default constructor saving world robot object and perception interface
     *
     * @param robot
     *            The robot object in the world
     * @param perception
     *            Robot perceptions
     */
    public MedicalRobotControl(Robot robot, RobotPerception perception) {
        super(robot, perception);
        fallbackRobotControl = new RescueRobotControl(robot, perception);
        this.amsService = AMSService.getAMSService();
        internalWorldMap = amsService.getInternalMap();
        this.setRobotName("Medic");
    }

    /**
     * Custom step strategy of the robot, implement your robot control here!
     *
     * @return one of the following actions:
     * <b>Action.STEP_UP</b> for step up,
     * <b>Action.STEP_RIGHT</b> for step right,
     * <b>Action.STEP_DOWN</b> for step down,
     * <b>Action.STEP_LEFT</b> for step left
     * <b>Action.PICK_UP</b> for pick up injured,
     * <b>Action.PUT_DOWN</b> for put down injured,
     * <b>Action.HEAL</b> for heal injured.
     * <b>Action.IDLE</b> for doing nothing.
     */
    @Override
    public Action step() {
        // Example: heal the injured at the current location
        if (robot.hasMedicine()
                && robot.getLocation().hasInjured()
                && robot.getLocation().getInjured().isAlive()
                && robot.getLocation().getInjured().getHealth() < Injured.MAXHEALTH / 3){
            AMSService.log(this, "Healing at current location...");
            return Action.HEAL;
        }
        // Otherwise work as a rescue agent (find an injured)
        return fallbackRobotControl.step();
    }
}
