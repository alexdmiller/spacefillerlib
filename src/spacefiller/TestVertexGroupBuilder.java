package spacefiller;

import java.util.Arrays;

public class TestVertexGroupBuilder {
  public static void main(String[] args) {
    VectorGroupBuilder builder = new VectorGroupBuilder();

    builder.addGroup(new Vector[] {new Vector(1, 1), new Vector(0, 0)});
    builder.addGroup(new Vector[] {new Vector(0, 0), new Vector(3, 3)});

    builder.addGroup(new Vector[] {new Vector(4, 4), new Vector(5, 5)});
    builder.addGroup(new Vector[] {new Vector(4, 4), new Vector(6, 6)});

    builder.addGroup(new Vector[] {new Vector(7, 7), new Vector(9, 9)});
    builder.addGroup(new Vector[] {new Vector(8, 8), new Vector(7, 7)});

    builder.addGroup(new Vector[] {new Vector(10, 10), new Vector(11, 11)});
    builder.addGroup(new Vector[] {new Vector(12, 12), new Vector(11, 11)});

    builder.merge();

    for (Vector[] group : builder.getGroups()) {
      System.out.println(Arrays.toString(group));
    }
  }
}
