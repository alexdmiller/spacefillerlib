package spacefiller.mapping.modes;

import geomerative.RShape;
import processing.core.PGraphics;
import spacefiller.mapping.MappingHost;

public class EditMode extends Mode {
  public EditMode(MappingHost mooYoung) {
    super(mooYoung);
    mooYoung.cursor();
  }
}
