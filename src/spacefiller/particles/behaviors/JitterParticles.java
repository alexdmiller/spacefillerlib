package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.List;

public class JitterParticles extends ParticleBehavior {
  private float jitterStrength = 1;

  public JitterParticles(float jitterStrength) {
    this.jitterStrength = jitterStrength;
  }

  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    particle.applyForce(Vector.random3D().setMag(jitterStrength));
  }
}
