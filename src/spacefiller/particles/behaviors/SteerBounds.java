package spacefiller.particles.behaviors;

import processing.core.PVector;
import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.List;

public class SteerBounds extends ParticleBehavior {
  private float threshold = 20;
  private float maxSpeed = 4;
  private float maxForce = 0.5f;

  public SteerBounds(float threshold, float maxSpeed, float maxForce) {
    this.threshold = threshold;
    this.maxSpeed = maxSpeed;
    this.maxForce = maxForce;
  }

  public SteerBounds() {
    this(20, 4, 0.5f);
  }

  public float getThreshold() {
    return threshold;
  }

  public void setThreshold(float threshold) {
    this.threshold = threshold;
  }

  public float getMaxSpeed() {
    return maxSpeed;
  }

  public void setMaxSpeed(float maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  public float getMaxForce() {
    return maxForce;
  }

  public void setMaxForce(float maxForce) {
    this.maxForce = maxForce;
  }

  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    Vector topLeft = getParticleSystem().getBounds().getTopBackLeft();
    Vector bottomRight = getParticleSystem().getBounds().getBottomFrontRight();

    Vector desired = null;

    if (particle.position.x < topLeft.x + threshold) {
      desired = new Vector(maxSpeed, particle.velocity.y);
    }

    if (particle.position.x > bottomRight.x - threshold) {
      desired = new Vector(-maxSpeed, particle.velocity.y);
    }

    if (particle.position.y < topLeft.y + threshold) {
      desired = new Vector(particle.velocity.x, maxSpeed);
    }

    if (particle.position.y > bottomRight.y - threshold) {
      desired = new Vector(particle.velocity.x, -maxSpeed);
    }

    if (desired != null) {
      Vector steer = Vector.sub(desired, particle.velocity);
      steer.limit(maxForce);
      particle.applyForce(steer);
    }
  }
}
