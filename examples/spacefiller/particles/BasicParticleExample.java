package spacefiller.particles;

import processing.core.PApplet;
import spacefiller.particles.behaviors.DonutBounds;
import spacefiller.particles.behaviors.FlockParticles;

public class BasicParticleExample extends PApplet {
  public static void main(String[] args) {
    main("spacefiller.particles.BasicParticleExample");
  }

  ParticleSystem system;

  public void settings() {
    size(1920, 1080, P3D);
  }

  public void setup() {
    system = new ParticleSystem(new Bounds(width, height), 1000);
    system.fillWithParticles(1000, 2);
    system.addBehavior(new DonutBounds());
    system.addBehavior(new FlockParticles());
  }

  public void draw() {
    background(0);

    system.update();

    stroke(255);
    strokeWeight(2);
    for (Particle p : system.getParticles()) {
      point(p.position.x, p.position.y);
    }

    text(frameRate, 10, 20);
  }
}
