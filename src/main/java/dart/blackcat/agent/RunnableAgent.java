package dart.blackcat.agent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunnableAgent extends Agent implements Runnable {
	
	private static final long serialVersionUID = 3737747276594259289L;
	private static final Log LOG = LogFactory.getLog(RunnableAgent.class);
	
	protected Thread thread;
	private boolean stopped = false;
	
	public RunnableAgent() {
		setBehavior(new NoOpBehavior());
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void resume() {
		this.notify();
	}
	
	public void stop() {
		stopped = true;
		resume();
	}

	public synchronized void run() {
		while ( ! stopped) {
			getBehavior().behave();
			try {
				wait();
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}
	}
}
