package com.kabu.graph.event;

import java.util.EventListener;

public interface EdgeSetChangeListener extends EventListener {

    public void edgeSetChanged(EdgeSetChangeEvent e);
}
