package spacefiller.mapping;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.mapping.modes.Mode;
import spacefiller.mapping.modes.RotateMode;
import spacefiller.mapping.modes.ScaleMode;
import spacefiller.mapping.modes.WarpMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;

public class Mapper {
  private Stack<List<Transformable>> transformables;
  private Transformable activeTransformable;
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

      for (int i = 0; i < transformables.peek().size(); i++) {
        Transformable transformable = transformables.peek().get(i);
        if (transformable.isPointOver(mouse)) {
          if (event.isControlDown()) {
            pushTransformables(transformable.getChildren());
          } else {
            activeTransformable = transformable;
          }
        }
      }
    }

    mode.mouseEvent(event);
  }

  public void keyEvent(KeyEvent keyEvent) {
    System.out.println(keyEvent.getKeyCode());
    if (keyEvent.getAction() == KeyEvent.PRESS) {
      if (keyEvent.getKey() == 'e') {
        mode = new WarpMode(this);
      } else if (keyEvent.getKey() == 'r') {
        mode = new RotateMode(this);
      } else if (keyEvent.getKey() == 's') {
        mode = new ScaleMode(this);
      }
    } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
      mode = new WarpMode(this);
    }

    mode.keyEvent(keyEvent);
  }

  public void draw(PGraphics canvas) {
    for (Transformable t : transformables.peek()) {
      t.renderControlPoints(canvas, t == activeTransformable);
    }
  }
}
