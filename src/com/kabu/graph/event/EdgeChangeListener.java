package com.kabu.graph.event;

import java.util.EventListener;

public interface EdgeChangeListener extends EventListener {
	
    public void edgeChanged(EdgeChangeEvent e);
}