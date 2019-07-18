package spacefiller.mapping;

import processing.core.PApplet;
import spacefiller.graph.Graph;
import spacefiller.graph.Node;

public class BasicGraphTest extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.mapping.BasicGraphTest");
  }

  private Mapper mapper;
  private Graph graph;
  private GraphTransformer graphTransformer;

  @Override
  public void settings() {
    size(1920, 1080, P3D);
  }


  @Override
  public void setup() {
    mapper = new Mapper(this);

    graph = new Graph();
    Node n1 = graph.createNode(200, 200);
    Node n2 = graph.createNode(200, 250);
    graph.createEdge(n1, n2);

    graphTransformer = new GraphTransformer(graph);
    mapper.addTransformable(graphTransformer);
  }

  @Override
  public void draw() {
    background(0);

    graphTransformer.renderUI(getGraphics());
  }
}