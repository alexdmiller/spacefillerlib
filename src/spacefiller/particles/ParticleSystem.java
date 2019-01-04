package spacefiller.particles;

import processing.core.PVector;
import spacefiller.Vector;
import spacefiller.particles.behaviors.ParticleBehavior;
import spacefiller.particles.sources.AreaSource;
import spacefiller.particles.sources.PointSource;
import spacefiller.particles.sources.Source;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by miller on 7/13/17.
 */
public class ParticleSystem {
  private static ExecutorService pool = Executors.newFixedThreadPool(4);

  public static void setThreadCount(int num) {
    pool = Executors.newFixedThreadPool(num);
  }

  private Bounds bounds;
  private List<Particle> particles;
  private List<Spring> springs;
  private List<Source> sources;
  private List<ParticleBehavior> behaviors;
  private float maxForce = 10;
  private List<ParticleEventListener> particleEventListeners;
  private int maxParticles;
  private HashMap<Integer, List<Particle>> teamMap;

  private ArrayList<Particle>[] hash;
  private int rows, cols;
  private float cellSize;

  public ParticleSystem(Bounds bounds, int maxParticles, float cellSize) {
    this.maxParticles = maxParticles;
    this.bounds = bounds;
    this.particles = new ArrayList<>();
    this.behaviors = new ArrayList<>();
    this.particleEventListeners = new ArrayList<>();
    this.sources = new ArrayList<>();
    this.springs = new ArrayList<>();
    this.teamMap = new HashMap<>();
    computeHash(cellSize);
  }

  public ParticleSystem(Bounds bounds, int maxParticles) {
    this(bounds, maxParticles, 100);
  }

  public ParticleSystem(float width, float height) {
    this(new Bounds(width, height), 8000, 100);
  }

  public ParticleSystem(float width, float height, float cellSize) {
    this(new Bounds(width, height), 8000, cellSize);
  }

  private void computeHash(float cellSize) {
    this.cellSize = cellSize;
    this.rows = (int) Math.ceil((bounds.getHeight() + cellSize) / cellSize);
    this.cols = (int) Math.ceil((bounds.getWidth() + cellSize) / cellSize);
    this.hash = new ArrayList[rows * cols];
    for (int i = 0; i < hash.length; i++) {
      hash[i] = new ArrayList<>();
    }

    // TODO: rehash particles
  }

  protected void registerTeam(Particle p, int team) {
    if (p.getTeam() != -1) {
      teamMap.get(p.getTeam()).remove(p);
    }

    if (!teamMap.containsKey(team)) {
      teamMap.put(team, new ArrayList<>());
    }

    teamMap.get(team).add(p);
  }

  public List<Particle> getTeam(int team) {
    return teamMap.get(team);
  }

  public Collection<List<Particle>> getTeams() {
    return teamMap.values();
  }

  public void setMaxParticles(int maxParticles) {
    this.maxParticles = maxParticles;
  }

  public void registerEventListener(ParticleEventListener particleEventListener) {
    // TODO: allow adding / removing particles
    this.particleEventListeners.add(particleEventListener);
  }

  public void fillWithParticles(int numParticles, int dimension, int teams) {
    for (int i = 0; i < numParticles; i++) {
      Particle p = createParticle(bounds.getRandomPointInside(dimension), dimension, -1);
      p.setRandomVelocity(1, 2, dimension);
      p.setTeam((int) (Math.random() * teams));
    }
  }

  public void fillWithParticles(int numParticles, int dimension) {
    fillWithParticles(numParticles, dimension, 1);
  }

  public void fillWithParticles(int numParticles) {
    fillWithParticles(numParticles, 2);
  }

  public Particle createParticle(float x, float y, float z) {
    return createParticle(new Vector(x, y, z), 3, -1);
  }

  public Particle createParticle(float x, float y) {
    return createParticle(new Vector(x, y), 2, -1);
  }


  public void createLoop(float x, float y, float radius, int num, float springLength, float springK, int team) {
    PVector center = new PVector(x, y);
    List<Particle> particles = new ArrayList<>();

    for (int i = 0; i < num; i++) {
      float theta = (float) (2f* Math.PI / num) * i;
      Particle p = createParticle(
          (float) (center.x + Math.cos(theta) * radius),
          (float) (center.y + Math.sin(theta) * radius));

      p.setTeam(team);
      particles.add(p);
    }

    Particle first = particles.get(0);
    Particle last = particles.get(particles.size() - 1);

    createSpring(first, particles.get(1), springLength, springK);
    createSpring(first, last, springLength, springK);

    createSpring(last, particles.get(particles.size() - 2), springLength, springK);
    createSpring(last, first, springLength, springK);

    for (int i = 1; i < particles.size() - 1; i++) {
      Particle n = particles.get(i);
      createSpring(n, particles.get(i - 1), springLength, springK);
      createSpring(n, particles.get(i + 1), springLength, springK);
    }

  }

  public Particle createParticle(Vector position) {
    return createParticle(position, 2, -1);
  }

  public Particle createParticle(Vector position, int dimension, int team) {
    Particle p = new Particle(this, position);
    // p.setRandomVelocity(1, 2, dimension);
    particles.add(p);

    if (bounds.contains(position)) {
      hash[hashPosition(position)].add(p);
    }

    for (ParticleEventListener eventListener : particleEventListeners) {
      eventListener.particleAdded(p);
    }

    if (team != -1) {
      p.setTeam(team);
    }

    return p;
  }

  public Spring createSpring(Particle n1, Particle n2, float length, float k) {
    Spring spring = new Spring(n1, n2, length, k);
    springs.add(spring);
    n1.addConnection(spring);
    n2.addConnection(spring);
    return spring;
  }

  public List<Spring> getSprings() {
    return springs;
  }

  public PointSource createPointSource(float x, float y, int spawnRate, int dimension) {
    PointSource pointSource = new PointSource(new Vector(x, y), spawnRate, dimension);
    sources.add(pointSource);
    return pointSource;
  }

  public AreaSource createAreaSource(Vector position, Bounds bounds, int spawnRate, int dimension) {
    AreaSource areaSource = new AreaSource(position, bounds, spawnRate, dimension);
    sources.add(areaSource);
    return areaSource;
  }

  public AreaSource createAreaSource(int spawnRate, int dimension) {
    return createAreaSource(new Vector(0, 0), this.bounds, spawnRate, dimension);
  }

  public List<Source> getSources() {
    return sources;
  }

  public void clear() {
    for (Particle p : particles) {
      p.removeFlag = true;
    }
  }

  public void update() throws InterruptedException {
    for (Spring spring : springs) {
      spring.update();
    }

    if (particles.size() < maxParticles) {
      if (sources.size() > 0) {
        // TODO: This is a strange way to spawn particles. Should spread them around to
        // all sources; not just update a single source.

        for (Source source : getSources()) {
          for (int i = 0; i < source.getSpawnRate() && particles.size() < maxParticles; i++) {
            Particle p = createParticle(source.generatePoint(), source.getDimension(), source.getTeam());
            p.setRandomVelocity(0.5f, 1, 2);
          }
        }
      }
    }

    List<Callable<Object>> runnables = new ArrayList<>();
    for (Particle p : particles) {
      runnables.add(Executors.callable(new ParticleRunnable(p)));
    }
    pool.invokeAll(runnables);

    for (int i = particles.size() - 1; i >= 0; i--) {
      Particle p = particles.get(i);
      if (p.removeFlag) {
        int oldHash = hashPosition(p.position);
        if (oldHash >= 0) {
          hash[oldHash].remove(p);
        }

        particles.remove(i);

        if (teamMap.get(p.getTeam()) != null) {
          teamMap.get(p.getTeam()).remove(p);
        }
      }
    }
  }

  private int hashPosition(Vector position) {
    return (int) (position.y / cellSize) * cols + (int) (position.x / cellSize);
  }

  protected List<Particle> getNeighbors(Vector position) {
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
        behavior.apply(p, getNeighbors(p.position));
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

  public void disconnect(Particle n1, Particle n2) {
    Spring spring = findSpring(n1, n2);
    if (spring != null) {
      springs.remove(spring);
      n1.getConnections().remove(spring);
      n2.getConnections().remove(spring);
    }
  }

  public Spring findSpring(Particle n1, Particle n2) {
    for (Spring spring : springs) {
      if (spring.other(n1) == n2) {
        return spring;
      }
    }
    return null;
  }
}
