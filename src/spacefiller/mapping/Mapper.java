package spacefiller.mapping;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.mapping.modes.Mode;
import spacefiller.mapping.modes.RotateMode;
import spacefiller.mapping.modes.ScaleMode;
import spacefiller.mapping.modes.WarpMode;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;

public class Mapper {
  private List<Transformable> transformables;
  private int activeTransformable;
  private Mode mode;
  private PApplet parent;

  public Mapper(PApplet parent) {
    this.mode = new WarpMode(this);
    this.parent = parent;

    parent.registerMethod("mouseEvent", this);
     parent.registerMethod("keyEvent", this);

    transformables = new ArrayList<>();
  }

  public void addTransformable(Transformable transformable) {
    transformables.add(transformable);
  }

  public Transformable getTransformTarget() {
    return transformables.get(activeTransformable);
  }

  public void mouseEvent(MouseEvent event) {
    mode.mouseEvent(event);
  }

  public void keyEvent(KeyEvent keyEvent) {
    if (keyEvent.getAction() == KeyEvent.PRESS) {
      if (keyEvent.getKey() == 'e') {
        mode = new WarpMode(this);
      } else if (keyEvent.getKey() == 'r') {
        mode = new RotateMode(this);
      } else if (keyEvent.getKey() == 's') {
        mode = new ScaleMode(this);
      } else if (keyEvent.getKeyCode() == RIGHT) {
        activeTransformable = Math.floorMod(activeTransformable + 1, transformables.size());
      } else if (keyEvent.getKeyCode() == LEFT) {
        activeTransformable = Math.floorMod(activeTransformable - 1, transformables.size());
      } else if (keyEvent.getKey() == 'b') {
        activeTransformable = 0;
      }
    } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
      mode = new WarpMode(this);
    }

    mode.keyEvent(keyEvent);
  }
}
