import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
public class Server{
    protected ServerSocket serverSock;
    protected ArrayList<LocalDateTime> time;
    public Server(int port){
        try{
            serverSock = new ServerSocket(port);
            time = new ArrayList<>();
        }
        catch(Exception e){
            System.err.println("Could not open server");
            System.exit(1);
        }
    }

    public void serve(int count){

        for (int i = 0; i < count; i++){
            try{
                Socket clientSock = serverSock.accept(); 
                System.out.println("New connection: "+clientSock.getRemoteSocketAddress());               
                (new ClientHandler(clientSock)).start();
                
            }
            catch(Exception e){} 
        }
    }

    private synchronized void addConnectionTime(LocalDateTime t) {
        time.add(t);
    }

    public synchronized ArrayList<LocalDateTime> getConnectedTimes() {
            return new ArrayList<>(time);
    }

    public void disconnect(){
        try{
        serverSock.close();
        }
        catch (Exception e){
            System.out.println("An exception happened.");
        }
    }

    private class ClientHandler extends Thread{

        Socket sock;
        public ClientHandler(Socket sock){
            this.sock = sock;
        }

        public void run(){
            PrintWriter out = null;
            BufferedReader in = null;
            try{
                out = new PrintWriter(sock.getOutputStream());
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //read and echo back forever!
                    
                    String msg = in.readLine();
                    if(!msg.equals("12345")){ //read null, remote closed
                        out.println("couldn't handshake");
                        out.flush();
                        sock.close();
                        return;
                    }
                
                addConnectionTime(LocalDateTime.now());

                String str = in.readLine();
                try{
                    int num = Integer.parseInt(str);
                    int count = 0;
                    for (int i = 1; i <= num; i++){
                        if (num % i == 0){
                            count++;
                        }
                    }
                    out.println("The number " + str + " has " + count + " factors");
                } 
                catch(Exception e){
                    out.println("There was an exception on the server");
                }
                
                out.close();
                in.close();
                sock.close();
                
            }catch(Exception e){}

            
            System.out.println("Connection lost: "+sock.getRemoteSocketAddress());
        }

        
    }
}
 