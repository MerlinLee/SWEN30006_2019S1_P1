package automail;

import java.util.LinkedList;
import java.util.ListIterator;

import exceptions.ItemTooHeavyException;

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
					return new Team(new SuperRobot[] {robot1, robot2});
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
					return new Team(new SuperRobot[] {robot1, robot2,robot3});
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
