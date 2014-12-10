package com.kabu.graph.algorithm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

/**
 * Rekursive Tiefensuche in einem Graphen. Dieser Algorithmus ist nur zu
 * Demonstrationszwecken gedacht, da er nur nach einer Ecke anhand ihres Labels
 * sucht.
 * 
 * Achtung: Es wird die erste Ecke zurückgegeben, die das gesuchte Label
 * besitzt! Alle weiteren Ecken werden ignoriert.
 */
public class DepthSearch extends GraphAlgorithm {

	private String label = "";
	private Deque<Vertex> path = null;

	public DepthSearch(Graph g) {
		super(g);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Auslesen des Weges, den der Algorithmus zur gesuchten Ecke gefunden hat.
	 */
	public Deque<Vertex> getPath() {
		return path;
	}

	/**
	 * Operationen bei Besuch einer Ecke. Ruft sich rekursiv auf, um sämtliche
	 * Nachbarn der jeweweiligen Ecke zu besuchen.
	 */
	public Vertex visit(Set<Vertex> visited, Vertex vertex) {
		System.out.println("Passiere " + vertex.getLabel());

		Vertex result = null;

		path.add(vertex);

		List<Vertex> neighbours = vertex.getNeighbours();

		// Ausgabe der Ecken ungeraden Grades
		if (neighbours.size() % 2 != 0) {
			System.out.println("Ecke " + vertex.getLabel()
					+ " ist ungeraden Grades. Grad=" + neighbours.size());
		}

		for (Vertex nb : neighbours) {
			// Wenn wir den Nachbarn noch nicht besucht haben, dann tun wir dies
			// jetzt
			if (!visited.contains(nb)) {
				// Wir merken uns den Besuch des Nachbarn
				visited.add(nb);
				// Durchführen des Besuchs
				result = visit(visited, nb);
				if (result != null) {
					return result;
				}
			}
		}

		if (vertex.getLabel().equals(label)) {
			result = vertex;
		} else {
			result = null;
			path.removeLast();
		}

		return result;
	}

	/**
	 * Start der eigentlichen Tiefensuche. Es wird ein Stack verwaltet, der
	 * sämtliche bereits besuchten Ecken speichert, um keine Ecke mehr als
	 * einmal besuchen zu müssen.
	 */
	public void execute() {
		Set<Vertex> visited = new HashSet<Vertex>();

		for (Vertex vertex : getGraph().getVertices()) {
			path = new ArrayDeque<Vertex>();

			if (!visited.contains(vertex)) {
				Vertex result = visit(visited, vertex);
				if (result != null) {
					System.out.println("Ecke gefunden:\n" + result + "\n"
							+ "Startecke:\n" + vertex);
					System.out.println("Weg:\n" + path);
					return;
				} else {
					System.out.println("Ecke ausgehend von Startecke:\n"
							+ vertex + "\nnicht gefunden.");
				}
			}
		}
	}
}
