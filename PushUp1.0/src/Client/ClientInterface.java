
public interface ClientInterface {
	
	/**
	 * @param server id that client is going to connect to
	 * @param server port that client is going to connect to
	 * 
	 * @return true if client connected to server 
	 */	
	public boolean connect(final String server, final int port);
	
	
	/**
	 *  close down connection with server
	 */
	public boolean disconnect();

	/**
	 * 
	 * @param Send Request object
	 * @return return true if successfully sent
	 */
	public boolean Send(Request request);
	
	/**
	 * 
	 * @param requestor ID
	 * @param messagetype
	 * @param update data content
	 * @return finished Request object
	 */
	public Request WriteRequest(String id, String type, String data);
	
}
