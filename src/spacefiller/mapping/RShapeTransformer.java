package spacefiller.mapping;

import geomerative.RCommand;
import geomerative.RPath;
import geomerative.RPoint;
import geomerative.RShape;
import processing.core.PGraphics;
import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RShapeTransformer extends Transformable implements Draggable, Serializable {
  private transient RShape shape;
  private transient float lastX;
  private transient float lastY;
//  private RShape originalShape;

  private List<RShapePin> pins;

  public RShapeTransformer() {
//    this.originalShape = new RShape(shape);
    this.pins = new ArrayList<>();

//    topLeft = new RShapePin(shape.getTopLeft(), this);
//    topRight = new RShapePin(shape.getTopRight(), this);
//    bottomLeft = new RShapePin(shape.getBottomLeft(), this);
//    bottomRight = new RShapePin(shape.getBottomRight(), this);
//
//    pins = new RShapePin[] {topLeft, topRight, bottomRight, bottomLeft};
  }

  public void setShape(RShape shape) {
    this.shape = shape;
  }

  public void createHandlesFromShape() {
    addHandles();
  }

  private List<RPoint> getCandidatePointList() {
//    List<RPoint> points = new ArrayList<>();
//    for (RPath path : shape.paths) {
//      for (RCommand command : path.commands) {
//        points.add(command.startPoint);
//        points.add(command.endPoint);
//
//        if (command.controlPoints != null) {
//          for (RPoint point : command.controlPoints) {
//            points.add(point);
//          }
//        }
//      }
//    }
//
//    return points;

    return Arrays.asList(shape.getHandles());
  }

  public void matchHandlesToShape() {
    List<RPoint> candidatePointList = getCandidatePointList();
    for (RShapePin pin : pins) {
      for (int pointIndex : pin.getPointIndices()) {
        RPoint point = candidatePointList.get(pointIndex);
        pin.addPoint(candidatePointList.get(pointIndex));
        pin.updatePoints();
      }
    }
  }

  private void addHandles() {
    List<RPoint> candidatePointList = getCandidatePointList();
    for (int i = 0; i < candidatePointList.size(); i++) {
      addPoint(candidatePointList.get(i), i);
    }
  }

  private boolean mergePoint(RPoint p, int index) {
    for (RShapePin pin : pins) {
      if (pin.mergePoint(p, index)) {
        return true;
      }
    }

    return false;
  }

  // Collapses identical points to a single pin so that they can be
  // dragged around at the same time
  private void addPoint(RPoint p, int pointIndex) {
    if (!mergePoint(p, pointIndex)) pins.add(new RShapePin(p, pointIndex, this));
  }


  @Override
  public void scale(float scale) {
    RPoint center = shape.getCenter();
    shape.scale(scale, center.x, center.y);

    for (RShapePin pin : pins) {
      pin.translate(-center.x, -center.y);
      PVector p = pin.getPosition();
      float newX = p.x * scale;
      float newY = p.y * scale;
      p.x = newX;
      p.y = newY;
      pin.translate(center.x, center.y);
    }
  }

  @Override
  public void rotate(float theta) {
    RPoint center = shape.getCenter();
    shape.rotate(theta, center.x, center.y);

    for (RShapePin pin : pins) {
      pin.translate(-center.x, -center.y);
      PVector p = pin.getPosition();
      float newX = (float) (p.x * Math.cos(theta) - p.y * Math.sin(theta));
      float newY = (float) (p.x * Math.sin(theta) + p.y * Math.cos(theta));
      p.x = newX;
      p.y = newY;
      pin.translate(center.x, center.y);
    }
  }

  @Override
  public Draggable select(PVector point, boolean innerNodes) {
    Pin closest = selectClosestPin(point);
    if (closest.getPosition().dist(point) < 30) {
      return closest;
    }

    if (isPointOver(point)) {
      lastX = point.x;
      lastY = point.y;
      return this;
    }

    return null;
  }

  @Override
  public Pin selectClosestPin(PVector point) {
    Pin closest = null;

    for (RShapePin pin : pins) {
      if (closest == null || pin.getPosition().dist(point) < closest.getPosition().dist(point)) {
        closest = pin;
      }
    }

    return closest;
  }

  @Override
  public PVector getCenter() {
    RPoint p = shape.getCenter();
    return new PVector(p.x, p.y);
  }

  @Override
  public PVector getRelativePoint(PVector point) {
    return null;
  }

  @Override
  public boolean isPointOver(PVector point) {
    for (RShapePin pin : pins) {
      if (pin.getPosition().dist(point) < 10) {
        return true;
      }
    }
    return false;
//    return shape.contains(point.x, point.y);
  }

  @Override
  public void renderUI(PGraphics canvas) {
    if (showUI) {
      canvas.noFill();
      canvas.strokeWeight(2);
      canvas.stroke(active ? Mapper.ACTIVE_COLOR : Mapper.DESELECTED_COLOR);

      for (int i = 0; i < pins.size(); i ++) {
        RShapePin pin = pins.get(i);
        canvas.ellipse(pin.getPosition().x, pin.getPosition().y, 10, 10);
      }
    }
  }

  @Override
  public List<Transformable> getChildren() {
    // TODO: implement?
    return null;
  }

  protected void computeWarp() {
//    float w = originalShape.getWidth();
//    float h = originalShape.getHeight();
//
//    PerspectiveTransform transform = PerspectiveTransform.getQuadToQuad(
//        topLeft.getOriginalPosition().x, topLeft.getOriginalPosition().y,
//        topRight.getOriginalPosition().x, topRight.getOriginalPosition().y,
//        bottomRight.getOriginalPosition().x, bottomRight.getOriginalPosition().y,
//        bottomLeft.getOriginalPosition().x, bottomLeft.getOriginalPosition().y,
//        topLeft.getPosition().x, topLeft.getPosition().y,
//        topRight.getPosition().x, topRight.getPosition().y,
//        bottomRight.getPosition().x, bottomRight.getPosition().y,
//        bottomLeft.getPosition().x, bottomLeft.getPosition().y);
//
//    WarpPerspective warpPerspective = new WarpPerspective(transform);
//
//    RPoint[] originalShapeHandles = originalShape.getHandles();
//    RPoint[] shapeHandles = shape.getHandles();
//
//    for (int i = 0; i < shape.getHandles().length; i++) {
//      Point2D point = warpPerspective.mapDestPoint(new Point((int) originalShapeHandles[i].x, (int) originalShapeHandles[i].y));
//      shapeHandles[i].x = (float) point.getX();
//      shapeHandles[i].y = (float) point.getY();
//    }
  }

  @Override
  public void translate(float x, float y) {
    shape.translate(x, y);

    for (RShapePin pin : pins) {
      pin.translate(x, y);
    }
  }

  @Override
  public void moveTo(float x, float y) {
    shape.translate(x - lastX, y - lastY);

    for (RShapePin pin : pins) {
      pin.translate(x - lastX, y - lastY);
    }

    lastX = x;
    lastY = y;
  }

  public RShape getShape() {
    return shape;
  }
}
