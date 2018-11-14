package spacefiller.particles.behaviors;

import geomerative.RShape;
import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.List;

public class ShapeBounds extends ParticleBehavior {
  private RShape shape;

  public ShapeBounds(RShape shape) {
    this.shape = shape;
  }

  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    //RShape shape = RShape.createCircle()
    //shape.getClosest()
  }
}
