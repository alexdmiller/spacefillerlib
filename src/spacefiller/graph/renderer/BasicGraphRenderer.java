package spacefiller.graph.renderer;

import processing.core.PGraphics;
import spacefiller.graph.Edge;
import spacefiller.graph.Graph;
import spacefiller.graph.Node;

import java.io.Serializable;

public class BasicGraphRenderer implements GraphRenderer, Serializable {
  private float thickness;
  private int color;

  public BasicGraphRenderer(float thickness) {
    this.thickness = thickness;
    this.color = 0xFFFFFFFF;
  }

  @Override
  public void render(PGraphics graphics, Graph graph) {
    graphics.fill(color);
    graphics.stroke(color);
    graphics.strokeWeight(thickness);

    for (Node n : graph.getNodes()) {
      graphics.ellipse(n.getPosition().x, n.getPosition().y, 3, 3);
    }

    for (Edge e: graph.getEdges()) {
      graphics.line(e.n1.getPosition().x, e.n1.getPosition().y, e.n2.getPosition().x, e.n2.getPosition().y);
    }
  }

  public void setColor(int color) {
    this.color = color;
  }
}
