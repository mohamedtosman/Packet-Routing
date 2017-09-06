package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PacketUserInputPanel extends JFrame
{
	JPanel jp = new JPanel(new MigLayout("fillx"));
	
	JLabel msgTxt = new JLabel("Message: ");
	JLabel numMsgs = new JLabel("Number of Messages: ");
	
	//To be used in later milestones
	//JLabel srcId = new JLabel("Source Node ID: ");
	//JLabel destId= new JLabel("Destination Node ID: ");
	
	JLabel rate = new JLabel("Rate in seconds: ");
	
    JTextField msgTxtField = new JTextField(20);   
	JTextField numMsgsField = new JTextField(20);
	//To be used in later milestones
	//JTextField srcIdField = new JTextField(20);
	//JTextField destIdField = new JTextField(20);
	JTextField rateField = new JTextField(20);
       
	JButton jb = new JButton("Enter");
	
	Controller controller;

	String[] routingAlgoString = { "RANDOM", "DEPTHFIRSTSEARCH", "BREADTHFIRSTSEARCH", "FLOODING" };
	JComboBox<String> routingAlgoList = new JComboBox<>(routingAlgoString);
	JLabel algoLabel = new JLabel("Algorithm: ");
	
	public PacketUserInputPanel(){
		controller = new Controller(null);
		jb.addActionListener(controller);

		routingAlgoList.setSelectedIndex(0);
		routingAlgoList.addActionListener(controller);
    }

       /**
        * Generates a view that prompts the user to enter Message, 
        * number of messages, and rate in seconds.
        */
       public void createView()
       {
    	   setTitle("Message Info");
    	   setVisible(true);
    	   setSize(400, 200);
    	   setDefaultCloseOperation(HIDE_ON_CLOSE);
    	   
    	   jp.add(msgTxt, "alignx trailing");
    	   jp.add(msgTxtField, "alignx leading, wrap");
    	   
    	   jp.add(numMsgs, "alignx trailing");
    	   jp.add(numMsgsField, "alignx leading, wrap");
    	   
    	   //To be used in later milestones
    	   //jp.add(srcId, "alignx trailing");
    	   //jp.add(srcIdField, "alignx leading, wrap");
    	   
    	   //To be used in later milestones
    	   //jp.add(destId, "alignx trailing");
    	   //jp.add(destIdField, "alignx leading, wrap");
    	   
    	   jp.add(rate, "alignx trailing");
    	   jp.add(rateField, "alignx leading, wrap");   	   
    	   
    	   // added a Combo Box
    	   jp.add(algoLabel, "alignx trailing");
    	   jp.add(routingAlgoList, "alignx leading, wrap");  
    	   
    	   jp.add(jb, "spanx, growx");
    	   
    	   setContentPane(jp);
    	   setLocationRelativeTo(null);
    	   pack();
    	   
       }
       
       /**
        * Sets text field message
        * @param msg
        */
       public void setMsgField(String msg){
    	   msgTxtField.setText(msg);
       }
       
       /**
        * Returns the JTextField containing the message
        * the user entered.
        * @return JTextfield containing the message the user entered.
        */
       public JTextField getMsgTxt() {
    	   return msgTxtField;
       }
       
       /**
        * Set number of messages field
        * @param numMsgs
        */
       public void setNumMsgsField(String numMsgs){
    	   numMsgsField.setText(numMsgs);
       }
       
       /**
        * Returns the JTextField containing the number of messages
        * the user entered.
        * @return JTextfield containing the number of message 
        * the user entered.
        */
       public JTextField getNumMsgs(){
    	   return numMsgsField;
       }
       
       //To be used in later milestones
       /*public JTextField getSrcId(){
    	   return srcIdField;
       }*/
       
       //To be used in later milestones
       /*public JTextField getDestId(){
    	   return destIdField;
       }*/
       
       /**
        * Set rate field
        * @param rate
        */
       public void setRateField(String rate){
    	   rateField.setText(rate);
       }
       
       /**
        * Returns the JTextField containing the rate
        * the user entered.
        * @return JTextfield containing the rate the user entered.
        */
       public JTextField getRate(){
    	   return rateField;
       }

}