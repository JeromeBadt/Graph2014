package com.kabu.graph.algorithm;

import java.util.List;

import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

/**
 * Konvertiert einen Graphen in seine äquivalente Adjazenzmatrix. Eine
 * Adjazenzmatrix A enthält Elemente a[i][j], für die gilt: a[i][j]=1, wenn es
 * eine Kante von Ecke i zu Ecke j gibt. Ansonsten ist a[i][j]=0.
 * 
 * Hinweis: Diese Realisierung ist sehr rudimentär und nicht weiter optimiert.
 * Kantengewichte werden ignoriert.
 */
public class Convert2AdjacencyMatrix extends GraphAlgorithm {
	private int adjacencyMatrix[][] = null;

	public Convert2AdjacencyMatrix(Graph g) {
		super(g);
	}

	public void execute() {
		List<Vertex> vertices = getGraph().getVertices();

		// Dimensionen der Adjazenzmatrix entprechen immer der Anzahl der Ecken
		// des Graphen
		int n = vertices.size();
		adjacencyMatrix = new int[n][n];

		// Initialisierung der Matrixelemente mit Null
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				adjacencyMatrix[i][j] = 0;
			}
		}

		// Für jeden Knoten lesen wir nun seine Nachbarn aus und vermerken diese
		// Adjazenz in der Matrix
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);

			// Für jeden Nachbarn bestimmen wir nun dessen Position in der Menge
			// der Knoten und setzen das entsprechende Matrixelement auf 1.
			List<Vertex> neighbours = vertex.getNeighbours();
			for (int k = 0; k < neighbours.size(); k++) {
				int j = vertices.indexOf(neighbours.get(k));

				adjacencyMatrix[i][j] = 1;
			}

		}

	}

	public int[][] getResult() {
		return adjacencyMatrix;
	}

	public String getResultString() {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < adjacencyMatrix.length; i++) {
			result.append(adjacencyMatrix[i][0] + " ");
			for (int j = 1; j < adjacencyMatrix[i].length; j++) {
				result.append(adjacencyMatrix[i][j] + " ");
			}
			result.append("\n");
		}

		return result.toString();
	}
}
