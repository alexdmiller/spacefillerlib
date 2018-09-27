package spacefiller.particles.behaviors;

import spacefiller.particles.Particle;

import java.util.List;

/**
 * Created by miller on 7/13/17.
 */
public class ParticleFriction extends ParticleBehavior {
  private float friction;

  public ParticleFriction(float friction) {
    this.friction = friction;
  }

  @Override
  public void apply(Particle p, List<Particle> neighbors) {
    p.applyFriction(friction);
  }
}
