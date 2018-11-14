package spacefiller.mapping;

import geomerative.RCommand;
import geomerative.RPath;
import geomerative.RPoint;
import geomerative.RShape;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RShapeTransformer extends Transformable implements Draggable {
  private RShape shape;
  private RShape originalShape;
  private RShapePin topLeft;
  private RShapePin topRight;
  private RShapePin bottomLeft;
  private RShapePin bottomRight;

  private float lastX;
  private float lastY;

  private List<RShapePin> pins;

  public RShapeTransformer(RShape shape) {
    this.shape = shape;
    this.originalShape = new RShape(shape);
    this.pins = new ArrayList<>();

//    topLeft = new RShapePin(shape.getTopLeft(), this);
//    topRight = new RShapePin(shape.getTopRight(), this);
//    bottomLeft = new RShapePin(shape.getBottomLeft(), this);
//    bottomRight = new RShapePin(shape.getBottomRight(), this);
//
//    pins = new RShapePin[] {topLeft, topRight, bottomRight, bottomLeft};
    addHandles(shape);
  }

  private void addHandles(RShape shape) {
    for (RPath path : shape.paths) {
      for (RCommand command : path.commands) {
        addPoint(command.startPoint);
        addPoint(command.endPoint);

        if (command.controlPoints != null) {
          for (RPoint point : command.controlPoints) {
            addPoint(point);
          }
        }
      }
    }


//    if (shape.children != null && shape.children.length > 0) {
//    } else {
//
//    }
  }

  // Collapses identical points to a single pin so that they can be
  // dragged around at the same time
  private void addPoint(RPoint p) {
    for (RShapePin pin : pins) {
      if (pin.mergePoint(p)) {
        return;
      }
    }

    pins.add(new RShapePin(p, this));
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
  public Draggable select(PVector point) {
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

//    for (RShapePin pin : pins) {
//      pin.translate(x, y);
//    }
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
