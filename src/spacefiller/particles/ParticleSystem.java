package spacefiller.particles;

import spacefiller.particles.behaviors.ParticleBehavior;
import processing.core.PVector;
import spacefiller.particles.sources.AreaSource;
import spacefiller.particles.sources.PointSource;
import spacefiller.particles.sources.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miller on 7/13/17.
 */
public class ParticleSystem {
  private Bounds bounds;
  private List<Particle> particles;
  private List<Source> sources;
  private List<ParticleBehavior> behaviors;
  private float maxForce = 10;
  private List<ParticleEventListener> particleEventListeners;
  private int maxParticles = 200;

  public ParticleSystem(Bounds bounds, int maxParticles) {
    this.maxParticles = maxParticles;
    this.bounds = bounds;
    this.particles = new ArrayList<>();
    this.behaviors = new ArrayList<>();
    this.particleEventListeners = new ArrayList<>();
    this.sources = new ArrayList<>();
  }

  public void setMaxParticles(int maxParticles) {
    this.maxParticles = maxParticles;
  }

  public void registerEventListener(ParticleEventListener particleEventListener) {
    // TODO: allow adding / removing particles
    this.particleEventListeners.add(particleEventListener);
  }

  public void fillWithParticles(int numParticles, int dimension) {
    for (int i = 0; i < numParticles; i++) {
      Particle p = createParticle(bounds.getRandomPointInside(dimension), dimension);
      p.setRandomVelocity(1, 2, dimension);
    }
  }

  public Particle createParticle(PVector position, int dimension) {
    Particle p = new Particle(position);
    p.setRandomVelocity(1, 2, dimension);
    particles.add(p);

    for (ParticleEventListener eventListener : particleEventListeners) {
      eventListener.particleAdded(p);
    }

    return p;
  }


  public void createPointSource(float x, float y, int spawnRate, int dimension) {
    sources.add(new PointSource(new PVector(x, y), spawnRate, dimension));
  }

  public void createAreaSource(PVector position, Bounds bounds, int spawnRate, int dimension) {
    sources.add(new AreaSource(position, bounds, spawnRate, dimension));
  }

  public void createAreaSource(int spawnRate, int dimension) {
    createAreaSource(new PVector(0, 0), this.bounds, spawnRate, dimension);
  }

  public List<Source> getSources() {
    return sources;
  }

  public void update() {
    if (particles.size() < maxParticles) {
      if (sources.size() > 0) {
        // TODO: This is a strange way to spawn particles. Should spread them around to
        // all sources; not just update a single source.
        Source source = sources.get((int) Math.floor(Math.random() * sources.size()));
        for (int i = 0; i < source.getSpawnRate() && particles.size() < maxParticles; i++) {
          createParticle(source.generatePoint(), source.getDimension());
        }
      }
    }

    for (ParticleBehavior behavior : behaviors) {
      behavior.apply(particles);
    }

    for (Particle p : particles) {
      p.flushForces(maxForce);
      p.update();
    }
  }

  public List<Particle> getParticles() {
    return particles;
  }

  public void addBehavior(ParticleBehavior behavior) {
    behavior.setParticleSystem(this);
    this.behaviors.add(behavior);
  }

  public Bounds getBounds() {
    return bounds;
  }

  public void setBounds(Bounds bounds) {
    this.bounds = bounds;
  }

  public void notifyRemoved(Particle p) {
    for (ParticleEventListener particleEventListener : particleEventListeners) {
      particleEventListener.particleRemoved(p);
    }
  }
}
