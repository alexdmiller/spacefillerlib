package spacefiller.mapping.modes;

import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.mapping.Mapper;

public class Mode {
  protected Mapper mooYoung;

  public Mode(Mapper mooYoung) {
    this.mooYoung = mooYoung;
  }

  public void draw() { }
  public void mouseEvent(MouseEvent mouseEvent) {}
  public void keyEvent(KeyEvent keyEvent) {}
}
