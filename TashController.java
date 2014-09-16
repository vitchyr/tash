package pong.tash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TashController extends Thread implements WindowListener, ActionListener, KeyListener{
	TashView tv;
	Socket socket;
	PrintWriter socketOut;
	BufferedReader socketIn;
	boolean active;
	String username, ip;
	int port;
	public TashController(){
		//active = false;
		//Open Menu
		//Menu menu = new Menu(this);
		//menu.setVisible(true);
		//while(!active)
			//active = false;
		//Open user GUI
		try {
			ip = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		port = TashModel.PORT;
		tv = new TashView();
		tv.exit.addActionListener(this);
		tv.open.addActionListener(this);
		tv.selectAll.addActionListener(this);
		tv.setUsername.addActionListener(this);
		tv.in.addKeyListener(this);
		tv.in.setText("Please Enter Your Username");
		tv.setVisible(true);	
		tv.addWindowListener(this);
		
		//Connect to server
		socket = null;
	    socketOut = null;
	    socketIn = null;
	    try {
	    	//Enter Server information here:
	    	//TODO have InetAddress host = InetAddress.getLocalHost();
	        socket = new Socket(ip, port);
	        socketOut = new PrintWriter(socket.getOutputStream(), true);
	        socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        this.start();
	    } catch (UnknownHostException e) {
	        System.err.printf("Don't know about host: LocalHost.\n");
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to: LocalHost.");
	        System.exit(1);
	    }
	}
	public void run(){
        //Read from the server
		String fromServer;
        try {
			while (true){
				if (Thread.interrupted()) {
				    throw new InterruptedException();
				}
				fromServer = socketIn.readLine();
				addTextToOut(fromServer);
			}
		} catch (InterruptedException e){ //TODO fix this -- it's never called when
			System.out.println("Interrupted!"); //close() is called
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addTextToOut(String message){
		tv.out.append(message + "\n");
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == tv.exit){
			close();
		}
		else if(ae.getSource() == tv.open)
			return;
		else if(ae.getSource() == tv.selectAll)
			tv.out.selectAll();
		else if(ae.getSource() == tv.setUsername){
			promptUsernameChange();
		}
	}
	private void promptUsernameChange() {
		Prompter prompt = new Prompter(this, "New Username");
		prompt.setVisible(true);
	}
	public void setUsername(String _username){
		username = _username;
	}
	public void setIPAddress(String _ip){
		ip = _ip;
	}
	public void setPort(int _port){
		port = _port;
	}
	
	//Listener Methods:
	@Override
	public void keyPressed(KeyEvent key) {
		//Do Nothing
	}
	@Override
	public void keyReleased(KeyEvent key) {
		if( key.getKeyChar() == KeyEvent.VK_ENTER){	
			if(!key.isShiftDown()){ //allow a new line with shift + enter
				String text = tv.in.getText();
				tv.in.setText("");
				//valid check because of short circuiting
				if(text.length() > 4 && text.substring(0,5).equals("/exit"))
					close();
				else{
					socketOut.println(text);
					tv.resetChatBox();
				}
			} else {
				tv.in.setText(tv.in.getText() + "\n");
				tv.growChatBox();
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent key) {
		//Do Nothing
	}
	public void close(){
		try {
			interrupt();
			socketOut.close();
	        socketIn.close();
	        socket.close();
	        System.out.println("CONTROLLER: Connection Closed");
	        System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		@SuppressWarnings("unused")
		TashController tc = new TashController();
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		close();
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
