package spacefiller.particles;

import processing.core.PApplet;
import spacefiller.Vector;
import spacefiller.particles.behaviors.DonutBounds;
import spacefiller.particles.behaviors.FlockParticles;
import spacefiller.particles.behaviors.ParticleFriction;
import spacefiller.particles.behaviors.ReflectiveBounds;

public class BasicParticleExample extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.particles.BasicParticleExample");
  }

  ParticleSystem system;

  public void settings() {
    size(1920, 1080, P3D);
  }

  public void setup() {
    system = new ParticleSystem(new Bounds(width, height), 5000);
    system.fillWithParticles(5000, 2);
//    system.createParticle(new Vector(400, 400), 2).velocity.zero();
//    system.createParticle(new Vector(499, 400), 2).velocity.zero();
    system.addBehavior(new ReflectiveBounds());
    system.addBehavior(new FlockParticles(1, 1, 1, 15, 50, 50, 0.1f, 2));
    //system.addBehavior(new ParticleFriction(0.9f));
  }

  public void draw() {
    background(0);

    stroke(255);
    strokeWeight(1);
    for (int x = 0; x < system.getCols(); x++) {
      for (int y = 0; y < system.getRows(); y++) {
        float cell = system.getCellSize();
        line(x * cell - 10, y * cell, x * cell + 10, y * cell);
        line(x * cell, y * cell - 10, x * cell, y * cell + 10);
      }
    }

    try {
      system.update();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    stroke(255);
    strokeWeight(3);
    for (Particle p : system.getParticles()) {
      point(p.position.x, p.position.y);
    }

    text(frameRate, 10, 20);
  }
}
