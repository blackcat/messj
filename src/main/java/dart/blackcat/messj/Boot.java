package dart.blackcat.messj;


public class Boot {
	
//	private static final Log LOG = LogFactory.getLog(Boot.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			String login = args[0];
			String passw = args[1];
			String host = args[2];
			int port = Integer.parseInt(args[3]);
			String user = args[4];
			
			if (
					login == null || login.isEmpty() || 
					passw == null || passw.isEmpty() || 
					host == null  || host.isEmpty()  || 
					port <= 0  || 
					user == null  || user.isEmpty()  
			) {
				showErrorAndExit();
			}
			/*MessjAgent agent = */new MessjAgent(login, passw, host, port);
		} catch (Exception e) {
			showErrorAndExit();
		}
	}
	
	protected static void showErrorAndExit() {
		System.out.println("messj <login> <password> <host> <port> <authorized_user>");
	}

}
