package spacefiller.particles;

import spacefiller.Vector;

/**
 * Created by miller on 7/13/17.
 */
public class ParticleUtils {
  public static Vector seek(Particle p, Vector target, float maxSpeed, float maxForce) {
    Vector desired = Vector.sub(target, p.position);  // A vector pointing from the position to the target
    desired.normalize();
    desired.mult(maxSpeed);
    Vector steer = Vector.sub(desired, p.velocity);
    steer.limit(maxForce);  // Limit to maximum steering force
    return steer;
  }

}
