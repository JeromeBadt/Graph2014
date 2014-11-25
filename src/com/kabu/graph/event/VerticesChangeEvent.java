package com.kabu.graph.event;

import java.util.EventObject;

import com.kabu.graph.Vertex;

public class VerticesChangeEvent extends EventObject {

	public static int VERTEX_ADDED = 0, VERTEX_REMOVED = 1;
	int flag = 0;
	Vertex vertex = null;

	public VerticesChangeEvent(Object o, int flag) {
		super(o);
		this.flag = flag;
	}

	public boolean isVertexAdded() {
		return flag == VERTEX_ADDED;
	}

	public boolean isVertexRemoved() {
		return flag == VERTEX_REMOVED;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public Vertex getVertex() {
		return vertex;
	}
}
