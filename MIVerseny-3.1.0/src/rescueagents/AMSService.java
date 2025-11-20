package rescueagents;

import interfaces.CellInfo;
import interfaces.RobotInterface;
import rescueframework.AbstractRobotControl;
import rescueframework.Action;
import rescueframework.RescueFramework;
import world.Map;
import world.Path;

/**
 * Agent Management Service (AMS)
 * It provides an internal world model that is automatically updated by agent perceptions.
 * It may also provide other services for agents (logging, routing etc.).
 */
public class AMSService {

    static AMSService amsService;
    public static Map internalWorldModel;

    private AMSService() {
    }

    public static AMSService getAMSService() {
        if (amsService == null) {
            amsService = new AMSService();
        }
        return amsService;
    }

    /**
     * Get the actual internal world model
     * @return the internal world model
     */
    public static Map getInternalMap() {
        return internalWorldModel;
    }

    /**
     * Initialize the internal world model.
     * This is called by the framework.
     * @param initialWorldMap the Map object created during initialization
     */
    public static void setInternalWorldModel(Map initialWorldMap) {
        internalWorldModel = initialWorldMap;
    }

    /**
     * Log a message with timestamp to the console
     * @param message       The message to log to the console
     */
    public static void log(AbstractRobotControl control, String message) {
        RescueFramework.log(control.getRobotName() + " " + message);
    }
    public static void log(RobotInterface rob, String message) {
        RescueFramework.log(rob.getName() + " " + message);
    }

    /*****************************************************
     * Common robot control functions can be placed here.
     *****************************************************/

    /**
     * Calculate the next step along a path
     * @param path
     * @return move action to be performed
     */
    public Action moveRobotAlongPath(RobotInterface robot, Path path) {
        CellInfo nextCell = path.getNextCell(robot.getLocation());
        if (nextCell == null || nextCell.hasObstacle() || nextCell.hasRobot()) {
            log(robot, "The road is blocked. Stopping.");
            return Action.IDLE;
        } else {
            return nextCell.directionFrom(robot.getLocation());
        }

    }

}