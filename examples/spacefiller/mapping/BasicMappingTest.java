package spacefiller.mapping;

import processing.core.PApplet;
import processing.core.PGraphics;
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

    surface.drawToCanvas(graphics -> {
      graphics.fill(255, 0 , 250);
      graphics.ellipse(250, 250, 300, 300);
    });
  }
}