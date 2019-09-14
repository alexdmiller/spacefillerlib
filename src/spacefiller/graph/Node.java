package spacefiller.graph;

import processing.core.PVector;
import spacefiller.mapping.Pin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable, Pin {
  public PVector position;
  private List<NodeListener> listeners;
  private Graph graph;

  public Node(Graph graph) {
    this.graph = graph;
    position = new PVector();
    listeners = new ArrayList<>();
  }

  public Node(Graph graph, float x, float y) {
    this.graph = graph;
    position = new PVector(x, y);
    listeners = new ArrayList<>();
  }

  public void listen(NodeListener listener) {
    listeners.add(listener);
  }

  public Node copy() {
    Node n = new Node(graph);
    n.position = this.position.copy();
    return n;
  }

  @Override
  public String toString() {
    return position.toString();
  }

  @Override
  public PVector getPosition() {
    return position;
  }

  @Override
  public void moveTo(float x, float y) {
    position.set(x, y);
    for (NodeListener listener : listeners) {
      listener.nodeUpdated();
    }
  }

  @Override
  public void translate(float dx, float dy) {
    position.add(dx, dy);
    for (NodeListener listener : listeners) {
      listener.nodeUpdated();
    }
  }

  public List<Node> getNeighbors() {
    return graph.getNeighbors(this);
  }
}
