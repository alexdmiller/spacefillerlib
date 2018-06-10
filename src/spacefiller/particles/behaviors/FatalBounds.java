package spacefiller.particles.behaviors;

import spacefiller.particles.Bounds;
import spacefiller.particles.Particle;

import java.util.Iterator;
import java.util.List;

public class FatalBounds extends ParticleBehavior {
  @Override
  public void apply(List<Particle> particles) {
    Iterator<Particle> iter = particles.iterator();
    while (iter.hasNext()) {
      Particle p = iter.next();

      if (!getParticleSystem().getBounds().contains(p.position)) {
        iter.remove();
        getParticleSystem().notifyRemoved(p);
      }
    }
  }
}
