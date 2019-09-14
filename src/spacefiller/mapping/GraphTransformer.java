package spacefiller.mapping;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import spacefiller.graph.Graph;
import spacefiller.graph.Node;
import spacefiller.graph.renderer.BasicGraphRenderer;
import spacefiller.graph.renderer.GraphRenderer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class GraphTransformer extends Transformable implements Draggable {
  private Graph graph;

  public GraphTransformer(Graph graph) {
    this.graph = graph;
  }

  @Override
  public void moveTo(float x, float y) {
    // TODO: be smarter about this
    translate(x, y);
  }

  public void translate(float dx, float dy) {
    graph.translate(dx, dy);
  }

  public void translate(PVector vector) {
    translate(vector.x, vector.y);
  }

  @Override
  public void scale(float scale) {
    scale(graph.computeCentroid(), scale);
  }

  public void scale(PVector origin, float scale) {
    translate(origin.mult(-1));
    graph.scale(scale);
    translate(origin.mult(-1));
  }

  @Override
  public void rotate(float theta) {
    PVector origin = getCenter();
    translate(PVector.mult(origin, -1));
    graph.rotate(theta);
    translate(origin);
  }


  @Override
  public Draggable select(PVector point, boolean innerNodes) {
    // first, see if the point is over a node
    for (Node node : graph.getNodes()) {
      if (point.dist(node.getPosition()) < 30) {
        return node;
      }
    }

    // TODO: then, see if point is over an edge
    //    if (isPointOver(point)) {
    //      return this;
    //    }

    return null;
  }

  @Override
  public Pin selectClosestPin(PVector point) {
    PVector relative = new PVector(point.x, point.y);
    Pin closest = null;

    // List<Node> nodes = getPostTransformGrid().getBoundingQuad().getNodes();
    List<Node> nodes = graph.getNodes();
    for (int i = 0; i < nodes.size(); i++) {
      if (closest == null || nodes.get(i).getPosition().dist(relative) < closest.getPosition().dist(relative)) {
        closest = nodes.get(i);
      }
    }

    return closest;
  }

  @Override
  public PVector getCenter() {
    return graph.computeCentroid();
  }

  // TODO: this is meaningless here, because a graph doesn't represent a surface with relative points
  // reveals problem in the Transformable interface trying to capture too much
  @Override
  public PVector getRelativePoint(PVector point) {
    return null;
  }

  // TODO: compute if mouse is over nodes & edges
  @Override
  public boolean isPointOver(PVector point) {
    for (Node node : graph.getNodes()) {
      if (point.dist(node.getPosition()) < 30) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void renderUI(PGraphics graphics) {
    if (showUI) {
      GraphRenderer graphRenderer = new BasicGraphRenderer(1);
      graphRenderer.render(graphics, graph);
    }

    for (Transformable t : getChildren()) {
      t.renderUI(graphics);
    }
  }
}
