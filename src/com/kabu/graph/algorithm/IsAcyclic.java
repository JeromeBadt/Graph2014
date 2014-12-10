package com.kabu.graph.algorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

/**
 * Algorithmus zur Bestimmung, ob ein Graph azaklisch ist. Basiert auf einer
 * modifizierten Tiefensuche.
 */
public class IsAcyclic extends GraphAlgorithm {
	private boolean acyclic = false;

	public IsAcyclic(Graph g) {
		super(g);
	}

	public boolean isAcyclic() {
		return acyclic;
	}

	/**
	 * Sucht nach Kreisen in dem Graphen, an denen die gegebene Ecke beteiligt
	 * ist. Wird von execute() aufgerufen.
	 */
	protected boolean searchCycle(Vertex vertex, Set<Vertex> visited,
			Set<Vertex> left) {

		visited.add(vertex);

		// Wir verwenden eine Tiefensuche, um nachzuprüfen, ob wir über
		// die Nachbarn und deren Nachbarn, usw. wieder zu dieser Ecke
		// zurückfinden können.
		List<Vertex> neighbours = vertex.getNeighbours();

		for (Vertex neighbour : neighbours) {
			if (!visited.contains(neighbour)) {
				// Rekursion
				if (searchCycle(neighbour, visited, left) == true) {
					return true;
				}
			} else {
				if (!left.contains(neighbour)) {
					// Zyklus gefunden!
					return true;
				}
			}
		}
		// Speichern, dass wir bei dieser Ecke bereits waren
		left.add(vertex);

		// Bisher haben wir keinen Kreis gefunden
		return false;
	}

	/**
	 * Durchlaufe den Graphen mit Tiefensuche und teste ob von der Ausgangsecke
	 * über die Nachbarn ein zyklischer Weg möglich ist.
	 */
	public void execute() {
		Graph graph = getGraph();

		Set<Vertex> visited = new HashSet<Vertex>();
		Set<Vertex> left = new HashSet<Vertex>();

		for (Vertex vertex : graph.getVertices()) {
			if (!visited.contains(vertex)) {
				if (searchCycle(vertex, visited, left) == true) {
					acyclic = false;
					return;
				}
			}
		}
		
		acyclic = true;
	}
}
