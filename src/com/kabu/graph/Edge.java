package com.kabu.graph;

import java.util.ArrayList;
import java.util.List;

import com.kabu.graph.event.EdgeChangeEvent;
import com.kabu.graph.event.EdgeChangeListener;

public class Edge {
	private Vertex head = null; // Zielecke
	private Vertex tail = null; // Startecke

	private String label = "";
	private double weight = 1;

	private List<EdgeChangeListener> edgeChangeListeners = new ArrayList<EdgeChangeListener>();

	public Edge(Vertex tail, Vertex head) {
		this.head = head;
		this.tail = tail;
	}

	public Edge(Vertex tail, Vertex head, String label) {
		this(tail, head);
		setLabel(label);
	}

	public Edge(Vertex tail, Vertex head, String label, double weight) {
		this(tail, head, label);
		setWeight(weight);
	}

	public Edge(Vertex tail, Vertex head, double weight) {
		this(tail, head);
		setWeight(weight);
	}

	public void setLabel(String label) {
		this.label = label;
		edgeChanged(new EdgeChangeEvent(this));
	}

	public String getLabel() {
		return label;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setHead(Vertex head) {
		this.head = head;
		edgeChanged(new EdgeChangeEvent(this));
	}

	public Vertex getHead() {
		return head;
	}

	public void setTail(Vertex tail) {
		this.tail = tail;
		edgeChanged(new EdgeChangeEvent(this));
	}

	public Vertex getTail() {
		return tail;
	}

	public boolean isConnected() {
		return head != null && tail != null;
	}

	/* ************************************************************************
	 * Event Management
	 * ************************************************************************
	 */

	public synchronized void addEdgeChangeListener(EdgeChangeListener listener) {
		edgeChangeListeners.add(listener);
	}

	public synchronized void removeEdgeChangeListener(
			EdgeChangeListener listener) {
		edgeChangeListeners.remove(listener);
	}

	// Sämtliche EdgeChangeListeners benachrichtigen
	protected void edgeChanged(EdgeChangeEvent e) {
		List<EdgeChangeListener> listeners = null;

		synchronized (this) {
			listeners = new ArrayList<EdgeChangeListener>(edgeChangeListeners);
		}

		for (EdgeChangeListener ecl : listeners) {
			ecl.edgeChanged(e);
		}
	}

	/* ************************************************************************
	 * Eine Kante als String repräsentieren
	 * ************************************************************************
	 */

	public String toString() {
		return toString(0);
	}

	// Darstellung der Kante als String mit vorwählbarem Einzug
	public String toString(int inset) {
		StringBuilder insetBuf = new StringBuilder();
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < inset; i++) {
			insetBuf.append("\t");
		}

		String insetString = new String(insetBuf.toString());

		buf.append(insetString);
		buf.append("(edge\n");

		buf.append(insetString + "\t");
		buf.append("(label \"" + label + "\")\n");

		buf.append(insetString + "\t");
		buf.append("(weight \"" + weight + "\")\n");

		if (this.head != null) {
			buf.append(insetString + "\t");
			buf.append("(head \"" + head.getLabel() + "\")\n");
		}

		if (this.tail != null) {
			buf.append(insetString + "\t");
			buf.append("(tail \"" + tail.getLabel() + "\")\n");
		}

		buf.append(insetString);
		buf.append(")\n");

		return buf.toString();
	}
}
