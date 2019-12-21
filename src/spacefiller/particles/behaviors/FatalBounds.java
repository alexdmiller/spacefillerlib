package spacefiller.particles.behaviors;

import spacefiller.particles.Bounds;
import spacefiller.particles.Particle;

import java.util.Iterator;
import java.util.List;

public class FatalBounds extends ParticleBehavior {
  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    if (!getParticleSystem().getBounds().contains(particle.getPosition())) {
      particle.removeFlag = true;
    }
  }
}
