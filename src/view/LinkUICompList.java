package view;

public class LinkUICompList {
	private LinkUIComp link;  // link to store
	private LinkUICompList next; // pointer to rest of link list
	
	public LinkUICompList() { 
	}
	
	/**
	 * List of all Links connected to a specific Node
	 * @param from
	 * @param next
	 */
	public LinkUICompList(LinkUIComp from, LinkUICompList next) { 
		this.link = from; 
		this.next = next;
	}

	public LinkUIComp getLink() {
		return link;
	}

	public void setLink(LinkUIComp link) {
		this.link = link;
	}

	public LinkUICompList getNext() {
		return next;
	}

	public void setNext(LinkUICompList next) {
		this.next = next;
	}

}
