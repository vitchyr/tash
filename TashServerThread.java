package pong.tash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TashServerThread extends Thread {
	private Socket socket = null;
	private TashModel tm = null;
	PrintWriter out;
	BufferedReader in;
	boolean active;
	String username;
    public TashServerThread(Socket socket, TashModel tm) {
		super("TashServerThread");
		this.socket = socket;
		this.tm = tm;
		active = true;
    }
    public void run() {
		try {
		    out = new PrintWriter(socket.getOutputStream(), true);
		    in = new BufferedReader(
					    new InputStreamReader(
					    socket.getInputStream()));
		    String inputLine;
		    while (active) {
		    	if(username==null){
		    		username = in.readLine(); 
		    		//sendToClient("Welcome " + username + "!");
		    	}
		    	inputLine = in.readLine();
		    	//TODO problem: chat always send an empty line after each line
		    	if(inputLine.length() > 0)
		    		if(inputLine.substring(0, 1).equals("/"))
		    			specialCase(inputLine);
		    		else{
		    			tm.add(inputLine, username);
		    		}
		    }
		    close();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (NullPointerException e){
			if (out != null) //if the connection has been made once
				System.out.printf("Client %s from %s has exited the server\n", username, socket.getInetAddress().getHostName());
		}
    }
    private void specialCase(String inputLine) {
		// TODO Auto-generated method stub
		
	}
	public void setUsername(String _username){
    	username = _username;
    }
    public void close(){
    	active = false;
	    out.close();
	    try {
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    tm.printOut("THREAD: Client Exit Server");
	    tm.closeSocket(this);
    }
    public void sendToClient(String message){
    	if(active)
    		out.println(message);
    }
}
