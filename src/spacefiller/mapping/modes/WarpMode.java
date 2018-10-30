package spacefiller.mapping.modes;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.mapping.Draggable;
import spacefiller.mapping.Mapper;
import spacefiller.mapping.Pin;
import spacefiller.mapping.Transformable;

public class WarpMode extends EditMode {
  private Draggable dragged;
  private PVector clickedPointDelta;
  private PVector lastClicked;
  private boolean forceDrag;

  public WarpMode(Mapper mooYoung) {
    super(mooYoung);
  }

  @Override
  public void mouseEvent(MouseEvent e) {
    PVector mouse = new PVector(e.getX(), e.getY());
    Transformable target = mooYoung.getTransformTarget();

    if (target != null) {
      mouse = target.getParentRelativePoint(mouse);

      switch (e.getAction()) {
        case MouseEvent.PRESS:
          if (forceDrag) {
            Pin draggedPin = target.selectClosestPin(mouse);
            dragged = draggedPin;
            clickedPointDelta = PVector.sub(draggedPin.getPosition(), mouse);
            lastClicked = mouse;
          } else {
            dragged = target.select(mouse);
            lastClicked = mouse;
          }
          break;

        case MouseEvent.DRAG:
          PVector delta = PVector.sub(mouse, lastClicked);
          lastClicked = mouse;
          if (dragged != null) {
            if (clickedPointDelta != null) {
              // TODO: change this to translate; remove moveTo from draggable interface?
              dragged.moveTo(mouse.x + clickedPointDelta.x, mouse.y + clickedPointDelta.y);
            } else {
              dragged.translate(delta.x, delta.y);
            }
          }
          break;

        case MouseEvent.RELEASE:
          dragged = null;
          clickedPointDelta = null;
          break;
      }
    }
  }

  @Override
  public void keyEvent(KeyEvent keyEvent) {
    if (keyEvent.getKey() == 'f') {
      if (keyEvent.getAction() == KeyEvent.PRESS) {
        forceDrag = true;
      } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
        forceDrag = false;
      }
    }
  }
}
