package spacefiller.graphics;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;
import spacefiller.mapping.Surface;

import static processing.core.PConstants.P2D;

public class Mask {
//  private static final String[] FRAG_SHADER =
//      ("#version 150\n" +
//          "uniform ivec2 size;\n" +
//      "\n" +
//      "uniform sampler2D mask;\n" +
//      "uniform sampler2D image;\n" +
//      "\n" +
//      "void main() {\n" +
//      "  vec2 position = (gl_FragCoord.xy / size.xy);\n" +
//      "\n" +
//      "  vec4 maskPixel = texture2D(mask, position);\n" +
//      "  vec4 imagePixel = texture2D(image, position);\n" +
//      "\n" +
//      "  gl_FragColor = mix(vec4(0), imagePixel, maskPixel.r);\n" +
//      "}").split("\n");
//
//  private static final String[] VERT_SHADER =
//      ("#version 150\n" +
//          "uniform mat4 transform;\n" +
//      "\n" +
//      "in vec4 position;\n" +
//      "\n" +
//      "\n" +
//      "void main() {\n" +
//      "  gl_Position = transform * position;\n" +
//      "}").split("\n");

  private PShader maskShader;
  private PGraphics maskLayer;
  private PGraphics canvas;
  private PGraphics composite;

  public Mask(PApplet parent, int width, int height) {
    maskShader = parent.loadShader("shaders/mask.frag");

    maskLayer = parent.createGraphics(width, height, P2D);
    canvas = parent.createGraphics(width, height, P2D);
    composite = parent.createGraphics(width, height, P2D);
  }

  public void drawToMask(Mask.DrawingFunction drawingFunction) {
    maskLayer.beginDraw();
    maskLayer.background(0);
    maskLayer.fill(255);
    maskLayer.noStroke();
    drawingFunction.draw(maskLayer);
    maskLayer.endDraw();
  }

  public void drawToBackground(Mask.DrawingFunction drawingFunction) {
    canvas.beginDraw();
    drawingFunction.draw(canvas);
    canvas.endDraw();
  }

  public PImage computeMask() {
    composite.beginDraw();
    composite.shader(maskShader);
    maskShader.set("size", maskLayer.width, maskLayer.height);
    maskShader.set("mask", maskLayer);
    maskShader.set("image", canvas);
    composite.rect(0, 0, maskLayer.width, maskLayer.height);
    composite.resetShader();
    composite.endDraw();

    return composite;
  }

  public interface DrawingFunction {
    void draw(PGraphics graphics);
  }
}
