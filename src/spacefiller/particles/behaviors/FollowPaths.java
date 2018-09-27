package spacefiller.particles.behaviors;

import javafx.util.Pair;
import spacefiller.Vector;
import spacefiller.particles.Particle;
import spacefiller.particles.ParticleUtils;

import java.util.ArrayList;
import java.util.List;

public class FollowPaths extends ParticleBehavior {
  private List<Pair<Vector, Vector>> pathSegments;
  public float radius = 20;
  public float maxForce = 5;
  public float maxSpeed = 10;

  public FollowPaths() {
    pathSegments = new ArrayList<>();
  }

  public void addPathSegment(Vector start, Vector end) {
    pathSegments.add(new Pair<>(start, end));
  }

  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    Vector closestNormalPoint = null;
    float closestDistance = 0;

    for (Pair<Vector, Vector> p : pathSegments) {
      if (p != null) {
        Vector normalPoint = getNormalPoint(p.getKey(), p.getValue(), particle);
        float distance = (float) Vector.sub(particle.position, normalPoint).magnitude();
        if (closestNormalPoint == null || distance < closestDistance) {
          closestNormalPoint = normalPoint;
          closestDistance = distance;
        }
      }
    }

    if (closestDistance > radius) {
      Vector steer =
          ParticleUtils.seek(particle, closestNormalPoint, maxSpeed, maxForce);
      particle.applyForce(steer);
    }
  }

  private Vector getNormalPoint(Vector start, Vector end, Particle particle) {
    Vector predictedPosition = Vector.add(particle.position, particle.velocity);
    Vector a = Vector.sub(predictedPosition, start);
    Vector b = Vector.sub(end, start);
    float segmentLength = (float) b.magnitude();

    float theta = Vector.angleBetween(a, b);

    float d = (float) (a.magnitude() * Math.cos(theta));
    d = Math.max(Math.min(d, segmentLength), 0);
    b.setMag(d);

    Vector normalPoint = Vector.add(start, b);
    return normalPoint;
  }
}
