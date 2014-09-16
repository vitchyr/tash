package pong.tash;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TashView extends JFrame{
	private static final long serialVersionUID = 1L;
	private static int INIT_WORLD_WIDTH = 400;
	private static int INIT_WORLD_HEIGHT = 600;
	private  int chatBoxWidth, chatBoxHeight;
	JTextArea out;
	JEditorPane in;
	private JMenu fileMenu, editMenu;
	private JMenuBar menuBar;
	JMenuItem open, exit, selectAll, setUsername;
	JScrollPane outScroll, inScroll;
	int lines = 0;
	public TashView(){
		super("Tash");
		
		//Making the Menu
		menuBar = new JMenuBar();
		//File Menu
		fileMenu = new JMenu("File");
		open = new JMenuItem("Open...");
		exit = new JMenuItem("Exit");
		fileMenu.add(open);
		fileMenu.add(exit);
		menuBar.add(fileMenu);
		//Edit Menu
		editMenu = new JMenu("Edit");
		selectAll = new JMenuItem("Select All");
		setUsername = new JMenuItem("Change Username");
		editMenu.add(selectAll);
		editMenu.add(setUsername);
		menuBar.add(editMenu);
		setJMenuBar(menuBar);
		
		//Interface
		out = new JTextArea();
        //StyledDocument doc = out.getStyledDocument();
        //outPanel = new JTextField(out);
        //addStylesToDocument(doc);
		out.setEditable(false);
		out.setLineWrap(true);
		outScroll = new JScrollPane(out, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(outScroll, BorderLayout.CENTER);
		in = new JEditorPane();
		inScroll = new JScrollPane(in, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatBoxHeight = 2 * inScroll.getFontMetrics(inScroll.getFont()).getHeight();
		chatBoxWidth = inScroll.getWidth();
		inScroll.setPreferredSize(new Dimension(chatBoxWidth, chatBoxHeight));
		getContentPane().add(inScroll, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(INIT_WORLD_WIDTH, INIT_WORLD_HEIGHT));
		pack();
	}
	public void growChatBox(){
		if(lines < 4){
			inScroll.setPreferredSize(new Dimension(chatBoxWidth, inScroll.getHeight() + inScroll.getFontMetrics(inScroll.getFont()).getHeight()));
			lines++;
			validate();
		}
	}
	public void resetChatBox(){
		inScroll.setPreferredSize(new Dimension(chatBoxWidth, chatBoxHeight));
		lines = 0;
		validate();
	}
}
