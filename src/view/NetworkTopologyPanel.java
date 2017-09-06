package view;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class NetworkTopologyPanel extends Panel  {
	private static int MAXNODES = 100;
	private static int numNodes; 
	private static NodeUIComp nodes[] = new NodeUIComp[MAXNODES];
	private int numLinks;  
	private LinkUIComp links[] = new LinkUIComp[2*MAXNODES];
	private Image offscreen;    // Double buffering so you don't get flicker
	private Dimension offscreensize;  // Size of screen
	private Graphics offgraphics;     // Graphics for offscreen
	private final Color labelDistanceColor = Color.gray;
	//private List<Line2D> lineCollection = new ArrayList<Line2D>();
    Rectangle2D rect = null;
	DemoApp view;
	
	public void setNodes(List<NodeUIComp> newNodes) {
		clearAllNodes();
		for (NodeUIComp node : newNodes) {
			nodes[numNodes] = node;
			numNodes++;
		}
	}
	
	public NodeUIComp[] getNodes() {
		return nodes;
	}
	
	public void clearAllNodes() {
		nodes = new NodeUIComp[MAXNODES];
		numNodes = 0;
	}
	
	public void setLinks(List<LinkUIComp> newLinks) {
		clearAllLinks();
		for (LinkUIComp link : newLinks) {
			links[numLinks] = link;
			numLinks++;
		}
	}
	
	public LinkUIComp[] getLinks() {
		return links;
	}

	public void clearAllLinks() {
		links = new LinkUIComp[2*MAXNODES];
		numLinks = 0;
	}
	
	public int getNumLinks() {
		return numLinks;
	}
	
	/**
	 * Creates the panel where the user draws the toplogy
	 * @param view: DemoApp
	 */
    public NetworkTopologyPanel(DemoApp view) { 
       numNodes = numLinks = 0;
       this.view = view;
       view.setSize(700,500); 
       view.setVisible(true);
       }
       
    /* 
     * Return index of node matching nodeLabel 
     */
    private int findNode(String nodeLabel) {
        for (int i = 0; i < numNodes; i++) {
            if (nodes[i].getNodeLabel().equals(nodeLabel)) {
            	return i;
            }
        }
        return addNode(nodeLabel);
    }
    
    /**
     * Add new node
     * @param nodeLabel: label on the node
     * @return index
     */
    public int addNode(String nodeLabel) {
        NodeUIComp n = new NodeUIComp();  // create contents of array 
        n.setX(30 + (int)(300 * Math.random())); 
        n.setY(30 + (int)(300 * Math.random())); 
        n.setColor(Color.cyan);
        n.setNodeLabel(nodeLabel);
        nodes[numNodes] = n;
        n.setId(numNodes+1);
        return numNodes++;
    }

    /**
     * Add link between "from" and "to" Node
     * @param from: nodeLabel
     * @param to: nodeLabel
     */
    public void addLink(String from, String to) {
        int fromSub = findNode(from);
        int toSub = findNode(to);
        LinkUIComp e = new LinkUIComp(fromSub,toSub);
        links[numLinks++] = e;
        nodes[fromSub].setAdj(new LinkUICompList(e,nodes[fromSub].getAdj())); 
        nodes[toSub].setAdj(new LinkUICompList(e,nodes[toSub].getAdj()));     
    }
    
    
    private ArrayDeque<Line2D> lineCollection = new ArrayDeque<Line2D>();
    private ArrayDeque<NextXYNumPackets> xypacketsCollection = new ArrayDeque<NextXYNumPackets>();

    
    /**
     * Plots a representation of packet moving through the network
     * each time a packet is send from node to another.
     * @param xCurr x value of current node
     * @param yCurr y value of current node
     * @param xNext x value of next node
     * @param yNext y value of next node
     * @param numPackets current number of packet hops
     * @throws InterruptedException
     */
    public void drawPacketSimulation(int xCurr, int yCurr, int xNext, int yNext, int numPackets) throws InterruptedException{
    	Graphics2D g = (Graphics2D) getGraphics();
    	super.paintComponents(g);
    	
    	Line2D line = new Line2D.Double(xCurr, yCurr, xNext, yNext);
    	lineCollection.addFirst(line);
    	
    	
		g.setStroke(new BasicStroke(5));
		g.setPaint(Color.RED);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Shape content : lineCollection) {
			g.draw(content);
		}
    	
    	g.fillRect(xNext, yNext, 20, 20);
    	g.setColor(Color.BLUE);
    	g.setFont(new Font("TimesRoman", Font.BOLD, 18));
    	g.drawString(String.valueOf(numPackets), xNext+(10/2), yNext+(30/2));
    	
    	NextXYNumPackets xypackets = new NextXYNumPackets();
    	xypackets.setxNext(xNext);
    	xypackets.setyNext(yNext);
    	xypackets.setNumPackets(numPackets);
    	
    	xypacketsCollection.addFirst(xypackets);
    	
		g.dispose();
    	
		Thread.sleep(1500);
    }    

    /**
     * Clear a representation of packet moving through the network
     * when user click "Clear".
     * @throws InterruptedException
     */
    public void clearPacketSimulation() throws InterruptedException{
    	lineCollection.removeAll(lineCollection);
    	xypacketsCollection.removeAll(xypacketsCollection);
    	repaint();
    }        

    
    public void clearLinesAtOneStep() {
    	super.repaint();
    	Graphics2D g = (Graphics2D) getGraphics();
    	super.paintComponents(g);
    	
     	lineCollection.removeFirst();
     	xypacketsCollection.removeFirst();
     	
		g.setStroke(new BasicStroke(5));
		g.setPaint(Color.RED);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Shape content : lineCollection) {
			g.draw(content);
		}
    }
    
    public void clearFloodingLines(int number) {
    	super.repaint();
    	Graphics2D g = (Graphics2D) getGraphics();
    	super.paintComponents(g);
    	
    	for(int i = 0; i < number; i++) {
         	lineCollection.removeFirst();
         	xypacketsCollection.removeFirst();
    	}
    	
		g.setStroke(new BasicStroke(5));
		g.setPaint(Color.RED);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Shape content : lineCollection) {
			g.draw(content);
		}
    }    
    
    
    public void redrawLines() throws InterruptedException{
    	Graphics2D g = (Graphics2D) getGraphics();
    	super.paintComponents(g);
    	
		g.setStroke(new BasicStroke(5));
		g.setPaint(Color.RED);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Shape content : lineCollection) {
			g.draw(content);
		}   
		
		for(Iterator<NextXYNumPackets> i = xypacketsCollection.iterator(); i.hasNext();) {
			NextXYNumPackets xyp = i.next();
			int xNext = xyp.getxNext();
			int yNext = xyp.getyNext();
			int numPackets = xyp.getNumPackets();			
			
			g.setPaint(Color.RED);
	    	g.fillRect(xNext, yNext, 20, 20);
	    	
	    	g.setFont(new Font("TimesRoman", Font.BOLD, 18));
	    	g.setColor(Color.BLUE);
	    	g.drawString(String.valueOf(numPackets), xNext+(10/2), yNext+(30/2));

		}
		
		g.dispose();	
		
    }
    
    /**
     * Draw node n on graphics g
     * @param g: graphics for offscreen
     * @param n: Gui node component to paint
     * @param fm: font metric used for node label
     */
    public void paintNode(Graphics g, NodeUIComp n, FontMetrics fm) {
        int x = (int) n.getX();  // grab current x location
        int y = (int) n.getY();  // grab current y location
        
        int rad = 20;
        int circlex= (int)(x)-rad;
        int circley=(int) (y)-rad;
        
        // draw black outline
        g.setColor(Color.BLACK);
        g.fillOval(circlex-2, circley-2,(rad+2)*2, (rad+2)*2);
        // draw circle
        g.setColor(n.getColor());
        g.fillOval(circlex, circley,rad*2, rad*2);
        
        g.setColor(Color.BLUE);
        g.drawString(n.getNodeLabel(), x - (30 - 10) / 2, (y - (20 - 4) / 2) + fm.getAscent());
    }

    public synchronized void update() {
    	Graphics g = getGraphics();
    	update(g);
    }
    
    /**
     * redraw all nodes and link components
     * @param g: graphics for offscreen
     */
    public synchronized void update(Graphics g) {
        Dimension d = getSize();  // current size of graphics  
        if ((offscreen == null) || (d.width != offscreensize.width)
                || (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();
            Font f = new Font("Helvetica", Font.BOLD, 18);
            offgraphics.setFont(f);
        }
        offgraphics.setColor(getBackground());
        offgraphics.fillRect(0, 0, d.width, d.height);
        
        // Draw each Link
        for (int i = 0; i < numLinks; i++) {
        	LinkUIComp e = links[i];
            int x1 = (int) nodes[e.getFrom()].getX();
            int y1 = (int) nodes[e.getFrom()].getY();
            int x2 = (int) nodes[e.getTo()].getX();
            int y2 = (int) nodes[e.getTo()].getY();

            offgraphics.setColor(Color.black);
            offgraphics.drawLine(x1,y1,x2,y2);
            offgraphics.setColor(labelDistanceColor);
            offgraphics.setColor(Color.black);

        }

        FontMetrics fm = offgraphics.getFontMetrics();
        // Draw each node
        for (int i = 0; i < numNodes; i++) {
            paintNode(offgraphics, nodes[i], fm);
        }
        // put the offscreen image to the screen
        g.drawImage(offscreen, 0, 0, null);
        
        try {
			redrawLines();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}