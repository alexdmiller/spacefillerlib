package spacefiller.mapping;

import processing.core.*;
import spacefiller.graph.Edge;
import spacefiller.graph.Node;
import spacefiller.graph.NodeListener;
import spacefiller.graph.renderer.BasicGraphRenderer;
import spacefiller.graph.renderer.GraphRenderer;

import javax.media.jai.PerspectiveTransform;
import javax.media.jai.WarpPerspective;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphTransformer extends Transformable implements Draggable, NodeListener, Serializable {
  // This grid stores the original node positions, before a perspective transformation
  // is applied.
  private Grid preTransformGrid;

  // And this grid stores the nodes after the transformation has been applied.
  private Grid postTransformGrid;

  // Maps the post-transform nodes to pre-transform nodes
  private Map<Node, Node> postToPre;

  private WarpPerspective currentPerspective;

  private transient PGraphics canvas;

  public GraphTransformer(Grid grid) {
    this.postTransformGrid = grid;

    for (Node node : postTransformGrid.getBoundingQuad().getNodes()) {
      node.listen(this);
    }

    this.postToPre = new HashMap<>();
    makeGraphCopy();

    recomputeNodesFromQuad();
  }

  public void createCanvas(PApplet parent) {
    canvas = parent.createGraphics((int) preTransformGrid.getWidth(), (int) preTransformGrid.getHeight());
  }

  public WarpPerspective getPerspective() {
    return currentPerspective;
  }

  public PGraphics getCanvas() {
    return canvas;
  }

  public void setCanvas(PGraphics canvas) {
    this.canvas = canvas;
  }

  public void translate(float dx, float dy) {
    for (Node node : postTransformGrid.getNodes()) {
      node.position.x += dx;
      node.position.y += dy;
    }

    postTransformGrid.getBoundingQuad().translate(dx, dy);
  }

  public void translate(PVector vector) {
    translate(vector.x, vector.y);
  }

  public Grid getPreTransformGrid() {
    return preTransformGrid;
  }

  public Grid getPostTransformGrid() {
    return postTransformGrid;
  }

  public Node getPreNode(Node postNode) {
    return postToPre.get(postNode);
  }

  public void recomputeNodesFromQuad() {
    Quad postQuad = postTransformGrid.getBoundingQuad();
    Quad preQuad = preTransformGrid.getBoundingQuad();

    PerspectiveTransform transform = PerspectiveTransform.getQuadToQuad(
        preQuad.getTopLeft().position.x, preQuad.getTopLeft().position.y,
        preQuad.getTopRight().position.x, preQuad.getTopRight().position.y,
        preQuad.getBottomRight().position.x, preQuad.getBottomRight().position.y,
        preQuad.getBottomLeft().position.x, preQuad.getBottomLeft().position.y,
        postQuad.getTopLeft().position.x, postQuad.getTopLeft().position.y,
        postQuad.getTopRight().position.x, postQuad.getTopRight().position.y,
        postQuad.getBottomRight().position.x, postQuad.getBottomRight().position.y,
        postQuad.getBottomLeft().position.x, postQuad.getBottomLeft().position.y);

    currentPerspective = new WarpPerspective(transform);

    float[] srcPoints = new float[postTransformGrid.getNodes().size() * 2];

    int i = 0;
    for (Node node : preTransformGrid.getNodes()) {
      srcPoints[i] = node.position.x;
      srcPoints[i + 1] = node.position.y;
      i += 2;
    }

    float[] destPoints = new float[postTransformGrid.getNodes().size() * 2];

    transform.transform(srcPoints, 0, destPoints, 0, postTransformGrid.getNodes().size());

    i = 0;
    for (Node node : postTransformGrid.getNodes()) {
      node.position.x = destPoints[i];
      node.position.y = destPoints[i + 1];
      i += 2;
    }

  }

  public void drawImage(PGraphics graphics) {
    graphics.beginShape(PApplet.TRIANGLES);
    graphics.noStroke();
    graphics.texture(canvas);
    for (Node[] triangle : postTransformGrid.getTriangles()) {
      for (int i = 0; i < triangle.length; i++) {
        Node preNode = postToPre.get(triangle[i]);
        graphics.vertex(
            triangle[i].position.x, triangle[i].position.y,
            preNode.position.x, preNode.position.y);
      }
    }
    graphics.endShape(PConstants.CLOSE);
  }

  private void makeGraphCopy() {
    preTransformGrid = new Grid();

    for (Node postNode : postTransformGrid.getNodes()) {
      Node preNode = preTransformGrid.createNode(postNode.position.x, postNode.position.y);
      postToPre.put(postNode, preNode);
    }

    for (Edge e : postTransformGrid.getEdges()) {
      Node n1 = postToPre.get(e.n1);
      Node n2 = postToPre.get(e.n2);
      preTransformGrid.createEdge(n1, n2);
    }

    preTransformGrid.setBoundingQuad(postTransformGrid.getBoundingQuad().copy());

    for (Node[] triangle : postTransformGrid.getTriangles()) {
      Node[] newTriangle = new Node[3];
      for (int i = 0; i < triangle.length; i++) {
        newTriangle[i] = postToPre.get(triangle[i]);
      }
      preTransformGrid.addTriangle(newTriangle);
    }

    for (Quad quad : postTransformGrid.getSquares()) {
      preTransformGrid.addSquare(copyQuad(quad));
    }
  }

  private Quad copyQuad(Quad quad) {
    return new Quad(
        postToPre.get(quad.getTopLeft()),
        postToPre.get(quad.getTopRight()),
        postToPre.get(quad.getBottomRight()),
        postToPre.get(quad.getBottomLeft()),
        postToPre.get(quad.getCenter())
    );
  }


  @Override
  public void moveTo(float x, float y) {
    // TODO: be smarter about this
    translate(x, y);
  }

  @Override
  public void scale(float scale) {
    scale(getPostTransformGrid().getBoundingQuad().getComputedCenter(), scale);
  }

  public void scale(PVector origin, float scale) {
    translate(origin.mult(-1));

    for (Node node : getPostTransformGrid().getNodes()) {
      node.position.mult(scale);
    }

    for (Node node : getPostTransformGrid().getBoundingQuad().getNodes()) {
      node.position.mult(scale);
    }

    translate(origin.mult(-1));
  }

  @Override
  public void rotate(float theta) {
    PVector origin = getCenter();
    translate(PVector.mult(origin, -1));

    for (Node node : postTransformGrid.getNodes()) {
      rotate(theta, node);
    }

    for (Node node : postTransformGrid.getBoundingQuad().getNodes()) {
      rotate(theta, node);
    }

    translate(origin);
  }

  private void rotate(float theta, Node node) {
    PVector p = node.getPosition();
    float newX = (float) (p.x * Math.cos(theta) - p.y * Math.sin(theta));
    float newY = (float) (p.x * Math.sin(theta) + p.y * Math.cos(theta));
    p.x = newX;
    p.y = newY;
  }

  /*
    public Draggable select(PVector point) {
    return select(point, true);
  }

  @Override
  public Pin selectClosestPin(PVector point) {
    PVector relative = new PVector(point.x, point.y);
    Pin closest = null;

    for (int i = 0; i < mesh.length; i++) {
      if (mesh[i].isControlPoint()) {
        if (closest == null || mesh[i].getPosition().dist(relative) < closest.getPosition().dist(relative)) {
          closest = mesh[i];
        }
      }
    }

    return closest;
  }

  public Draggable select(PVector point, boolean controlPoints) {
    if (controlPoints) {
      // first, see if one of the control points are selected
      for (int i = 0; i < mesh.length; i++) {
        if (PApplet.dist(mesh[i].x, mesh[i].y, point.x, point.y) < 30
            && mesh[i].isControlPoint())
          return mesh[i];
      }
    }

    // then, see if the surface itself is selected
    if (isPointOver(point)) {
      clickX = point.x;
      clickY = point.y;
      return this;
    }
    return null;
  }
  */

  @Override
  public Draggable select(PVector point, boolean innerNodes) {
    if (innerNodes) {
      for (Node node : postTransformGrid.getNodes()) {
        if (point.dist(node.position) < 30) {
          return node;
        }
      }
    } else {
      // first, see if one of the control points are selected
      List<Node> nodes = postTransformGrid.getBoundingQuad().getNodes();
      for (int i = 0; i < nodes.size(); i++) {
        if (PApplet.dist(nodes.get(i).position.x, nodes.get(i).position.y, point.x, point.y) < 30) {
          return nodes.get(i);
        }
      }
    }

    // then, see if the surface itself is selected
    if (isPointOver(point)) {
      return this;
    }

    return null;
  }

  public boolean isPointOver(PVector point) {
    return
        isPointInTriangle(
            point.x, point.y,
            postTransformGrid.getBoundingQuad().getTopLeft().position,
            postTransformGrid.getBoundingQuad().getTopRight().position,
            postTransformGrid.getBoundingQuad().getBottomLeft().position)
        ||
        isPointInTriangle(
            point.x, point.y,
            postTransformGrid.getBoundingQuad().getTopRight().position,
            postTransformGrid.getBoundingQuad().getBottomRight().position,
            postTransformGrid.getBoundingQuad().getBottomLeft().position);
  }

  private boolean isPointInTriangle(float x, float y, PVector a,
                                    PVector b, PVector c) {
    // http://www.blackpawn.com/texts/pointinpoly/default.html
    PVector v0 = new PVector(c.x - a.x, c.y - a.y);
    PVector v1 = new PVector(b.x - a.x, b.y - a.y);
    PVector v2 = new PVector(x - a.x, y - a.y);

    float dot00 = v0.dot(v0);
    float dot01 = v1.dot(v0);
    float dot02 = v2.dot(v0);
    float dot11 = v1.dot(v1);
    float dot12 = v2.dot(v1);

    // Compute barycentric coordinates
    float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
    float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

    // Check if point is in triangle
    return (u > 0) && (v > 0) && (u + v < 1);
  }

  @Override
  public Pin selectClosestPin(PVector point) {
    PVector relative = new PVector(point.x, point.y);
    Pin closest = null;

    List<Node> nodes = getPostTransformGrid().getBoundingQuad().getNodes();
    for (int i = 0; i < nodes.size(); i++) {
      if (closest == null || nodes.get(i).getPosition().dist(relative) < closest.getPosition().dist(relative)) {
        closest = nodes.get(i);
      }
    }

    return closest;
  }

  @Override
  public PVector getCenter() {
    return getPostTransformGrid().getBoundingQuad().getComputedCenter();
  }

  public PVector getRelativePoint(PVector p) {
    Point2D point = currentPerspective.mapSourcePoint(new Point((int) p.x, (int) p.y));
    return new PVector((int) point.getX(), (int) point.getY());
  }

//  @Override
//  public void renderUI(PGraphics graphics) {
//    graphics.stroke(255);
//    graphics.noFill();
//
//    graphics.strokeWeight(2);
//    graphics.beginShape();
//    for (PVector node : postTransformGrid.getBoundingQuad().getVertices()) {
//      graphics.vertex(node.x, node.y);
//    }
//    graphics.endShape(PConstants.CLOSE);
//
//    for (Node node : postTransformGrid.getNodes()) {
//      graphics.fill(255);
//      graphics.noStroke();
//      graphics.ellipse(node.position.x, node.position.y, 3 , 3);
//    }
//
//    for (Edge e : postTransformGrid.getEdges()) {
//      graphics.stroke(255);
//      graphics.strokeWeight(1);
//      graphics.line(e.n1.position.x, e.n1.position.y, e.n2.position.x, e.n2.position.y);
//    }
//
//
//  }


  @Override
  public void renderUI(PGraphics graphics) {
    if (showUI) {
      graphics.noFill();
      graphics.stroke(active ? 0xffffffff : 0x33ffffff);
      graphics.beginShape();
      for (Node node : postTransformGrid.getBoundingQuad().getNodes()) {
        graphics.vertex(node.getPosition().x, node.getPosition().y);
      }
      graphics.endShape(PConstants.CLOSE);

      for (Node node : postTransformGrid.getBoundingQuad().getNodes()) {
        graphics.noFill();
        graphics.stroke(255);
        graphics.ellipse(node.getPosition().x, node.getPosition().y, 10, 10);
      }

      GraphRenderer graphRenderer = new BasicGraphRenderer(1);
      graphRenderer.render(canvas, preTransformGrid);
    }

    for (Transformable t : getChildren()) {
      t.renderUI(canvas);
    }
  }

  @Override
  public void nodeUpdated() {
    recomputeNodesFromQuad();
  }

  public void resetTransform() {
    postTransformGrid.getBoundingQuad().getTopLeft().position.set(preTransformGrid.getBoundingQuad().getTopLeft().position);
    postTransformGrid.getBoundingQuad().getTopRight().position.set(preTransformGrid.getBoundingQuad().getTopRight().position);
    postTransformGrid.getBoundingQuad().getBottomLeft().position.set(preTransformGrid.getBoundingQuad().getBottomLeft().position);
    postTransformGrid.getBoundingQuad().getBottomRight().position.set(preTransformGrid.getBoundingQuad().getBottomRight().position);
    recomputeNodesFromQuad();
  }
}
