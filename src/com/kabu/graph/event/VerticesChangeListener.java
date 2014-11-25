package com.kabu.graph.event;

import java.util.EventListener;

public interface VerticesChangeListener extends EventListener {

    public void verticesChanged(VerticesChangeEvent e);
}
