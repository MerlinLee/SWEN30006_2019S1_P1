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
					
					for(int j=0;j<Automail.getInstance().robots.length;j++) {
						if(Automail.getInstance().robots[j].id.equals(robot1.id) || Automail.getInstance().robots[j].id.equals(robot2.id)) {
							Automail.getInstance().robots[j] = null;
						}
					}
					
					for(int j=0;j<Automail.getInstance().robots.length;j++) {
						if(Automail.getInstance().robots[j]==null) {
							Automail.getInstance().robots[j] = team;
							break;
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
