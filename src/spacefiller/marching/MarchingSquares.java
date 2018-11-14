package spacefiller.marching;

import processing.core.PGraphics;
import processing.core.PVector;

import static processing.core.PConstants.LINES;

public class MarchingSquares {
  private static PVector[][] templates;

  static {
    templates = new PVector[16][];

    for (int i = 0; i < 16; i++) {
      templates[i] = new PVector[0];
    }

    // no corners
    templates[0] = new PVector[0];

    // bottom left
    templates[1] = new PVector[] {
        new PVector(0, 0.5f),
        new PVector(0.5f, 1),
    };

    // bottom right
    templates[2] = new PVector[] {
        new PVector(0.5f, 1),
        new PVector(1f, 0.5f),
    };

    // bl, br
    templates[3] = new PVector[] {
        new PVector(0, 0.5f),
        new PVector(1f, 0.5f),
    };

    // tr
    templates[4] = new PVector[] {
        new PVector(0.5f, 0),
        new PVector(1f, 0.5f),
    };

    templates[5] = new PVector[] {
        new PVector(0, 0.5f),
        new PVector(0.5f, 0f),
        new PVector(0.5f, 1f),
        new PVector(1f, 0.5f),
    };

    templates[6] = new PVector[] {
        new PVector(0.5f, 0),
        new PVector(0.5f, 1f),
    };

    templates[7] = new PVector[] {
        new PVector(0f, 0.5f),
        new PVector(0.5f, 0f),
    };

    templates[8] = templates[7];
    templates[9] = templates[6];

    templates[10] = new PVector[] {
        new PVector(0f, 0.5f),
        new PVector(0.5f, 1f),
        new PVector(0.5f, 0f),
        new PVector(1f, 0.5f),
    };

    templates[11] = templates[4];
    templates[12] = templates[3];
    templates[13] = templates[2];
    templates[14] = templates[1];
    templates[15] = templates[0];
  }

  private float cellSize = 20;
  private float threshold = 0.5f;
  private float width, height;
  private Function function;

  public Function getFunction() {
    return function;
  }

  public void setFunction(Function function) {
    this.function = function;
  }

  public MarchingSquares(float width, float height) {
    this.width = width;
    this.height = height;
  }

  public float getThreshold() {
    return threshold;
  }

  public void setThreshold(float threshold) {
    this.threshold = threshold;
  }

  public float getCellSize() {
    return cellSize;
  }

  public void setCellSize(float cellSize) {
    this.cellSize = cellSize;
  }

  public void draw(PGraphics canvas) {
    for (int x = 0; x < width / cellSize; x++) {
      for (int y = 0; y < height / cellSize; y++) {
        PVector noiseSpace = new PVector(x * cellSize, y * cellSize);

        int tl = function.value(noiseSpace.x - cellSize/2f, noiseSpace.y - cellSize/2f) > threshold ? 1 : 0;
        int tr = function.value(noiseSpace.x + cellSize/2f, noiseSpace.y - cellSize/2f) > threshold ? 1 : 0;
        int br = function.value(noiseSpace.x + cellSize/2f, noiseSpace.y + cellSize/2f) > threshold ? 1 : 0;
        int bl = function.value(noiseSpace.x - cellSize/2f, noiseSpace.y + cellSize/2f) > threshold ? 1 : 0;

        int index = bl + br * 2 + tr * 4 + tl * 8;

        PVector[] template = templates[index];

        canvas.beginShape(LINES);
        for (PVector p : template) {
          canvas.vertex(noiseSpace.x + p.x * cellSize, noiseSpace.y + p.y * cellSize);
        }
        canvas.endShape();
      }
    }
  }

  public interface Function {
    float value(float x, float y);
  }
}
