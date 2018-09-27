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
  public void apply(List<Particle> particles) {
    for (Particle p1 : particles) {
      p1.applyForce(Vector.random3D().setMag(jitterStrength));
    }
  }
}
