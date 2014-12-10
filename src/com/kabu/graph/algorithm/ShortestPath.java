package com.kabu.graph.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kabu.graph.Edge;
import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

/**
 * Berechnung der k�rzesten Wege von einer Startecke zu s�mtlichen anderen Ecken
 * des Graphs. Der Algorithmus basiert auf den Bellman'schen Gleichungen und ist
 * daher nur f�r kantenbewertete Graphen geeignet, bei denen die L�nge jedes
 * geschlossenen Kantenzugs gr��er oder gleich 0 ist.
 */
public class ShortestPath extends GraphAlgorithm {
	// d[i] enth�lt eine obere Grenze f�r die Distanz d(start,i) zwischen der
	// Startecke start und der Ecke i.
	private Map<Vertex, Double> d = null;
	// predecessor[i] enth�lt die Vorg�nger der Ecke i im zugeh�rigen
	// k�rzesten-Wege-Baum (kurz: kw-Baum).
	private Map<Vertex, Vertex> predecessor = null;
	private Vertex start = null;

	public ShortestPath(Graph g) {
		super(g);
	}

	/**
	 * Initialisierung des Algorithmus.
	 * 
	 * Anlegen der Distanzzabelle Anlegen der Vorg�ngertabelle (stellt sp�ter
	 * den kw-Baum dar) Vorbelegung der Distanztabelle, so dass s�mtliche Knoten
	 * unendlich weit von dem Startknoten entfernt sind.
	 * 
	 * Ausnahme: Der Startknoten ist von sich selbst 0 Einheiten entfernt.
	 * 
	 * Vorbelegung der Vorg�ngertabelle, so dass jeder Knoten zu Beginn keinen
	 * Vorg�nger besitzt.
	 */
	private void initSP(Vertex start) {
		d = new HashMap<Vertex, Double>(getGraph().getOrder());
		predecessor = new HashMap<Vertex, Vertex>(getGraph().getOrder());

		List<Vertex> vertices = getGraph().getVertices();

		for (Vertex vertex : vertices) {
			if (vertex != start) {
				d.put(vertex, new Double(Double.POSITIVE_INFINITY));
			} else {
				d.put(vertex, new Double(0));
			}

			predecessor.put(vertex, null);
		}
	}

	/**
	 * Entscheidet �ber die Verwendbarkeit einer Ecke, um die in der
	 * Distanztabelle gespeicherten Werte zu optimieren.
	 * 
	 * @param v
	 *            Die Ecke, �ber deren Verwendbarkeit entschieden werden soll.
	 * @return true, wenn d[i]+b[i,j]<d[j], mit b[i,j] als geringstes
	 *         Kantengewicht zwischen i und j.
	 */
	private boolean usable(Vertex v) {
		List<Vertex> neighbours = v.getNeighbours();

		for (Vertex nb : neighbours) {
			Double temp = (Double) d.get(v);
			double d1 = temp.doubleValue();
			temp = (Double) d.get(nb);
			double d2 = temp.doubleValue();

			// Wir interessieren uns nur f�r die g�nstigste - also k�rzeste -
			// Kante
			Edge e12 = getShortestEdgeBetween(v, nb);

			// Tr�gt sie zur Optmierung bei?
			if (d1 + e12.getWeight() < d2) {
				return true;
			}
		}
		// v bietet uns keine Kanten zur Optimierung an, also ist sie
		// uninteressant. Wir signalisieren dies durch den R�ckgabewert false

		return false;
	}

	/**
	 * Berechne die zur Wegoptimierung verwendbaren Ecken.
	 * 
	 * @see #usable(Vertex v)
	 */
	private List<Vertex> getUsableVertices() {
		List<Vertex> vertices = getGraph().getVertices();
		List<Vertex> result = new ArrayList<Vertex>();

		for (Vertex vertex : vertices) {
			if (usable(vertex)) {
				result.add(vertex);
			}
		}

		return result;
	}

	/**
	 * Berechnung der k�rzesten Kante zwischen zwei gegebenen Ecken. Die
	 * k�rzeste Kante ist diejenige mit dem geringsten Kantengewicht.
	 * 
	 * Dies im Falle von Multigraphen (Graphen bei denen Ecken mit mehr als
	 * einer Kante verbunden sein k�nnen) wichtig.
	 */
	private Edge getShortestEdgeBetween(Vertex v1, Vertex v2) {
		List<Edge> edges = getGraph().getEdges(v1, v2);

		// Wir gehen zun�chst davon aus, dass ein undendliches Kantengewicht
		// vorliegt
		double minWeight = Double.POSITIVE_INFINITY;
		Edge result = null;

		for (Edge e : edges) {
			/*
			 * Ist das Gewicht der aktuellen Kante niedriger als das bisher
			 * geringste Gewicht zwischen v1 und v2, so registrieren wir diese
			 * aktuelle Kante als neue optimale Kante und merken uns ihr
			 * Gewicht.
			 */
			if (e.getWeight() < minWeight) {
				minWeight = e.getWeight();
				result = e;
			}
		}

		return result;
	}

	/**
	 * Verk�rzen der bisher registrierten minimalen Entfernung zwischen den
	 * Ecken v1 unnd v2.
	 */
	private void shorten(Vertex v1, Vertex v2) {
		// Auslesen der bisherigen optimalen Entfernung zwischen jeweils der
		// Startecke und v1 und v2
		double d1 = d.get(v1).doubleValue();
		double d2 = d.get(v2).doubleValue();

		// Berechnen der g�nstigsten Kante zwischen v1 und v2.
		Edge e12 = getShortestEdgeBetween(v1, v2);

		/*
		 * Wenn die Kante e12 zwischen v1 und v2 dazu f�hrt, das v2 g�nstiger
		 * als bisher von der Startecke erreicht werden kann, so m�ssen wir den
		 * k�rzesten Weg zu v2 verk�rzen (in der Distanztabelle) und v1 ist
		 * neuer Vorg�nger von v2.
		 */
		if (d1 + e12.getWeight() < d2) {
			d.remove(v2);
			d.put(v2, new Double(d1 + e12.getWeight()));
			predecessor.remove(v2);
			predecessor.put(v2, v1);
		}
	}

	/**
	 * Ausgangspunkt des Algorithmus zur Berechnung der k�rzesten Wege. Hier
	 * wird zum einen die Initialisierung der Hilfs-Datenstrukturen
	 * initSP(Vertex v) ausgel�st. Zum anderen erfolgt hier ein Aufruf der
	 * Methode shorten(Vertex v1, Vertex v2), solange Ecken gefunden werden
	 * k�nnen, die einen vielversprechenden Beitrag zur Optimierung bieten
	 * k�nnen.
	 * 
	 * @param start
	 *            Startecke
	 * @see #initSP(Vertex v)
	 * @see #shorten(Vertex v1, Vertex v2)
	 */
	private void bellman(Vertex start) {
		// Hilfs-Datenstrukturen vorbereiten
		initSP(start);

		// Welche Ecken helfen uns weiter?
		List<Vertex> usableVertices = getUsableVertices();

		// Solange wir Ecken finden, die uns weiterhelfen, ...
		while (usableVertices.size() > 0) {
			// ... versuchen wir die Entfernung zwischen ihnen und ihren
			// Nachbarn zu verk�rzen
			for (Vertex usableVertex : usableVertices) {
				List<Vertex> neighbours = usableVertex.getNeighbours();

				for (Vertex neighbour : neighbours) {
					shorten(usableVertex, neighbour);
				}
			}
			// N�tzliche Ecken f�r den n�chsten while-Durchlauf berechnen
			usableVertices = getUsableVertices();
		}
	}

	public void setStart(Vertex start) {
		this.start = start;
	}

	public Graph getShortestPathsTree() {
		// Einen leeren Graphen anlegen
		// spTree wird aufgebaut, indem die Vorg�ngertabelle transformiert wird
		Graph spTree = new Graph();

		// Wir durchlaufen nun die Vorg�ngertabelle und legen in spTree zun�chst
		// die Ecken an
		// Diese befinden sich als Key in der Vorg�ngertabelle
		for (Vertex v : predecessor.keySet()) {
			// Knoten in den spTree eintragen
			spTree.addVertex(v);
		}

		/*
		 * Nun durchlaufen wir die Vorg�ngertabelle und legen in spTree
		 * entsprechende Kanten an. Eine Kante entspricht einem Key-Value-Pair
		 * aus der Vorg�ngertabelle.
		 */
		for (Map.Entry<Vertex, Vertex> entry : predecessor.entrySet()) {
			Vertex v1 = entry.getKey();
			Vertex v2 = entry.getValue();

			if (v1 != null && v2 != null) {
				spTree.connect(v2, v1);
			}
		}
		// Schlie�lich haben wir den kw-Baum erstellt
		return spTree;
	}

	/**
	 * Auslesen der Distanztabelle und Darstellung als Graph.
	 * 
	 * @return Graph, der die Distanztabelle darstellt. Sollte erst nach
	 *         execute() aufgerufen werden.
	 */
	public Graph getDistances() {
		/*
		 * Einen leeren Graphen anlegen. distG wird aufgebaut, indem die
		 * Distanztabelle transformiert wird.
		 */
		Graph distG = new Graph();

		/*
		 * Wir durchlaufen nun die Distanztabelle und legen in distG zun�chst
		 * die Ecken an. Diese befinden sich als Key in der Distanztabelle.
		 */
		for (Vertex vertex : d.keySet()) {
			distG.addVertex(vertex);
		}

		/*
		 * Nun durchlaufen wir die Distanztabelle und legen in distG
		 * entsprechende Kanten an. Eine Kante entspricht einem Key-Value-Pair
		 * aus der Distanztabelle.
		 */
		for (Map.Entry<Vertex, Double> entry : d.entrySet()) {
			Vertex v = (Vertex) entry.getKey();
			Double distance = (Double) entry.getValue();
			distG.connect(start, v, "", distance.doubleValue());
		}
		/*
		 * Schlie�lich haben wir einen Graphen, der hilft, die Distanztabelle
		 * graphisch darzustellen.
		 */
		return distG;
	}

	/**
	 * Ausf�hren des Algorithmus. Wenn die Startecke vorher nicht gesetzt wurde,
	 * so erfolgt keine Aktion.
	 */
	public void execute() {
		if (start != null) {
			bellman(start);
		}
	}
}
