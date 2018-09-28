package spacefiller.particles.behaviors;

import spacefiller.FloatField2;
import spacefiller.FloatField3;
import spacefiller.Vector;
import spacefiller.particles.Particle;
import spacefiller.particles.ParticleUtils;
import sun.jvm.hotspot.oops.FloatField;

import java.util.List;

public class FlockParticles extends ParticleBehavior {
  public float separationWeight = 1;
  public float alignmentWeight = 1;
  public float cohesionWeight = 1;
  public float alignmentThreshold = 100;
  public float cohesionThreshold = 100;

  private FloatField2 desiredSeparation;
  private FloatField2 separationField = FloatField2.ONE;
  private FloatField2 cohesionField = FloatField2.ONE;
  private FloatField2 alignmentField = FloatField2.ONE;
  private FloatField2 maxSpeed = FloatField2.ONE;
  private FloatField2 maxForce = FloatField2.ONE;

  public FlockParticles(
      float separationWeight,
      float alignmentWeight,
      float cohesionWeight,
      float desiredSeparation,
      float alignmentThreshold,
      float cohesionThreshold,
      float maxForce,
      float maxSpeed) {
    this.separationWeight = separationWeight;
    this.alignmentWeight = alignmentWeight;
    this.cohesionWeight = cohesionWeight;
    this.desiredSeparation = new FloatField2.Constant(desiredSeparation);
    this.alignmentThreshold = alignmentThreshold;
    this.cohesionThreshold = cohesionThreshold;
    this.maxForce = new FloatField2.Constant(maxForce);
    this.maxSpeed = new FloatField2.Constant(maxSpeed);
  }

  public FlockParticles() {
    this(2, 1, 1, 30, 100, 100, 0.1f, 4);
  }

  public void setMaxSpeed(FloatField2 maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  public FloatField2 getDesiredSeparation() {
    return desiredSeparation;
  }

  public void setDesiredSeparation(FloatField2 desiredSeparation) {
    this.desiredSeparation = desiredSeparation;
  }

  public void setCohesionThreshold(float cohesionThreshold) {
    this.cohesionThreshold = cohesionThreshold;
  }

  public void setCohesionWeight(float cohesionWeight) {
    this.cohesionWeight = cohesionWeight;
  }

  public void setAlignmentThreshold(float alignmentThreshold) {
    this.alignmentThreshold = alignmentThreshold;
  }

  public void setSeparationField(FloatField2 separationField) {
    this.separationField = separationField;
  }

  public void setCohesionField(FloatField2 cohesionField) {
    this.cohesionField = cohesionField;
  }

  public void setAlignmentField(FloatField2 alignmentField) {
    this.alignmentField = alignmentField;
  }

  public void setMaxForce(FloatField2 maxForce) {
    this.maxForce = maxForce;
  }

  @Override
  public void apply(Particle p, List<Particle> particles) {
    float localMaxSpeed = maxSpeed.get(p.position.x, p.position.y);
    float localMaxForce = maxForce.get(p.position.x, p.position.y);

    Vector sep = separate(p, particles, localMaxSpeed, localMaxForce);
    Vector ali = align(p, particles, localMaxSpeed, localMaxForce);
    Vector coh = cohesion(p, particles, localMaxSpeed, localMaxForce);

    sep.mult(separationWeight * separationField.get(p.position.x, p.position.y));
    ali.mult(alignmentWeight * alignmentField.get(p.position.x, p.position.y));
    coh.mult(cohesionWeight * cohesionField.get(p.position.x, p.position.y));

    p.applyForce(sep);
    p.applyForce(ali);
    p.applyForce(coh);
  }

  // Separation
  // Method checks for nearby boids and steers away
  Vector separate(Particle p, List<Particle> particles, float maxSpeed, float maxForce) {
    Vector steer = new Vector(0, 0, 0);

    int count = 0;
    // For every boid in the system, check if it's too close
    for (Particle other : particles) {
      float d = (float) p.position.dist(other.position);
      float separation = desiredSeparation.get(p.position.x, p.position.y);
      separation *= p.team == other.team ? 1 : 2;
      if (other != p && (d < separation)) {
        // Calculate vector pointing away from neighbor
        Vector diff = Vector.sub(p.position, other.position);
        diff.normalize();
        diff.div(d);        // Weight by distance
        steer.add(diff);
        count++;            // Keep track of how many
      }
    }
    // Average -- divide by how many
    if (count > 0) {
      steer.div((float)count);
    }

    // As long as the vector is greater than 0
    if (steer.magnitude() > 0) {
      steer.normalize();
      steer.mult(maxSpeed);
      steer.sub(p.velocity);
      steer.limit(maxForce);
    }
    return steer;
  }

  // Alignment
  // For every nearby boid in the system, calculate the average velocity
  Vector align(Particle p, List<Particle> particles, float maxSpeed, float maxForce) {
    Vector sum = new Vector(0, 0);
    int count = 0;
    for (Particle other : particles) {
      float d = (float) p.position.dist(other.position);
      if ((d > 0) && (d < alignmentThreshold)) {
        sum.add(other.velocity);
        count++;
      }
    }
    if (count > 0) {
      sum.div((float)count);
      // First two lines of code below could be condensed with new Vector setMag() method
      // Not using this method until Processing.js catches up
      // sum.setMag(maxSpeed);

      // Implement Reynolds: Steering = Desired - Velocity
      if (sum.magnitude() > 0) {
        sum.normalize();
      }

      sum.mult(maxSpeed);
      Vector steer = Vector.sub(sum, p.velocity);
      steer.limit(maxForce);
      return steer;
    }
    else {
      return new Vector(0, 0);
    }
  }

  // Cohesion
  // For the average position (i.e. center) of all nearby boids, calculate steering vector towards that position
  Vector cohesion(Particle p, List<Particle> particles, float maxSpeed, float maxForce) {
    Vector sum = new Vector(0, 0);   // Start with empty vector to accumulate all locations
    int count = 0;
    for (Particle other : particles) {
      float d = (float) p.position.dist(other.position);

      float cohesionThreshold2 = cohesionThreshold;


      if ((d > 0) && (d < cohesionThreshold2)) {
        sum.add(other.position); // Add position
        count++;
      }
    }
    if (count > 0) {
      sum.div(count);
      return ParticleUtils.seek(p, sum, maxSpeed, maxForce);  // Steer towards the position
    }
    else {
      return new Vector(0, 0);
    }
  }
}