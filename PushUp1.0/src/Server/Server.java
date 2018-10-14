package Server;


import Request.Request;


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
            Request req;
            String type;
            
            type = "request";
            req = new Request(Time+1, type, "msg", this.ServerID);
            
            return false;
        }
        
        public boolean SendResponse() {
            Request req;
            String type;
            
            type = "response";
            req = new Request(Time+1, type, "msg", this.ServerID);
            
            return false;
        }
        
        public boolean SendRelease() {
            Request req;
            String type;
            
            type = "release";
            req = new Request(Time+1, type, null, this.ServerID);
            
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
