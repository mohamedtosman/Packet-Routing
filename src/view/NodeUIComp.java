package view;
import java.awt.Color;

public class NodeUIComp {
	private int id;
	private Color color;
	private double x;  // x coordinate of node in drawing
	private double y;  // y coordinate of node in drawing
	private String nodeLabel;  //name
	private LinkUICompList adj;  // all links linked to Node
	
	public NodeUIComp() {
		
	}
			
	/**
	 * Create a new GUI Node component
	 * @param id: id of the node
	 * @param color: color of the node
	 * @param x: x postion of the node
	 * @param y: y position of the node
	 * @param nodeLabel: label on the node
	 * @param adj: list of link connecting this node
	 */
	public NodeUIComp(int id, Color color, double x, double y, String nodeLabel, LinkUICompList adj) {
		this.id = id;
		this.color = color;
		this.x = x;
		this.y = y;
		this.nodeLabel = nodeLabel;
		this.adj = adj;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getNodeLabel() {
		return nodeLabel;
	}
	public void setNodeLabel(String nodeLabel) {
		this.nodeLabel = nodeLabel;
	}
	public LinkUICompList getAdj() {
		return adj;
	}
	public void setAdj(LinkUICompList adj) {
		this.adj = adj;
	}
	
	
}
