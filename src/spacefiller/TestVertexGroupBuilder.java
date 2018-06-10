package spacefiller;

import processing.core.PVector;

import java.util.Arrays;

public class TestVertexGroupBuilder {
  public static void main(String[] args) {
    VectorGroupBuilder builder = new VectorGroupBuilder();

    builder.addGroup(new PVector[] {new PVector(1, 1), new PVector(0, 0)});
    builder.addGroup(new PVector[] {new PVector(0, 0), new PVector(3, 3)});

    builder.addGroup(new PVector[] {new PVector(4, 4), new PVector(5, 5)});
    builder.addGroup(new PVector[] {new PVector(4, 4), new PVector(6, 6)});

    builder.addGroup(new PVector[] {new PVector(7, 7), new PVector(9, 9)});
    builder.addGroup(new PVector[] {new PVector(8, 8), new PVector(7, 7)});

    builder.addGroup(new PVector[] {new PVector(10, 10), new PVector(11, 11)});
    builder.addGroup(new PVector[] {new PVector(12, 12), new PVector(11, 11)});

    builder.merge();

    for (PVector[] group : builder.getGroups()) {
      System.out.println(Arrays.toString(group));
    }
  }
}
