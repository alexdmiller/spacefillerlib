package spacefiller.graph;

import processing.core.PVector;
import spacefiller.mapping.Pin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable, Pin {
  public PVector position;
  public List<Edge> connections;

  private List<NodeListener> listeners;

  protected Node() {
    position = new PVector();
    connections = new ArrayList<>();
    listeners = new ArrayList<>();
  }

  public void listen(NodeListener listener) {
    listeners.add(listener);
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
}
