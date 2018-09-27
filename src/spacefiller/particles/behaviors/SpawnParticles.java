package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Particle;
import java.util.List;

public class SpawnParticles extends ParticleBehavior {
  public float wiggleAmplitudeMultiplier = 1;
  public float wiggleStepMultiplier = 10;

  @Override
  public void apply(List<Particle> particles) {
    for (Particle particle : particles) {
      Vector wiggleDirection = particle.position.copy().rotate((float) Math.PI / 2f);

      if (!particle.hasUserData("wiggleStep")) {
        particle.setUserData("wiggleStep", 0f);
      }
      float wiggleStep = (float) particle.getUserData("wiggleStep");

      wiggleDirection.setMag((float) Math.sin(wiggleStep) * particle.velocity.magnitude() * wiggleAmplitudeMultiplier);
      particle.position.add(wiggleDirection);
      wiggleStep += particle.position.magnitude() * wiggleStepMultiplier;
      particle.setUserData("wiggleStep", wiggleStep);
    }
  }
}
