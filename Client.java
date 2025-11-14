import java.net.*;
import java.io.*;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class Client{
    protected Socket sock;
    protected PrintWriter pw;
    protected BufferedReader in;
    public Client (String host, int port){
        try{
        sock = new Socket(host,port);   
        pw = new PrintWriter(sock.getOutputStream());
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));  
        }
        catch(Exception e){
            System.err.println("Cannot Connect");
            System.exit(1);   
        }
    }

    public void handshake(){
        try{
        pw.println("12345");
        pw.flush();
        }
        catch (Exception e){
        System.out.println("couldn't handshake");
        System.exit(1);
        }
    }

    public String request(String num){
        try{
        pw.println(num);
        pw.flush();
        String reply = in.readLine();
        return reply;
        }
        catch (Exception e){
        System.out.println("An exception happened.");
        return null;
        }
        
    }

    public void disconnect(){
        try{
        pw.close();
        in.close();
        sock.close();
        }
        catch (Exception e){
            System.out.println("An exception happened.");
        }
    }

    public Socket getSocket() {
        return sock;
    }
    
}