package spacefiller.particles.sources;

import processing.core.PVector;
import spacefiller.particles.Bounds;

public class AreaSource implements Source {
  private PVector position;
  private Bounds bounds;
  private int spawnRate;
  private int dimension;

  public AreaSource(PVector position, Bounds bounds, int spawnRate, int dimension) {
    this.position = position;
    this.bounds = bounds;
    this.spawnRate = spawnRate;
    this.dimension = dimension;
  }

  @Override
  public PVector generatePoint() {
    return this.bounds.getRandomPointInside(dimension).add(position);
  }

  @Override
  public int getSpawnRate() {
    return spawnRate;
  }

  @Override
  public int getDimension() {
    return dimension;
  }
}
