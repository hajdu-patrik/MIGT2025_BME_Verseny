package rescueagents;

import interfaces.CellInfo;
import interfaces.RobotPerception;
import rescueframework.AbstractRobotControl;
import rescueframework.Action;
import world.Path;
import world.Robot;

/**
 * RescueRobotControl class to implement custom robot control strategies for flying drones.
 * The main aim of drones is to discover the map and the location of injured people.
 */
public class FlyingDroneControl extends AbstractRobotControl {
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
	public FlyingDroneControl(Robot robot, RobotPerception perception) {
		super(robot, perception);
		this.amsService = AMSService.getAMSService();
		internalWorldMap = AMSService.getInternalMap();
        this.setRobotName("Drone");
    }

	/**
	 * Custom step strategy of the robot, implement your robot control here!
	 *
	 * @return one of the following actions:
	 * <b>Action.STEP_UP</b> for step up,
	 * <b>Action.STEP_RIGHT</b> for step right,
	 * <b>Action.STEP_DOWN</b> for step down,
	 * <b>Action.STEP_LEFT</b> for step left
	 * <b>Action.IDLE</b> for doing nothing.
	 */
	@Override
	public Action step() {
		path = internalWorldMap.getShortestUnknownPath(robot.getLocation());
		if (path != null) {
			// Move the robot along the path
			AMSService.log(this, "calculating next step along the path...");
			return amsService.moveRobotAlongPath(robot, path);
		} else if (!robot.getLocation().isExit()) {
			AMSService.log(this, "Can't discover anything new. Flying to the exit.");
			path = internalWorldMap.getShortestExitPath(robot.getLocation());
			return amsService.moveRobotAlongPath(robot, path);
		}
		AMSService.log(this, "Sleeping at the exit.");
		return Action.IDLE;
	}

}
