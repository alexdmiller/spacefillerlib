package spacefiller.mapping;

import processing.core.PVector;
import processing.event.MouseEvent;

public class ScaleMode extends EditMode {
  private PVector lastMouse;

  public ScaleMode(Mapper mooYoung) {
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

            float lastDistance = center.dist(lastMouse);
            float currentDistance = center.dist(mouse);

            float scale = currentDistance / lastDistance;
            target.scale(scale);

            lastMouse = mouse;
          }

          break;
      }
    }
  }
}
