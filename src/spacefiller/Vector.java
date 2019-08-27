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

  public static Vector div(Vector p1, float s) { return new Vector(p1.x / s, p1.y / s, p1.z / s); }

  public static Vector mult(Vector p1, float s) { return new Vector(p1.x * s, p1.y * s, p1.z * s); }

  public static Vector fromAngle(float a) {
    return new Vector((float) Math.cos(a), (float) Math.sin(a));
  }

  public static Vector random2D() {
    return fromAngle((float) (Math.random() * Math.PI*2));
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

    float dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    float v1mag = (float) Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    float v2mag = (float) Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
    // This should be a number between -1 and 1, since it's "normalized"
    float amt = dot / (v1mag * v2mag);
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

  public float dist(Vector other) {
    float dx = other.x - x;
    float dy = other.y - y;
    float dz = other.z - z;
    return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  public Vector sub(Vector other) {
    x -= other.x;
    y -= other.y;
    z -= other.z;
    return this;
  }

  public Vector sub(float x, float y) {
    x -= x;
    y -= y;
    return this;
  }

  // TODO: returning a Vector implies that the Vector object is immutable. bad pattern here
  public Vector add(Vector other) {
    x += other.x;
    y += other.y;
    z += other.z;
    return this;
  }

  public Vector add(float x, float y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public Vector add(float x, float y, float z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }

  public Vector div(float i) {
    x /= i;
    y /= i;
    z /= i;
    return this;
  }

  public float magnitude() {
    return (float) Math.sqrt(x * x + y * y + z * z);
  }

  public Vector normalize() {
    float mag = magnitude();
    return div(mag);
  }

  public Vector mult(float s) {
    this.x *= s;
    this.y *= s;
    this.z *= s;
    return this;
  }

  public Vector limit(float limit) {
    float mag = magnitude();
    if (mag > limit) {
      setMag(limit);
    }
    return this;
  }

  public Vector setMag(float mag) {
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

  public void set(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector cross(Vector v2) {
    return new Vector(
        this.y * v2.z - this.z * v2.y,
        this.z * v2.x - this.x * v2.z,
        this.x * v2.y - this.y * v2.x
    );
  }

  public float dot(Vector v2) {
    return this.x * v2.x +
        this.y * v2.y +
        this.z * v2.z;
  }
}
