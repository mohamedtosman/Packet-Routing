package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.IGP;
import model.IGP.ALGORITHM;
import model.Node;
import view.TopologyUIComp;
import model.UserMessage;
import view.DemoApp;
import view.LinkUIComp;
import view.LinkUICompList;
import view.NetworkTopologyPanel;
import view.NodeUIComp;
import view.PacketUserInputPanel;

public class Controller implements ActionListener, MouseListener, MouseMotionListener{
	private static DemoApp view;
	private int numNodes = 0;
	private NetworkTopologyPanel panel;
	private NodeUIComp pick;   // Node you have picked
	private boolean islinkMode = false;
	private boolean isEditMode = false;
	private NodeUIComp linkPick1 = null;
	private static PacketUserInputPanel packetUserInputPanel = new PacketUserInputPanel();
	private static String message = null;
	private static int numMsgs = 0;
	private static int srcId = 0;
	private static int destId = 0;
	private static int rate = 0;
	private static List<UserMessage> msgList = null;
	private static List<Node> nodes = new ArrayList<Node>();
	ArrayList<Integer> nodeX = new ArrayList<Integer>();
	ArrayList<Integer> nodeY = new ArrayList<Integer>();
	public static int packetStep = 0;
	private double totalMessages = 0.0;
	private double avgHops = 0.0;
	private boolean have_injected_msgs = false;
	private int step_session_id = 0;
	private ObjectMapper mapper;
	public boolean isDebugMode = false; //used to enable print statement for debugging
	/**
	 * Specify the timestamp for the last message being sent.
	 */
	private long rate_timestamp = 0;
	
	/**
	 * Specify the default routing algorithm for simulation.
	 */
	private static IGP.ALGORITHM routingAlgoMethod = IGP.ALGORITHM.RANDOM;	
	
	
	public ArrayDeque<Node> floodNodeStack = new ArrayDeque<Node>();	
	public ArrayDeque<Integer> step_session_id_Stack = new ArrayDeque<Integer>();
	public ArrayDeque<Integer> packetStepStack = new ArrayDeque<Integer>();	
	private ArrayDeque<Boolean> have_injected_msgs_Stack = new ArrayDeque<Boolean>();
	public static int packetsAtOneStep = 0;
	private ArrayDeque<Integer> packetsAtOneStep_Stack = new ArrayDeque<Integer>();
	
	
	public Controller (DemoApp view) {
		Controller.view = view;
		mapper = new ObjectMapper();
	}
	
	public int get_step_session_id () {
		return step_session_id;
	}
	
	public void actionPerformed( ActionEvent e )
	{
	       if(e.getActionCommand().equals("Create")) { //Create Menu Option
	    	   create();
	    	   view.getEditPanel().setVisible(true);
	    	   view.getSubmitPanel().setVisible(true);
	    	   isEditMode = false;
	    	   islinkMode = false;
	    	   view.getCreateItem().setEnabled(false);
	    	   view.getSaveItem().setEnabled(true);
	    	   view.getOpenItem().setEnabled(true);
			} else if (e.getActionCommand().equals("Save")) { //Add Node Option in Create Topology mode
        	    save();
           	} else if (e.getActionCommand().equals("Open")) { //Add Node Option in Create Topology mode
           		load();
	       	} else if (e.getActionCommand().equals("Add Node")) { //Add Node Option in Create Topology mode
	    	   isEditMode = true;
	    	   islinkMode = false;
	    	   createNode();
	       	} else if (e.getActionCommand().equals("Edit Links")) { //Edit Links Option in Create Topology mode
	    	   JOptionPane.showMessageDialog(view, "Connect Nodes by double clicking on first node and single click on node you wish to connect to");
	    	   isEditMode = true;
	    	   islinkMode = true;
	       	} else if (e.getActionCommand().equals("Move Nodes")) { //Move Nodes Option in Create Topology mode
	    	   JOptionPane.showMessageDialog(view, "Drag and drop nodes into the prefered arrangemnet");
	    	   isEditMode = true;
	    	   islinkMode = false;
	       	} else if (e.getActionCommand().equals("Start Over")) { //Start Over Option(clear everything) in Create Topology mode
	    	   isEditMode = false;
	    	   islinkMode = false;
	    	   startOver();
	       	} else if (e.getActionCommand().equals("Modify")) { //Go back to Create Topology mode
	    	   isEditMode = false;
	    	   islinkMode = false;
	    	   view.getEditPanel().setVisible(true);
	    	   view.getSubmitPanel().setVisible(true);
	    	   view.getSubmitPanel().setEnabled(true);
	       	} else if (e.getActionCommand().equals("Submit")) { //Submit Topology in Create Topology mode
	    	   isEditMode = false;
	    	   islinkMode = false;
	    	   submit();
	    	   view.getEditPanel().setVisible(false);
	    	   view.getSubmitPanel().setVisible(false);
	    	   view.getSubmitPanel().setEnabled(false);
	    	   view.getStartItem().setEnabled(true);
	    	   view.getModifyItem().setEnabled(true);
	       	} else if (e.getActionCommand().equals("Start")) { //Show Simulation configurations entry Panel
	    	   start();
	       	} else if (e.getActionCommand().equals("comboBoxChanged")) { //Set routing algorithm based on user's input
	    	   JComboBox cb = (JComboBox)e.getSource();
	    	   String algoMethod = (String)cb.getSelectedItem();
	    	   if(algoMethod.equals("RANDOM")) {
	    		   routingAlgoMethod = IGP.ALGORITHM.RANDOM;	    		   
	    	   } else if(algoMethod.equals("DEPTHFIRSTSEARCH")) {
	    		   routingAlgoMethod = IGP.ALGORITHM.DEPTHFIRSTSEARCH;	    		   
	    	   } else if(algoMethod.equals("BREADTHFIRSTSEARCH")) {
	    		   routingAlgoMethod = IGP.ALGORITHM.BREADTHFIRSTSEARCH;	    		   
	    	   } else if(algoMethod.equals("FLOODING")) {
	    		   routingAlgoMethod = IGP.ALGORITHM.FLOODING;	    		   
	    	   } 
	    	   
	    	   
	       	} else if (e.getActionCommand().equals("Enter")) { //submit Simulation configurations
	    	   view.getRunItem().setEnabled(true);
	    	   view.getClearItem().setEnabled(true);
	    	   view.getStepItem().setEnabled(true);
	    	   view.getStartItem().setEnabled(false);
	    	   if(!packetUserInputPanel.getMsgTxt().getText().isEmpty() && !packetUserInputPanel.getNumMsgs().getText().isEmpty() && !packetUserInputPanel.getRate().getText().isEmpty()) {
	    		   if(checkUserInput()){
		    		   try {
		    			   enter();
		    			   packetUserInputPanel.setMsgField("");
		    			   packetUserInputPanel.setNumMsgsField("");
		    			   packetUserInputPanel.setRateField("");
		    			   packetUserInputPanel.dispose();
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		   }
	    		   else{
	    			   JOptionPane.showMessageDialog(packetUserInputPanel, "Invalid input!");
	    		   }
	    	   }
	    	   else {
	    		   JOptionPane.showMessageDialog(packetUserInputPanel, "One or more field is empty!");
	    	   }
	       } else if (e.getActionCommand().equals("Run")){ //run Simulation
	    	   try {
	    		   view.getRunItem().setEnabled(false);
	    		   view.getStepItem().setEnabled(false);
	    		   run();				
	    	   } catch (NumberFormatException | InterruptedException e1) {
	    		   e1.printStackTrace();
	    	   }
	    	   
	       } else if (e.getActionCommand().equals("Clear")){ //clear Simulation drawings and return to clean topology image
	    	   try {
	    		   clear();	
		    	   view.getStartItem().setEnabled(true);
	    	   } catch (NumberFormatException | InterruptedException e1) {
	    		   e1.printStackTrace();
	    	   }
	    	   
	       } else if (e.getActionCommand().equals("Step")){ //step through Simulation
	    	   try {
	    		   view.getRunItem().setEnabled(false);
	    		   view.getUndoItem().setEnabled(true);
				step();
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	  	       	   
	       }else if (e.getActionCommand().equals("Undo")){
	    	   stepBack();
	       }
	       
	}
	
	/**
	 * Create the Topology creation panel
	 */
	public void create() {
		panel = new NetworkTopologyPanel(view);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		view.add(panel,BorderLayout.CENTER);        
		view.setPanel(panel);
	}
	
	/**
	 * save GUI nodes and links of the topology to a json file
	 * dialogue input box to take filename from user
	 * checks if the filename already exists and offer the user the option to overwrite it
	 */
	public void save() {
		NodeUIComp nodes[] = panel.getNodes();
		LinkUIComp links[] =  panel.getLinks();
		
		List<TopologyUIComp> topologyUIComps = new ArrayList<TopologyUIComp>();
		for (int i=0 ;i<numNodes; i++){
			TopologyUIComp topologyUIComp = new TopologyUIComp();
			topologyUIComp.setId(nodes[i].getId());
			topologyUIComp.setX(nodes[i].getX());
			topologyUIComp.setY(nodes[i].getY());
			topologyUIComp.setNodeLabel(nodes[i].getNodeLabel());
			topologyUIComp.setAdj(nodes[i].getAdj());
			topologyUIComp.setType("node");
			topologyUIComps.add(topologyUIComp);
		} 
		
		for (int i=0 ;i<panel.getNumLinks(); i++){
			TopologyUIComp topologyUIComp = new TopologyUIComp();
			topologyUIComp.setTo(links[i].getTo());
			topologyUIComp.setFrom(links[i].getFrom());
			topologyUIComp.setType("link");
			topologyUIComps.add(topologyUIComp);
		}
		
		String filename = JOptionPane.showInputDialog(view,"Please Input the toplogy Json file name (e.g. topology1.json)","Save", 3);
		if (filename !=null) {
			File file = new File(filename);
			boolean exists = file.exists();
			if (exists) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "This file already exists. Would you like to overwrite it?","Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.NO_OPTION){
					return;
				}
			}

			try {
				mapper.writeValue(new File(filename), topologyUIComps);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * alert user that all changes that are not saved will be lost and offer the user the option to proceed
	 * load GUI nodes and links of the topology from a json file
	 * dialogue input box to take filename from user
	 * alert user if file does not exists
	 */
	public void load() {
		 List<NodeUIComp> nodes = new ArrayList<NodeUIComp>();
		 List<LinkUIComp> links = new ArrayList<LinkUIComp>();
		 List<TopologyUIComp> topologyUIComps = null;
		 
		 int dialogResult = JOptionPane.showConfirmDialog (null, "All changes made will be lost if you have not save your current topology. Would you like to proceed ?","Warning",JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.NO_OPTION){
				return;
			}
			
		 String filename = JOptionPane.showInputDialog(view,"Please Input the toplogy Json file name (e.g. topology1.json)","Load",3);
		 if (filename!=null) {
			 File file = new File(filename);
			 boolean exists = file.exists();
		  	 if (!exists) {
		  		 JOptionPane.showMessageDialog(view, "This file does not exist");
		  		 return;
			 }
			 
			 try {
				 topologyUIComps = mapper.readValue(new File(filename), new TypeReference<List<TopologyUIComp>>(){});
			 } catch (JsonParseException e) {
				 e.printStackTrace();
			 } catch (JsonMappingException e) {
				 e.printStackTrace();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
			 
			 for(TopologyUIComp topologyUIComp : topologyUIComps) {
				 if(topologyUIComp.getType().equals("node")) {
					 NodeUIComp n = new NodeUIComp();
					 n.setAdj(topologyUIComp.getAdj());
					 n.setColor(Color.cyan);
					 n.setId(topologyUIComp.getId());
					 n.setNodeLabel(topologyUIComp.getNodeLabel());
					 n.setX(topologyUIComp.getX());
					 n.setY(topologyUIComp.getY());
					 nodes.add(n);
				 } else if (topologyUIComp.getType().equals("link")) {
					 LinkUIComp l = new LinkUIComp(topologyUIComp.getFrom(), topologyUIComp.getTo());
					 links.add(l);
				 }
			 }
			 
			 panel.setLinks(links);
			 panel.setNodes(nodes);
			 numNodes = nodes.size();
		     panel.repaint();
		     
    	     view.getEditPanel().setVisible(true);
    	     view.getSubmitPanel().setVisible(true);
    	     isEditMode = true;
    	     islinkMode = false;
    	     view.getCreateItem().setEnabled(false);
    	     view.getSaveItem().setEnabled(true);
    	     view.getOpenItem().setEnabled(true);
		 }
	}
	
	/**
	 * Displays a view that prompts the user to enter
	 * information about the messages to be sent.
	 */
	public void start() {
		packetUserInputPanel.createView();
	}
	
	/**
	 * Saves the values (message, number of messages, rate) into
	 * sharable variables and generates a random source and destination
	 * ID then creates the list of messages to be later sent.
	 */
	public void enter(){
		Random randomGenerator = new Random();
		
		nodes = getNodes();
		message = packetUserInputPanel.getMsgTxt().getText();
		numMsgs = Integer.parseInt(packetUserInputPanel.getNumMsgs().getText());
		rate = Integer.parseInt(packetUserInputPanel.getRate().getText());
		srcId = randomGenerator.nextInt((nodes.size() - 1) + 1) + 1;
		destId = generateDestinationId(srcId);
		JOptionPane.showMessageDialog(view, "Source Node:  " + srcId + "\nDestination Node:  " + destId, "Random Generated Nodes", JOptionPane.INFORMATION_MESSAGE);
		msgList = createMessage(Controller.message, Controller.numMsgs, Controller.srcId, Controller.destId);
	}
	
	/**
	 * A mode of simulation where the messages are sent 
	 * automatically without user intervention. It then 
	 * calculates total number of messages and average 
	 * hops then displays them to the user.
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public void run() throws NumberFormatException, InterruptedException{		
		sendMessage(Controller.nodes, Controller.msgList, Controller.rate);
		totalMessages = getTotalNumMessages(Controller.nodes);
		avgHops = getAverageHops(totalMessages);
		printResult(totalMessages, avgHops);
	}
	
	/**
	 * A mode of simulation where the messages are sent 
	 * automatically without user intervention. This function
	 * clear the path of message passing when user click "Clear".
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public void clear() throws NumberFormatException, InterruptedException{
		panel.clearPacketSimulation();
		resetTotalNumMessages(Controller.nodes);
		nodes.removeAll(nodes);
		msgList.removeAll(msgList);
		packetStep = 0;
		have_injected_msgs = false;
	}
	
	/**
	 * Another mode of simulation where the messages
	 * travels from one node to another node and stops
	 * for the user to click on "Step" again.
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public void step() throws NumberFormatException, InterruptedException{	
		sendStepMessage(Controller.nodes, Controller.msgList);
	}
	
	/**
	 * Submit the topology created by the user to IGP
	 */
	public void submit() {
		nodeX.clear();
		nodeY.clear();
		NodeUIComp nodes[] = panel.getNodes();
		IGP.topology.clear();
		
		List<Node> modelNodes = new ArrayList<Node>();
		for (int i=0 ;i<numNodes; i++){
			modelNodes.add(new Node(nodes[i].getId()));
		}
		
		for (int i=0 ;i<numNodes; i++){
			List<Node> neighbours = new ArrayList<Node>();
			if (nodes[i] != null) {
				if (nodes[i].getAdj() != null) {
					LinkUICompList adj = nodes[i].getAdj();
					while (adj != null) {
						if(nodes[i].getId() == nodes[adj.getLink().getFrom()].getId()) {
							if(!neighbours.contains(new Node(nodes[adj.getLink().getTo()].getId()))) {
								for (Node node : modelNodes) {
									if(node.getId() == nodes[adj.getLink().getTo()].getId()) {
										neighbours.add(node);	
									}
								}
							}
						} else {
							if(!neighbours.contains(new Node(nodes[adj.getLink().getFrom()].getId()))) {
								for (Node node : modelNodes) {
									if(node.getId() == nodes[adj.getLink().getFrom()].getId()) {
										neighbours.add(node);	
									}
								}
							}
						}
						adj = adj.getNext();
					}
					IGP.topology.put(modelNodes.get(i), neighbours);;
				}
				setXCoord((int) nodes[i].getX());
				setYCoord((int) nodes[i].getY());
			}
		}
		IGP.bootup_nodes();
		Map<Node, List<Node>> topology = IGP.topology;
		Set<Node> topology_nodes = topology.keySet();
		for (Node node : topology_nodes) {
			node.setNeighbours(topology.get(node));
			if(isDebugMode){
				System.out.println("ParentNode:" + node.getId());
				for (Node neighbour : topology.get(node)) {
					System.out.println("NeighbourNode:" + neighbour.getId());
				}
			}
		}
	}
	
	/**
	 * Clear all GUI nodes and links components
	 */
	public void startOver() {
		panel.clearAllNodes();
		panel.clearAllLinks();
        numNodes = 0;
        panel.repaint();
	}
	
	/**
	 * Clear a new node component
	 */
	public void createNode() {
		numNodes++;
        panel.addNode("N"+ numNodes);
        view.setPanel(panel); //update
	}

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// Simulation Implementation                          														   ////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Uses the srcId parameter to generate a destination ID
	 * that does not match the source ID.
	 * 
	 * @param srcId
	 * @return destination ID
	 */
	public int generateDestinationId(int srcId){
		Random randomGenerator = new Random();
		destId = randomGenerator.nextInt((nodes.size() - 1) + 1) + 1;
		while(srcId == destId){
			generateDestinationId(srcId);
		}
		return destId;
	}
	
	/**
	 * Receives IDs of current node and next node
	 * and finds their coordinates,
	 * then calls drawPacketSimulation() to simulate
	 * the packet moving through the network.
	 * @param currNodeId ID of current node
	 * @param nextNodeId ID of node we heading to
	 * @param numPackets current total of steps the message has travelled
	 * @throws InterruptedException
	 */
	public void curAndNextNodeId(int currNodeId, int nextNodeId, int numPackets) throws InterruptedException {
		int xCurr = nodeX.get(currNodeId-1);
		int yCurr = nodeY.get(currNodeId-1);
		
		int xNext = nodeX.get(nextNodeId-1);
		int yNext= nodeY.get(nextNodeId-1);

		panel.drawPacketSimulation(xCurr, yCurr, xNext, yNext, numPackets);
		
	}
	
	/**
	 * Returns the x coordinate of a given node
	 * @param index
	 * @return x coordinate
	 */
	public int getXCoord(int index){
		return nodeX.get(index);
	}
	
	/**
	 * Sets the x coordinate of a given node
	 * @param x coordinate
	 */
	public void setXCoord(int x){
		nodeX.add(x);
	}
	
	/**
	 * Returns the y coordinate of a given node
	 * @param index
	 * @return y coordinate
	 */
	public int getYCoord(int index){
		return nodeY.get(index);
	}
	
	/**
	 * Sets the y coordinate of a given node
	 * @param y coordinate
	 */
	public void setYCoord(int y){
		nodeY.add(y);
	}

	/**
	 * Generates a list of all the nodes created
	 * by the user.
	 * @return list of nodes
	 */
	private List<Node> getNodes(){
		List<Node> nodes = new ArrayList<Node>();
		
		for(Map.Entry <Node, List<Node>> entry : IGP.topology.entrySet()){
			nodes.add(entry.getKey());
		}
		
		return nodes;		
	}
	
	/**
	 * Parses user input and checks if Message is text and not integer.
	 * Checks if number of messages is an integer greater than 0 and if rate is
	 * an integer.
	 * @return true if input is proper and false if not
	 */
	public boolean checkUserInput(){
		if(!isInteger(packetUserInputPanel.getMsgTxt().getText()) && isInteger(packetUserInputPanel.getNumMsgs().getText()) && isInteger(packetUserInputPanel.getRate().getText())){
			if(Integer.parseInt(packetUserInputPanel.getNumMsgs().getText()) > 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates a set of messages saved into a list
	 * using the user's inputs.
	 * @param message message entered by the user
	 * @param numMsgs number of messages to be sent
	 * @param srcId randomly generated source node ID
	 * @param destId randomly generated destination node ID
	 * @param rate how many seconds to wait between each message
	 * @return list of all created messages
	 */
	private static List<UserMessage> createMessage(String message, int numMsgs, int srcId, int destId){
		List<UserMessage> msgList = new ArrayList<UserMessage>();
				
		while (numMsgs > 0) {
			msgList.add(new UserMessage(numMsgs));
			numMsgs --;
		}		
		
		for (UserMessage msg : msgList) {
			msg.setSrcId(srcId);
			msg.setDestId(destId);
			msg.setText(message);
		}
		return msgList;
	}
	
	/**
	 * Uses the list of nodes and messages to find the path
	 * of each node to reach its destination.
	 * @param nodes list containing all nodes
	 * @param msgList list containing all messages
	 * @param rate seconds to wait between each message
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	private void sendMessage(List<Node> nodes, List<UserMessage> msgList, int rate) throws NumberFormatException, InterruptedException{
		Node srcNode = null;
		for (UserMessage msg : msgList) {
			for (Node node : nodes) {
				if (node.getId() ==  msg.getSrcId()) {
					srcNode = node;
					break;
				}
			}
			if(routingAlgoMethod == IGP.ALGORITHM.FLOODING){
				msg.setTTL(nodes.size());
			}
			srcNode.recieveForward(msg, this, routingAlgoMethod);
			Thread.sleep(rate*1000);
		}
	}
	
	public void stepBack() {
		
		if (routingAlgoMethod.equals(ALGORITHM.FLOODING)) {			
			Boolean local_have_injected_msgs =  have_injected_msgs_Stack.pollFirst();
			
			if(!local_have_injected_msgs.booleanValue()){
				for (Node node : nodes) {
					if(!node.getMessage_buffer().isEmpty()){
						node.getMessage_buffer().removeAll(node.getMessage_buffer());
					}				
				}				
				for (UserMessage msg : msgList) {
					if(!msg.getNodePathStack().isEmpty()){
						msg.getNodePathStack().removeAll(msg.getNodePathStack());
					}
					if(!msg.getSession_id_stack().isEmpty()) {
						msg.getSession_id_stack().removeAll(msg.getSession_id_stack());
					}
					msg.set_session_id(0);
					msg.setCurrentNode(null);
				}
				
				floodNodeStack.removeAll(floodNodeStack);
				step_session_id_Stack.removeAll(step_session_id_Stack);
				step_session_id = 0;
				packetStepStack.removeAll(packetStepStack);
				packetStep = 0;
				have_injected_msgs_Stack.removeAll(have_injected_msgs_Stack);
				have_injected_msgs = false;	
				
				for (Node node : nodes) {
					node.setNumPackets(0);
				}				
				
				try {
					panel.clearPacketSimulation();
					panel.redrawLines();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
				view.getStepItem().setEnabled(true);				
		
			} else {				
				step_session_id = step_session_id_Stack.removeFirst();

				Node preNode = floodNodeStack.removeFirst();
				UserMessage msg = preNode.getMsgStack().removeFirst();
				preNode.getMessage_buffer().addFirst(msg);

				int msg_session_id = msg.getSession_id_stack().removeFirst().intValue();

				msg.set_session_id(msg_session_id);
				int prePacketStep = packetStepStack.removeFirst().intValue();

				int numFlooding = packetStep - prePacketStep;

				for (Node node : preNode.getNeighbours()) {
					if (!node.getMessage_buffer().isEmpty()) {
						node.getMessage_buffer().removeLast();
					}
					node.setNumPackets(node.getNumPackets() - 1);
				}
				packetStep = prePacketStep;
				panel.clearFloodingLines(numFlooding);
				try {
					panel.redrawLines();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				view.getStepItem().setEnabled(true);
			}
			
		} else {
			Boolean local_have_injected_msgs =  have_injected_msgs_Stack.pollFirst();
	
			if(!local_have_injected_msgs.booleanValue()){
				for (Node node : nodes) {
					if(!node.getMessage_buffer().isEmpty()){
						node.getMessage_buffer().removeAll(node.getMessage_buffer());
					}				
				}
				
				for (UserMessage msg : msgList) {
					if(!msg.getNodePathStack().isEmpty()){
						msg.getNodePathStack().removeAll(msg.getNodePathStack());
					}
					if(!msg.getSession_id_stack().isEmpty()) {
						msg.getSession_id_stack().removeAll(msg.getSession_id_stack());
					}
					msg.set_session_id(0);
					msg.setCurrentNode(null);
				}
				
				step_session_id_Stack.removeAll(step_session_id_Stack);
				step_session_id = 0;
				packetStepStack.removeAll(packetStepStack);
				packetStep = 0;
				have_injected_msgs_Stack.removeAll(have_injected_msgs_Stack);
				have_injected_msgs = false;
				
				for (Node node : nodes) {
					if (!(node.getNumPacketStack().isEmpty())) {
						node.setNumPackets(node.getNumPacketStack().removeFirst().intValue());;
					}
				}						
	
				panel.clearLinesAtOneStep();
				try {
					panel.redrawLines();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
				view.getStepItem().setEnabled(true);
			} else {				
				step_session_id = step_session_id_Stack.removeFirst();				
				for (UserMessage msg : msgList) {
					int msg_session_id = msg.getSession_id_stack().removeFirst().intValue();
					if (msg_session_id != step_session_id) {
						
						msg.set_session_id(msg_session_id);
						
						msg.getCurrentNode().getMessage_buffer().remove(msg);
						Node currentNode = msg.getNodePathStack().removeFirst();
						currentNode.getMessage_buffer().addFirst(msg);
						msg.setCurrentNode(currentNode);
						
						packetStep = packetStepStack.removeFirst().intValue();
						
						for (Node node : nodes) {
							if (!(node.getNumPacketStack().isEmpty())) {
								node.setNumPackets(node.getNumPacketStack().removeFirst().intValue());;
							}
						}	
						
						panel.clearLinesAtOneStep();
						try {
							panel.redrawLines();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						view.getStepItem().setEnabled(true);
						
					} 					
				}
			}
		}		

	}
	
	
	/**
	 * Uses the list of nodes and messages to step
	 * through the topology one node at a time.
	 * @param nodes list containing all nodes
	 * @param msgList list containing all messages
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	private void sendStepMessage(List<Node> nodes, List<UserMessage> msgList) throws NumberFormatException, InterruptedException{
		Node srcNode = null;
		boolean is_simulation_completed = true;
		
		if (!have_injected_msgs){
			packetsAtOneStep = 0;
			for (UserMessage msg : msgList) {
				for (Node node : nodes) {
					if (node.getId() ==  msg.getSrcId()) {
						srcNode = node;
						break;
					}
				}
				
				if (routingAlgoMethod.equals(ALGORITHM.FLOODING)) {
					msg.setTTL(nodes.size());
				}
				
				srcNode.oneStepReceiveForwared(msg, this, true, routingAlgoMethod);	
				
				
				Thread.sleep(rate*1000);
			}

			packetsAtOneStep_Stack.addFirst(new Integer(packetsAtOneStep));
			
			have_injected_msgs_Stack.addFirst(new Boolean(have_injected_msgs));
			//step_session_id_Stack.addFirst(new Integer(step_session_id));
			
			
			have_injected_msgs = true;
			is_simulation_completed = false;
		} else {
			
			have_injected_msgs_Stack.addFirst(new Boolean(have_injected_msgs));
			step_session_id_Stack.addFirst(new Integer(step_session_id));
			
			for (Node node : nodes) {
				packetsAtOneStep = 0;
				if (routingAlgoMethod.equals(ALGORITHM.FLOODING)) {
					
					
					//packetStepStack.addFirst(packetStep);
					
					if (!node.oneStepFlood(null, this, false)) {					
						is_simulation_completed = false;
						
						
						
						
						
						
					}
				} else if (!node.oneStepReceiveForwared(null, this, false, routingAlgoMethod)) {

					packetsAtOneStep_Stack.addFirst(new Integer(packetsAtOneStep));
					is_simulation_completed = false;
				}
			}
			step_session_id = (step_session_id ==0)?1:0;
		}
		
		if(is_simulation_completed){
			view.getStepItem().setEnabled(false);
			totalMessages = getTotalNumMessages(Controller.nodes);
			avgHops = getAverageHops(totalMessages);
			printResult(totalMessages, avgHops);
		}	
	}
	
	/**
	 * Checks if string is an integer.
	 * @param s
	 * @return true if an integer, false if not
	 */
	private static boolean isInteger(String s) {
		if (s == null)
			return false;
		
		try{
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Calculates total of packet transmissions through
	 * the network.
	 * @param nodes list containing all nodes
	 * @return total packet transmitted
	 */
	private static int getTotalNumMessages(List<Node> nodes){
		int total = 0;
		for(Node node : nodes){
			total+=node.getNumPackets();
		}
		return total;
	}
	
	private static void resetTotalNumMessages(List<Node> nodes){
		for(Node node : nodes){
			node.resetNumPackets();
		}
	}
	
	/**
	 * Uses total packet transmissions and divides by
	 * the number of messages sent to find the average
	 * number of hops.
	 * @param total
	 * @return
	 */
	private static double getAverageHops(double total){
		return total/msgList.size();
	}
	
	/**
	 * Displays to the user totale number of messages
	 * and average number of hops.
	 * @param totalMessages
	 * @param avgHops
	 */
	private static void printResult(double totalMessages, double avgHops){
		String[] routingAlgoString = {"RANDOM", "DEPTHFIRSTSEARCH", "BREADTHFIRSTSEARCH", "FLOODING"};
		JOptionPane.showMessageDialog(view, "\nSimulation has been completed using the routing algorithm " + routingAlgoString[routingAlgoMethod.ordinal()] + "\n"
				+ "\nThe total number of packets transmitted is " + String.format("%.2f", totalMessages)
				+ "\nThe average number of hops each message goes through is " + String.format("%.2f", avgHops), "Results", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void messageDroppedNotification(String message){
		JOptionPane.showMessageDialog(view, message);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// MouseListener and MouseMotionListener                            											   ////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(isEditMode) {
			if(!islinkMode) {
				MoveNodeGUI(e);
			} else {
				linkNodesGUI(e);
			}
		}	
	}
	
	/**
	 * Mouse pressed listener for move movement mode
	 * @param e: MouseEvent
	 */
	public void MoveNodeGUI(MouseEvent e) {
		double bestdist = Double.MAX_VALUE;  // Distance between mouse click and closest node
        for (int i = 0; i < numNodes; i++) {
            NodeUIComp n = panel.getNodes()[i];
            double dist = (n.getX() - e.getX()) * (n.getX() - e.getX()) + (n.getY() - e.getY()) * (n.getY() - e.getY());
            if (dist < bestdist) {
                pick = n;
                bestdist = dist;
            }
        }
        pick.setX(e.getX()); 
        pick.setY(e.getY());
        panel.repaint();
	}
	
	/**
	 * Mouse pressed listener for linking nodes mode
	 * @param e: MouseEvent
	 */
	public void linkNodesGUI(MouseEvent e) {
		if(e.getClickCount() == 1) {
        	if (linkPick1 != null) {
	        	NodeUIComp linkPick2 = null;
	        	double bestdist = Double.MAX_VALUE;  // Distance between mouse click and closest node
		        for (int i = 0; i < numNodes; i++) {
		            NodeUIComp n = panel.getNodes()[i];
		            double dist = (n.getX() - e.getX()) * (n.getX() - e.getX()) + (n.getY() - e.getY()) * (n.getY() - e.getY());
		            if (dist < bestdist) {
		            	linkPick2 = n;
		                bestdist = dist;
		            }
		        }
		        if(linkPick2 != null && linkPick2.getId() != linkPick1.getId()) {
			        panel.addLink(linkPick1.getNodeLabel(), linkPick2.getNodeLabel());
		        }
		        linkPick1.setColor(Color.CYAN);
		        panel.repaint();
		        linkPick1 = null;
        	}
		} else if (e.getClickCount() == 2) {
			double bestdist = Double.MAX_VALUE;
	        for (int i = 0; i < numNodes; i++) {
	            NodeUIComp n = panel.getNodes()[i];
	            double dist = (n.getX() - e.getX()) * (n.getX() - e.getX()) + (n.getY() - e.getY()) * (n.getY() - e.getY());
	            if (dist < bestdist) {
	            	linkPick1 = n;
	                bestdist = dist;
	            }
	        }
	        linkPick1.setColor(Color.YELLOW);
	        panel.repaint();
		}
	}
	
	/**
	 * mouse released listener used to finalize node position after end of dragging is done around the GUI
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if(isEditMode) {
			if(!islinkMode) {
				pick.setX(e.getX()); 
		        pick.setY(e.getY());    
		        pick = null;
		        panel.repaint();
			}
		}
	}
	
	
	/**
	 * mouse dragged listener used to drag node around the GUI
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if(isEditMode) {
			if(!islinkMode) {
				pick.setX(e.getX()); 
		        pick.setY(e.getY());
		        panel.repaint();	
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
