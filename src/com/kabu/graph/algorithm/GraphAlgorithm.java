package com.kabu.graph.algorithm;

import com.kabu.graph.Graph;

/**
 * Basisklasse s‰mtlicher Algrotihmen, die auf die Klasse Graph angewendet
 * werden sollen. Der betreffende Graph ist dem Konstruktor mitzugeben. Die
 * abstrakte Methode execute() kann von konkreten Algorithmen implementiert
 * werden. Sie soll von auﬂen aufgerufen werden, um den jeweiligen Algorithmus
 * zu starten.
 */
public abstract class GraphAlgorithm {
	private Graph graph = null;

	public GraphAlgorithm(Graph graph) {
		this.graph = graph;
	}

	public Graph getGraph() {
		return graph;
	}

	/**
	 * Diese Methode soll von auﬂen aufgerufen werden, um den jeweiloigen
	 * Algorithmus zu starten.
	 */
	public abstract void execute();
}
