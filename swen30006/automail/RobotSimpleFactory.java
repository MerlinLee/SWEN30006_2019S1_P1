package automail;

import java.util.LinkedList;
import java.util.ListIterator;

import exceptions.ItemTooHeavyException;
import strategies.Automail;

public class RobotSimpleFactory {
	
	public static SuperRobot productRobot(ListIterator<SuperRobot> i,MailItem mailItem,LinkedList<SuperRobot> robots) throws ItemTooHeavyException {
		try {
			if(mailItem.weight<=2000) {
				SuperRobot robot1 = i.next();
				i.remove();
				return robot1;
				
			} else if(mailItem.weight<=2600) {
				if(robots.size()>1) {
					SuperRobot robot1 = i.next();
					i.remove();
					SuperRobot robot2 = i.next();
					i.remove();
					Team team = new Team(new SuperRobot[] {robot1, robot2});
					
					for(SuperRobot robot : Automail.getInstance().robots) {
						if(robot.id.equals(robot1.id) || robot.id.equals(robot2.id)) {
							robot = null;
						}
					}
					
					for(SuperRobot robot : Automail.getInstance().robots) {
						if(robot==null) {
							robot = team;
						}
					}
					
					return team;
				}else {
					i.next();
				}
			} else {
				if(robots.size()>2) {
					SuperRobot robot1 = i.next();
					i.remove();
					SuperRobot robot2 = i.next();
					i.remove();
					SuperRobot robot3 = i.next();
					i.remove();
					Team team = new Team(new SuperRobot[] {robot1, robot2,robot3});
					Automail.getInstance().robots = new SuperRobot[] {team,null,null};
					return team;
				}else {
					i.next();
				}
			}
		} catch(Exception e) {
			throw new ItemTooHeavyException();
		}
		
		return null;
	}
}
