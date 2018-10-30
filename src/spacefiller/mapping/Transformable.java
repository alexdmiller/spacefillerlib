package spacefiller.mapping;

import processing.core.PGraphics;
import processing.core.PVector;

import java.util.List;

public interface Transformable {
  void scale(float scale);
  void rotate(float theta);
  Draggable select(PVector point);
  Pin selectClosestPin(PVector point);
  PVector getCenter();
  PVector getRelativePoint(PVector point);
  PVector getParentRelativePoint(PVector point);
  void setParent(Transformable parent);
  boolean isPointOver(PVector point);
  void renderControlPoints(PGraphics canvas, boolean active);

  PVector[] getControlPoints();
  void setControlPoints(PVector[] points);
  List<Transformable> getChildren();
}
