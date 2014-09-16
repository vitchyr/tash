package pong.tash;

public class Message {
	private String text;
	private String sender;
	private int line;
	public Message(String _text, String _sender, int _line){
		text = _text;
		sender = _sender;
		line = _line;
	}
	public String getText(){
		return text;
	}
	public String getSender(){
		return sender;
	}
	public int getLine(){
		return line;
	}
}
