package strategies;

import automail.IMailDelivery;
import automail.Robot;
import automail.SuperRobot;


public class Automail {
	      
    public SuperRobot[] robots;
    public IMailPool mailPool;
    private static Automail automail;
    
    public Automail(IMailPool mailPool, IMailDelivery delivery, int numRobots) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	
    	/** Initialize robots */
    	robots = new SuperRobot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, mailPool);
    	
    }
    
    public static Automail getInstance(IMailPool mailPool, IMailDelivery delivery, int numRobots) {
    	automail = new Automail(mailPool, delivery, numRobots);
    	return automail;
    }
    
    public static Automail getInstance() {
    	return automail;
    }
    
}
