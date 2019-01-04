package spacefiller.particles;

import spacefiller.Vector;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by miller on 9/28/16.
 */
public class Particle {
  public Vector position;
  public Vector velocity;
  public boolean removeFlag = false;
  public Color color;

  private Vector lastPosition;
  private Vector forces;
  private int team = -1;
  private Map<String, Object> userData;
  private Set<String> tags;
  private boolean teleportFlag = false;
  private ParticleSystem system;
  private int life;

  protected List<Spring> connections;
  protected boolean staticBody;

  public Particle(ParticleSystem system, Vector position, Color color) {
    this.position = position;
    this.velocity = new Vector();
    this.forces = new Vector();
    this.color = color;
    this.userData = new HashMap<>();
    this.connections = new ArrayList<>();
    this.tags = new HashSet<>();
    this.system = system;
  }

  public void seek(Vector target) {
    if (!staticBody) {
      Vector delta = Vector.sub(target, position);
      delta.mult(0.1f);
      velocity.add(delta);
    }
  }

  public Particle(ParticleSystem system, float x, float y, float z, Color color) {
    this(system, new Vector(x, y, z), color);
  }

  public Particle(ParticleSystem system) {
    this(system, new Vector(), null);
  }

  public Particle(ParticleSystem system, float x, float y, float z) {
    this(system, new Vector(x, y, z), null);
  }

  public Particle(ParticleSystem system, float x, float y) {
    this(system, new Vector(x, y), null);
  }

  public Particle(ParticleSystem system, Vector p) {
    this(system, p, null);
  }

  public void addTag(String tag) {
    tags.add(tag);
  }

  public boolean hasTag(String tag) {
    return tags.contains(tag);
  }

  public int getTeam() {
    return team;
  }

  public void setTeam(int team) {
    system.registerTeam(this, team);
    this.team = team;
  }

  public void update() {
    lastPosition = position.copy();

    if (!staticBody) {
      position.add(velocity);
    }

    life++;
  }

  public void applyFriction(float friction) {
    velocity.mult(friction);
  }

  public void flushForces(float limit) {
    forces.limit(limit);
    velocity.add(forces);
    if (forces.magnitude() > 0) {
      forces.setMag(0);
    }
  }

  public void applyForce(Vector force) {
    if (!staticBody) {
      forces.add(force);
    }
  }

  public void setRandomVelocity(float min, float max, int dimension) {
    if (dimension == 3) {
      this.velocity = Vector.random3D();
      this.velocity.setMag((float) Math.random() * (max - min) + min);
    } else if (dimension == 2) {
      this.velocity = Vector.random2D();
      this.velocity.setMag((float) Math.random() * (max - min) + min);
    }
  }

  public void setUserData(String key, Object o) {
    userData.put(key, o);
  }

  public boolean hasUserData(String key) {
    return userData.containsKey(key);
  }

  public Object getUserData(String key) {
    return userData.get(key);
  }

  public boolean hasTeleported() {
    return teleportFlag;
  }

  public void setTeleportFlag(boolean value) {
    teleportFlag = value;
  }

  public void addConnection(Spring spring) {
    connections.add(spring);
  }

  public List<Spring> getConnections() {
    return connections;
  }

  public void setStatic(boolean value) {
    this.staticBody = value;
  }

  public int getLife() {
    return life;
  }
}
