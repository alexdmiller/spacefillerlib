package spacefiller.mapping;

import processing.core.PVector;
import processing.event.MouseEvent;

public class TranslateMode extends EditMode {
  private PVector lastMouse;

  public TranslateMode(Mapper mooYoung) {
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
          PVector delta = PVector.sub(mouse, lastMouse);

          if (target != null) {
            target.translate(delta.x, delta.y);
          }

          lastMouse = mouse;
          break;
      }
    }
  }
}
