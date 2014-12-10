package com.kabu.graph.viewer;

import java.util.List;

import com.kabu.graph.Edge;
import com.kabu.graph.Graph;
import com.kabu.graph.Vertex;

/**
 * Konvertiert einen Graphen in die DOT Language. Diese Sprach kann z.B. von
 * Graphviz verwendet werden um Graphen grafisch darzustellen.
 * http://www.graphviz.org
 */
public class Converter {
	private StringBuilder buf = null;
	private boolean useLabels = true;
	private boolean useWeights = true;

	public Converter() {
	}
	
	public Converter(boolean useLabels, boolean useWeights) {
		this.useLabels = useLabels;
		this.useWeights = useWeights;
	}

	public void setUseLabels(boolean useLabels) {
		this.useLabels = useLabels;
	}

	public void setUseWeights(boolean useWeights) {
		this.useWeights = useWeights;
	}

	/**
	 * Erstellung der Graphiz-konformen Darstellung des Graphen.
	 */
	public void convert(Graph graph) {
		List<Edge> edgeSet = graph.getAllEdges();
		List<Vertex> vertex = graph.getVertices();
		buf = new StringBuilder();

		buf.append("digraph G {\n");

		for (int i = 0; i < vertex.size(); i++) {
			Vertex v = (Vertex) vertex.get(i);

			buf.append("  " + "\"" + v.getLabel() + "\"");
			buf.append(";\n");
		}

		for (Edge edge : edgeSet) {
			buf.append("  " + "\"" + edge.getTail().getLabel() + "\"" + " -> "
					+ "\"" + edge.getHead().getLabel() + "\"");
			if (useLabels || useWeights) {
				buf.append("[label=\"");
			}
			if (useLabels) {
				buf.append(edge.getLabel());
			}
			if (useLabels && useWeights) {
				buf.append(",\\n");
			}
			if (useWeights) {
				buf.append(edge.getWeight());
			}
			if (useLabels || useWeights) {
				buf.append("\"]");
			}
			buf.append(";\n");
		}
		buf.append("}\n");
	}

	public String getResult() {
		return buf.toString();
	}
}
