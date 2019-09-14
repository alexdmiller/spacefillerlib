package spacefiller.mapping;

import processing.core.*;
import processing.event.MouseEvent;
import spacefiller.graph.Edge;
import spacefiller.graph.Node;
import spacefiller.graph.NodeListener;
import spacefiller.graph.renderer.BasicGraphRenderer;

import javax.media.jai.PerspectiveTransform;
import javax.media.jai.WarpPerspective;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spacefiller.mapping.Mapper.ACTIVE_COLOR;
import static spacefiller.mapping.Mapper.DESELECTED_COLOR;

public class Surface extends Transformable implements Draggable, NodeListener, Serializable, Drawable {
  // This grid stores the original node positions, before a perspective transformation
  // is applied.
  private Grid preTransformGrid;

  // And this grid stores the nodes after the transformation has been applied.
  private Grid postTransformGrid;

  private GraphTransformer graphTransformer;

  // Maps the post-transform nodes to pre-transform nodes
  private Map<Node, Node> postToPre;

  private WarpPerspective currentPerspective;
  private BasicGraphRenderer graphRenderer;
  private String name;

  private transient PGraphics canvas;
  private transient boolean canvasDrawn;
  private transient boolean showMesh;
  private transient List<MouseEventListener> mouseEventListeners;

  public Surface(Grid grid, String name) {
    this.postTransformGrid = grid;
    this.name = name;

    for (Node node : postTransformGrid.getBoundingQuad().getNodes()) {
      node.listen(this);
    }

    this.postToPre = new HashMap<>();
    makeGraphCopy();

    graphTransformer = new GraphTransformer(postTransformGrid);

    recomputeNodesFromQuad();

    graphRenderer = new BasicGraphRenderer(1);
  }

  public Surface(Grid grid) {
    this(grid, "untitled surface");
  }

  public void createCanvas(PApplet parent) {
    canvas = parent.createGraphics((int) preTransformGrid.getWidth(), (int) preTransformGrid.getHeight(), PConstants.P3D);
  }

  public WarpPerspective getPerspective() {
    return currentPerspective;
  }

  protected void setCanvas(PGraphics canvas) {
    this.canvas = canvas;
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

  public PerspectiveTransform updatePerspective() {
    Quad postQuad = postTransformGrid.getBoundingQuad();
    Quad preQuad = preTransformGrid.getBoundingQuad();

    PerspectiveTransform transform = PerspectiveTransform.getQuadToQuad(
        preQuad.getTopLeft().getPosition().x, preQuad.getTopLeft().getPosition().y,
        preQuad.getTopRight().getPosition().x, preQuad.getTopRight().getPosition().y,
        preQuad.getBottomRight().getPosition().x, preQuad.getBottomRight().getPosition().y,
        preQuad.getBottomLeft().getPosition().x, preQuad.getBottomLeft().getPosition().y,
        postQuad.getTopLeft().getPosition().x, postQuad.getTopLeft().getPosition().y,
        postQuad.getTopRight().getPosition().x, postQuad.getTopRight().getPosition().y,
        postQuad.getBottomRight().getPosition().x, postQuad.getBottomRight().getPosition().y,
        postQuad.getBottomLeft().getPosition().x, postQuad.getBottomLeft().getPosition().y);

    currentPerspective = new WarpPerspective(transform);

    return transform;
  }

  public void recomputeNodesFromQuad() {
    PerspectiveTransform transform = updatePerspective();

    float[] srcPoints = new float[postTransformGrid.getNodes().size() * 2];

    int i = 0;
    for (Node node : preTransformGrid.getNodes()) {
      srcPoints[i] = node.getPosition().x;
      srcPoints[i + 1] = node.getPosition().y;
      i += 2;
    }

    float[] destPoints = new float[postTransformGrid.getNodes().size() * 2];

    transform.transform(srcPoints, 0, destPoints, 0, postTransformGrid.getNodes().size());

    i = 0;
    for (Node node : postTransformGrid.getNodes()) {
      node.getPosition().x = destPoints[i];
      node.getPosition().y = destPoints[i + 1];
      i += 2;
    }

  }

  public void draw(PGraphics graphics) {
    graphics.beginShape(PApplet.TRIANGLES);
    graphics.noStroke();
    graphics.texture(canvas);
    for (Node[] triangle : postTransformGrid.getTriangles()) {
      for (int i = 0; i < triangle.length; i++) {
        Node preNode = postToPre.get(triangle[i]);
        graphics.vertex(
            triangle[i].getPosition().x, triangle[i].getPosition().y,
            preNode.getPosition().x, preNode.getPosition().y);
      }
    }
    graphics.endShape(PConstants.CLOSE);
  }

  private void makeGraphCopy() {
    preTransformGrid = new Grid(postTransformGrid.getName() + " pre");

    for (Node postNode : postTransformGrid.getNodes()) {
      Node preNode = preTransformGrid.createNode(postNode.getPosition().x, postNode.getPosition().y);
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

    preTransformGrid.setColumns(postTransformGrid.getColumns());
    preTransformGrid.setColumns(postTransformGrid.getRows());
    preTransformGrid.setCellSize(postTransformGrid.getCellSize());
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
  public void translate(float dx, float dy) {
    graphTransformer.translate(dx, dy);
    updatePerspective();
  }

  @Override
  public void scale(float scale) {
    graphTransformer.scale(scale);
    updatePerspective();
  }

  @Override
  public void rotate(float theta) {
    graphTransformer.rotate(theta);
    updatePerspective();
  }

  @Override
  public Draggable select(PVector point, boolean innerNodes) {
    if (showMesh) {
      for (Node node : postTransformGrid.getNodes()) {
        if (point.dist(node.getPosition()) < 30) {
          return node;
        }
      }
    } else {
      // first, see if one of the control points are selected
      List<Node> nodes = postTransformGrid.getBoundingQuad().getNodes();
      for (int i = 0; i < nodes.size(); i++) {
        if (PApplet.dist(nodes.get(i).getPosition().x, nodes.get(i).getPosition().y, point.x, point.y) < 30) {
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

  @Override
  public boolean isPointOver(PVector point) {
    return
        isPointInTriangle(
            point.x, point.y,
            postTransformGrid.getBoundingQuad().getTopLeft().getPosition(),
            postTransformGrid.getBoundingQuad().getTopRight().getPosition(),
            postTransformGrid.getBoundingQuad().getBottomLeft().getPosition())
        ||
        isPointInTriangle(
            point.x, point.y,
            postTransformGrid.getBoundingQuad().getTopRight().getPosition(),
            postTransformGrid.getBoundingQuad().getBottomRight().getPosition(),
            postTransformGrid.getBoundingQuad().getBottomLeft().getPosition());
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

    // List<Node> nodes = getPostTransformGrid().getBoundingQuad().getNodes();
    List<Node> nodes = getPostTransformGrid().getNodes();
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

  @Override
  public PVector getRelativePoint(PVector p) {
    Point2D point = currentPerspective.mapSourcePoint(new Point((int) p.x, (int) p.y));
    return new PVector((int) point.getX(), (int) point.getY());
  }

  @Override
  public void renderUI(PGraphics graphics) {
    canvas.beginDraw();

    if (!canvasDrawn) {
      canvas.background(0);
    }

    if (showUI) {
      int color = active ? ACTIVE_COLOR : DESELECTED_COLOR;
      graphics.noFill();
      graphics.stroke(color);
      graphics.strokeWeight(1);
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

      if (showMesh) {
        graphRenderer.setColor(color);
        graphRenderer.render(canvas, preTransformGrid);
      }
    }

    for (Transformable t : getChildren()) {
      t.renderUI(canvas);
    }
    canvas.endDraw();
  }

  @Override
  public void nodeUpdated() {
    recomputeNodesFromQuad();
  }

  public void resetTransform() {
    postTransformGrid.getBoundingQuad().getTopLeft().getPosition().set(preTransformGrid.getBoundingQuad().getTopLeft().getPosition());
    postTransformGrid.getBoundingQuad().getTopRight().getPosition().set(preTransformGrid.getBoundingQuad().getTopRight().getPosition());
    postTransformGrid.getBoundingQuad().getBottomLeft().getPosition().set(preTransformGrid.getBoundingQuad().getBottomLeft().getPosition());
    postTransformGrid.getBoundingQuad().getBottomRight().getPosition().set(preTransformGrid.getBoundingQuad().getBottomRight().getPosition());
    recomputeNodesFromQuad();
  }

  public boolean isShowMesh() {
    return showMesh;
  }

  public void setShowMesh(boolean showMesh) {
    this.showMesh = showMesh;
  }

  public void drawToSurface(DrawingFunction drawingFunction) {
    canvas.beginDraw();
//    canvas.background(0);
    drawingFunction.draw(canvas);
    canvas.endDraw();
    canvasDrawn = true;
  }


  public interface DrawingFunction {
    void draw(PGraphics graphics);
  }

  public interface MouseEventListener {
    void process(MouseEvent event);
  }

  public void onMouseEvent(MouseEventListener mouseEventListener) {
    if (mouseEventListeners == null) {
      mouseEventListeners = new ArrayList<>();
    }

    mouseEventListeners.add(mouseEventListener);
  }

  protected void mouseEvent(MouseEvent event) {
    MouseEvent transformedEvent = new MouseEvent(
        event.getNative(),
        event.getMillis(),
        event.getAction(),
        event.getModifiers(),
        event.getX(),
        event.getY(),
        event.getButton(),
        event.getCount());

    if (mouseEventListeners != null) {
      mouseEventListeners.forEach(listener -> listener.process(transformedEvent));
    }

    // TODO: notify children of mouse event (if there are ever nested surfaces?)
  }

  public float getWidth() {
    return preTransformGrid.getWidth();
  }

  public float getHeight() {
    return preTransformGrid.getHeight();
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PGraphics getCanvas() {
    return canvas;
  }
}
