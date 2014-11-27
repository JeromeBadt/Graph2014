package com.kabu.graph;

import java.util.ArrayList;
import java.util.List;

import com.kabu.graph.Edge;
import com.kabu.graph.Vertex;
import com.kabu.graph.event.VertexChangeEvent;
import com.kabu.graph.event.VertexChangeListener;

public class Vertex {
	/**
	 * Die Kantenliste enthält alle Kanten, die von dieser Ecke ausgehen. Diese
	 * Liste wird als Adjazenzliste benutzt.
	 */
	private List<Edge> edgeList = new ArrayList<Edge>();

	private String label = "";

	private List<VertexChangeListener> vertexChangeListeners = new ArrayList<VertexChangeListener>();

	public Vertex() {
	}

	public Vertex(String label) {
		setLabel(label);
	}

	public List<Vertex> getNeighbours() {
		List<Vertex> adj = new ArrayList<Vertex>();

		for (Edge edge : edgeList) {
			adj.add(edge.getHead());
		}

		return adj;
	}

	public void setLabel(String label) {
		this.label = label;
		vertexChanged(new VertexChangeEvent(this));
	}

	public String getLabel() {
		return label;
	}

	public void addEdge(Edge edge) {
		if (!edgeList.contains(edge)) {
			edgeList.add(edge);
			vertexChanged(new VertexChangeEvent(this));
		}
	}

	public void removeEdge(Edge edge) {
		edgeList.remove(edge);
		vertexChanged(new VertexChangeEvent(this));
	}

	void removeAllEdges() {
		edgeList = new ArrayList<Edge>();
		vertexChanged(new VertexChangeEvent(this));
	}

	/**
	 * Klonen einer Ecke. Hierbei wird eine neue, isolierte Ecke erstellt, die
	 * das gleiche Label erhält wie die ursprüngliche Ecke. Die edgeList wird
	 * nicht geklont, da sie von der Graph.clone() Methode geklont wird.
	 */
	protected Object clone() { // throws CloneNotSupportedException
		Vertex vertex = new Vertex(label);

		return vertex;
	}

	/* ************************************************************************
	 * Event Management
	 * **********************************************************************
	 */

	public synchronized void addVertexChangeListener(
			VertexChangeListener listener) {
		vertexChangeListeners.add(listener);
	}

	public synchronized void removeVertexChangeListener(
			VertexChangeListener listener) {
		vertexChangeListeners.remove(listener);
	}

	/**
	 * Sämtliche VertexChangeListeners benachrichtigen.
	 */
	protected void vertexChanged(VertexChangeEvent e) {
		List<VertexChangeListener> listeners = null;

		synchronized (this) {
			listeners = new ArrayList<VertexChangeListener>(
					vertexChangeListeners);
		}

		for (VertexChangeListener vcl : listeners) {
			vcl.vertexChanged(e);
		}
	}

	/* ************************************************************************
	 * Eine Ecke als String repräsentieren
	 * ************************************************************************
	 */

	public String toString() {
		return toString(0);
	}

	/**
	 * Darstellung der Ecke als String mit vorwählbarem Einzug.
	 */
	public String toString(int inset) {

		StringBuilder insetBuf = new StringBuilder();
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < inset; i++) {
			insetBuf.append("\t");
		}

		String insetString = new String(insetBuf.toString());

		buf.append(insetString);
		buf.append("(vertex\n");

		buf.append(insetString + "\t");
		buf.append("(label \"" + label + "\")\n");

		buf.append(insetString + "\t");
		buf.append("(edges\n");

		// Only show the edge labels instead of the whole edge.toString() to
		// avoid recursion!
		StringBuilder edgeBuf = new StringBuilder();

		for (Edge edge : edgeList) {
			edgeBuf.append(insetString + "\t\t");
			edgeBuf.append("\"" + edge.getLabel() + "\"\n");
		}

		buf.append(edgeBuf.toString());

		buf.append(insetString + "\t");
		buf.append(")\n");

		buf.append(insetString);
		buf.append(")\n");

		return buf.toString();
	}
}
