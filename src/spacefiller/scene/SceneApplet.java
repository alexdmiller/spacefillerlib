package spacefiller.scene;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PJOGL;

import java.util.ArrayList;
import java.util.List;

public class SceneApplet extends PApplet {
  public static int WIDTH = 1920;
  public static int HEIGHT = 1080;

  protected int nextSceneIndex = -1;
  protected int currentSceneIndex = -1;
  protected boolean transitionIn = false;
  protected boolean transitioning = false;

  protected List<Scene> scenes;
  protected PGraphics canvas;

  public SceneApplet() {
    scenes = new ArrayList<>();
  }

  public void settings() {
    // fullScreen(2);
    size(1920, 1080, P2D);
    PJOGL.profile = 1;
  }

  public void setup() {
    setCanvas(getGraphics());
    queueScene(0);
  }

  public interface TransitionCallback {
    void done();
  }

  public void draw() {
    canvas.background(0);

    if (currentSceneIndex != -1) {
      scenes.get(currentSceneIndex).draw(canvas);
    }

    // if there's a next scene, we should be transitioning out of the old scene
//    if (nextSceneIndex != -1) {
//      Scene nextScene = scenes.get(nextSceneIndex);
//
//      if (currentScene == null) {
//        swapScene(nextScene);
//      } else if (currentScene.transitionOut()) {
//        // we're done transitioning
//        if (currentScene.alwaysReset()) {
//          currentScene.teardown();
//        }
//        swapScene(nextScene);
//      }
//    }
//
//    if (currentScene != null) {
//      currentScene.draw(this.canvas);
//    }
  }

  private void swapNextScene() {
    currentSceneIndex = nextSceneIndex;
    nextSceneIndex = -1;

    scenes.get(currentSceneIndex).transitionIn(() -> {
      transitioning = false;
    });
  }

  public void queueScene(int sceneIndex) {
    if (sceneIndex < scenes.size()) {
      nextSceneIndex = sceneIndex;

      transitioning = true;

      if (currentSceneIndex != -1) {
        scenes.get(currentSceneIndex).transitionOut(() -> {
          swapNextScene();
        });
      } else {
        swapNextScene();
      }
    }
  }

  public void queueNextScene() {
    queueScene((currentSceneIndex + 1) % scenes.size());
  }

  public void addScene(Scene scene) {
    scene.setDimensions(width, height);
    scene.setParent(this);
    scenes.add(scene);
    scene.setup();
  }

  public final void addAllScenes(Scene[] sceneArray) {
    for (int i = 0; i < sceneArray.length; i++) {
      addScene(sceneArray[i]);
    }
  }

  public void setCanvas(PGraphics canvas) {
    this.canvas = canvas;
  }
}
