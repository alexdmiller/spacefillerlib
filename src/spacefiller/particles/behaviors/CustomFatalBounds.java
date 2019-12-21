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
  public void apply(Particle particle, List<Particle> neighbors) {
    // TODO: implement removeFlag logic in particle system
    if (!bounds.contains(particle.getPosition())) {
      particle.removeFlag = true;
    }
  }
}
