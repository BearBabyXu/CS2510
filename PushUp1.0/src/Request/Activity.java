package Request;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2193091130641718696L;
	private int TimeStamp;  
	private long requesterID;   // session ID
	private ClientRequest clientRequest;    // Client request
	
	public Activity(int time, long requestID, ClientRequest clientRequest) 
	{
		// initialization constructor
		this.TimeStamp = time;
		this.requesterID = requestID;
		this.clientRequest=clientRequest;
	}
	
	// get TimeStamp of this Request
	public long getTimeStamp() 
	{
		return TimeStamp;
	}
	
	// get Requester ID
	public long getRequesterId()
	{
		return requesterID;
	}


	
	// get attached request content
	public ClientRequest getRequest() {
		return clientRequest;
	}
        
        public static Activity RequestConversion(int time, long requestID,ClientRequest request){
            
            return new Activity(time,requestID ,request);
            
        
        }
	
	

}
