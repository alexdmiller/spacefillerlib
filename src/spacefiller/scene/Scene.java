package spacefiller.scene;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Scene {
  protected int width, height;
  protected PApplet parent;

  // A flag that tracks whether or not setup() has been called yet
  private boolean isSetup;

  // Whether setup() should be called again if isSetup is already true
  private boolean alwaysReset;

  public void setup() {
    isSetup = true;
  }
  public void draw(PGraphics graphics) { }

  public void transitionIn(SceneApplet.TransitionCallback callback) { callback.done(); }

  public void transitionOut(SceneApplet.TransitionCallback callback) {
    callback.done();
  }

  public void setDimensions(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public PApplet getParent() {
    return parent;
  }

  public void setParent(PApplet parent) {
    this.parent = parent;
  }
}
