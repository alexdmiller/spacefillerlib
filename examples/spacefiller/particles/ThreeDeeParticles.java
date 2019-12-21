package spacefiller.particles;

import processing.core.PApplet;
import spacefiller.FloatField2;
import spacefiller.FloatField3;
import spacefiller.Vector;
import spacefiller.particles.behaviors.*;

import java.awt.*;

public class ThreeDeeParticles extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.particles.ThreeDeeParticles");
  }

  ParticleSystem system;
  Particle p1;

  public void settings() {
    size(1920, 1080, P3D);
  }

  public void setup() {
    system = new ParticleSystem(new Bounds(600, 600, 600), 8000, 200);

    system.fillWithParticles(5000, 3);
    p1 = system.createParticle(0, 0, 0);

//    system.fillWithParticles(10000, 3, 6);
//    system.createParticle(new Vector(400, 400), 2).velocity.zero();
//    system.createParticle(new Vector(499, 400), 2).velocity.zero();
    system.addBehavior(new ReflectiveBounds());

//    system.addBehavior(flock);
    system.addBehavior(new ParticleFriction(0.9f));
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
//    fill(0, 50);
//    rect(0, 0, width, height);

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
    rotateX(-PI / 4);
    rotateY(mouseX / 100f);
    noFill();
    strokeWeight(1);
    stroke(255, 50);

    box(system.getBounds().getWidth());
    translate(
        -system.getBounds().getWidth() / 2,
        -system.getBounds().getHeight() / 2,
        -system.getBounds().getDepth() / 2);

    for (float x = 0; x <= system.getBounds().getWidth(); x += system.getCellSize()) {
      for (float y = 0; y <= system.getBounds().getWidth(); y += system.getCellSize()) {
        line(x,y, 0, x, y, system.getBounds().getDepth());
      }
    }

    for (float x = 0; x <= system.getBounds().getWidth(); x += system.getCellSize()) {
      for (float z = 0; z <= system.getBounds().getWidth(); z += system.getCellSize()) {
        line(x, 0, z, x, system.getBounds().getHeight(), z);
      }
    }

    for (float y = 0; y <= system.getBounds().getWidth(); y += system.getCellSize()) {
      for (float z = 0; z <= system.getBounds().getWidth(); z += system.getCellSize()) {
        line(0, y, z, system.getBounds().getWidth(), y, z);
      }
    }

    for (int x = 0; x < system.getRows(); x++) {
      for (int y = 0; y < system.getCols(); y++) {
        for (int z = 0; z < system.getStacks(); z++) {
          float posX = x * system.getCellSize() + system.getCellSize() / 2;
          float posY = y * system.getCellSize()+ system.getCellSize() / 2;
          float posZ = z * system.getCellSize()+ system.getCellSize() / 2;
          textSize(20);
          text(x + ", " + y + ", " + z, posX, posY, posZ);
          int hash = system.hashCellCoordinates(x,y,z);
          text(hash, posX, posY + 20, posZ);
        }
      }
    }

    stroke(255);
    system.getParticles().forEach(p -> {
      strokeWeight(3);
      point(p.position.x, p.position.y, p.position.z);
    });

    system.getNeighbors(p1.position).forEach(p -> {
      strokeWeight(10);
      stroke(255, 0, 0);
      point(p.position.x, p.position.y, p.position.z);
    });

    // TODO: change view to see top down vs side

    // p1.setPosition(new Vector(mouseX / (float) width * system.getBounds().getWidth(), mouseY / (float) height * system.getBounds().getHeight()));

    fill(255);
  }
}
