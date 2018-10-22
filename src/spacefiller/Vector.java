package spacefiller;

public class Vector {
  public float x, y, z;

  public Vector() {
    x = y = z = 0;
  }

  public Vector(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vector(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public static Vector sub(Vector p1, Vector p2) {
    return new Vector(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
  }

  public static Vector random2D() {
    return fromAngle(Math.random() * Math.PI*2);
  }

  private static Vector fromAngle(double theta) {
    return new Vector((float) Math.cos(theta), (float) Math.sin(theta));
  }

  // based on Daniel Shiffman's PVector class
  public static Vector random3D() {
    float angle = (float) (Math.random()*Math.PI*2);
    float vz = (float) (Math.random()*2-1);
    float vx = (float) (Math.sqrt(1-vz*vz)*Math.cos(angle));
    float vy = (float) (Math.sqrt(1-vz*vz)*Math.sin(angle));
    Vector target = new Vector(vx, vy, vz);
    return target;
  }

  // Taken from Shiffman PVector
  public static float angleBetween(Vector v1, Vector v2) {
    // We get NaN if we pass in a zero vector which can cause problems
    // Zero seems like a reasonable angle between a (0,0,0) vector and something else
    if (v1.x == 0 && v1.y == 0 && v1.z == 0 ) return 0.0f;
    if (v2.x == 0 && v2.y == 0 && v2.z == 0 ) return 0.0f;

    double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
    // This should be a number between -1 and 1, since it's "normalized"
    double amt = dot / (v1mag * v2mag);
    // But if it's not due to rounding error, then we need to fix it
    // http://code.google.com/p/processing/issues/detail?id=340
    // Otherwise if outside the range, acos() will return NaN
    // http://www.cppreference.com/wiki/c/math/acos
    if (amt <= -1) {
      return (float) Math.PI;
    } else if (amt >= 1) {
      // http://code.google.com/p/processing/issues/detail?id=435
      return 0;
    }
    return (float) Math.acos(amt);
  }

  public static Vector add(Vector p1, Vector p2) {
    return new Vector(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
  }

  public double dist(Vector other) {
    double dx = other.x - x;
    double dy = other.y - y;
    double dz = other.z - z;
    return Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  public Vector add(Vector other) {
    x += other.x;
    y += other.y;
    z += other.z;
    return this;
  }


  public Vector sub(Vector other) {
    x -= other.x;
    y -= other.y;
    z -= other.z;
    return this;

  }

  public void div(double i) {
    x /= i;
    y /= i;
    z /= i;
  }

  public double magnitude() {
    return Math.sqrt(x * x + y * y + z * z);
  }

  public void normalize() {
    double mag = magnitude();
    div(mag);
  }

  public void mult(double s) {
    this.x *= s;
    this.y *= s;
    this.z *= s;
  }

  public void limit(double limit) {
    double mag = magnitude();
    if (mag > limit) {
      setMag(limit);
    }
  }

  public Vector setMag(double mag) {
    normalize();
    mult(mag);
    return this;
  }

  public Vector copy() {
    return new Vector(x, y, z);
  }

  public Vector rotate(float theta) {
    float temp = x;
    // Might need to check for rounding errors like with angleBetween function?
    x = x * (float) Math.cos(theta) - y * (float) Math.sin(theta);
    y = temp * (float) Math.sin(theta) + y * (float) Math.cos(theta);
    return this;
  }

  public void zero() {
    x = y = z = 0;
  }

  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }
}
