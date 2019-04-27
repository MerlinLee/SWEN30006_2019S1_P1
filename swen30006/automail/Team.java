package automail;

import exceptions.ItemTooHeavyException;
import strategies.Automail;

/**
 * @author Tom
 * The team of robots
 */
public class Team extends SuperRobot{
	protected SuperRobot[] teamOfRobots;
	
	public Team(SuperRobot[] teamOfRobots) {
		delivery = teamOfRobots[0].delivery;
		this.teamOfRobots=teamOfRobots;
		current_state = RobotState.DELIVERING;
		current_floor = Building.MAILROOM_LOCATION;
		id = "";
		String out = "";
		for(int i=0;i<teamOfRobots.length;i++) {
			id = id + teamOfRobots[i].id;
			out = out + teamOfRobots[i].id + " ";
		}
		System.out.printf("T: %3d > %7sare a team.\n", Clock.Time(), out);
		
	}
	
	protected void moveTowards(int destination) {
		// The team has 2 robots
        if(teamOfRobots.length==2) {
        	if(teamOfRobots[0].current_floor==teamOfRobots[1].current_floor) {
        		// Robot1 goes up
        		teamOfRobots[0].current_floor++;
        	}else {
        		// Robot2 goes up
        		teamOfRobots[1].current_floor++;
        		current_floor++;
        	}
        }
        
        // The team has 3 robots
        if(teamOfRobots.length==3) {
        	if(teamOfRobots[0].current_floor==teamOfRobots[1].current_floor) {
        		if(teamOfRobots[1].current_floor==teamOfRobots[2].current_floor) {
        			// Robot1 goes up
        			teamOfRobots[0].current_floor++;
        		}else {
        			// Robot3 goes up
        			teamOfRobots[2].current_floor++;
        			current_floor++;
        		}
        	}else {
        		// Robot2 goes up
        		teamOfRobots[1].current_floor++;
        	}
        }
    }
	
	/**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
	protected void changeState(RobotState nextState){
    	for(int i=0;i<teamOfRobots.length;i++) {
    		teamOfRobots[i].changeState(nextState);
    	}
    	current_state = nextState;
    	if(nextState==RobotState.RETURNING) {
    		releaseRobots();
    	}
    }
	
	public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
		deliveryItem = mailItem;
		destination_floor = mailItem.destination_floor;
		for(int i=0;i<teamOfRobots.length;i++) {
    		teamOfRobots[i].addToHand(mailItem);
    	}
	}
	
	public void dispatch() {
		for(int i=0;i<teamOfRobots.length;i++) {
    		teamOfRobots[i].dispatch();
    	}
    }
	
	private void releaseRobots() {
		for(SuperRobot robot : teamOfRobots) {
			robot.deliveryItem = null;
			Automail.getInstance().robots[robot.hashCode()] = robot;
		}
	}
	
}
