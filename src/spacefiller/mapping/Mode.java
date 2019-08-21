package spacefiller.mapping;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class Mode {
  protected Mapper mapper;

  public Mode(Mapper mapper) {
    this.mapper = mapper;
  }

  public void draw() { }
  public void mouseEvent(MouseEvent mouseEvent) {}
  public void keyEvent(KeyEvent keyEvent) {}
  public void start() {}
  public void stop() {}
}
