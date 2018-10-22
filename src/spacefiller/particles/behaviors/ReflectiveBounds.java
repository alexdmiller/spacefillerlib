package spacefiller.particles.behaviors;

import spacefiller.particles.Bounds;
import spacefiller.particles.Particle;

import java.util.List;

public class ReflectiveBounds extends ParticleBehavior {
  @Override
  public void apply(Particle particles, List<Particle> neighbors) {
    getParticleSystem().getBounds().constrain(particles);
  }
}
