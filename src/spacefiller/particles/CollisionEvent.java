package spacefiller.particles;

import processing.core.PVector;
import spacefiller.Vector;

public class CollisionEvent {
  private Particle particle;
  private Vector collisionLocation;
  private Vector preCollisionVelocity;

  public CollisionEvent(Particle particle, Vector collisionLocation, Vector preCollisionVelocity) {
    this.particle = particle;
    this.collisionLocation = collisionLocation;
    this.preCollisionVelocity = preCollisionVelocity;
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
}
