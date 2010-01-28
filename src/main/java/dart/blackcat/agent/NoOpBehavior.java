package dart.blackcat.agent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NoOpBehavior implements Behavior {

	private static final Log LOG = LogFactory.getLog(NoOpBehavior.class);
	private static final long serialVersionUID = -3932345889346285726L;

	public void behave() {
		LOG.debug("\"No operation behaviour\" behave() method was called");
	}

}
