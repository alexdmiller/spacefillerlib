package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.List;

public class AttractParticles extends ParticleBehavior {
  private float attractThreshold;
  private float attractStrength;

  public AttractParticles(float attractThreshold, float attractStrength) {
    this.attractThreshold = attractThreshold;
    this.attractStrength = attractStrength;
  }

  @Override
  public void apply(List<Particle> particles) {
    for (Particle p1 : particles) {
      for (Particle p2 : particles) {
        Vector delta = Vector.sub(p1.position, p2.position);
        float dist = (float) delta.magnitude();
        if (dist < attractThreshold) {
          delta.setMag(attractStrength);
          p2.applyForce(delta);
          delta.mult(-1);
          p1.applyForce(delta);
        }
      }
    }
  }
}
