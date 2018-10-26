package spacefiller.mapping;

import processing.core.PVector;
import spacefiller.graph.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by miller on 8/21/17.
 */
public class Quad implements Serializable {
  private Node topLeft;
  private Node topRight;
  private Node bottomLeft;
  private Node bottomRight;
  private Node center;
  private Node[][] triangles;

  public Quad(Node topLeft, Node topRight, Node bottomRight, Node bottomLeft) {
    this.topLeft = topLeft;
    this.topRight = topRight;
    this.bottomLeft = bottomLeft;
    this.bottomRight = bottomRight;
  }

  public Quad(Node topLeft, Node topRight, Node bottomRight, Node bottomLeft, Node center) {
    this.topLeft = topLeft;
    this.topRight = topRight;
    this.bottomLeft = bottomLeft;
    this.bottomRight = bottomRight;
    this.center = center;
    computeTriangles();
  }

  private void computeTriangles() {
    this.triangles = new Node[4][3];
    triangles[0] = new Node[] { topLeft, topRight, center };
    triangles[1] = new Node[] { topRight, bottomRight, center };
    triangles[2] = new Node[] { bottomRight, bottomLeft, center };
    triangles[3] = new Node[] { bottomLeft, topLeft, center };
  }

  public List<PVector> getVertices() {
    List<PVector> list = new ArrayList<>();
    list.add(topLeft.position);
    list.add(topRight.position);
    list.add(bottomRight.position);
    list.add(bottomLeft.position);
    return list;
  }

  public List<Node> getNodes() {
    return Arrays.asList(new Node[] {topLeft, topRight, bottomLeft, bottomRight});
  }

  public void translate(float dx, float dy) {
    for (PVector v : getVertices()) {
      v.x += dx;
      v.y += dy;
    }
  }

  public Node getTopLeft() {
    return topLeft;
  }

  public void setTopLeft(Node topLeft) {
    this.topLeft = topLeft;
  }

  public Node getTopRight() {
    return topRight;
  }

  public void setTopRight(Node topRight) {
    this.topRight = topRight;
  }

  public Node getBottomLeft() {
    return bottomLeft;
  }

  public void setBottomLeft(Node bottomLeft) {
    this.bottomLeft = bottomLeft;
  }

  public Node getBottomRight() {
    return bottomRight;
  }

  public void setBottomRight(Node bottomRight) {
    this.bottomRight = bottomRight;
  }

  public Node getCenter() {
    return center;
  }

  public void setCenter(Node center) {
    this.center = center;
  }

  public PVector getComputedCenter() {
    PVector center = new PVector();
    for (PVector vertex : getVertices()) {
      center.add(vertex);
    }

    center.div(getVertices().size());
    return center;
  }

  public Quad copy() {
    return new Quad(
        topLeft.copy(), topRight.copy(), bottomRight.copy(), bottomLeft.copy(), center != null ? center.copy() : null
    );
  }

  public Node[][] getTriangles() {
    return triangles;
  }

  @Override
  public String toString() {
    return topLeft + ", " + topRight + ", " + bottomRight + ", " + bottomLeft;
  }

  public float getWidth() {
    return topRight.position.x - topLeft.position.x;
  }

  public float getHeight() {
    return topRight.position.x - topLeft.position.x;
  }
}
