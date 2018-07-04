package spacefiller.particles.sources;

import processing.core.PVector;

public interface Source {
  PVector generatePoint();
  int getSpawnRate();
  int getDimension();
}
