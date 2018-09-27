package spacefiller.particles.sources;

import spacefiller.Vector;
import spacefiller.particles.Bounds;

public class AreaSource implements Source {
  private Vector position;
  private Bounds bounds;
  private int spawnRate;
  private int dimension;

  public AreaSource(Vector position, Bounds bounds, int spawnRate, int dimension) {
    this.position = position;
    this.bounds = bounds;
    this.spawnRate = spawnRate;
    this.dimension = dimension;
  }

  @Override
  public Vector generatePoint() {
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
