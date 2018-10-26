package spacefiller.mapping.modes;

import processing.event.KeyEvent;
import processing.event.MouseEvent;
import spacefiller.mapping.MappingHost;

public class Mode {
  protected MappingHost mooYoung;

  public Mode(MappingHost mooYoung) {
    this.mooYoung = mooYoung;
  }

  public void draw() { }
  public void mouseEvent(MouseEvent mouseEvent) {}
  public void keyEvent(KeyEvent keyEvent) {}
}
