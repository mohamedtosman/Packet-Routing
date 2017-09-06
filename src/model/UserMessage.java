package model;

import java.util.ArrayDeque;

public class UserMessage {
	private int id;
	private String text;
	private int srcId;
	private int desId;
	private int ttl = 3;
	/**
	 * Specify the forwarding session_id of the controller.
	 */
	private int session_id;
	private Node currentNode = null;
	private ArrayDeque<Node> nodePathStack = new ArrayDeque<Node>();
	private ArrayDeque<Integer> session_id_stack = new ArrayDeque<Integer>();
	
	
	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public ArrayDeque<Node> getNodePathStack() {
		return nodePathStack;
	}

	public void setNodePathStack(ArrayDeque<Node> nodePathStack) {
		this.nodePathStack = nodePathStack;
	}

	public ArrayDeque<Integer> getSession_id_stack() {
		return session_id_stack;
	}

	public void setSession_id_stack(ArrayDeque<Integer> session_id_stack) {
		this.session_id_stack = session_id_stack;
	}

	
	
	/**
	 * Construct a message to be sent.
	 * @param id
	 * @param text
	 * @param srcId
	 * @param desId
	 */
	public UserMessage(int id, String text, int srcId, int desId) {
		this.id = id;
		this.text = text;
		this.srcId = srcId;
		this.desId = desId;
		session_id = 0;
	}
	
	/*
	 * setTTL() : sets message time to live
	 */
	public void setTTL (int timeToLive) {
		ttl = timeToLive;
	}
	
	/*
	 * getTTL() : get message time to live.
	 */
	public int getTTL() {
		return ttl;
	}
	
	/*
	 * decrementTTL() : decrements message time to live by 1
	 */
	public void decrementTTL () {
		ttl--;
	}
	
	/**
	 * getter for the session_id.
	 * @return return the session_id.
	 */
	public int get_session_id() {
		return session_id;
	}
	
	/**
	 * Setter for the session_id.
	 * @param session_id.
	 */
	public void set_session_id(int session_id) {
		this.session_id = session_id;
	}	
	
	public UserMessage(int id){
		this.id = id;
	}
	
	public int getMsgId() {
		return this.id;
	}
	
	public void setSrcId(int id) {
		this.srcId = id;
	}
		
	public int getSrcId() {
		return srcId;
	}

	public void setDestId(int id) {
		this.desId = id;
	}
	
	public int getDesId() {
		return desId;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

	public String toString() {
		return "id: " + id + "; text: " + text + "; srcId: " + srcId + "; desId: " + desId; 
	}
}