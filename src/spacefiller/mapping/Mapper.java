package spacefiller.mapping;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.graph.Graph;
import spacefiller.graph.GridUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Mapper {
  public static final int ACTIVE_COLOR = 0xFFFFFFFF;
  public static final int DESELECTED_COLOR = 0x33FFFFFF;

  private static final char DRILL_OUT = 'u';
  private static final char WARP_MODE = 'e';
  private static final char ROTATE_MODE = 'r';
  private static final char SCALE_MODE = 's';
  private static final char TRANSLATE_MODE = 't';
  private static final char SHOW_MESH = 'm';

  private Stack<List<Transformable>> transformables;
  private List<Drawable> drawables;
  private Transformable activeTransformable;
  private Draggable lastDragged;
  private Mode mode;
  private PApplet parent;
  private boolean meshesShown;

  public Mapper(PApplet parent) {
    this.mode = new WarpMode(this);
    this.parent = parent;

    parent.registerMethod("mouseEvent", this);
    parent.registerMethod("keyEvent", this);
    parent.registerMethod("draw", this);

    transformables = new Stack<>();
    transformables.add(new ArrayList<>());

    drawables = new ArrayList<>();
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
      } else if (keyEvent.getKey() == SHOW_MESH) {
        toggleMeshes();
      }
    } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
      mode = new WarpMode(this);
    }

    mode.keyEvent(keyEvent);
  }

  private List<Transformable> getRootTransformables() {
    return transformables.get(0);
  }


  public void draw() {
    for (Transformable transformable : getRootTransformables()) {
      transformable.renderUI(parent.getGraphics());
    }

    for (Drawable drawable : drawables) {
      drawable.draw(parent.getGraphics());
    }
  }

  public Surface createSurface(int rows, int cols, int spacing) {
    Surface surface = GridUtils.createSurface(rows, cols, spacing);
    surface.createCanvas(parent);
    addTransformable(surface);
    addDrawable(surface);
    return surface;
  }

  public Graph createGraph() {
    Graph graph = new Graph();
    GraphTransformer transformer = new GraphTransformer(graph);
    addTransformable(transformer);
    return graph;
  }

  private void addTransformable(Transformable transformable) {
    transformable.setShowUI(true);
    getRootTransformables().add(transformable);
  }

  private void addDrawable(Drawable drawable) {
    drawables.add(drawable);
  }

  private void pushTransformables(List<Transformable> transformables) {
    this.transformables.push(transformables);
  }

  protected Transformable getTransformTarget() {
    return activeTransformable;
  }

  protected Draggable getLastDragged() {
    return lastDragged;
  }

  protected void setLastDragged(Draggable lastDragged) {
    this.lastDragged = lastDragged;
  }

  protected void setActiveTransformable(Transformable transformable) {
    if (activeTransformable != null) {
      activeTransformable.setActive(false);
    }

    transformable.setActive(true);
    activeTransformable = transformable;
  }

  protected void clearActiveTransformable() {
    if (activeTransformable != null) {
      activeTransformable.setActive(false);
    }
    activeTransformable = null;
  }

  protected List<Transformable> getCurrentActiveLayer() {
    if (!transformables.empty()) {
      return transformables.peek();
    } else {
      return null;
    }
  }

  protected void drillOut() {
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

  protected void drillIn(Transformable transformable) {
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

  private void toggleMeshes() {
    if (!meshesShown) {
      showMeshes();
    } else {
      hideMeshes();
    }
  }

  // TODO: this shows and hides all top level meshes
  // there currently isn't any logic around showing / hiding meshes at
  // certain levels in the hierarchy. for example, a surface that contains
  // another surface. the code is set up to accommodate this -- just make
  // showMeshes more similar to showUI.
  private void showMeshes() {
    for (Transformable transformable : getRootTransformables()) {
      if (transformable instanceof Surface) {
        ((Surface) transformable).setShowMesh(true);
      }
    }
    meshesShown = true;
  }

  private void hideMeshes() {
    for (Transformable transformable : getRootTransformables()) {
      if (transformable instanceof Surface) {
        ((Surface) transformable).setShowMesh(false);
      }
    }
    meshesShown = false;
  }
}
