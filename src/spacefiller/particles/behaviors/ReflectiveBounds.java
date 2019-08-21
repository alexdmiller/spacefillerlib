package spacefiller.particles.behaviors;

import spacefiller.Vector;
import spacefiller.particles.Bounds;
import spacefiller.particles.CollisionEvent;
import spacefiller.particles.CollisionEventListener;
import spacefiller.particles.Particle;

import java.util.ArrayList;
import java.util.List;

public class ReflectiveBounds extends ParticleBehavior {
  private List<CollisionEventListener> listeners;

  public ReflectiveBounds() {
    listeners = new ArrayList<>();
  }

  public void addListener(CollisionEventListener listener) {
    listeners.add(listener);
  }

  @Override
  public void apply(Particle p, List<Particle> neighbors) {
    Vector topBackLeft = getParticleSystem().getBounds().getTopBackLeft();
    Vector bottomFrontRight = getParticleSystem().getBounds().getBottomFrontRight();

    Vector preCollisionVelocity = p.velocity.copy();
    boolean collision = false;

    if (p.position.x < topBackLeft.x) {
      p.position.x = topBackLeft.x;
      p.velocity.x *= -1;
      collision = true;
    } else if (p.position.x > bottomFrontRight.x) {
      p.position.x = bottomFrontRight.x;
      p.velocity.x *= -1;
      collision = true;
    }

    if (p.position.y < topBackLeft.y) {
      p.position.y = topBackLeft.y;
      p.velocity.y *= -1;
      collision = true;
    } else if (p.position.y > bottomFrontRight.y) {
      p.position.y = bottomFrontRight.y;
      p.velocity.y *= -1;
      collision = true;
    }

    if (p.position.z < topBackLeft.z) {
      p.position.z = topBackLeft.z;
      p.velocity.z *= -1;
      collision = true;
    } else if (p.position.z > bottomFrontRight.z) {
      p.position.z = bottomFrontRight.z;
      p.velocity.z *= -1;
      collision = true;
    }

    if (collision) {
      CollisionEvent event = new CollisionEvent(p, p.position.copy(), preCollisionVelocity);
      listeners.forEach(listener -> {
        listener.onCollide(event);
      });
    }
  }
}
