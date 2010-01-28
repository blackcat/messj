package dart.blackcat.messj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Boot {
	
	private static final Log LOG = LogFactory.getLog(Boot.class);

	/**
	 * messj
	 * megaprog
	 * @param args
	 */
	public static void main(String[] args) {

		String login = args[0];
		String passw = args[1];
		String host = args[2];
		int port = Integer.parseInt(args[3]);
		
		/*MessjAgent agent = */new MessjAgent(login, passw, host, port);
	}

}
