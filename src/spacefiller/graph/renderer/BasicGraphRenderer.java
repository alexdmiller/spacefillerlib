package spacefiller.graph.renderer;

import processing.core.PGraphics;
import spacefiller.graph.Edge;
import spacefiller.graph.Graph;
import spacefiller.graph.Node;

public class BasicGraphRenderer implements GraphRenderer {
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
      graphics.ellipse(n.position.x, n.position.y, 3, 3);
    }

    for (Edge e: graph.getEdges()) {
      graphics.line(e.n1.position.x, e.n1.position.y, e.n2.position.x, e.n2.position.y);
    }
  }

  public void setColor(int color) {
    this.color = color;
  }
}
