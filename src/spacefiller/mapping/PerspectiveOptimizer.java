package spacefiller.mapping;

import processing.core.PVector;

import javax.media.jai.PerspectiveTransform;
import javax.media.jai.WarpPerspective;
import java.awt.geom.Point2D;

public class PerspectiveOptimizer {
  private static float bump = 0.01f;

  public static void optimize(
      Surface transformer,
      PVector[] preTransformPoints,
      PVector[] postTransformPoints,
      int iterations) {
    Quad srcQuad = transformer.getPreTransformGrid().getBoundingQuad();
    Quad destQuad = transformer.getPostTransformGrid().getBoundingQuad();

    PVector[] translations = new PVector[] {
        new PVector(0, 0),
        new PVector(bump, 0),
        new PVector(-bump, 0),
        new PVector(0, bump),
        new PVector(0, -bump),
    };

    for (int i = 0; i < iterations; i++) {
      for (PVector p : destQuad.getVertices()) {
        float minScore = 99999999;
        int minScoreIndex = 0;

        for (int j = 0; j < translations.length; j++) {
          p.add(translations[j]);
          WarpPerspective perspective = getPerspective(srcQuad, destQuad);
          float score = score(perspective, preTransformPoints, postTransformPoints);
          if (score < minScore) {
            minScore = score;
            minScoreIndex = j;
          }
          p.sub(translations[j]);
        }

        p.add(translations[minScoreIndex]);
      }
    }

    transformer.recomputeNodesFromQuad();
  }

  private static WarpPerspective getPerspective(Quad preQuad, Quad postQuad) {
    PerspectiveTransform transform = PerspectiveTransform.getQuadToQuad(
        preQuad.getTopLeft().position.x, preQuad.getTopLeft().position.y,
        preQuad.getTopRight().position.x, preQuad.getTopRight().position.y,
        preQuad.getBottomRight().position.x, preQuad.getBottomRight().position.y,
        preQuad.getBottomLeft().position.x, preQuad.getBottomLeft().position.y,
        postQuad.getTopLeft().position.x, postQuad.getTopLeft().position.y,
        postQuad.getTopRight().position.x, postQuad.getTopRight().position.y,
        postQuad.getBottomRight().position.x, postQuad.getBottomRight().position.y,
        postQuad.getBottomLeft().position.x, postQuad.getBottomLeft().position.y);

    WarpPerspective perspective = new WarpPerspective(transform);
    return perspective;
  }

  public static float score(
      Surface transformer,
      PVector[] preTransformPoints,
      PVector[] postTransformPoints) {
    return score(transformer.getPerspective(), preTransformPoints, postTransformPoints);
  }


  public static float score(
      WarpPerspective perspective,
      PVector[] preTransformPoints,
      PVector[] postTransformPoints) {

    float score = 0;

    for (int i = 0; i < preTransformPoints.length; i++) {
      Point2D pre = new Point2D.Float(preTransformPoints[i].x, preTransformPoints[i].y);
      Point2D actualPost = perspective.mapDestPoint(pre);
      Point2D targetPost = new Point2D.Float(postTransformPoints[i].x, postTransformPoints[i].y);
      score += actualPost.distanceSq(targetPost);
    }

    return score;
  }
}
