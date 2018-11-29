

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author brantxu
 */
public class FTPtest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        
             int port=8888;
         System.out.println("TinyGoogle");
         
        ServerSocket serverSocket = new ServerSocket(8888);
        
        Socket socket=serverSocket.accept();
        DataInputStream dis=new DataInputStream(socket.getInputStream());
        System.out.print(dis.readInt());
    }
    
}
