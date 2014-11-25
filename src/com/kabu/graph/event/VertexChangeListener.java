package com.kabu.graph.event;

import java.util.EventListener;

public interface VertexChangeListener extends EventListener {

    public void vertexChanged(VertexChangeEvent e);
}
