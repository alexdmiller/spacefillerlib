package spacefiller.particles;

import spacefiller.Vector;
import spacefiller.particles.behaviors.ParticleBehavior;
import spacefiller.particles.sources.AreaSource;
import spacefiller.particles.sources.PointSource;
import spacefiller.particles.sources.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
  private int maxParticles;

  private ArrayList<Particle>[] hash;
  private int rows, cols;
  private float cellSize;
  private ExecutorService pool;

  public ParticleSystem(Bounds bounds, int maxParticles) {
    this.maxParticles = maxParticles;
    this.bounds = bounds;
    this.particles = new ArrayList<>();
    this.behaviors = new ArrayList<>();
    this.particleEventListeners = new ArrayList<>();
    this.sources = new ArrayList<>();
    this.pool = Executors.newFixedThreadPool(10);
    computeHash(100);
  }

  private void computeHash(int cellSize) {
    this.cellSize = cellSize;
    this.rows = (int) Math.ceil((bounds.getHeight() + cellSize) / cellSize);
    this.cols = (int) Math.ceil((bounds.getWidth() + cellSize) / cellSize);
    this.hash = new ArrayList[rows * cols];
    for (int i = 0; i < hash.length; i++) {
      hash[i] = new ArrayList<>();
    }

    // TODO: rehash particles
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

  public Particle createParticle(Vector position, int dimension) {
    Particle p = new Particle(position);
    p.setRandomVelocity(1, 2, dimension);
    particles.add(p);

    if (bounds.contains(position)) {
      hash[hashPosition(position)].add(p);
    }

    for (ParticleEventListener eventListener : particleEventListeners) {
      eventListener.particleAdded(p);
    }

    return p;
  }


  public void createPointSource(float x, float y, int spawnRate, int dimension) {
    sources.add(new PointSource(new Vector(x, y), spawnRate, dimension));
  }

  public void createAreaSource(Vector position, Bounds bounds, int spawnRate, int dimension) {
    sources.add(new AreaSource(position, bounds, spawnRate, dimension));
  }

  public void createAreaSource(int spawnRate, int dimension) {
    createAreaSource(new Vector(0, 0), this.bounds, spawnRate, dimension);
  }

  public List<Source> getSources() {
    return sources;
  }

  public void update() throws InterruptedException {
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

    List<Callable<Object>> runnables = new ArrayList<>();
    for (Particle p : particles) {
      runnables.add(Executors.callable(new ParticleRunnable(p)));
    }
    pool.invokeAll(runnables);
  }

  private int hashPosition(Vector position) {
    return (int) (position.y / cellSize) * cols + (int) (position.x / cellSize);
  }

  protected List<Particle> getNeighbors(Vector position, float radius) {
    // return particles;

    if (radius == 0) {
      return null;
    }

    if (radius > cellSize) {
      // TODO: recompute hash
      // System.out.println("BAD");
    }

    int cx = (int) (position.x / cellSize);
    int cy = (int) (position.y / cellSize);

    List<Particle> neighbors = new ArrayList<>();

    for (int x = cx - 1; x <= cx + 1; x++) {
      for (int y = cy - 1; y <= cy + 1; y++) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
          neighbors.addAll(hash[x + y * cols]);
        }
      }
    }

    return neighbors;
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

  public float getCellSize() {
    return cellSize;
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  private class ParticleRunnable implements Runnable {
    private Particle p;

    public ParticleRunnable(Particle particle) {
      this.p = particle;
    }

    @Override
    public void run() {
      int oldHash = hashPosition(p.position);

      for (ParticleBehavior behavior : behaviors) {
        behavior.apply(p, getNeighbors(p.position, behavior.neighborhoodRadius()));
      }

      p.flushForces(maxForce);
      p.update();

      int newHash = hashPosition(p.position);
      if (newHash != oldHash) {
        synchronized (hash) {
          hash[oldHash].remove(p);
          hash[newHash].add(p);
        }
      }
    }
  }
}
