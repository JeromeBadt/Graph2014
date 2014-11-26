package com.kabu.graph.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.kabu.graph.Edge;
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

        Edge e1 = g.connect(v1, v2, "N1->N2", 3);
        Edge e2 = g.connect(v1, v3, "N1->N3", 1);
        Edge e3 = g.connect(v1, v4, "N1->N4", 5);
        Edge e4 = g.connect(v2, v4, "N2->N4", 8);
        Edge e5 = g.connect(v3, v4, "N3->N4", 0.3);

        System.out.println(g.toString());
        
        final double DELTA = 1e-15;
        
        assertEquals(g.getAllEdges().get(0).getWeight(), 3, DELTA);
        assertEquals(g.getAllEdges().get(1).getWeight(), 1, DELTA);
        assertEquals(g.getAllEdges().get(2).getWeight(), 5, DELTA);
        assertEquals(g.getAllEdges().get(3).getWeight(), 8, DELTA);
        assertEquals(g.getAllEdges().get(4).getWeight(), 0.3, DELTA);
    }
}
