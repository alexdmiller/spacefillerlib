package spacefiller.graph;

import processing.core.PVector;

import java.io.Serializable;
import java.util.*;

/**
 * Created by miller on 7/16/17.
 */
public class Graph implements Serializable {
  private List<Node> nodes;
  private Map<Node, Set<Node>> nodeNeighbors;
  private List<Edge> edges;

  private String name;

  public Graph() {
    nodes = new ArrayList<>();
    nodeNeighbors = new HashMap<>();
    edges = new ArrayList<>();
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public Node createNode(float x, float y) {
    Node n = new Node(this, x, y);
    nodes.add(n);
    return n;
  }

  /*
  TODO: remove Edge class?
        remove connections array from Node?
  */

  public Node createNode(Node n) {
    Node newNode = new Node(this, n.getPosition().x, n.getPosition().y);
    nodes.add(newNode);
    return newNode;
  }

  public void createEdge(Node n1, Node n2) {
//    Edge e = new Edge(n1, n2);
//    edges.add(e);
//    n1.connections.add(e);
//    n2.connections.add(e);
//    return e;

    if (!nodeNeighbors.containsKey(n1)) {
      nodeNeighbors.put(n1, new HashSet());
    }

    if (!nodeNeighbors.containsKey(n2)) {
      nodeNeighbors.put(n2, new HashSet());
    }

    nodeNeighbors.get(n1).add(n2);
    nodeNeighbors.get(n2).add(n1);

    edges.add(new Edge(n1, n2));
  }

  public Graph copy() {
    Map<Node, Node> oldToNew = new HashMap<>();
    Graph newGraph = new Graph();
    for (Node n : nodes) {
      Node newNode = newGraph.createNode(n.getPosition().x, n.getPosition().y);
      oldToNew.put(n, newNode);
    }

    for (Edge e : edges) {
      Node n1 = oldToNew.get(e.n1);
      Node n2 = oldToNew.get(e.n2);
      newGraph.createEdge(n1, n2);
    }

    return newGraph;
  }

  public PVector getRandomPointOnEdge() {
    Edge e = edges.get((int) Math.floor(Math.random() * edges.size()));

    PVector delta = PVector.sub(e.n1.getPosition(), e.n2.getPosition());
    float d = (float) Math.random();
    return new PVector(e.n1.getPosition().x + delta.x * d, e.n1.getPosition().y + delta.y * d);
  }

  public PVector computeCentroid() {
    PVector center = new PVector(0, 0);
    for (Node n : nodes) {
      center.add(n.getPosition());
    }
    center.div(nodes.size());
    return center;
  }

  public void translate(float dx, float dy) {
    for (Node node : nodes) {
      node.getPosition().x += dx;
      node.getPosition().y += dy;
    }
  }

  public void scale(float scale) {
    for (Node node : nodes) {
      node.getPosition().mult(scale);
    }
  }

  public void rotate(float theta) {
    for (Node node : nodes) {
      rotate(theta, node);
    }
  }

  protected void rotate(float theta, Node node) {
    PVector p = node.getPosition();
    float newX = (float) (p.x * Math.cos(theta) - p.y * Math.sin(theta));
    float newY = (float) (p.x * Math.sin(theta) + p.y * Math.cos(theta));
    p.x = newX;
    p.y = newY;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Node> getNeighbors(Node node) {
    return null;
  }
}
