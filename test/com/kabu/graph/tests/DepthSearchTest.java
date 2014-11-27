package com.kabu.graph.tests;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;
import com.kabu.graph.algorithm.DepthSearch;

public class DepthSearchTest {

	@Test
	public void test() {
		Graph g = new Graph();

		Vertex v1 = g.createVertex("1");
		Vertex v2 = g.createVertex("2");
		Vertex v3 = g.createVertex("3");
		Vertex v4 = g.createVertex("4");
		Vertex v5 = g.createVertex("5");
		Vertex v6 = g.createVertex("6");
		Vertex v7 = g.createVertex("7");
		Vertex v8 = g.createVertex("8");

		g.connect(v1, v2, "N1->N2");
		g.connect(v1, v5, "N1->N5");
		g.connect(v1, v7, "N1->N7");
		g.connect(v2, v5, "N2->N5");
		g.connect(v3, v2, "N3->N2");
		g.connect(v3, v4, "N3->N4");
		g.connect(v4, v2, "N4->N2");
		g.connect(v5, v4, "N5->N4");
		g.connect(v5, v6, "N5->N6");
		g.connect(v6, v4, "N6->N4");
		g.connect(v7, v8, "N7->N8");
		g.connect(v8, v5, "N8->N5");
		g.connect(v8, v6, "N8->N6");

		/*
		 * Convert2Graphviz a1 = new Convert2Graphviz(g);
		 * a1.setUseLabels(false); a1.setUseWeights(false); a1.execute();
		 * 
		 * ViewUsingGraphviz a2 = new ViewUsingGraphviz(g);
		 * a2.setDotRep(a1.getResult());
		 * a2.setDotExe("C:\\Portable\\Graphviz\\bin\\dot.exe"); a2.execute();
		 */

		DepthSearch algorithm = new DepthSearch(g);

		algorithm.setLabel("4");
		algorithm.execute();

		assertThat(algorithm.getPath()).containsExactly(v1, v2, v5, v4);

		algorithm.setLabel("3");
		algorithm.execute();

		assertThat(algorithm.getPath()).containsExactly(v3);
	}

}
