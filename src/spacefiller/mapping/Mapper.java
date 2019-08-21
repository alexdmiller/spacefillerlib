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

  protected static final char DRILL_OUT = 'u';
  protected static final char WARP_MODE = 'e';
  protected static final char ROTATE_MODE = 'r';
  protected static final char SCALE_MODE = 's';
  protected static final char TRANSLATE_MODE = 't';
  protected static final char SHOW_MESH = 'm';
  protected static final char FORCE_DRAG = 'f';
  protected static final char EDIT_MESH = 'n';
  protected static final char TOGGLE = ' ';

  private Stack<List<Transformable>> transformables;
  private List<Drawable> drawables;
  private Transformable activeTransformable;
  private Draggable lastDragged;
  private Mode mode;
  private PApplet parent;
  private boolean meshesShown;

  public Mapper(PApplet parent) {
    // this.mode = new WarpMode(this);

    this.parent = parent;

    parent.registerMethod("mouseEvent", this);
    parent.registerMethod("keyEvent", this);
    parent.registerMethod("draw", this);

    transformables = new Stack<>();
    transformables.add(new ArrayList<>());

    drawables = new ArrayList<>();

    setMode(new NoOpMode(this));

    System.out.println();
    System.out.println("SPACEFILLER MAPPER");
    System.out.println("==================");
    System.out.println();
    System.out.println("space" + "\t toggle mapper");
    System.out.println("ctrl+click" + "\t drill into surface");
    System.out.println(DRILL_OUT + "\t\t\t drill out");
    System.out.println(WARP_MODE + "+drag" + "\t\t warp");
    System.out.println(ROTATE_MODE + "+drag\t\t rotate");
    System.out.println(TRANSLATE_MODE + "+drag\t\t translate");
    System.out.println(SHOW_MESH + "\t\t\t toggle mesh");
    System.out.println(FORCE_DRAG + "\t\t\t force drag");
    System.out.println();
  }

  public void mouseEvent(MouseEvent event) {
    if (mode instanceof NoOpMode) {
      for (Transformable transformable : getRootTransformables()) {
        if (transformable instanceof Surface) {
          ((Surface) transformable).mouseEvent(event);
        }
      }
    } if (event.getAction() == MouseEvent.PRESS) {
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
      if (keyEvent.getKey() == TOGGLE) {
        if (mode instanceof NoOpMode) {
          setMode(new WarpMode( this));
        } else {
          setMode(new NoOpMode(this));
        }
      } else if (keyEvent.getKey() == DRILL_OUT) {
        drillOut();
      } else if (keyEvent.getKey() == WARP_MODE) {
        setMode(new WarpMode(this));
      } else if (keyEvent.getKey() == ROTATE_MODE) {
        setMode(new RotateMode(this));
      } else if (keyEvent.getKey() == SCALE_MODE) {
        setMode(new ScaleMode(this));
      } else if (keyEvent.getKey() == TRANSLATE_MODE) {
        setMode(new TranslateMode(this));
      } else if (keyEvent.getKey() == SHOW_MESH) {
        toggleMeshes();
      }
    } else if (keyEvent.getAction() == KeyEvent.RELEASE) {
      // if we're in NoOp mode, stay in NoOp mode even when key is released
      if (!(mode instanceof NoOpMode)) {
        setMode(new WarpMode(this));
      }
    }

    mode.keyEvent(keyEvent);
  }

  private void setMode(Mode newMode) {
    if (mode != null) {
      mode.stop();
    }
    mode = newMode;
    mode.start();
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

  public Surface createSurface(Grid grid) {
    Surface surface = new Surface(grid);
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

  public void save() {

  }

  private void addTransformable(Transformable transformable) {
    if (!(mode instanceof NoOpMode)) {
      transformable.setShowUI(true);
    }
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
    // turn off the current layer's UI
    hideUI();

    if (transformables.size() > 1) {
      transformables.pop();
    }

    // turn on the parent layer's UI
    clearActiveTransformable();

    showUI();
  }


  protected void drillIn(Transformable transformable) {
    if (transformable.getChildren() != null && !transformable.getChildren().isEmpty()) {
      hideUI();

      clearActiveTransformable();
      pushTransformables(transformable.getChildren());

      showUI();
    }
  }

  protected void showUI() {
    for (Transformable t : getCurrentActiveLayer()) {
      t.setShowUI(true);
    }
  }

  protected void hideUI() {
    if (getCurrentActiveLayer() != null) {
      for (Transformable t : getCurrentActiveLayer()) {
        t.setShowUI(false);
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
