package spacefiller.mapping;

import geomerative.RPoint;
import processing.core.PVector;

import java.io.Serializable;

public class RShapePin implements Pin, Serializable {
  private PVector originalPosition;
  private RShapeTransformer parent;
  private RPoint point;

  public RShapePin(RPoint point, RShapeTransformer parent) {
    this.originalPosition = new PVector(point.x, point.y);
    this.point = point;
    this.parent = parent;
  }

  @Override
  public void moveTo(float x, float y) {
    this.point.x = x;
    this.point.y = y;
    this.parent.computeWarp();
  }

  @Override
  public void translate(float dx, float dy) {
    this.point.x += dx;
    this.point.y += dy;

    this.parent.computeWarp();
  }

  public void setPosition(float x, float y) {
    this.point.x = x;
    this.point.y = y;
  }

  public PVector getOriginalPosition() {
    return originalPosition;
  }

  @Override
  public PVector getPosition() {
    return new PVector(point.x, point.y);
  }
}
