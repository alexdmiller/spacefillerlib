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
public void apply(Particle particle, List<Particle> neighbors) {
    for (Particle p2 : neighbors) {
      Vector delta = Vector.sub(particle.position, p2.position);
      float dist = (float) delta.magnitude();
      if (dist < attractThreshold) {
        delta.setMag(attractStrength);
        p2.applyForce(delta);
        delta.mult(-1);
        particle.applyForce(delta);
      }
    }
  }
}
