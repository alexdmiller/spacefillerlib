package spacefiller.mapping;

import processing.core.PApplet;
import spacefiller.graph.Graph;
import spacefiller.graph.GridUtils;
import spacefiller.graph.Node;

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

  @Override
  public void setup() {
    mapper = new Mapper(this);

    surface = GridUtils.createGraphTransformer(5, 5, 100);
    surface.createCanvas(this);

    mapper.addTransformable(surface);

    graph = new Graph();
    Node n1 = graph.createNode(200, 200);
    Node n2 = graph.createNode(200, 250);
    graph.createEdge(n1, n2);

    graphTransformer = new GraphTransformer(graph);
    surface.addChild(graphTransformer);
  }

  @Override
  public void draw() {
    background(0);

    surface.getCanvas().beginDraw();
    surface.getCanvas().background(0);
    surface.renderUI(getGraphics());
    surface.getCanvas().endDraw();

    surface.drawImage(getGraphics());
  }
}