package unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.Controller;
import model.Node;
import model.UserMessage;
import model.IGP.ALGORITHM;
import view.DemoApp;

public class NodeTest {
	private Node node1;
	private Node node2;
	private Node node3;
	private Node node4;
	private Controller controller;

	@Before
	public void setUp() throws Exception {
		node1 = new Node(1);
		node2 = new Node(2);
		node3 = new Node(3);
		node4 = new Node(4);
		controller = new TestController(new DemoApp());
	}

	@After
	public void tearDown() throws Exception {
		node1 = null;
		node2 = null;
		node3 = null;
		node4 = null;
	}
	
	@Test
	public void testgetNeighbours(){
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		assertEquals(n, node1.getNeighbours());
	}

	@Test
	public void testSetNeighbours() {
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		assertEquals(n, node1.getNeighbours());
	}

	@Test
	public void testRecieveForward() {
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		
		UserMessage msg = new UserMessage(1, "message1", 1, 4);
		try {
			node1.recieveForward(msg, controller, ALGORITHM.RANDOM);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(4, msg.getDesId());
		
	}
	
	@Test
	public void testfloodMessage() {
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		
		UserMessage msg = new UserMessage(1, "message1", 1, 4);
		try {
			node1.floodMessage(msg, controller);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(4, msg.getDesId());
	}

	@Test
	public void testoneStepFlood(){
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		
		UserMessage msg = new UserMessage(1, "message1", 1, 4);
		try {
			node1.oneStepFlood(msg, controller, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(4, msg.getDesId());
	}
	
	@Test
	public void testoneStepReceiveForwared(){
		List<Node> n = new ArrayList<Node>();
		n.add(node2);
		n.add(node4);
		node1.setNeighbours(n);
		
		List<Node> n2 = new ArrayList<Node>();
		n2.add(node1);
		n2.add(node3);
		node2.setNeighbours(n2);
		
		List<Node> n3 = new ArrayList<Node>();
		n3.add(node2);
		n3.add(node4);
		node3.setNeighbours(n3);
		
		List<Node> n4 = new ArrayList<Node>();
		n4.add(node1);
		n4.add(node3);
		node4.setNeighbours(n4);
		
		UserMessage msg = new UserMessage(1, "message1", 1, 4);
		try {
			node1.oneStepFlood(msg, controller, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(4, msg.getDesId());
	}
	
	@Test
	public void testgetRoutingTable() {
		Map<Integer,Node> rt_node1 = new HashMap<Integer,Node>();
		rt_node1.put(2, node2);
		rt_node1.put(3, node2);
		rt_node1.put(4, node4);
		node1.setRoutingTable(rt_node1);
		assertEquals(rt_node1, node1.getRoutingTable());
		
	}

	@Test
	public void testSetRoutingTable() {
		Map<Integer,Node> rt_node1 = new HashMap<Integer,Node>();
		rt_node1.put(2, node2);
		rt_node1.put(3, node2);
		rt_node1.put(4, node4);
		node1.setRoutingTable(rt_node1);
		assertEquals(rt_node1, node1.getRoutingTable());
		
	}

	@Test
	public void testGetId() {
		assertEquals(1, node1.getId());
		assertEquals(2, node2.getId());
		assertEquals(3, node3.getId());
		assertEquals(4, node4.getId());
	}

	@Test
	public void testGetNumPackets() {
		assertEquals(0, node1.getNumPackets());
		assertEquals(0, node2.getNumPackets());
		assertEquals(0, node3.getNumPackets());
		assertEquals(0, node4.getNumPackets());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(node1.equals(node1));
		assertTrue(node2.equals(node2));
		assertTrue(node3.equals(node3));
		assertTrue(node4.equals(node4));
	}

}
