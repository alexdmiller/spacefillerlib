package spacefiller.particles;

import spacefiller.Vector;
import spacefiller.particles.behaviors.ParticleBehavior;

import java.util.List;

public class WiggleBehavior extends ParticleBehavior {
  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    particle.applyForce(Vector.random2D());
  }
}
