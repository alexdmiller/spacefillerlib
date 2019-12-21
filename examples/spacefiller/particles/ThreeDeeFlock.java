package spacefiller.particles;

import processing.core.PApplet;
import spacefiller.FloatField2;
import spacefiller.FloatField3;
import spacefiller.Vector;
import spacefiller.particles.behaviors.*;

import java.awt.*;

public class ThreeDeeFlock extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.particles.ThreeDeeFlock");
  }

  ParticleSystem system;
  FlockParticles flock;

  public void settings() {
    size(1920, 1080, P3D);
  }

  public void setup() {
    system = new ParticleSystem(new Bounds(800, 800, 800), 8000, 40);

    system.fillWithParticles(5000, 3);

//    system.fillWithParticles(10000, 3, 6);
//    system.createParticle(new Vector(400, 400), 2).velocity.zero();
//    system.createParticle(new Vector(499, 400), 2).velocity.zero();
    system.addBehavior(new ReflectiveBounds());

    flock = new FlockParticles(3, 1, 1, 20, 40, 40, 0.1f, 10);
    system.addBehavior(flock);
    system.addBehavior(new ParticleFriction(0.97f));
    system.addBehavior(new SteerBounds());
//    system.addBehavior(new WiggleBehavior());
//    shapeField = new FieldAttractParticles(FloatField3.ZERO);
//    system.addBehavior(shapeField);
  }

  public void draw() {

//    background(0);
//    ortho();
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
    background(0);
    text(frameRate, 10, 20);

//    fill(0, 50);
//    rect(0, 0, width, height, -system.getBounds().getDepth());

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

    system.update();

    stroke(255);
    strokeWeight(3);

    translate(width / 2, height / 2);
    // rotateX(-PI / 4);
    rotateY(mouseX / 100f);
    noFill();
    strokeWeight(1);
    stroke(255, 50);

    box(system.getBounds().getWidth());
    translate(
        -system.getBounds().getWidth() / 2,
        -system.getBounds().getHeight() / 2,
        -system.getBounds().getDepth() / 2);

    stroke(255);
    system.getParticles().forEach(p -> {
      strokeWeight(3);
      point(p.position.x, p.position.y, p.position.z);
    });

    // TODO: change view to see top down vs side

    // p1.setPosition(new Vector(mouseX / (float) width * system.getBounds().getWidth(), mouseY / (float) height * system.getBounds().getHeight()));

    fill(255);
  }
}
