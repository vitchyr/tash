package pong.tash;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TashModel extends ArrayList<Message>{
	private static final long serialVersionUID = 1L;
	static final int PORT = 4441;
	ArrayList<Message> chatLog;
	ArrayList<TashServerThread> clients;
	int line;
	ServerSocket serverSocket;
	public TashModel(){
		chatLog = new ArrayList<Message>();
		clients = new ArrayList<TashServerThread>();
		line = 0;
	}
	public boolean add(String text, String sender){
		add(new Message(text, sender, line));
		line++;
		for(TashServerThread client : clients){
			client.sendToClient(sender + ": " + text);
		}
		return true;
	}
	public void run() throws IOException{
		serverSocket = null;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.printf("Could not listen on port: %d.\n", PORT);
            System.exit(-1);
        }

        while (listening){
        	TashServerThread client = new TashServerThread(serverSocket.accept(), this);
        	client.start();
        	clients.add(client);
        }
        serverSocket.close();
	}
	public void closeSocket(TashServerThread client){
		clients.get(clients.indexOf(client)).close();
		clients.remove(client);
	}
	public void close(){
		for(TashServerThread client: clients){
			client.close();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("MODEL: Server Exited");
	}
	//For debugging
	public void printOut(String text){
		System.out.println(text);
	}
	public static void main(String[] args) throws IOException{
		TashModel tm = new TashModel();
		tm.run();
	}
}
