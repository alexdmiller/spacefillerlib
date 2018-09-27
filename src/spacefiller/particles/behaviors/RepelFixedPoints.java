package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.ArrayList;
import java.util.List;

public class RepelFixedPoints extends ParticleBehavior {
  public float repelThreshold;
  public float repelStrength;

  private List<Vector> fixedPoints;

  public RepelFixedPoints (float repelThreshold, float repelStrength) {
    this.repelThreshold = repelThreshold;
    this.repelStrength = repelStrength;
    this.fixedPoints = new ArrayList<>();
  }

  public void setRepelThreshold(float repelThreshold) {
    this.repelThreshold = repelThreshold;
  }

  public void setRepelStrength(float repelStrength) {
    this.repelStrength = repelStrength;
  }

  public void addFixedPoint(Vector p) {
    fixedPoints.add(p);
  }

  public List<Vector> getFixedPoints() {
    return fixedPoints;
  }

  @Override
  public void apply(List<Particle> particles) {
    for (Particle p1 : particles) {
      for (Vector fixed : fixedPoints) {
        Vector delta = Vector.sub(p1.position, fixed);
        float dist = (float) delta.magnitude();
        if (dist < repelThreshold) {
          delta.setMag(dist * repelStrength);
          p1.applyForce(delta);
        }
      }
    }
  }
}
