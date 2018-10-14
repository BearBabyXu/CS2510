
public abstract class ServerAbs {
	
	private final String ServerID;
        private int Time;
	
	public ServerAbs(String id) {
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
        
        private void TimeSyncronized(int time) {
            
            this.Time = time;
        }
        
        private void QueueOrdering() {
            
        }
        
}
