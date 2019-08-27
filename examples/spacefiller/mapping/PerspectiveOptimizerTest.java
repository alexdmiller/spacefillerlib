package spacefiller.mapping;

import processing.core.PApplet;
import processing.core.PVector;
import spacefiller.graph.Node;

import java.awt.geom.Point2D;

public class PerspectiveOptimizerTest extends PApplet {


  public static void main(String[] args) {
    main("spacefiller.mapping.PerspectiveOptimizerTest");
  }

  public void settings() {
    size(1920, 1080, P3D);
  }

  private Mapper mapper;
  private Surface transformer;
  private PVector[] preTransformPoints;
  private PVector[] postTransformPoints;
  private Quad actualQuad;

  public void setup() {
    mapper = Mapper.load(this);
    transformer = mapper.createSurface(10, 10, 50);

    preTransformPoints = new PVector[10];
    for (int i = 0; i < preTransformPoints.length; i++) {
      preTransformPoints[i] = new PVector(
          (float) Math.random() * transformer.getPreTransformGrid().getWidth(),
          (float) Math.random() * transformer.getPreTransformGrid().getHeight());
    }

    for (Node n : transformer.getPostTransformGrid().getBoundingQuad().getNodes()) {
      n.translate((float) Math.random() * 500 - 250, (float) Math.random() * 500 - 250);
    }

    actualQuad = transformer.getPostTransformGrid().getBoundingQuad().copy();

    postTransformPoints = new PVector[preTransformPoints.length];
    for (int i = 0; i < preTransformPoints.length; i++) {
      Point2D src = new Point2D.Float(preTransformPoints[i].x, preTransformPoints[i].y);
      Point2D dest = transformer.getPerspective().mapDestPoint(src);
      postTransformPoints[i] = new PVector((float) dest.getX(), (float) dest.getY());
    }

    transformer.resetTransform();

//    for (Node n : transformer.getPostTransformGrid().getNodes()) {
//      n.translate((float) Math.random() * 100 - 50, (float) Math.random() * 100 - 50);
//    }
  }

  public void draw() {
    background(0);

    pushMatrix();
    translate(width/2 - transformer.getPreTransformGrid().getWidth() / 2f, height/2 - transformer.getPreTransformGrid().getHeight() /2f);

    transformer.drawToSurface(canvas -> {
      canvas.strokeWeight(10);
      canvas.stroke(255);
      for (PVector p : preTransformPoints) {
        canvas.point(p.x, p.y);
      }
    });

    strokeWeight(2);
    stroke(255, 0, 0);
    for (PVector p : postTransformPoints) {
      ellipse(p.x, p.y, 20, 20);
    }

    strokeWeight(2);
    transformer.draw(getGraphics());
    transformer.renderUI(getGraphics());

    stroke(255, 0, 0);
    beginShape();
    for (PVector p : actualQuad.getVertices()) {
      vertex(p.x, p.y);
    }
    endShape(CLOSE);

    float score = PerspectiveOptimizer.score(transformer, preTransformPoints, postTransformPoints);
    PerspectiveOptimizer.optimize(transformer, preTransformPoints, postTransformPoints, 500);

    popMatrix();

    text(score, 20, 20);
  }

  @Override
  public void keyPressed() {
    if (key == ' ') {
      transformer.resetTransform();
      for (Node n : transformer.getPostTransformGrid().getNodes()) {
        n.translate((float) Math.random() * 100 - 50, (float) Math.random() * 100 - 50);
      }
    }
  }
}
