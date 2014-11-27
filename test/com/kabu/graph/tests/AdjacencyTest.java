package com.kabu.graph.tests;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

import com.kabu.graph.algorithm.Convert2AdjacencyMatrix;

/**
 * Funktionstest des Algorithmus zur Berechnung einer Adjazenzmatrix.
 */
public class AdjacencyTest {

	@Test
	public void test() {
		Graph g = new Graph();

		Vertex v1 = g.createVertex("Node 1");
		Vertex v2 = g.createVertex("Node 2");
		Vertex v3 = g.createVertex("Node 3");
		Vertex v4 = g.createVertex("Node 4");

		g.connect(v1, v2, "N1->N2");
		g.connect(v1, v3, "N1->N3");
		g.connect(v1, v4, "N1->N4");
		g.connect(v2, v4, "N2->N4");
		g.connect(v3, v4, "N3->N4");
		g.connect(v4, v4, "N4->N4");
		g.connect(v4, v1, "N4->N1");

		assertThat(g.getVertices().get(0).getLabel()).isEqualTo("Node 1");
		assertThat(g.getAllEdges().get(0).getLabel()).isEqualTo("N1->N2");

		assertThat(v1.getNeighbours()).containsExactly(v2, v3, v4);
		assertThat(v2.getNeighbours()).containsExactly(v4);
		assertThat(v3.getNeighbours()).containsExactly(v4);
		assertThat(v4.getNeighbours()).containsExactly(v4, v1);

		Convert2AdjacencyMatrix algorithm = new Convert2AdjacencyMatrix(g);
		algorithm.execute();

		assertThat(algorithm.getResult()).isEqualTo(
				new int[][] { { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 1 },
						{ 1, 0, 0, 1 } });
	}

}
