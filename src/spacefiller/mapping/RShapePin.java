package spacefiller.mapping;

import geomerative.RPoint;
import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RShapePin implements Pin, Serializable {
  private PVector originalPosition;
  private RShapeTransformer parent;
  private List<RPoint> points;

  public RShapePin(RPoint point, RShapeTransformer parent) {
    this.originalPosition = new PVector(point.x, point.y);
    this.points = new ArrayList<>();
    this.points.add(point);
    this.parent = parent;
  }

  @Override
  public void moveTo(float x, float y) {
    for (RPoint point : points) {
      point.x = x;
      point.y = y;
    }
    this.parent.computeWarp();
  }

  @Override
  public void translate(float dx, float dy) {
    for (RPoint point : points) {
      point.x += dx;
      point.y += dy;
    }

    this.parent.computeWarp();
  }

  public void setPosition(float x, float y) {
    for (RPoint point : points) {
      point.x = x;
      point.y = y;
    }
  }

  public PVector getOriginalPosition() {
    return originalPosition;
  }

  @Override
  public PVector getPosition() {
    return new PVector(points.get(0).x, points.get(0).y);
  }

  public boolean mergePoint(RPoint point) {
    if (points.get(0).dist(point) == 0 && points.get(0) != point) {
      addPoint(point);
      return true;
    }
    return false;
  }

  public void addPoint(RPoint point) {
    points.add(point);
  }
}
