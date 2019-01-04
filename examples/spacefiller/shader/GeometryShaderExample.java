package spacefiller.shader;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PShader;

public class GeometryShaderExample extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.shader.GeometryShaderExample");
  }




  // TODO: the geometry shader doesn't seem to be sucessfully sampling from the
  // passed texture. try sampling from texture the normal way in fragment shader?
  // might have something to do with coordinates?


  private PShader shader;
  private float cellSize = 20;
  private PGraphics canvas;

  public void settings() {
    size(1080, 1080, P3D);
  }

  public void setup() {
    //
    shader = loadShader("basic.frag", "basic.vert"); //new GeometryShader(this, "basic.vert", "basic.geom", "basic.frag");
    canvas = createGraphics((int) ((float) width / cellSize), (int) ((float) height / cellSize), P2D);
  }

  public void draw() {
    background(0);

    canvas.beginDraw();
    canvas.loadPixels();
    for (int x = 0; x < canvas.width; x++) {
      for (int y = 0; y < canvas.height; y++) {
        canvas.pixels[y * canvas.width + x] = color(noise(x / 50f, y / 50f, frameCount / 100f) > 0.5f ? 255 : 0);
      }
    }
    canvas.updatePixels();
    canvas.endDraw();

    int gridWidth = (int) (width / cellSize);
    int gridHeight = (int) (height / cellSize);

    shader(shader);
    texture(canvas);
    noStroke();

    beginShape(TRIANGLE_STRIP);
    for (int x = 0; x < gridWidth; x++) {
      for (int y = 0; y < gridHeight; y++) {
        vertex(x * cellSize, y * cellSize);
      }
    }
    endShape();

    resetShader();
    image(canvas, 0, 0);
  }
}
