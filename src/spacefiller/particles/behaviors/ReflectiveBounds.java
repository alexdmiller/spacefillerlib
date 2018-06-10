package spacefiller.particles.behaviors;

import spacefiller.particles.Bounds;
import spacefiller.particles.Particle;

import java.util.List;

public class ReflectiveBounds extends ParticleBehavior {
  @Override
  public void apply(List<Particle> particles) {
    for (Particle p : particles) {
      getParticleSystem().getBounds().constrain(p);
    }
  }
}
