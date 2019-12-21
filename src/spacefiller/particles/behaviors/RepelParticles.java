package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.List;

public class RepelParticles extends ParticleBehavior {
  private float repelThreshold;
  private float repelStrength;

  public RepelParticles(float repelThreshold, float repelStrength) {
    this.repelThreshold = repelThreshold;
    this.repelStrength = repelStrength;
  }

  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    for (Particle p2 : neighbors) {
      Vector delta = Vector.sub(particle.getPosition(), p2.getPosition());
      float mag = (float) delta.magnitude();
      if (mag < repelThreshold) {
        float force = repelStrength / (mag * mag + 0.0001f);
        delta.mult(force);
        particle.applyForce(delta);
      }
    }
  }

  public float getRepelThreshold() {
    return repelThreshold;
  }

  public void setRepelThreshold(float repelThreshold) {
    this.repelThreshold = repelThreshold;
  }

  public float getRepelStrength() {
    return repelStrength;
  }

  public void setRepelStrength(float repelStrength) {
    this.repelStrength = repelStrength;
  }
}
