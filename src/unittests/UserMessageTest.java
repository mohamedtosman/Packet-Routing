package unittests;

import static org.junit.Assert.*;

import java.nio.channels.MembershipKey;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.UserMessage;

public class UserMessageTest {
	private UserMessage message;
	private UserMessage message2;
	
	@Before
	public void setUp() throws Exception {
		message = new UserMessage(1, "Message 1", 2, 3);
		message2 = new UserMessage(2);
	}

	@After
	public void tearDown() throws Exception {
		message = null;
		message2 = null;
	}
	
	@Test
	public void testsetTTL(){
		message.setTTL(1);
		assertEquals(1, message.getTTL());
	}
	
	@Test
	public void testgetTTL(){
		message.setTTL(1);
		assertEquals(1, message.getTTL());
	}
	
	@Test
	public void testdecrementTTL(){
		message.setTTL(1);
		message.decrementTTL();
		assertEquals(0, message.getTTL());
	}
	
	@Test
	public void testget_session_id(){
		assertEquals(0, message.get_session_id());
	}
	
	@Test
	public void testset_session_id(){
		message.set_session_id(1);
		assertEquals(1, message.get_session_id());
	}

	@Test
	public void testUserMessageIntStringIntInt() {
		assertTrue(Integer.class.isInstance(message.getMsgId()));
		assertTrue(String.class.isInstance(message.getText()));
		assertTrue(Integer.class.isInstance(message.getSrcId()));
		assertTrue(Integer.class.isInstance(message.getDesId()));
	}

	@Test
	public void testUserMessageInt() {
		assertTrue(Integer.class.isInstance(message2.getMsgId()));
	}

	@Test
	public void testGetMsgId() {
		assertEquals(1, message.getMsgId());
	}

	@Test
	public void testSetSrcId() {
		assertEquals(2, message.getSrcId());
		message.setSrcId(111);
		assertEquals(111, message.getSrcId());
	}

	@Test
	public void testGetSrcId() {
		assertEquals(2, message.getSrcId());
	}

	@Test
	public void testSetDestId() {
		assertEquals(3, message.getDesId());
		message.setDestId(222);
		assertEquals(222, message.getDesId());
	}

	@Test
	public void testGetDesId() {
		assertEquals(3, message.getDesId());
	}

	@Test
	public void testSetText() {
		assertEquals("Message 1", message.getText());
		message.setText("Test setText");
		assertEquals("Test setText", message.getText());
	}

	@Test
	public void testGetText() {
		assertEquals("Message 1", message.getText());
	}

	@Test
	public void testToString() {

		String expectedStr = "id: 1; text: Message 1; srcId: 2; desId: 3";
		assertEquals(expectedStr, message.toString());
	}

}
