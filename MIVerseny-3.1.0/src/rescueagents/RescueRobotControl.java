package rescueagents;

import interfaces.CellInfo;
import interfaces.RobotPerception;
import rescueframework.AbstractRobotControl;
import rescueframework.Action;
import world.Robot;

/**
 * RobotControl class to implement custom robot control strategies for rescue robots.
 * The main aim of rescue robots is to discover injured people and carry them to the exit.
 */
public class RescueRobotControl extends AbstractRobotControl {
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
    public RescueRobotControl(Robot robot, RobotPerception perception) {
        super(robot, perception);
        this.amsService = AMSService.getAMSService();
        internalWorldMap = AMSService.getInternalMap();
        this.setRobotName("Rescue");
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
     * <b>Action.IDLE</b> for doing nothing.
     */
    @Override
    public Action step() {     
        path = null;
        
        // Rescue injured people
        if (!robot.hasInjured()) {
            if (robot.getLocation().hasInjured()){
                AMSService.log(this, "picking up injured...");
                return Action.PICK_UP;
            } else {
                AMSService.log(this, "calculating shortest injured path...");
                path = internalWorldMap.getShortestInjuredPath(robot.getLocation());
            }
        } else {
            if (robot.getLocation().isExit()) {
                AMSService.log(this, "putting down injured on exit cell");
                return Action.PUT_DOWN;
            } else {
                AMSService.log(this, "calculating shortest exit path...");
                path = internalWorldMap.getShortestExitPath(robot.getLocation());
            }
        }
        
        // No path found - discover the whole map
        if (path == null) {
            AMSService.log(this, "calculating shortest unknown path...");
            path = internalWorldMap.getShortestUnknownPath(robot.getLocation());
        }
        
        if (path != null) { 
            // Move the robot along the path
            AMSService.log(this, "calculating next step along the path...");
            return amsService.moveRobotAlongPath(robot, path);
        } else {
            // If no path found - the robot stays in place and does nothing
            AMSService.log(this, "no path found. Stopping.");
            return Action.IDLE;
        }
    }
}
