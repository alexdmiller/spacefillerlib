package spacefiller.mapping;

import processing.core.PApplet;
import spacefiller.graph.Graph;
import spacefiller.graph.Node;

public class SerializationTest extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.mapping.SerializationTest");
  }

  private Mapper mapper;
  private Graph graph;

  @Override
  public void settings() {
    size(1920, 1080, P3D);
  }


  @Override
  public void setup() {
    for (int i = 0; i < 100; i++) {
      mapper = Mapper.load("serialization_test", this);

      graph = mapper.createGraph("graph", (graph) -> {
        Node n1 = graph.createNode(200, 200);
        Node n2 = graph.createNode(200, 250);
        graph.createEdge(n1, n2);
        mapper.addToRoot(graph);
      });

      mapper.save();
    }
  }

  @Override
  public void draw() {
    background(0);
  }
}