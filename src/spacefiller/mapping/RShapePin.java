package spacefiller.mapping;

import geomerative.RPoint;
import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RShapePin implements Pin, Serializable {
  private PVector position;
  private RShapeTransformer parent;
  private List<Integer> pointIndices;
  private transient List<RPoint> points;

  public RShapePin(RPoint point, int pointIndex, RShapeTransformer parent) {
    this.position = new PVector(point.x, point.y);
    this.points = new ArrayList<>();
    this.pointIndices = new ArrayList<>();
    this.points.add(point);
    this.pointIndices.add(pointIndex);
    this.parent = parent;
  }

  public List<Integer> getPointIndices() {
    return pointIndices;
  }

  @Override
  public void moveTo(float x, float y) {
    position.set(x, y);
    updatePoints();
    parent.computeWarp();
  }

  @Override
  public void translate(float dx, float dy) {
    position.add(dx, dy);
    updatePoints();
    parent.computeWarp();
  }

  public void setPosition(float x, float y) {
    position.set(x, y);
    updatePoints();
  }

  public void updatePoints() {
    for (RPoint point : points) {
      point.x = position.x;
      point.y = position.y;
    }
  }

  @Override
  public PVector getPosition() {
    return position;
  }

  private void ensurePointArraysInitialized() {
    if (points == null) {
      points = new ArrayList<>();
    }

    if (pointIndices == null) {
      pointIndices = new ArrayList<>();
    }
  }

  public boolean mergePoint(RPoint point, int index) {
    ensurePointArraysInitialized();

    PVector newPosition = new PVector(point.x, point.y);
    if (position.dist(newPosition) == 0 && !points.contains(point)) {
      addPoint(point, index);
      return true;
    }
    return false;
  }

  public void addPoint(RPoint point, int index) {
    ensurePointArraysInitialized();

    points.add(point);
    pointIndices.add(index);
  }

  public void addPoint(RPoint point) {
    ensurePointArraysInitialized();

    points.add(point);
  }
}
