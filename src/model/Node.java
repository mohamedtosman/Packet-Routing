package model;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Random;

import controller.Controller;
import model.IGP.ALGORITHM;

public class Node {
	private int id;
	private Map<Integer,Node> routingTable;
	private List<Node> neighbours = null;
	private int numPackets;
	public void setNumPackets(int numPackets) {
		this.numPackets = numPackets;
	}

	private ArrayDeque<UserMessage> message_buffer;

	private ArrayDeque<UserMessage> msgStack = new ArrayDeque<UserMessage>();
	private ArrayDeque<Integer> numPacketStack = new ArrayDeque<Integer>();
	
	public ArrayDeque<UserMessage> getMsgStack() {
		return msgStack;
	}

	public ArrayDeque<Integer> getNumPacketStack() {
		return numPacketStack;
	}

	public void setNumPacketStack(ArrayDeque<Integer> numPacketStack) {
		this.numPacketStack = numPacketStack;
	}

	public void setMsgStack(ArrayDeque<UserMessage> msgStack) {
		this.msgStack = msgStack;
	}
	
	
	public ArrayDeque<UserMessage> getMessage_buffer() {
		return message_buffer;
	}

	public void setMessage_buffer(ArrayDeque<UserMessage> message_buffer) {
		this.message_buffer = message_buffer;
	}

	public Node(int id) {
		this.id = id;
		numPackets = 0;
		message_buffer = new ArrayDeque<UserMessage>();
	}
	
	public void setNeighbours (List<Node> n) {
		neighbours = n;
	}
	
	/**
	 * forwards received message to destination node using the an algorithm
	 * @param Optional message to be processed.
	 * @param Controller calling this method
	 * @param Algorithm to be using for this message
	 */
	public void recieveForward(UserMessage msg, Controller controller, ALGORITHM algorithm) throws InterruptedException {		
		if (this.getId() != msg.getDesId()) {
			Node nextNode = null;
			//forward message to the next node
			switch (algorithm) {
				case RANDOM:
					nextNode= getRandomNextNode();
					break;
				case DEPTHFIRSTSEARCH:
					IGP.generateDepthFirstSearchTable();
					nextNode= routingTable.get(msg.getDesId());
					break;			
				case BREADTHFIRSTSEARCH:
					IGP.generateBreadthFirstSearchTable();
					nextNode= routingTable.get(msg.getDesId());
					break;
				case FLOODING:
					floodMessage(msg, controller);
					return;
				default:
					throw new UnsupportedOperationException("Unsupported algorith: "+ algorithm);
			}
			if(controller.isDebugMode){
				System.out.println("\nMESSAGE" + msg.getMsgId() + "\nNode" + id + ":Message received and forwarded to Node" + nextNode.getId());
			}
			numPackets++;
			Controller.packetStep++;

			controller.curAndNextNodeId(this.id, nextNode.id, Controller.packetStep);
			nextNode.recieveForward(msg, controller, algorithm);
		}
		else{
			if(controller.isDebugMode){
				System.out.println("Node" + id + ":Message received by destination Node!! Message is: " + msg.getText());

			}
		}
	}
	
	/**
	 * forwards received message to destination node using the flooding algorithm.
	 * @param Optional message to be processed.
	 * @param Controller calling this method
	 */
	public void floodMessage(UserMessage msg, Controller controller) throws InterruptedException {
		if (this.getId() != msg.getDesId()) {
			msg.decrementTTL();
			
			if (msg.getTTL() > 0) {
				if(controller.isDebugMode){
					System.out.println("\nMESSAGE" + msg.getMsgId() + "received in Node:" + id);
				}
				for (Node node : neighbours) {
					if(controller.isDebugMode){
						System.out.println("Flooding the message to node: " + node.getId());
					}
					numPackets++;
					Controller.packetStep++;
					controller.curAndNextNodeId(this.id, node.id, Controller.packetStep);
					node.floodMessage(msg, controller);
				}
			}
			else {
				controller.messageDroppedNotification("\nMessage" + msg.getMsgId() + " dropped. TTL = 0");
				if(controller.isDebugMode){
					System.out.println("\nMESSAGE" + msg.getMsgId() + " dropped. TTL = 0");
				}
			}
		} 
		else  {
			if(controller.isDebugMode){
				System.out.println("Node" + id + ":Message received by destination Node!! Message is: " + msg.getText());
			}
		}
	}
	

	/**
	 * one step flood of the message
	 * @param Optional message to be processed.
	 * @param Controller calling this method
	 * @param Specify whether the message is injected newly injected or not.
	 */
	public boolean oneStepFlood(UserMessage msg, Controller controller, boolean isInjected) throws InterruptedException {
		UserMessage next_polled_msg = null;
		ArrayDeque<UserMessage> filtred_msgs = new ArrayDeque<UserMessage>();
		
		if (msg == null && message_buffer.isEmpty()) {
			return true;
		}
		
		while (!message_buffer.isEmpty()) {
				
			next_polled_msg = message_buffer.pollFirst();
			if (next_polled_msg.get_session_id() == controller.get_step_session_id()) {
				filtred_msgs.addLast(next_polled_msg);
				continue;
			}
			
			msgStack.addFirst(next_polled_msg);
			next_polled_msg.getSession_id_stack().addFirst(next_polled_msg.get_session_id());
			controller.packetStepStack.addFirst(new Integer(controller.packetStep));			
			controller.floodNodeStack.addFirst(this);
			
			for (Node node : neighbours) {
				if(controller.isDebugMode){
					System.out.println("\nMESSAGE" + next_polled_msg.getMsgId() + "\nNode" + id + ":Message forwarded to Node" + node.getId());
				}
				numPackets++;
				Controller.packetStep++;
				controller.curAndNextNodeId(this.id, node.id, Controller.packetStep);
				node.oneStepFlood(next_polled_msg, controller, false);
			}
		}
		
		while(!filtred_msgs.isEmpty()) {
			message_buffer.addLast(filtred_msgs.pollFirst());
		}
		
		if (msg!= null ) {
			if (this.getId() != msg.getDesId()) {
				msg.decrementTTL();
				if (msg.getTTL() <= 0) {
					controller.messageDroppedNotification("\nMessage" + msg.getMsgId() + " dropped. TTL = 0");
					if(controller.isDebugMode){
						System.out.println("\nMESSAGE" + msg.getMsgId() + " dropped. TTL = 0");
					}
				} else if (isInjected) {
					
					msgStack.addFirst(msg);	
					msg.getSession_id_stack().addFirst(msg.get_session_id());
					controller.packetStepStack.addFirst(new Integer(controller.packetStep));
			
					for (Node node : neighbours) {
						numPackets++;
						Controller.packetStep++;
						controller.curAndNextNodeId(this.id, node.id, Controller.packetStep);
						node.oneStepFlood(msg, controller, false);
					}
				} else {
					msg.set_session_id(controller.get_step_session_id());
					if(controller.isDebugMode){
						System.out.println("\nMESSAGE" + msg.getMsgId() + "\nNode" + id + ":Message received");
					}
					message_buffer.addLast(msg);
				}
			}
		}
		
		return false;
	}
	

	/**
	 * Send the messages previously stored to the next node and forward/store
	 *		the received message depending on isInjected value.
	 * @param Optional message to be processed.
	 * @param Controller calling this method
	 * @param Specify whether the message is injected newly injected or not.
	 */
	public boolean oneStepReceiveForwared(UserMessage msg, Controller controller, boolean isInjected, ALGORITHM algorithm) throws InterruptedException {
		Node nextNode = null;
		UserMessage next_polled_msg = null;
		ArrayDeque<UserMessage> filtred_msgs = new ArrayDeque<UserMessage>();
		
		if (msg == null && message_buffer.isEmpty()) {
			return true;
		}
		
		while (!message_buffer.isEmpty()) {
			next_polled_msg = message_buffer.pollFirst();
			
  		    switch (algorithm) {
				case RANDOM:
					nextNode= getRandomNextNode();
					break;
				case DEPTHFIRSTSEARCH:
					IGP.generateDepthFirstSearchTable();
					nextNode= routingTable.get(next_polled_msg.getDesId());
					break;			
				case BREADTHFIRSTSEARCH:
					IGP.generateBreadthFirstSearchTable();
					nextNode= routingTable.get(next_polled_msg.getDesId());
					break;
				case FLOODING:
					return oneStepFlood(next_polled_msg, controller, isInjected);
				default:
					throw new UnsupportedOperationException("Unsupported algorith: "+ algorithm);
			}
			
			if (next_polled_msg.get_session_id() == controller.get_step_session_id()) {
				filtred_msgs.addLast(next_polled_msg);
				continue;
			}
			
			next_polled_msg.getNodePathStack().addFirst(this);
			next_polled_msg.getSession_id_stack().addFirst(next_polled_msg.get_session_id());
			controller.packetStepStack.addFirst(new Integer(controller.packetStep));
			numPacketStack.addFirst(new Integer(numPackets));
			
			Controller.packetStep++;
			Controller.packetsAtOneStep++;
			controller.curAndNextNodeId(this.id, nextNode.id, Controller.packetStep);			
			if(controller.isDebugMode){
				System.out.println("\nMESSAGE" + next_polled_msg.getMsgId() + "\nNode" + id + ":Message forwarded to Node" + nextNode.getId());
			}
			nextNode.oneStepReceiveForwared(next_polled_msg, controller, false, algorithm);
			numPackets++;
		}
		
		while(!filtred_msgs.isEmpty()) {
			message_buffer.addLast(filtred_msgs.pollFirst());
		}
		
		if (msg!= null ) {
			if (this.getId() != msg.getDesId()) {
				if (isInjected) {
					switch (algorithm) {
						case RANDOM:
							nextNode= getRandomNextNode();
							break;
						case DEPTHFIRSTSEARCH:
							IGP.generateDepthFirstSearchTable();
							nextNode= routingTable.get(msg.getDesId());
							break;			
						case BREADTHFIRSTSEARCH:
							IGP.generateBreadthFirstSearchTable();
							nextNode= routingTable.get(msg.getDesId());
							break;
						case FLOODING:
							return oneStepFlood(msg, controller, isInjected);
						default:
							throw new UnsupportedOperationException("Unsupported algorith: "+ algorithm);
					}
					
					msg.getNodePathStack().addFirst(this);
					msg.getSession_id_stack().addFirst(msg.get_session_id());
					controller.packetStepStack.addFirst(new Integer(controller.packetStep));
					numPacketStack.addFirst(new Integer(numPackets));
				
					Controller.packetStep++;
					Controller.packetsAtOneStep++;
					controller.curAndNextNodeId(this.id, nextNode.id, Controller.packetStep);
					numPackets++;					
					nextNode.oneStepReceiveForwared(msg, controller, false, algorithm);
				} else {
					msg.set_session_id(controller.get_step_session_id());
					if(controller.isDebugMode){
						System.out.println("\nMESSAGE" + msg.getMsgId() + "\nNode" + id + ":Message received");
					}
					message_buffer.addLast(msg);
					msg.setCurrentNode(this);
					
				}
			}
			else{
				if(controller.isDebugMode){
					System.out.println("Node" + id + ":Message received by destination Node!! Message is: " + msg.getText());
				}
			}
		}
		
		return false;
	}
	
	public Node getRandomNextNode() {
		Random randomGenerator = new Random();
		return neighbours.get(randomGenerator.nextInt(neighbours.size()));
	}
	
	public List<Node> getNeighbours(){
		return neighbours;
	}
	
	public void setRoutingTable(Map<Integer,Node> routingTable) {
		this.routingTable = routingTable;
	}
	
	public Map<Integer,Node> getRoutingTable() {
		return routingTable;
	}
	
	
	public int getId() {
		return id;
	}
	
	public int getNumPackets() {
		return numPackets;
	}
	
	/**
	 * Resets the total number of transmissions
	 * packets has gone through each node.
	 */
	public void resetNumPackets(){
		numPackets = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getId() == ((Node) obj).getId();
	}
}
