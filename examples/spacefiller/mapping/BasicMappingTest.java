package spacefiller.mapping;

import processing.core.PApplet;

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
    mapper = Mapper.load("basis_mapping_test", this);
    surface = mapper.createSurface("surface", 10, 10, 50);
  }

  @Override
  public void draw() {
    background(0);

    surface.drawToSurface(graphics -> {
      graphics.fill(255, 0 , 250);
      graphics.ellipse(250, 250, 300, 300);
    });
  }
}