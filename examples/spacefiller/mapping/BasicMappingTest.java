package spacefiller.mapping;

import processing.core.PApplet;
import spacefiller.graph.GridUtils;

public class BasicMappingTest extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.mapping.BasicMappingTest");
  }

  private Mapper mapper;
  private Surface surface;

  @Override
  public void settings() {
    size(1920, 1080, P3D);
  }


  @Override
  public void setup() {
    mapper = new Mapper(this);

    surface = mapper.createSurface(10, 10, 50);
  }

  @Override
  public void draw() {
    background(0);

    surface.getCanvas().beginDraw();
    surface.getCanvas().background(0);
    surface.getCanvas().fill(255, 0 , 250);
    surface.getCanvas().ellipse(250, 250, 300, 300);
    // surface.renderUI(getGraphics());
    surface.getCanvas().endDraw();
  }
}