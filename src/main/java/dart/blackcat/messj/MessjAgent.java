package dart.blackcat.messj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import dart.blackcat.agent.RunnableAgent;

public class MessjAgent extends RunnableAgent {

	private static final long serialVersionUID = 5067203172483447065L;
	private static final Log LOG = LogFactory.getLog(MessjAgent.class);
	
	/**
	 * program runtime
	 */
	Runtime run = null;
	private Process proc = null;
	/**
	 * work directory
	 */
	private File currDir = null;
	private final XMPPConnection connection;

	public MessjAgent(String login, String passw, String host, int port) {
		super();
		
		// Create a connection to the jabber.org server on a specific port.
		ConnectionConfiguration config = new ConnectionConfiguration(host, port);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		
		connection = new XMPPConnection(config);
		
		try {
			connection.connect();
		    connection.login(login, passw);
		    
		    // Assume we've created an XMPPConnection name "connection".
		    ChatManager chatManager = connection.getChatManager();
		    
		    // shell
			run = Runtime.getRuntime();
		    
		    chatManager.addChatListener(new ChatManagerListener() {
				
				public void chatCreated(Chat chat, boolean arg1) {
					chat.addMessageListener(new MessageListener() {
						
						public void processMessage(Chat chat, Message message) {
							
							// check only chat messages
							if (message.getType() == Message.Type.chat) {
								String command = message.getBody();
								
								// check for disconnect
								if ("disconnect".equals(command)) {
									stop();
								} else if (command != null) {
									
									try {
										try {
											
											// start process or start process in last accessed directory
											if (currDir == null) {
												proc = run.exec("/bin/bash", null);
											} else {
												proc = run.exec("/bin/bash", null, currDir);
											}
											BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream())); 
											PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
											
											// execute all commands and exit
											StringTokenizer st = new StringTokenizer(command, "\n", false);
											while (st.hasMoreTokens()) {
												String comm = st.nextToken();
												out.println(comm);
											}
											out.println("pwd");		// obtain new work directory (it could be changed)
											out.println("exit");
											
											// read response
											StringBuffer sb = new StringBuffer();
											String line1;
											String line2 = null;
											while ( (line1 = in.readLine()) != null) {
												if (line2 != null) {
													sb.append(line2).append("\n");
												}
												line2 = line1;
											}
											currDir = new File(line2);		// last line in response is result of "pwd" command
											
											// send response
											chat.sendMessage(sb.toString());
											
											// close all
											proc.waitFor();
											in.close();
											out.close();
											proc.destroy();
										} catch (InterruptedException e) {
											LOG.error(e);
											chat.sendMessage(e.getMessage());
											chat.sendMessage("Thread exception!!! Maybe program will shutdown...");
										} catch (IOException e) {
											LOG.error(e);
											chat.sendMessage(e.getMessage());
										}
									} catch (XMPPException e) {
										LOG.error(e);
									}
								}
							}
						}
					});
				}
			});
		} catch (XMPPException e) {
			LOG.error(e);
		}
	}
	
	@Override
	public void stop() {
		connection.disconnect(new Presence(Presence.Type.unavailable, "Bye-bye!", 10, Presence.Mode.dnd));
		super.stop();
	}
}