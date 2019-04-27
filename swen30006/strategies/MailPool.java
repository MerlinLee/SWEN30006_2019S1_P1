package strategies;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.ListIterator;

import automail.MailItem;
import automail.PriorityMailItem;
import automail.Robot;
import automail.RobotSimpleFactory;
import automail.SuperRobot;
import exceptions.ItemTooHeavyException;

public class MailPool implements IMailPool {

	private class Item {
		int priority;
		int destination;
		MailItem mailItem;
		// Use stable sort to keep arrival time relative positions
		
		public Item(MailItem mailItem) {
			priority = (mailItem instanceof PriorityMailItem) ? ((PriorityMailItem) mailItem).getPriorityLevel() : 1;
			destination = mailItem.getDestFloor();
			this.mailItem = mailItem;
		}
	}
	
	public class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			int order = 0;
			if (i1.priority < i2.priority) {
				order = 1;
			} else if (i1.priority > i2.priority) {
				order = -1;
			} else if (i1.destination < i2.destination) {
				order = 1;
			} else if (i1.destination > i2.destination) {
				order = -1;
			}
			return order;
		}
	}
	
	private LinkedList<Item> pool;
	private LinkedList<SuperRobot> robots;

	public MailPool(int nrobots){
		// Start empty
		pool = new LinkedList<Item>();
		robots = new LinkedList<SuperRobot>();
	}

	public void addToPool(MailItem mailItem) {
		Item item = new Item(mailItem);
		pool.add(item);
		pool.sort(new ItemComparator());
	}
	
	@Override
	public void step() throws ItemTooHeavyException {
		try{
			ListIterator<SuperRobot> i = robots.listIterator();
			while (i.hasNext()) loadRobot(i);
		} catch (Exception e) { 
            throw e; 
        } 
	}
	
	private void loadRobot(ListIterator<SuperRobot> i) throws ItemTooHeavyException {
		ListIterator<Item> j = pool.listIterator();
		if (pool.size() > 0) {
			MailItem mailItem = j.next().mailItem;
			SuperRobot superRobot = RobotSimpleFactory.productRobot(i,mailItem,robots);
			if(superRobot!=null) {
				superRobot.addToHand(mailItem);
				j.remove();
				
				if(superRobot instanceof Robot && pool.size() > 0) {
					superRobot.addToTube(j.next().mailItem);
					j.remove();
				}
				superRobot.dispatch();
			}
		}else {
			i.next();
		}
	}

	@Override
	public void registerWaiting(SuperRobot robot) { // assumes won't be there already
		robots.add(robot);
	}

}
