package view;
import java.awt.Color;

public class LinkUIComp {
	private int from;   // endpoint of start node
	private int to;     // endpoint of end node

	public LinkUIComp() {
    }
	
	/**
	 * Creates a link between 2 Nodes
	 * @param from
	 * @param to
	 */
	public LinkUIComp(int from, int to) {
    	this.from = from; 
    	this.to = to;
    }
    
    public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}
	
}
