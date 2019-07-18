package spacefiller.mapping;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.mapping.modes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.SCREEN;

public class Mapper {
  public static final int ACTIVE_COLOR = 0xFFFFFFFF;
  public static final int DESELECTED_COLOR = 0x33FFFFFF;

  private static final char DRILL_OUT = 'u';
  private static final char WARP_MODE = 'e';
  private static final char ROTATE_MODE = 'r';
  private static final char SCALE_MODE = 's';
  private static final char TRANSLATE_MODE = 't';

  private Stack<List<Transformable>> transformables;
  private Transformable activeTransformable;
  private Draggable lastDragged;
  private Mode mode;
  private PApplet parent;

  public Mapper(PApplet parent) {
    this.mode = new WarpMode(this);
    this.parent = parent;

    parent.registerMethod("mouseEvent", this);
    parent.registerMethod("keyEvent", this);

    transformables = new Stack<>();
    transformables.add(new ArrayList<>());
  }

  public void addTransformable(Transformable transformable) {
    transformable.setShowUI(true);
    transformables.peek().add(transformable);
  }

  public void pushTransformables(List<Transformable> transformables) {
    this.transformables.push(transformables);
  }

  public Transformable getTransformTarget() {
    return activeTransformable;
  }

  public void mouseEvent(MouseEvent event) {
    if (event.getAction() == MouseEvent.PRESS) {
      PVector mouse = new PVector(event.getX(), event.getY());

      if (getCurrentActiveLayer() != null) {
        for (int i = 0; i < getCurrentActiveLayer().size(); i++) {
          Transformable transformable = getCurrentActiveLayer().get(i);
          PVector local = transformable.getParentRelativePoint(mouse);
          if (transformable.isPointOver(local)) {
            if (event.isControlDown()) {
              drillIn(transformable);
              return;
            } else {
              setActiveTransformable(transformable);
            }
          }
        }
      }
    }

    mode.mouseEvent(event);
  }

  public Draggable getLastDragged() {
    return lastDragged;
  }

  public void setLastDragged(Draggable lastDragged) {
    this.lastDragged = lastDragged;
  }

  public void setActiveTransformable(Transformable transformable) {
    if (activeTransformable != null) {
      activeTransformable.setActive(false);
    }

    transformable.setActive(true);
    activeTransformable = transformable;
  }

  public void clearActiveTransformable() {
    if (activeTransformable != null) {
      activeTransformable.setActive(false);
    }
    activeTransformable = null;
  }

  public List<Transformable> getCurrentActiveLayer() {
    if (!transformables.empty()) {
      return transformables.peek();
    } else {
      return null;
    }
  }

  public void drillOut() {
    for (Transformable t : getCurrentActiveLayer()) {
      t.setShowUI(false);
    }

    if (transformables.size() > 1) {
      transformables.pop();
    }

    clearActiveTransformable();

    for (Transformable t : getCurrentActiveLayer()) {
      t.setShowUI(true);
    }
  }

  public void drillIn(Transformable transformable) {
    if (transformable.getChildren() != null && !transformable.getChildren().isEmpty()) {
      for (Transformable t : getCurrentActiveLayer()) {
        t.setShowUI(false);
      }

      clearActiveTransformable();
      pushTransformables(transformable.getChildren());

      for (Transformable t : getCurrentActiveLayer()) {
        t.setShowUI(true);
      }
    }
  }

  public void keyEvent(KeyEvent keyEvent) {
    if (keyEvent.getAction() == KeyEvent.PRESS) {
      if (keyEvent.getKey() == DRILL_OUT) {
        drillOut();
      } else if (keyEvent.getKey() == WARP_MODE) {
        mode = new WarpMode(this);
      } else if (keyEvent.getKey() == ROTATE_MODE) {
        mode = new RotateMode(this);
      } else if (keyEvent.getKey() == SCALE_MODE) {
        mode = new ScaleMode(this);
      } else if (keyEvent.getKey() == TRANSLATE_MODE) {
        mode = new TranslateMode(this);
      }
    } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
      mode = new WarpMode(this);
    }

    mode.keyEvent(keyEvent);
  }
}
