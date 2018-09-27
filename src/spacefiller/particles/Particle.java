package spacefiller.particles;

import spacefiller.Vector;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miller on 9/28/16.
 */
public class Particle {
  public Vector position;
  public Vector velocity;
  public boolean removeFlag = false;
  private Map<String, Object> userData;

  public Color color;
  private boolean teleportFlag = false;


  public Vector forces;

  public Particle(float x, float y, float z, Color color) {
    this.position = new Vector(x, y, z);
    this.velocity = new Vector();
    this.forces = new Vector();
    this.color = color;
    this.userData = new HashMap<>();
  }

  public Particle() {
    this.position = new Vector();
    this.velocity = new Vector();
    this.forces = new Vector();
    this.userData = new HashMap<>();
  }

  public Particle(float x, float y, float z) {
    this.position = new Vector(x, y, z);
    this.velocity = new Vector();
    this.forces = new Vector();
    this.userData = new HashMap<>();
  }

  public Particle(float x, float y) {
    this.position = new Vector(x, y);
    this.velocity = new Vector();
    this.forces = new Vector();
    this.userData = new HashMap<>();
  }

  public Particle(Vector p) {
    this.position = p;
    this.velocity = new Vector();
    this.forces = new Vector();
    this.userData = new HashMap<>();
  }

  public void update() {
    position.add(velocity);
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
    forces.add(force);
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

}
