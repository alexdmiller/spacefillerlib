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

  @Override
  public void settings() {
    size(1920, 1080, P3D);
  }


  @Override
  public void setup() {
    mapper = Mapper.load("basic_graph_test", this);

    graph = mapper.createGraph("graph", (graph) -> {
      Node n1 = graph.createNode(200, 200);
      Node n2 = graph.createNode(200, 250);
      graph.createEdge(n1, n2);
    });
  }

  @Override
  public void draw() {
    background(0);
  }
}