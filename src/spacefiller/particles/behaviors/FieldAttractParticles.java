package spacefiller.particles.behaviors;

import spacefiller.FloatField2;
import spacefiller.FloatField3;
import spacefiller.Vector;
import spacefiller.particles.Particle;

import java.util.List;

public class FieldAttractParticles extends ParticleBehavior {
  private FloatField3 attractionField;

  public FieldAttractParticles(FloatField3 attractionField) {
    this.attractionField = attractionField;
  }

  public void setAttractionField(FloatField3 attractionField) {
    this.attractionField = attractionField;
  }

  @Override
  public void apply(Particle particle, List<Particle> neighbors) {
    if (neighbors != null) {
       for (Particle neighbor : neighbors) {
         if (particle != neighbor) {
           Vector delta = Vector.sub(particle.position, neighbor.position);
           float dist = (float) delta.magnitude();
           float strength = attractionField.get(particle.position.x, particle.position.y, dist);
           delta.setMag(strength);
           particle.velocity.add(delta);
         }
       }
     }
  }
}