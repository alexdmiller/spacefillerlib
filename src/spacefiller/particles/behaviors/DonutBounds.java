package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.Iterator;
import java.util.List;

public class DonutBounds extends ParticleBehavior {
  @Override
  public void apply(Particle p, List<Particle> neighbors) {
    Vector a = getParticleSystem().getBounds().getTopBackLeft();
    Vector b = getParticleSystem().getBounds().getBottomFrontRight();

    p.setTeleportFlag(false);
    if (p.position.x < a.x) {
      p.position.x = b.x;
      p.setTeleportFlag(true);
    } else if (p.position.x > b.x) {
      p.position.x = a.x;
      p.setTeleportFlag(true);
    }

    if (p.position.y < a.y) {
      p.position.y = b.y;
      p.setTeleportFlag(true);
    } else if (p.position.y > b.y) {
      p.position.y = a.y;
      p.setTeleportFlag(true);
    }

    if (p.position.z < a.z) {
      p.position.z = b.z;
      p.setTeleportFlag(true);
    } else if (p.position.z > b.z) {
      p.position.z = a.z;
      p.setTeleportFlag(true);
    }
  }
}
