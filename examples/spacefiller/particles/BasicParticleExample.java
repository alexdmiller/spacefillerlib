package spacefiller.particles;

import processing.core.PApplet;
import spacefiller.FloatField2;
import spacefiller.FloatField3;
import spacefiller.particles.behaviors.*;

import java.awt.*;

public class BasicParticleExample extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.particles.BasicParticleExample");
  }

  ParticleSystem system;
  FlockParticles flock;
  FieldAttractParticles shapeField;

  public void settings() {
    size(1920, 1080, P3D);
  }

  public void setup() {
    system = new ParticleSystem(new Bounds(width, height), 8000, 50);
    system.fillWithParticles(3000, 2, 6);
//    system.createParticle(new Vector(400, 400), 2).velocity.zero();
//    system.createParticle(new Vector(499, 400), 2).velocity.zero();
    system.addBehavior(new ReflectiveBounds());

    flock = new FlockParticles(3, 1, 1, 20, 50, 50, 0.1f, 10);
    system.addBehavior(flock);
    system.addBehavior(new ParticleFriction(0.9f));
//    shapeField = new FieldAttractParticles(FloatField3.ZERO);
//    system.addBehavior(shapeField);
  }

  public void draw() {
//    background(0);

//    stroke(255);
//    strokeWeight(1);
//    for (int x = 0; x < system.getCols(); x++) {
//      for (int y = 0; y < system.getRows(); y++) {
//        float cell = system.getCellSize();
//        line(x * cell - 10, y * cell, x * cell + 10, y * cell);
//        line(x * cell, y * cell - 10, x * cell, y * cell + 10);
//      }
//    }
    noStroke();
    fill(0, 10);
    rect(0, 0, width, height);

    Polygon poly = new Polygon();
    poly.addPoint(100, 100);
    poly.addPoint(400, 100);
    poly.addPoint(400, 400);
    poly.addPoint(100, 400);

    //flock.setDesiredSeparation((x, y) -> poly.contains(x, y) ? 25 : 25);
//    flock.setMaxSpeed((x, y) -> poly.contains(x, y) ? 2 : 3);
//    flock.setMaxForce((x, y) -> poly.contains(x, y) ? 0.01f : 0.1f);

    //shapeField.setAttractionField((x, y, z) -> poly.contains(x, y) && z < 50 ? 10 / z : 0);
    //println(poly.contains(200, 200));

//    System.out.println(shape.contains(200, 200));
//    System.out.println(shape.contains(500, 200));

    try {
      system.update();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    stroke(255);
    strokeWeight(3);
    for (Particle p : system.getParticles()) {
      strokeWeight((float) p.getTeam() + 1);
      point(p.position.x, p.position.y);
    }

    fill(255);

    text(frameRate, 10, 20);
  }
}
