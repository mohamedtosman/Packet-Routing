package view;

import java.awt.Color;

import view.LinkUICompList;

public class TopologyUIComp {
	private int id;
	private double x;  // x coordinate of node in drawing
	private double y;  // y coordinate of node in drawing
	private String nodeLabel;  //name
	private LinkUICompList adj;  // all links linked to Node
	
	private int from;   // endpoint of start node
	private int to;     // endpoint of end node
	private String type;
	
	public TopologyUIComp() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
