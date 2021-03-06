package spacefiller.mapping;

import processing.core.PVector;
import processing.event.MouseEvent;

public class RotateMode extends EditMode {
  private PVector lastMouse;

  public RotateMode(Mapper mooYoung) {
    super(mooYoung);
    lastMouse = new PVector();
  }

  @Override
  public void mouseEvent(MouseEvent e) {
    PVector mouse = new PVector(e.getX(), e.getY());
    Transformable target = mapper.getTransformTarget();

    if (target != null) {
      mouse = target.getParentRelativePoint(mouse);

      switch (e.getAction()) {
        case MouseEvent.PRESS:
          lastMouse = mouse;
          break;

        case MouseEvent.DRAG:
          if (target != null) {
            PVector center = target.getCenter();

            float initialAngle = (float) Math.atan2(center.y - lastMouse.y, center.x - lastMouse.x);
            float currentAngle = (float) Math.atan2(center.y - mouse.y, center.x - mouse.x);

            target.rotate(currentAngle - initialAngle);
            lastMouse = mouse;
          }
          break;
      }
    }
  }
}
