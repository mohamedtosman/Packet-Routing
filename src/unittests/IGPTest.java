package unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.IGP;
import model.Node;

public class IGPTest {
	private IGP igp;
	private Node node1;
	private Node node2;
	private Node node3;
	private Node node4;
	
	@Before
	public void setUp() throws Exception {
		igp = new IGP();
		node1 = new Node(1);
		node2 = new Node(2);
		node3 = new Node(3);
		node4 = new Node(4);
	}

	@After
	public void tearDown() throws Exception {
		igp = null;
	}

	@Test
	public void testBootup_nodes() {
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		igp.topology.put(node1, n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		igp.topology.put(node2, n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		igp.topology.put(node3, n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		igp.topology.put(node4, n4);	
		
		igp.generateDepthFirstSearchTable();
		
		igp.bootup_nodes();
		
		assertEquals(n, node1.getNeighbours());
		assertEquals(n2, node2.getNeighbours());
		assertEquals(n3, node3.getNeighbours());
		assertEquals(n4, node4.getNeighbours());
		
	}
	
	@Test
	public void testgenerateBreadthFirstSearchTable(){
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		igp.topology.put(node1, n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		igp.topology.put(node2, n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		igp.topology.put(node3, n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		igp.topology.put(node4, n4);
		
		igp.generateBreadthFirstSearchTable();
		
		// Check RoutingTable for node1
		assertEquals(node1, node1.getRoutingTable().get(1));
		assertEquals(node2, node1.getRoutingTable().get(2));
		assertEquals(node2, node1.getRoutingTable().get(3));
		assertEquals(node4, node1.getRoutingTable().get(4));
		
		// Check RoutingTable for node2
		assertEquals(node1, node2.getRoutingTable().get(1));
		assertEquals(node2, node2.getRoutingTable().get(2));
		assertEquals(node3, node2.getRoutingTable().get(3));
		assertEquals(node1, node2.getRoutingTable().get(4));
		
		// Check RoutingTable for node3
		assertEquals(node2, node3.getRoutingTable().get(1));
		assertEquals(node2, node3.getRoutingTable().get(2));
		assertEquals(node3, node3.getRoutingTable().get(3));
		assertEquals(node4, node3.getRoutingTable().get(4));
		
		// Check RoutingTable for node4
		assertEquals(node1, node4.getRoutingTable().get(1));
		assertEquals(node1, node4.getRoutingTable().get(2));
		assertEquals(node3, node4.getRoutingTable().get(3));
		assertEquals(node4, node4.getRoutingTable().get(4));
	}
	
	@Test
	public void testgenerateDepthFirstSearchTable(){
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		igp.topology.put(node1, n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		igp.topology.put(node2, n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		igp.topology.put(node3, n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		igp.topology.put(node4, n4);
		
		igp.generateDepthFirstSearchTable();
		
		// Check RoutingTable for node1
		assertEquals(node1, node1.getRoutingTable().get(1));
		assertEquals(node2, node1.getRoutingTable().get(2));
		assertEquals(node2, node1.getRoutingTable().get(3));
		assertEquals(node4, node1.getRoutingTable().get(4));
		
		// Check RoutingTable for node2
		assertEquals(node1, node2.getRoutingTable().get(1));
		assertEquals(node2, node2.getRoutingTable().get(2));
		assertEquals(node3, node2.getRoutingTable().get(3));
		assertEquals(node1, node2.getRoutingTable().get(4));
		
		// Check RoutingTable for node3
		assertEquals(node2, node3.getRoutingTable().get(1));
		assertEquals(node2, node3.getRoutingTable().get(2));
		assertEquals(node3, node3.getRoutingTable().get(3));
		assertEquals(node4, node3.getRoutingTable().get(4));
		
		// Check RoutingTable for node4
		assertEquals(node1, node4.getRoutingTable().get(1));
		assertEquals(node1, node4.getRoutingTable().get(2));
		assertEquals(node3, node4.getRoutingTable().get(3));
		assertEquals(node4, node4.getRoutingTable().get(4));
	}

	@Test
	public void testGenerateRandomRoutingTable() {
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		igp.topology.put(node1, n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		igp.topology.put(node2, n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		igp.topology.put(node3, n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		igp.topology.put(node4, n4);	
		
		igp.generateDepthFirstSearchTable();
		
		// Check RoutingTable for node1
		assertEquals(node1, node1.getRoutingTable().get(1));
		assertEquals(node2, node1.getRoutingTable().get(2));
		assertEquals(node2, node1.getRoutingTable().get(3));
		assertEquals(node4, node1.getRoutingTable().get(4));
		
		// Check RoutingTable for node2
		assertEquals(node1, node2.getRoutingTable().get(1));
		assertEquals(node2, node2.getRoutingTable().get(2));
		assertEquals(node3, node2.getRoutingTable().get(3));
		assertEquals(node1, node2.getRoutingTable().get(4));
		
		// Check RoutingTable for node3
		assertEquals(node2, node3.getRoutingTable().get(1));
		assertEquals(node2, node3.getRoutingTable().get(2));
		assertEquals(node3, node3.getRoutingTable().get(3));
		assertEquals(node4, node3.getRoutingTable().get(4));
		
		// Check RoutingTable for node4
		assertEquals(node1, node4.getRoutingTable().get(1));
		assertEquals(node1, node4.getRoutingTable().get(2));
		assertEquals(node3, node4.getRoutingTable().get(3));
		assertEquals(node4, node4.getRoutingTable().get(4));
	}
	
	@Test
	public void testGetNextNodeBS(){
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		igp.topology.put(node1, n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		igp.topology.put(node2, n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		igp.topology.put(node3, n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		igp.topology.put(node4, n4);	
		
		Node testNode =  igp.getNextNodeBS(node1, node4, null, null);
		assertTrue(node4.equals(testNode));
		
		Node testNode2 =  igp.getNextNodeBS(node1, node3, null, null);
		assertTrue(node2.equals(testNode2));		
		
		Node testNode3 =  igp.getNextNodeBS(node2, node4, null, null);
		assertTrue(node1.equals(testNode3));
	}

	@Test
	public void testGetNextNodeDS() {
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		igp.topology.put(node1, n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		igp.topology.put(node2, n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		igp.topology.put(node3, n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		igp.topology.put(node4, n4);	
		
		Node testNode =  igp.getNextNodeDS(node1, node4, null);
		assertTrue(node4.equals(testNode));
		
		Node testNode2 =  igp.getNextNodeDS(node1, node3, null);
		assertTrue(node2.equals(testNode2));		
		
		Node testNode3 =  igp.getNextNodeDS(node2, node4, null);
		assertTrue(node1.equals(testNode3));
		
	}
	


}
