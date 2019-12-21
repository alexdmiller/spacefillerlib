package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Particle;
import java.util.List;

public class WiggleParticles extends ParticleBehavior {
  public float wiggleAmplitudeMultiplier = 1;
  public float wiggleStepMultiplier = 10;

  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    Vector wiggleDirection = particle.getPosition().rotate((float) Math.PI / 2f);

    if (!particle.hasUserData("wiggleStep")) {
      particle.setUserData("wiggleStep", 0f);
    }
    float wiggleStep = (float) particle.getUserData("wiggleStep");

    wiggleDirection.setMag((float) Math.sin(wiggleStep) * particle.getVelocity().magnitude() * wiggleAmplitudeMultiplier);
    particle.translate(wiggleDirection);
    wiggleStep += particle.getPosition().magnitude() * wiggleStepMultiplier;
    particle.setUserData("wiggleStep", wiggleStep);
  }
}
