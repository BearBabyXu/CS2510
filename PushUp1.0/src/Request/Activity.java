package Request;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2193091130641718696L;
	private int TimeStamp;
	private String requesterID;
	private String type;
	private String msg;
	private ClientRequest clientRequest;
	
	public Activity(int time, String type, String msg, String id) 
	{
		// initialization constructor
		this.TimeStamp = time;
		this.requesterID = id;
		this.type = type;
		this.msg = msg;
	}
	
	// get TimeStamp of this Request
	public long getTimeStamp() 
	{
		return TimeStamp;
	}
	
	// get Requester ID
	public String getRequesterId()
	{
		return requesterID;
	}
	
	// get type of this Request
	public String getType()
	{
		return type;
	}
	
	// get Message of this Request
	public String getMessage() {
		return msg;
	}
	
	// get attached object content
	public ClientRequest getObjContent() {
		return clientRequest;
	}
        
        
	
	

}
