package com.kabu.graph.tests;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;
import com.kabu.graph.algorithm.IsAcyclic;

public class AcyclicityTest {

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
		
		IsAcyclic a = new IsAcyclic(g);
		a.execute();

		assertThat(a.isAcyclic()).isEqualTo(true);
	}

	@Test
	public void test2() {
		Graph g = new Graph();

		Vertex v1 = g.createVertex("Node 1");
		Vertex v2 = g.createVertex("Node 2");
		Vertex v3 = g.createVertex("Node 3");
		Vertex v4 = g.createVertex("Node 4");

		g.connect(v1, v2, "N1->N2");
		g.connect(v1, v3, "N1->N3");
		g.connect(v4, v1, "N4->N1");
		g.connect(v2, v4, "N2->N4");
		g.connect(v3, v4, "N3->N4");
		g.connect(v4, v4, "N4->N4");

		IsAcyclic a = new IsAcyclic(g);
		a.execute();
		
		assertThat(a.isAcyclic()).isEqualTo(false);
	}
}
