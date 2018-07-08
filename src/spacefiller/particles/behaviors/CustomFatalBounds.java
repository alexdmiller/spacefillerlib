package spacefiller.particles.behaviors;

import spacefiller.particles.Bounds;
import spacefiller.particles.Particle;

import java.util.Iterator;
import java.util.List;

public class CustomFatalBounds extends ParticleBehavior {
  private Bounds bounds;

  public CustomFatalBounds(Bounds bounds) {
    this.bounds = bounds;
  }

  @Override
  public void apply(List<Particle> particles) {
    Iterator<Particle> iter = particles.iterator();
    while (iter.hasNext()) {
      Particle p = iter.next();

      if (!bounds.contains(p.position)) {
        iter.remove();
        getParticleSystem().notifyRemoved(p);
      }
    }
  }
}
