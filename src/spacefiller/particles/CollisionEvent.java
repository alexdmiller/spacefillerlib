package spacefiller.particles;

import processing.core.PVector;
import spacefiller.Vector;

public class CollisionEvent {
  private Particle particle;
  private Vector collisionLocation;
  private Vector preCollisionVelocity;
  private Vector normal;

  public CollisionEvent(Particle particle, Vector collisionLocation, Vector preCollisionVelocity, Vector normal) {
    this.particle = particle;
    this.collisionLocation = collisionLocation;
    this.preCollisionVelocity = preCollisionVelocity;
    this.normal = normal;
  }

  public Particle getParticle() {
    return particle;
  }

  public Vector getCollisionLocation() {
    return collisionLocation;
  }

  public Vector getPreCollisionVelocity() {
    return preCollisionVelocity;
  }

  public Vector getNormal() {
    return normal;
  }
}
