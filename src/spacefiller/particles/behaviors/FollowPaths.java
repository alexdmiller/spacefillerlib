package spacefiller.particles.behaviors;

import javafx.util.Pair;
import spacefiller.particles.Particle;
import spacefiller.particles.ParticleUtils;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class FollowPaths extends ParticleBehavior {
  private List<Pair<PVector, PVector>> pathSegments;
  public float radius = 20;
  public float maxForce = 5;
  public float maxSpeed = 10;

  public FollowPaths() {
    pathSegments = new ArrayList<>();
  }

  public void addPathSegment(PVector start, PVector end) {
    pathSegments.add(new Pair<>(start, end));
  }

  @Override
  public void apply(List<Particle> particles) {
    for (Particle particle : particles) {
      PVector closestNormalPoint = null;
      float closestDistance = 0;

      for (Pair<PVector, PVector> p : pathSegments) {
        if (p != null) {
          PVector normalPoint = getNormalPoint(p.getKey(), p.getValue(), particle);
          float distance = PVector.sub(particle.position, normalPoint).mag();
          if (closestNormalPoint == null || distance < closestDistance) {
            closestNormalPoint = normalPoint;
            closestDistance = distance;
          }
        }
      }

      if (closestDistance > radius) {
        PVector steer =
            ParticleUtils.seek(particle, closestNormalPoint, maxSpeed, maxForce);
        particle.applyForce(steer);
      }
    }
  }

  private PVector getNormalPoint(PVector start, PVector end, Particle particle) {
    PVector predictedPosition = PVector.add(particle.position, particle.velocity);
    PVector a = PVector.sub(predictedPosition, start);
    PVector b = PVector.sub(end, start);
    float segmentLength = b.mag();

    float theta = PVector.angleBetween(a, b);

    float d = (float) (a.mag() * Math.cos(theta));
    d = Math.max(Math.min(d, segmentLength), 0);
    b.setMag(d);

    PVector normalPoint = PVector.add(start, b);
    return normalPoint;
  }
}
