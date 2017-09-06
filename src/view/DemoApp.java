package view;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Image;
import java.awt.Panel;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.Controller;


@SuppressWarnings("serial")
public class DemoApp extends JApplet {
	private Controller controller;
	private JMenuItem createItem;
	private JMenuItem saveItem;
	private JMenuItem openItem;
	private JMenuItem startItem;
	private JMenuItem modifyItem;
	private JMenuItem runItem;
	private JMenuItem clearItem;
	private JMenuItem stepItem;
	private JMenuItem undoItem;
	private NetworkTopologyPanel panel;
	private JButton jbbEdit;
	private Panel editPanel;
	private Panel submitPanel;
	private JMenu runMenu;
	private JMenu stepMenu;
	private JMenu undoMenu;
	
	public DemoApp() {
    	controller = new Controller(this);
    }
	
	public JMenuItem getSaveItem() {
		return saveItem;
	}

	public JMenuItem getOpenItem() {
		return openItem;
	}
	
	public JMenu getRunMenu() {
		return runMenu;
	}

	public JMenu getStepMenu() {
		return stepMenu;
	}

	public JMenu getUndoMenu() {
		return undoMenu;
	}

	public JMenuItem getCreateItem() {
		return createItem;
	}

	public void setCreateItem(JMenuItem createItem) {
		this.createItem = createItem;
	}

	public JMenuItem getModifyItem() {
		return modifyItem;
	}

	public void setModifyItem(JMenuItem modifyItem) {
		this.modifyItem = modifyItem;
	}

	public JMenuItem getStartItem() {
		return startItem;
	}

	public void setStartItem(JMenuItem startItem) {
		this.startItem = startItem;
	}

	public void setSubmitPanel(Panel submitPanel) {
		this.submitPanel = submitPanel;
	}

	public Panel getEditPanel() {
		return editPanel;
	}

	public Panel getSubmitPanel() {
		return submitPanel;
	}
	
    public JButton getJbbEdit() {
		return jbbEdit;
	}

	public void setJbbEdit(JButton jbbEdit) {
		this.jbbEdit = jbbEdit;
	}
    
    public NetworkTopologyPanel getPanel() {
		return panel;
	}

	public void setPanel(NetworkTopologyPanel panel) {
		this.panel = panel;
	}
	
	public JMenuItem getRunItem() {
		return runItem;
	}
	
	public JMenuItem getClearItem() {
		return clearItem;
	}
	
	public JMenuItem getStepItem() {
		return stepItem;
	}
	
	public JMenuItem getUndoItem() {
		return undoItem;
	}

	public void createMenuBar() {
    	JMenuBar menuBar = new JMenuBar( );
    	
        JMenu topologyMenu = new JMenu( "Topology" );
        menuBar.add(topologyMenu);
        createItem = new JMenuItem ( "Create" );
        createItem.addActionListener(controller);
        topologyMenu.add(createItem);
        createItem.setEnabled(true);
        
        openItem = new JMenuItem ( "Open" );
        openItem.addActionListener(controller);
        topologyMenu.add(openItem);
        openItem.setEnabled(false);
     
        saveItem = new JMenuItem ( "Save" );
        saveItem.addActionListener(controller);
        topologyMenu.add(saveItem);
        saveItem.setEnabled(false);
        
        modifyItem = new JMenuItem ("Modify");
        modifyItem.addActionListener(controller);
        topologyMenu.add(modifyItem);
        modifyItem.setEnabled(false);
        
        JMenu simulationMenu = new JMenu( "Simulation" );
        menuBar.add(simulationMenu);
        startItem = new JMenuItem ( "Start" );
        startItem.addActionListener(controller);
        simulationMenu.add(startItem); 
        startItem.setEnabled(false);
        
        Icon icon = new ImageIcon("Start.png");
		Image img = ((ImageIcon) icon).getImage() ;  
		Image newimg = img.getScaledInstance( 20,20,  java.awt.Image.SCALE_SMOOTH ) ;  
		icon = new ImageIcon( newimg );
        runMenu = new JMenu("");
        menuBar.add(runMenu);
        runItem = new JMenuItem("Run");
        runItem.addActionListener(controller);
        runMenu.add(runItem);
        clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(controller);
        simulationMenu.add(clearItem);
        
        runMenu.setIcon(icon);
        runItem.setEnabled(false);
        clearItem.setEnabled(false);
        
        Icon icon2 = new ImageIcon("step.png");
		Image img2 = ((ImageIcon) icon2).getImage() ;  
		Image newimg2 = img2.getScaledInstance( 20,20,  java.awt.Image.SCALE_SMOOTH ) ;  
		icon2 = new ImageIcon( newimg2 );
		stepMenu = new JMenu("");
		menuBar.add(stepMenu);
		stepItem = new JMenuItem("Step");
		stepItem.addActionListener(controller);
		stepMenu.add(stepItem);
		stepMenu.setIcon(icon2);
		stepItem.setEnabled(false);
		
		Icon icon3 = new ImageIcon("undo.png");
		Image img3 = ((ImageIcon) icon3).getImage() ;  
		Image newimg3= img3.getScaledInstance( 20,20,  java.awt.Image.SCALE_SMOOTH ) ;  
		icon3 = new ImageIcon( newimg3 );
		undoMenu = new JMenu("");
		menuBar.add(undoMenu);
		undoItem = new JMenuItem("Undo");
		undoItem.addActionListener(controller);
		undoMenu.add(undoItem);
		undoMenu.setIcon(icon3);
		undoItem.setEnabled(false);        
        
        setJMenuBar(menuBar);
    }
    
	public void createToplogyPanels() {
		editPanel = new Panel();
        add(editPanel,BorderLayout.NORTH);
        Button addButton = new Button("Add Node");
        addButton.addActionListener(controller);
        editPanel.add(addButton);

        Button editLinksButton = new Button("Edit Links");
        editLinksButton.addActionListener(controller);
        editPanel.add(editLinksButton);

        Button moveButton = new Button("Move Nodes");        
        moveButton.addActionListener(controller);
        editPanel.add(moveButton);
        
        Button startOverButton = new Button("Start Over");        
        startOverButton.addActionListener(controller);
        editPanel.add(startOverButton);
        editPanel.setVisible(false);
        
        submitPanel = new Panel();
        add(submitPanel,BorderLayout.SOUTH);
        Button submitButton = new Button("Submit");
        submitButton.addActionListener(controller);
        submitPanel.add(submitButton);        
        submitPanel.setVisible(false);

	}

    public void init() {
        setSize(800,500);
        setLayout(new BorderLayout());
        createMenuBar();
        createToplogyPanels();
    }
}