package spacefiller.mapping;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.DOWN;
import static processing.core.PConstants.UP;

public class WarpMode extends EditMode {
  private Draggable dragged;
  private PVector clickedPointDelta;
  private PVector lastClicked;
  private boolean forceDrag;
  private float bumpAmount = 1f;
  private boolean innerNodes;

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
            dragged = target.select(mouse, innerNodes);
            mooYoung.setLastDragged(dragged);
            lastClicked = mouse;
          }
          break;

        case MouseEvent.DRAG:
          if (lastClicked != null) {
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
          }
          break;

        case MouseEvent.RELEASE:
          dragged = null;
          clickedPointDelta = null;
          break;
      }
    }
  }

  private void bump(int x, int y) {
    if (mooYoung.getLastDragged() != null) {
      mooYoung.getLastDragged().translate(x * bumpAmount, y * bumpAmount);
    }
  }

  @Override
  public void keyEvent(KeyEvent keyEvent) {
    if (keyEvent.getKey() == Mapper.FORCE_DRAG) {
      if (keyEvent.getAction() == KeyEvent.PRESS) {
        forceDrag = true;
      } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
        forceDrag = false;
      }
    } else if (keyEvent.getKey() == Mapper.EDIT_MESH) {
      if (keyEvent.getAction() == KeyEvent.PRESS) {
        innerNodes = true;
      } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
        innerNodes = false;
      }
    } else if (keyEvent.getAction() == KeyEvent.PRESS) {
      if (keyEvent.getKeyCode() == LEFT) {
        bump(-1, 0);
      } else if (keyEvent.getKeyCode() == RIGHT) {
        bump(1, 0);
      } else if (keyEvent.getKeyCode() == DOWN) {
        bump(0, 1);
      } else if (keyEvent.getKeyCode() == UP) {
        bump(0, -1);
      }
    }
  }
}
