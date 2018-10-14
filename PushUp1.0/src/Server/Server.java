package Server;


import Request.Activity;


public abstract class Server {
	
	private final String ServerID;
        private int Time;
        protected int port;
	public String name;
	abstract void start();
	
	public Server(int _SERVER_PORT, String _serverName, String id) {
		port = _SERVER_PORT;
		name = _serverName; 
                ServerID = id;
	}
        
        public boolean SendRequest() {
            Activity req;
            String type;
            
            type = "request";
            req = new Activity(Time+1, type, "msg", this.ServerID);
            
            return false;
        }
        
        public boolean SendResponse() {
            Activity req;
            String type;
            
            type = "response";
            req = new Activity(Time+1, type, "msg", this.ServerID);
            
            return false;
        }
        
        public boolean SendRelease() {
            Activity req;
            String type;
            
            type = "release";
            req = new Activity(Time+1, type, null, this.ServerID);
            
            return false;
        }
        
        public int getPort() {
		return port;
	}
	
	public String getName() {
		return name;
	}
        
        private void TimeSyncronized(int time) {
            
            this.Time = time;
        }
        
        private void QueueOrdering() {
            
        }
        
}
