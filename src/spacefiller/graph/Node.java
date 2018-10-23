package spacefiller.graph;

import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
  public PVector position;
  public List<Edge> connections;

  protected Node() {
    position = new PVector();
    connections = new ArrayList<>();
  }

  public Node copy() {
    Node n = new Node();
    n.position = this.position.copy();
    return n;
  }

  @Override
  public String toString() {
    return position.toString();
  }
}
