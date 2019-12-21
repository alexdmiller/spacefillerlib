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

    Vector preCollisionVelocity = p.getVelocity();
    Vector collisionNormal = null;

    Vector position = p.getPosition();
    Vector velocity = p.getVelocity();

    if (position.x < topBackLeft.x) {
      position.x = topBackLeft.x;
      position.x *= -1;
      collisionNormal = new Vector(1, 0, 0);
    } else if (position.x > bottomFrontRight.x) {
      position.x = bottomFrontRight.x;
      velocity.x *= -1;
      collisionNormal = new Vector(-1, 0, 0);
    }

    if (position.y < topBackLeft.y) {
      position.y = topBackLeft.y;
      velocity.y *= -1;
      collisionNormal = new Vector(0, 1, 0);
    } else if (position.y > bottomFrontRight.y) {
      position.y = bottomFrontRight.y;
      velocity.y *= -1;
      collisionNormal = new Vector(0, -1, 0);
    }

    if (position.z < topBackLeft.z) {
      position.z = topBackLeft.z;
      velocity.z *= -1;
      collisionNormal = new Vector(0, 0, 1);
    } else if (position.z > bottomFrontRight.z) {
      position.z = bottomFrontRight.z;
      velocity.z *= -1;
      collisionNormal = new Vector(0, 0, -1);
    }

    p.setPosition(position);
    p.setVelocity(velocity);

    if (collisionNormal != null) {
      CollisionEvent event = new CollisionEvent(p, p.getPosition(), preCollisionVelocity, collisionNormal);
      listeners.forEach(listener -> {
        listener.onCollide(event);
      });
    }
  }
}
