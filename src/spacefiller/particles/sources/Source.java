package spacefiller.particles.sources;

import spacefiller.Vector;

public interface Source {
  Vector generatePoint();
  int getSpawnRate();
  int getDimension();
  int getTeam();
}
