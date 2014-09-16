package pong.tash;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;

public class Menu extends JFrame implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	private static int MENU_WIDTH = 400;
	private static int MENU_HEIGHT= 300;
	JButton enter;
	JTextField ip, username, port;
	TashController parent;
	JPanel ipPanel, usernamePanel, portPanel;
	public Menu(TashController _parent){
		super("Tash Menu");
		parent = _parent;
		
		getContentPane().setLayout(
			    new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		//username panel
		usernamePanel = new JPanel();
		JLabel usernamePrompt = new JLabel("Host IP Address:");
		username = new JTextField("<Your Username>");
		username.addKeyListener(this);
		usernamePanel.add(usernamePrompt, BorderLayout.CENTER);
		usernamePanel.add(username, BorderLayout.EAST);
		username.setAlignmentX(Component.CENTER_ALIGNMENT);
		getContentPane().add(usernamePanel);

		//ip panel
		ipPanel = new JPanel();
		JLabel ipPrompt = new JLabel("Host IP Address:");
		ip = new JTextField("<IP Address of Host>");
		ip.addKeyListener(this);
		ipPanel.add(ipPrompt, BorderLayout.CENTER);
		ipPanel.add(ip, BorderLayout.EAST);
		getContentPane().add(ipPanel);
		
		//port panel
		portPanel = new JPanel();
		JLabel portPrompt = new JLabel("Host IP Address:");
		port = new JTextField("<Port Number of Host>");
		port.addKeyListener(this);
		portPanel.add(portPrompt, BorderLayout.CENTER);
		portPanel.add(port, BorderLayout.EAST);
		getContentPane().add(portPanel);
		
		//submit
		enter = new JButton("Submit");
		enter.addActionListener(this);
		getContentPane().add(enter, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
		pack();
	}
	private void submit(String ip, String username, String port){
		try{
			parent.setUsername(username);
			parent.setIPAddress(ip);
			parent.setPort(Integer.parseInt(port));
			parent.active = true;
			System.exit(0);
		} catch (NumberFormatException e){
			System.err.println("Please enter valid elements.");
		}
	}
	@Override
	public void keyPressed(KeyEvent key) {
		if( key.getKeyChar() == KeyEvent.VK_ENTER){
			submit(ip.getText(), username.getText(), port.getText());
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == enter){
			submit(ip.getText(), username.getText(), port.getText());
		}
	}
}
