package rescueagents;

import interfaces.RobotPerception;
import rescueframework.AbstractRobotControl;
import rescueframework.Action;
import rescueframework.MainFrame;
import world.Robot;

/**
 * RobotControl class to implement simple sensors.
 * Static sensors are cheap, small, battery powered devices dropped down on the site.
 * Their main purpose is to collect sensor data.
 * They perform no actions on their own.
 */
public class StaticSensorControl extends AbstractRobotControl {
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
	public StaticSensorControl(Robot robot, RobotPerception perception) {
		super(robot, perception);
		this.amsService = AMSService.getAMSService();
		internalWorldMap = amsService.getInternalMap();
		this.setRobotName("Sensor");
	}

	/**
	 * This agent has no action on its own.
	 * It is collecting sensor data in its surroundings.
	 * @return IDLE Action
	 */
	@Override
	public Action step() {
		amsService.log(this, "monitoring my location...");
		return Action.IDLE;
	}
	
	/**
	 * This method will be called during the initialization of the map.
	 * The application puts the sensor nearby the returned value.
	 * @param mapWidth the width of the map
	 * @param mapHeight the height of the map
	 * @return the X position of the sensor
	 */
	public static int generateXCoord(int mapWidth, int mapHeight) {	return MainFrame.randomBetween(0, mapWidth); }
	
	/**
	 * This method will be called during the initialization of the map.
	 * The application puts the sensor nearby the returned value.
	 * @param mapWidth the width of the map
	 * @param mapHeight the height of the map
	 * @return the Y position of the sensor
	 */
	public static int generateYCoord(int mapWidth, int mapHeight) {
		return MainFrame.randomBetween(0, mapHeight);
	}
	
}
