package pong.tash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Prompter extends JFrame implements ActionListener, KeyListener{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int PROMPTER_WIDTH = 400;
	private static int PROMPTER_HEIGHT = 100;
	JTextField input;
	JButton enter;
	TashController parent;
	public Prompter(TashController _parent, String text){
		super("Username Change");
		parent = _parent;
		JLabel prompt = new JLabel(text);
		getContentPane().add(prompt, BorderLayout.NORTH);
		input = new JTextField();
		input.addKeyListener(this);
		getContentPane().add(input, BorderLayout.CENTER);
		
		enter = new JButton("Enter");
		enter.addActionListener(this);
		getContentPane().add(enter, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(PROMPTER_WIDTH, PROMPTER_HEIGHT));
		pack();
	}
	private void submit(String text){
		parent.setUsername(text);
		System.exit(0);
	}
	@Override
	public void keyPressed(KeyEvent key) {
		if( key.getKeyChar() == KeyEvent.VK_ENTER){
			submit(input.getText());
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
			submit(input.getText());
		}
	}
}
