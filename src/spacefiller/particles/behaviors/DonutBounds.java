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

    Vector position = p.getPosition();

    p.setTeleportFlag(false);
    if (position.x < a.x) {
      position.x = b.x;
      p.setTeleportFlag(true);
    } else if (position.x > b.x) {
      position.x = a.x;
      p.setTeleportFlag(true);
    }

    if (position.y < a.y) {
      position.y = b.y;
      p.setTeleportFlag(true);
    } else if (position.y > b.y) {
      position.y = a.y;
      p.setTeleportFlag(true);
    }

    if (position.z < a.z) {
      position.z = b.z;
      p.setTeleportFlag(true);
    } else if (position.z > b.z) {
      position.z = a.z;
      p.setTeleportFlag(true);
    }

    p.setPosition(position);
  }
}
