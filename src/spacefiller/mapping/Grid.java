package spacefiller.mapping;

import spacefiller.graph.Graph;
import spacefiller.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miller on 8/22/17.
 */
public class Grid extends Graph {
  private List<Node[]> triangles;
  private List<Quad> squares;
  private Quad boundingQuad;
  private float cellSize;
  private int columns;
  private int rows;

  public Grid(String name) {
    triangles = new ArrayList<>();
    squares = new ArrayList<>();
  }

  public void addTriangle(Node n1, Node n2, Node n3) {
    addTriangle(new Node[] {n1, n2, n3});
  }

  public void addTriangle(Node[] nodes) {
    triangles.add(nodes);
  }

  public void addSquare(Node tl, Node tr, Node br, Node bl, Node center) {
    Quad quad = new Quad(tl, tr, br, bl, center);
    addSquare(quad);
  }

  public void addSquare(Quad quad) {
    squares.add(quad);
  }

  public List<Node[]> getTriangles() {
    return triangles;
  }

  public List<Quad> getSquares() {
    return squares;
  }

  public Quad getBoundingQuad() {
    return boundingQuad;
  }

  public float getWidth() {
    return boundingQuad.getTopRight().getPosition().x - boundingQuad.getTopLeft().getPosition().x;
  }

  public float getHeight() {
    return boundingQuad.getBottomLeft().getPosition().y - boundingQuad.getTopLeft().getPosition().y;
  }

  public void setBoundingQuad(Quad boundingQuad) {
    this.boundingQuad = boundingQuad;
  }

  public float getCellSize() {
    return cellSize;
  }

  public void setCellSize(float cellSize) {
    this.cellSize = cellSize;
  }

  public int getColumns() {
    return columns;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  @Override
  public void translate(float dx, float dy) {
    super.translate(dx, dy);
    boundingQuad.translate(dx, dy);
  }

  @Override
  public void scale(float scale) {
    super.scale(scale);
    for (Node node : boundingQuad.getNodes()) {
      node.getPosition().mult(scale);
    }
  }

  @Override
  public void rotate(float theta) {
    super.rotate(theta);
    for (Node node : boundingQuad.getNodes()) {
      rotate(theta, node);
    }
  }
}
