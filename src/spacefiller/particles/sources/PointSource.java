package spacefiller.particles.sources;

import processing.core.PVector;

public class PointSource implements Source {
  private PVector position;
  private int spawnRate;
  private int dimension;

  public PointSource(float x, float y, int spawnRate, int dimension) {
    this(new PVector(x, y), spawnRate, dimension);
  }

  public PointSource(PVector position, int spawnRate, int dimension) {
    this.position = position;
    this.spawnRate = spawnRate;
    this.dimension = dimension;
  }

  public PVector getPosition() {
    return position;
  }

  public void setPosition(PVector position) {
    this.position = position;
  }

  public int getSpawnRate() {
    return spawnRate;
  }

  public void setSpawnRate(int spawnRate) {
    this.spawnRate = spawnRate;
  }

  public int getDimension() {
    return dimension;
  }

  @Override
  public PVector generatePoint() {
    return getPosition().copy();
  }
}
