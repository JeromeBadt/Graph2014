package com.kabu.graph.tests;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

/**
 * Einfacher Funktionstest zur Kantenbewertung von Graphen.
 */
public class WeightTest {

	@Test
	public void test() {
		Graph g = new Graph();

		Vertex v1 = g.createVertex("Node 1");
		Vertex v2 = g.createVertex("Node 2");
		Vertex v3 = g.createVertex("Node 3");
		Vertex v4 = g.createVertex("Node 4");

		g.connect(v1, v2, "N1->N2", 3);
		g.connect(v1, v3, "N1->N3", 1);
		g.connect(v1, v4, "N1->N4", 5);
		g.connect(v2, v4, "N2->N4", 8);
		g.connect(v3, v4, "N3->N4", 0.3);

		System.out.println(g.toString());

		final double DELTA = 1e-15;

		assertThat(g.getAllEdges().get(0).getWeight()).isCloseTo(3.0,
				within(DELTA));
		assertThat(g.getAllEdges().get(1).getWeight()).isCloseTo(1.0,
				within(DELTA));
		assertThat(g.getAllEdges().get(2).getWeight()).isCloseTo(5.0,
				within(DELTA));
		assertThat(g.getAllEdges().get(3).getWeight()).isCloseTo(8.0,
				within(DELTA));
		assertThat(g.getAllEdges().get(4).getWeight()).isCloseTo(0.3,
				within(DELTA));
	}
}
