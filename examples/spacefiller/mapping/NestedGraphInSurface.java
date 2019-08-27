package spacefiller.mapping;

import processing.core.PApplet;
import spacefiller.graph.Graph;
import spacefiller.graph.Node;
import spacefiller.graph.renderer.GraphRenderer;
import spacefiller.graph.renderer.SinGraphRenderer;

public class NestedGraphInSurface extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.mapping.NestedGraphInSurface");
  }

  @Override
  public void settings() {
    size(1920, 1080, P3D);
  }

  private Mapper mapper;
  private Surface surface;
  private Graph graph;
  private GraphTransformer graphTransformer;
  private GraphRenderer renderer;

  @Override
  public void setup() {
    mapper = Mapper.load("nested_graph_test", this);
    surface = mapper.createSurface("surface", 10, 10, 50);

    graph = mapper.createGraph("graph", (graph) -> {
      Node n1 = graph.createNode(200, 200);
      Node n2 = graph.createNode(200, 250);
      graph.createEdge(n1, n2);
    });

    renderer = new SinGraphRenderer();
  }

  @Override
  public void draw() {
    background(0);

    surface.drawToSurface(graphics -> {
      graphics.background(0);
      renderer.render(graphics, graph);
    });
  }
}