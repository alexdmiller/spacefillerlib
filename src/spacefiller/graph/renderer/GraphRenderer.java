package spacefiller.graph.renderer;

import processing.core.PGraphics;
import spacefiller.graph.Graph;

public interface GraphRenderer {
  void render(PGraphics graphics, Graph graph);
}
