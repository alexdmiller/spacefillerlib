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

  public void setRepelThreshold(float repelThreshold) {
    this.repelThreshold = repelThreshold;
  }

  @Override
  public void apply(List<Particle> particles) {
    for (Particle p1 : particles) {
      for (Particle p2 : particles) {
        Vector delta = Vector.sub(p1.position, p2.position);
        float dist = (float) delta.magnitude();
        if (dist < repelThreshold) {
          delta.setMag(repelStrength);
          p1.applyForce(delta);
          delta.mult(-1);
          p2.applyForce(delta);
        }
      }
    }
  }
}
