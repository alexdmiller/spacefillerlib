package spacefiller.mapping;

import processing.core.PGraphics;
import processing.core.PVector;

public interface Transformable {
  void scale(float scale);
  void rotate(float theta);
  Draggable select(PVector point);
  Pin selectClosestPin(PVector point);
  PVector getCenter();
  PVector getRelativePoint(PVector point);
  void renderControlPoints(PGraphics graphics, PGraphics canvas);

  PVector[] getControlPoints();
  void setControlPoints(PVector[] points);
}
