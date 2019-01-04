package spacefiller.particles;

import spacefiller.Vector;

public class Spring {
  private Particle n1, n2;
  private float springLength;
  private float k;

  public Spring(Particle n1, Particle n2, float springLength, float k) {
    this.n1 = n1;
    this.n2 = n2;
    this.springLength = springLength;
    this.k = k;
  }

  public void update() {
    Vector delta = Vector.sub(n1.position, n2.position);
    float displacement = (float) ((springLength - delta.magnitude()) * k);
    delta.normalize();
    delta.mult(displacement);
    n1.velocity.add(delta);
    n2.velocity.sub(delta);
  }

  public Particle getN1() {
    return n1;
  }

  public Particle getN2() {
    return n2;
  }

  public float getSpringLength() {
    return springLength;
  }

  public void setSpringLength(float springLength) {
    this.springLength = springLength;
  }

  public Particle other(Particle n) {
    if (n1 == n) {
      return n2;
    } else if (n2 == n) {
      return n1;
    } else {
      return null;
    }
  }
}