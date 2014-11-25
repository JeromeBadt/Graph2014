package com.kabu.graph.event;

import java.util.EventObject;

public class EdgeSetChangeEvent extends EventObject {

	public static int EDGE_ADDED = 0, EDGE_REMOVED = 1, EDGE_MODIFIED = 2;
	int flag = 0;

	public EdgeSetChangeEvent(Object o, int flag) {
		super(o);
		this.flag = flag;
	}

	public boolean isEdgeAdded() {
		return flag == EDGE_ADDED;
	}

	public boolean isEdgeRemoved() {
		return flag == EDGE_REMOVED;
	}

	public boolean isEdgeModified() {
		return flag == EDGE_MODIFIED;
	}
}
