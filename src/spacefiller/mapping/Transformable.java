package spacefiller.mapping;

import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public abstract  class Transformable {
  protected boolean showUI;
  protected boolean active;

  private Transformable parent;
  private List<Transformable> children;

  public Transformable() {
    children = new ArrayList<>();
  }

  public boolean isShowUI() {
    return showUI;
  }

  public void setShowUI(boolean showUI) {
    this.showUI = showUI;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Transformable getParent() {
    return parent;
  }

  public List<Transformable> getChildren() {
    return children;
  }

  public void addChild(Transformable transformable) {
    transformable.setParent(this);
    children.add(transformable);
  }

  public void setParent(Transformable parent) {
    this.parent = parent;
  }

  public PVector getParentRelativePoint(PVector point) {
    if (parent != null) {
      return getParent().getRelativePoint(point);
    }
    return point;
  }

  public abstract void translate(float dx, float dy);
  public abstract void scale(float scale);
  public abstract void rotate(float theta);

  // return anything that is draggable, pin or otherwise
  public abstract Draggable select(PVector point, boolean innerNodes);

  // return just the closest pin
  public abstract Pin selectClosestPin(PVector point);

  public abstract PVector getCenter();
  public abstract PVector getRelativePoint(PVector point);
  public abstract boolean isPointOver(PVector point);
  public abstract void renderUI(PGraphics canvas);
}
