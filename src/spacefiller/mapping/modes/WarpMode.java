package spacefiller.mapping.modes;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.mapping.Draggable;
import spacefiller.mapping.MappingHost;
import spacefiller.mapping.Pin;
import spacefiller.mapping.Transformable;

public class WarpMode extends EditMode {
  private Draggable dragged;
  private PVector clickedPointDelta;
  private boolean forceDrag;

  public WarpMode(MappingHost mooYoung) {
    super(mooYoung);
  }

  @Override
  public void mouseEvent(MouseEvent e) {
    PVector mouse = new PVector(e.getX(), e.getY());
    Transformable target = mooYoung.getTransformTarget();

    mouse = target.getRelativePoint(mouse);

    switch (e.getAction()) {
      case MouseEvent.PRESS:
        if (forceDrag) {
          Pin draggedPin = target.selectClosestPin(mouse);
          dragged = draggedPin;
          clickedPointDelta = PVector.sub(draggedPin.getPosition(), mouse);
        } else {
          dragged = target.select(mouse);
        }
        break;

      case MouseEvent.DRAG:
        if (dragged != null) {
          if (clickedPointDelta != null) {
            dragged.moveTo(mouse.x + clickedPointDelta.x, mouse.y + clickedPointDelta.y);
          } else {
            dragged.moveTo(mouse.x, mouse.y);
          }
        }
        break;

      case MouseEvent.RELEASE:
        dragged = null;
        clickedPointDelta = null;
        break;
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
