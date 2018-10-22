package spacefiller.particles.behaviors;

import spacefiller.particles.Particle;
import spacefiller.particles.ParticleSystem;

import java.util.List;

public abstract class ParticleBehavior {
  private ParticleSystem particleSystem;

  public ParticleSystem getParticleSystem() {
    return particleSystem;
  }
  public void setParticleSystem(ParticleSystem particleSystem) {
    this.particleSystem = particleSystem;
  }
  public abstract void apply(Particle particle, List<Particle> neighbors);
}
