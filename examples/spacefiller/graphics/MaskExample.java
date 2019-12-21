package spacefiller.graphics;

import processing.core.PApplet;

public class MaskExample extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.graphics.MaskExample");
  }

  private Mask mask;

  @Override
  public void settings() {
    size(500, 500, P2D);
  }


  @Override
  public void setup() {
    mask = new Mask(this, 500, 500);

    mask.drawToMask(graphics -> {
      graphics.ellipse(250, 250, 500, 500);
    });

    mask.drawToBackground(graphics -> {
      graphics.beginShape();

      graphics.fill(255, 0, 0);
      graphics.vertex(0, 0);

      graphics.fill(255, 0, 0);
      graphics.vertex(500, 0);

      graphics.fill(255, 0, 255);
      graphics.vertex(500, 500);

      graphics.fill(255, 0, 255);
      graphics.vertex(0, 500);

      graphics.endShape();
    });
  }

  @Override
  public void draw() {
    background(0);

    image(mask.computeMask(), 0, 0);
  }
}