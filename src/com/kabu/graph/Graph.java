package com.kabu.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kabu.graph.event.EdgeSetChangeEvent;
import com.kabu.graph.event.EdgeSetChangeListener;
import com.kabu.graph.event.VerticesChangeEvent;
import com.kabu.graph.event.VerticesChangeListener;

/**
 * Implementation eines gerichteten Graphen. Die Implementation entspricht der
 * allg. üblichen Definition gerichteter Graphen als G=(V,E), mit V der
 * Eckenmenge (vertices) sowie E, der Kantenmenge (Edges). E ist Teilmenge von
 * VxV.
 * 
 * Um ungerichtete Graphen darzustellen, kann man je 2 entgegengerichtete Kanten
 * verwenden.
 * 
 * Hinweis: Edge- und Vertex-Objekte sollten stets mit Hilfe der in der Klasse
 * Graph enthaltenen Factory-Methoden createVertice und connect angelegt werden.
 * Auf ein herkömmliches "new Edge" oder "new Vertex" sollte verzichtet werden,
 * da die Factory-Methoden eine Reihe von Verwaltungsoperationen durchführen.
 */

public class Graph {
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private List<Edge> edges = new ArrayList<Edge>();

	private List<EdgeSetChangeListener> edgeSetChangeListeners = new ArrayList<EdgeSetChangeListener>();
	private List<VerticesChangeListener> verticesChangeListeners = new ArrayList<VerticesChangeListener>();

	public Graph() {
	}

	// Festellen, ob sich die gegebene Ecke in der Eckenmenge befindet
	public boolean contains(Vertex vertex) {
		return vertices.contains(vertex);
	}

	// Festellen, ob sich die gegebene Kante in der Kantenmenge befindet
	public boolean contains(Edge edge) {
		return edges.contains(edge);
	}

	/**
	 * Berechnen der Ordnung des Graphen.
	 * 
	 * Hinweis: Die Ordnung eines Graphen ist die Anzahl seiner Ecken.
	 */
	public int getOrder() {
		return vertices.size();
	}

	/**
	 * Anlegen einer neuen Ecke. Factory-Methode.
	 * 
	 * @return die neue Ecke, nachdem sie durch addVertex() intern registriert
	 *         wurde und sämtliche VerticesChangeListener benachrichtigt worden
	 *         sind.
	 */
	private Vertex createVertex() {
		Vertex vertex = new Vertex();
		addVertex(vertex);

		return vertex;
	}

	/**
	 * Hinzufügen einer neuen Ecke. Hierüber werden sämtliche
	 * VerticesChangeListener benachrichtigt.
	 * 
	 * Hinweis: Diese Methode ist nur für den Package-internen Gebrauch gedacht.
	 * Der Anwendungsprogrammierer sollte diese Methode nicht benutzen! Für ihn
	 * stehen createVertex() sowie createVertex(String label) bereit.
	 * 
	 * @see #createVertex()
	 * @see #createVertex(String label)
	 */
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);

		VerticesChangeEvent e = new VerticesChangeEvent(vertex,
				VerticesChangeEvent.VERTEX_ADDED);
		verticesChanged(e);
	}

	/**
	 * Anlegen einer neuen Ecke und Angabe der Eckenbezeichnung. Aufruf von
	 * createVertice(). Factory-Methode.
	 */
	public Vertex createVertex(String label) {
		Vertex vertex = createVertex();
		vertex.setLabel(label);

		return vertex;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public List<Edge> getAllEdges() {
		return edges;
	}

	// Auslesen sämtlicher Kanten von "from" nach "to".
	public List<Edge> getEdges(Vertex from, Vertex to) {
		List<Edge> result = new ArrayList<Edge>();

		for (Edge edge : edges) {
			if (edge.getHead() == to && edge.getTail() == from) {
				result.add(edge);
			}
		}

		return result;
	}

	/**
	 * Anlegen einer Kante zwischen den gegebenen Ecken. Factory-Methode.
	 * Sämtliche EdgeSetChangeListener werden benachrichtigt.
	 */
	public Edge connect(Vertex from, Vertex to) {
		Edge edge = new Edge(from, to);
		edges.add(edge);

		// Hinzufügen zur Kantenliste des Ausgangsknotens
		from.addEdge(edge);

		EdgeSetChangeEvent e = new EdgeSetChangeEvent(edge,
				EdgeSetChangeEvent.EDGE_ADDED);
		edgeSetChanged(e);

		return edge;
	}

	/**
	 * Anlegen einer Kante zwischen den gegebenen Ecken mit Kantenbeschriftung.
	 * Factory-Methode. Sämtliche EdgeSetChangeListener werden benachrichtigt.
	 */
	public Edge connect(Vertex from, Vertex to, String label) {
		Edge edge = connect(from, to);
		edge.setLabel(label);

		return edge;
	}

	/**
	 * Anlegen einer Kante zwischen den gegebenen Ecken mit Kantenbeschriftung
	 * und Kantenbewertung bzw. Kantengewicht. Factory-Methode. Sämtliche
	 * EdgeSetChangeListener werden benachrichtigt.
	 */
	public Edge connect(Vertex from, Vertex to, String label, double weight) {
		Edge edge = connect(from, to, label);
		edge.setWeight(weight);

		return edge;
	}

	/**
	 * Anlegen einer Kante zwischen den gegebenen Ecken mit Kantenbewertung bzw.
	 * Kantengewicht. Factory-Methode. Sämtliche EdgeSetChangeListener werden
	 * benachrichtigt.
	 */
	public Edge connect(Vertex from, Vertex to, double weight) {
		Edge edge = connect(from, to);
		edge.setWeight(weight);

		return edge;
	}

	/**
	 * Auslesen, ob zwei Ecken durch mind. eine Kante verbunden (adjazent) sind.
	 */
	public boolean isConnected(Vertex from, Vertex to) {
		for (Edge edge : edges) {
			if (edge.getTail() == from && edge.getHead() == to) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Berechnen sämtlicher Vorgänger einer Ecke, also sämtlicher Ecken, von
	 * denen Kanten ausgehen, die auf die gegebene Ecke weisen.
	 */
	public List<Vertex> getPred(Vertex target) {
		List<Vertex> predecessors = new ArrayList<Vertex>();

		for (Vertex vertex : vertices) {
			if (vertex.getNeighbours().contains(target)) {
				predecessors.add(vertex);
			}
		}

		return predecessors;
	}

	public void remove(Edge edge) {
		for (Vertex vertex : vertices) {
			vertex.removeEdge(edge);
		}
		edges.remove(edge);

		EdgeSetChangeEvent e = new EdgeSetChangeEvent(edge,
				EdgeSetChangeEvent.EDGE_REMOVED);
		edgeSetChanged(e);
	}

	public void remove(Vertex vertex) {
		for (Edge edge : new ArrayList<Edge>(edges)) {
			if (edge.getHead() == vertex || edge.getTail() == vertex) {
				remove(edge);
			}
		}
		vertices.remove(vertex);

		VerticesChangeEvent e = new VerticesChangeEvent(vertex,
				VerticesChangeEvent.VERTEX_REMOVED);
		verticesChanged(e);
	}

	/* ************************************************************************
	 * Graph Reproduction
	 * ************************************************************************
	 */

	public Graph copy() {
		return (Graph) clone();
	}

	protected Object clone() { // throws CloneNotSupportedException

		Class thisClass = getClass();
		Graph g = null;

		try {
			g = (Graph) thisClass.newInstance();
		} catch (IllegalAccessException ex) {
			System.out.println(ex);
		} catch (InstantiationException ex) {
			System.out.println(ex);
		}

		// Bildet alte Ecken auf Neue ab. key=alt value=neu
		Map<Vertex, Vertex> assoc = new HashMap<Vertex, Vertex>();

		// Jede Ecke klonen
		for (Vertex vertex : vertices) {
			Vertex newVertex = (Vertex) vertex.clone();
			assoc.put(vertex, newVertex);
			g.addVertex(newVertex);
		}

		for (Edge edge : edges) {
			Vertex head = edge.getHead();
			Vertex tail = edge.getTail();
			Vertex newHead = null, newTail = null;

			// Nur verbundene Kanten klonen
			if (head == null || tail == null) {
				continue;
			}

			newHead = assoc.get(head);
			newTail = assoc.get(tail);

			if (newHead == null || newTail == null) {
				System.out.println("Edge wasn't cloned: " + edge.toString());
				continue;
			}

			g.connect(newTail, newHead, edge.getLabel());
		}

		return g;
	}

	/* ************************************************************************
	 * Event Management
	 * ************************************************************************
	 */

	public synchronized void addVertexChangeListener(
			VerticesChangeListener listener) {
		verticesChangeListeners.add(listener);
	}

	public synchronized void removeVertexChangeListener(
			VerticesChangeListener listener) {
		verticesChangeListeners.remove(listener);
	}

	// Sämtliche VerticesChangeListeners benachrichtigen
	protected void verticesChanged(VerticesChangeEvent e) {
		List<VerticesChangeListener> listeners = null;

		synchronized (this) {
			listeners = new ArrayList<VerticesChangeListener>(
					verticesChangeListeners);
		}

		for (VerticesChangeListener vcl : listeners) {
			vcl.verticesChanged(e);
		}
	}

	public synchronized void addEdgeSetChangeListener(
			EdgeSetChangeListener listener) {
		edgeSetChangeListeners.add(listener);
	}

	public synchronized void removeEdgeSetChangeListener(
			EdgeSetChangeListener listener) {
		edgeSetChangeListeners.remove(listener);
	}

	// Sämtliche EdgeSetChangeListener benachrichtigen
	public void edgeSetChanged(EdgeSetChangeEvent e) {
		List<EdgeSetChangeListener> listeners = null;

		synchronized (this) {
			listeners = new ArrayList<EdgeSetChangeListener>(
					edgeSetChangeListeners);
		}

		for (EdgeSetChangeListener ecl : listeners) {
			ecl.edgeSetChanged(e);
		}
	}

	/* ************************************************************************
	 * Einen Graphen als String repräsentieren
	 * ************************************************************************
	 */

	public String toString() {
		return toString(0);
	}

	// Darstellung des Graphen als String mit vorwählbarem Einzug
	public String toString(int inset) {
		StringBuilder insetBuf = new StringBuilder();
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < inset; i++) {
			insetBuf.append("\t");
		}

		String insetString = new String(insetBuf.toString());

		buf.append(insetString);
		buf.append("(Graph \n");

		buf.append(insetString + "\t");
		buf.append("(vertices\n");

		for (Vertex vertex : vertices) {
			buf.append(vertex.toString(inset + 2));
		}

		buf.append(insetString + "\t");
		buf.append(")\n");

		buf.append(insetString + "\t");
		buf.append("(edges\n");

		for (Edge edge : edges) {
			buf.append(edge.toString(inset + 2));
		}

		buf.append(insetString + "\t");
		buf.append(")\n");

		buf.append(insetString);
		buf.append(")\n");

		return buf.toString();
	}
}