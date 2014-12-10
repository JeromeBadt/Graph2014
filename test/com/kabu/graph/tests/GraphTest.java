package com.kabu.graph.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.kabu.graph.Edge;
import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

/**
 * Einfacher Funktionstest der Klassen Graph, Edge und Vertex.
 */
public class GraphTest {
	
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

		assertThat(g.getVertices()).containsExactly(v1, v2, v3, v4);
		assertThat(g.getAllEdges()).containsExactly(e1, e2, e3, e4, e5);
		System.out.println(g.toString());

		g.remove(e1);
		
		assertThat(g.getAllEdges()).containsExactly(e2, e3, e4, e5);
		System.out.println("After removing e1:\n" + g.toString());

		g.remove(v4);

		assertThat(g.getVertices()).containsExactly(v1, v2, v3);
		System.out.println("After removing v4:\n" + g.toString());
	}
}
