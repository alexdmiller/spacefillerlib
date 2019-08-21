package spacefiller.mapping;


public class NoOpMode extends Mode {
  public NoOpMode(Mapper mapper) {
    super(mapper);
  }

  @Override
  public void start() {
    mapper.hideUI();
  }

  @Override
  public void stop() {
    mapper.showUI();
  }
}
