package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class IGP {
	/**
	 * topology: Specify the current topology simulated.
	 */
	public static Map<Node, List<Node>> topology = new HashMap<Node, List<Node>>();
	
	/**
	 * Boot up the node.
	 */
	public static void bootup_nodes() {
		Set<Node> topology_nodes = topology.keySet();
		for (Node node : topology_nodes) {
			node.setNeighbours(topology.get(node));
		}
	}
	
	public static enum ALGORITHM{
		RANDOM,
		DEPTHFIRSTSEARCH,
		BREADTHFIRSTSEARCH,
		FLOODING
	}
	
	/**
	 * generateDepthFirstSeatchTable: generate routing table using depth first search for each
	 * 							node in the topology.
	 */
	public static void generateDepthFirstSearchTable() {
		Set<Node> topology_nodes = topology.keySet();
		
		for (Node node: topology_nodes) {
			Map<Integer, Node> routing_table = new HashMap<Integer, Node> ();
				
			for (Node hop: topology_nodes) {				
				routing_table.put(hop.getId(), getNextNodeDS(node, hop, null));	
			}
			
			node.setRoutingTable(routing_table);
		}
	}
	
	/**
	 * generateBreadthFirstSeatchTable: generate routing table using breath first search for each
	 * 							node in the topology.
	 */
	public static void generateBreadthFirstSearchTable() {
		Set<Node> topology_nodes = topology.keySet();
		
		for (Node node: topology_nodes) {
			Map<Integer, Node> routing_table = new HashMap<Integer, Node> ();
				
			for (Node hop: topology_nodes) {
				routing_table.put(hop.getId(), getNextNodeBS(node, hop, null, null));	
			}
			
			node.setRoutingTable(routing_table);
		}
			
		
	}
	
	/**
	 * Gets the next node to send the message to if using Breath first search
	 * 
	 * @param source node of the message
	 * @param final destination node
	 * @param Used to track the list of visited node during the search
	 * @return The next node to send the message to.
	 */
	public static Node getNextNodeBS(Node src, Node dest, List<Node> visited_nodes, Queue<Node> queue) {
		 List<Node> src_adj_nodes = new ArrayList<Node>();
		 src_adj_nodes.addAll(topology.get(src));
		 
		 if (src.getId() == dest.getId()) {
				return src;
		 }
		 
		 if (src_adj_nodes.contains(dest)) {
				return dest;
		 }
		 
		 if (queue == null) {
			 queue = new LinkedList<Node>();
		 }
		 
		 if (visited_nodes == null) {
				visited_nodes = new ArrayList<Node>();
		 } else {
			 src_adj_nodes.removeAll(visited_nodes); 
		 }
		 
		 visited_nodes.add(src);
		 
		 while (!queue.isEmpty()) {
			 Node nextNode = queue.poll();
			 if (topology.get(nextNode).contains(dest)) {
				 return nextNode;
			 }
		 }
		 
		 for (Node node : src_adj_nodes) {
			 queue.add(node);
		 }
		 
		 for (Node node : src_adj_nodes) {
			 Node ret_node = null;
			 if ((ret_node = getNextNodeBS(node, dest, visited_nodes, queue)) != null) {
				 if (src_adj_nodes.contains(ret_node))
					 return ret_node;
				 return node;
			 }
		 }
		 
		 
		 
		
		 
		 return null;
	 }
	
	/**
	 * Gets the next node to send the message to if using Depth first search
	 * 
	 * @param source node of the message
	 * @param final destination node
	 * @param Used to track the list of visited node during the search
	 * @return The next node to send the message to.
	 */
	 public static Node getNextNodeDS(Node src, Node dest, List<Node> visited_nodes) {
		List<Node> src_adj_nodes = new ArrayList<Node>();
		src_adj_nodes.addAll(topology.get(src));
		Node next_node = null;
		
		if (src.getId() == dest.getId()) {
			return src;
		}
		
		if (src_adj_nodes.contains(dest)) {
			return dest;
		}
		
		if (visited_nodes == null) {
			visited_nodes = new ArrayList<Node>();
		} else {
			src_adj_nodes.removeAll(visited_nodes);
			if (src_adj_nodes.isEmpty())
				return null;
		}
		
		visited_nodes.add(src);
		
		for (Node hop: src_adj_nodes) {
			next_node = getNextNodeDS(hop, dest, visited_nodes);
			if (next_node != null) {
				return hop;
			}
		}
		
		return null;
			
	}
}