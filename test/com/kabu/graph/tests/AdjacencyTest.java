package com.kabu.graph.tests;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.kabu.graph.Edge;
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

		Edge e1 = g.connect(v1, v2, "N1->N2");
		Edge e2 = g.connect(v1, v3, "N1->N3");
		Edge e3 = g.connect(v1, v4, "N1->N4");
		Edge e4 = g.connect(v2, v4, "N2->N4");
		Edge e5 = g.connect(v3, v4, "N3->N4");
		Edge e6 = g.connect(v4, v4, "N4->N4");
		Edge e7 = g.connect(v4, v1, "N4->N1");

		assertEquals(v1.getLabel(), "Node 1");
		assertEquals(e1.getLabel(), "N1->N2");

		assertThat(v1.getNeighbours(), CoreMatchers.hasItems(v2, v3, v4));
		assertThat(v2.getNeighbours(), CoreMatchers.hasItems(v4));
		assertThat(v3.getNeighbours(), CoreMatchers.hasItems(v4));
		assertThat(v4.getNeighbours(), CoreMatchers.hasItems(v4, v1));

		Convert2AdjacencyMatrix a = new Convert2AdjacencyMatrix(g);
		a.execute();

		assertThat(a.getResult(), CoreMatchers.is(new int[][] {
						{ 0, 1, 1, 1 }, { 0, 0, 0, 1 },
						{ 0, 0, 0, 1 }, { 1, 0, 0, 1 }}));
	}

}
