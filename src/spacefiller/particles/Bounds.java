package spacefiller.particles;

import spacefiller.Vector;

import java.io.Serializable;

public class Bounds implements Serializable {
  Vector topBackLeft;
  Vector bottomFrontRight;

  public Bounds(float width, float height, float depth) {
    this.topBackLeft = new Vector(0, 0, 0);
    this.bottomFrontRight = new Vector(width, height, depth);
  }

  public Bounds(float width, float height) {
    this.topBackLeft = new Vector(0, 0, 0);
    this.bottomFrontRight = new Vector(width, height, 0);
  }

  public Bounds(float size) {
    this(size, size, size);
  }

//  public boolean contains(double x, double y) {
//    return (x > -width / 2 && x < width / 2) &&
//        (y > -height / 2 && y < height / 2);
//  }

  public boolean contains(float x, float y) {
    return x >= topBackLeft.x && y >= topBackLeft.y && x <= bottomFrontRight.x && y <= bottomFrontRight.y;
  }

  public boolean contains(float x, float y, float z) {
    return x >= topBackLeft.x && y >= topBackLeft.y && z >= topBackLeft.z &&
        x <= bottomFrontRight.x && y <= bottomFrontRight.y && z <= bottomFrontRight.z;
  }

  public boolean contains(Vector p) {
    return contains(p.x, p.y, p.z);
  }

  public void constrain(Particle p) {
    if (p.position.x < topBackLeft.x) {
      p.position.x = topBackLeft.x;
      p.velocity.x *= -1;
    } else if (p.position.x > bottomFrontRight.x) {
      p.position.x = bottomFrontRight.x;
      p.velocity.x *= -1;
    }

    if (p.position.y < topBackLeft.y) {
      p.position.y = topBackLeft.y;
      p.velocity.y *= -1;
    } else if (p.position.y > bottomFrontRight.y) {
      p.position.y = bottomFrontRight.y;
      p.velocity.y *= -1;
    }

    if (p.position.z < topBackLeft.z) {
      p.position.z = topBackLeft.z;
      p.velocity.z *= -1;
    } else if (p.position.z > bottomFrontRight.z) {
      p.position.z = bottomFrontRight.z;
      p.velocity.z *= -1;
    }
  }

  public Vector getTopBackLeft() {
    return topBackLeft;
  }

  public Vector getBottomFrontRight() {
    return bottomFrontRight;
  }

  public Vector getRandomPointInside(int dimension) {
    return new Vector(
        (float) Math.random() * getWidth() + topBackLeft.x,
        (float) Math.random() * getHeight() + topBackLeft.y,
        dimension == 3 ? (float) Math.random() * getHeight() + topBackLeft.z : 0);
  }

  public float getWidth() {
    return bottomFrontRight.x - topBackLeft.x;
  }

  public float getHeight() {
    return bottomFrontRight.y - topBackLeft.y;
  }

  public float getDepth() {
    return bottomFrontRight.z - topBackLeft.z;
  }
}
