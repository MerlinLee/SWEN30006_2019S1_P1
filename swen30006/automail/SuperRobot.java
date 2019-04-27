package automail;

import java.util.Map;
import java.util.TreeMap;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import strategies.IMailPool;

public abstract class SuperRobot{
	
	static public final int INDIVIDUAL_MAX_WEIGHT = 2000;
    static public final int PAIR_MAX_WEIGHT = 2600;
    static public final int TRIPLE_MAX_WEIGHT = 3000;
    
    IMailDelivery delivery;
    protected String id;
    
    public enum RobotState { DELIVERING, WAITING, RETURNING }
    
    public RobotState current_state;
    protected int current_floor;
    protected int destination_floor;
    protected IMailPool mailPool;
    protected boolean receivedDispatch;
    
    protected MailItem deliveryItem = null;
    protected MailItem tube = null;
    
    protected int deliveryCounter;
    
    static protected int count = 0;
	static protected Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

	protected abstract void moveTowards(int destination);
	public abstract void dispatch();
	protected abstract void changeState(RobotState nextState);
	public abstract void addToHand(MailItem mailItem) throws ItemTooHeavyException;
	
	 /* 
	  * This is called on every time step
      * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
      */
    public void step() throws ExcessiveDeliveryException {    	
    	switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == Building.MAILROOM_LOCATION){
                	if (tube != null) {
                		mailPool.addToPool(tube);
                        System.out.printf("T: %3d > old addToPool [%s]%n", Clock.Time(), tube.toString());
                        tube = null;
                	}
        			/** Tell the sorter the robot is ready */
        			mailPool.registerWaiting(this);
                	changeState(RobotState.WAITING);
                } else {
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.MAILROOM_LOCATION);
                	break;
                }
    		case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!isEmpty() && receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
        			setRoute();
                	changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
    			if(current_floor == destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
    				if(!deliveryItem.isDeliveryed) {
    					delivery.deliver(deliveryItem);
    				}
                    //delivery.deliver(deliveryItem);
                    deliveryItem = null;
                    deliveryCounter++;
                    if(deliveryCounter > 2){  // Implies a simulation bug
                    	throw new ExcessiveDeliveryException();
                    }
                    /** Check if want to return, i.e. if there is no item in the tube*/
                    if(tube == null){
                    	changeState(RobotState.RETURNING);
                    }
                    else{
                        /** If there is another item, set the robot's route to the location to deliver the item */
                        deliveryItem = tube;
                        tube = null;
                        setRoute();
                        changeState(RobotState.DELIVERING);
                    }
    			} else {
	        		/** The robot is not at the destination yet, move towards it! */
	                moveTowards(destination_floor);
    			}
                break;
    	}
    }
    
    /**
     * Sets the route for the robot
     */
    protected void setRoute() {
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }
    
    protected String getIdTube() {
    	return String.format("%s(%1d)", id, (tube == null ? 0 : 1));
    }
    
    public MailItem getTube() {
		return tube;
	}
    
    @Override
	public int hashCode() {
		Integer hash0 = super.hashCode();
		Integer hash = hashMap.get(hash0);
		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
		return hash;
	}
    
    public boolean isEmpty() {
		return (deliveryItem == null && tube == null);
	}

    public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
		assert(tube == null);
		tube = mailItem;
		//if (tube.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}
    
    
}
