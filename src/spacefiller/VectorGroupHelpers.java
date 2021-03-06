package spacefiller;

public class VectorGroupHelpers {
  protected static abstract class MergeStrategy {
    public float distance(Vector[] group1, Vector[] group2) {
      return (float) point1(group1).dist(point2(group2));
    }

    abstract Vector point1(Vector[] group1);
    abstract Vector point2(Vector[] group2);
    abstract Vector[] merge(Vector[] group1, Vector[] group2);
  }

  protected static final MergeStrategy HEAD_TO_HEAD = new MergeStrategy() {
    @Override
    public Vector point1(Vector[] group1) {
      return group1[0];
    }

    @Override
    public Vector point2(Vector[] group2) {
      return group2[0];
    }

    @Override
    public Vector[] merge(Vector[] group1, Vector[] group2) {
      return VectorGroupHelpers.merge(reverse(group1), group2);
    }
  };

  protected static final MergeStrategy HEAD_TO_TAIL = new MergeStrategy() {
    @Override
    public Vector point1(Vector[] group1) {
      return group1[0];
    }

    @Override
    public Vector point2(Vector[] group2) {
      return group2[group2.length - 1];
    }

    @Override
    public Vector[] merge(Vector[] group1, Vector[] group2) {
      return VectorGroupHelpers.merge(group2, group1);
    }
  };

  protected static final MergeStrategy TAIL_TO_HEAD = new MergeStrategy() {
    @Override
    public Vector point1(Vector[] group1) {
      return group1[group1.length - 1];
    }

    @Override
    public Vector point2(Vector[] group2) {
      return group2[0];
    }

    @Override
    public Vector[] merge(Vector[] group1, Vector[] group2) {
      return VectorGroupHelpers.merge(group1, group2);
    }
  };

  protected static final MergeStrategy TAIL_TO_TAIL = new MergeStrategy() {
    @Override
    public Vector point1(Vector[] group1) {
      return group1[group1.length - 1];
    }

    @Override
    public Vector point2(Vector[] group2) {
      return group2[group2.length - 1];
    }

    @Override
    public Vector[] merge(Vector[] group1, Vector[] group2) {
      return VectorGroupHelpers.merge(group1, reverse(group2));
    }
  };

  protected static final MergeStrategy[] MERGE_STRATEGIES = {
      HEAD_TO_HEAD, HEAD_TO_TAIL, TAIL_TO_HEAD, TAIL_TO_TAIL
  };

  protected static Vector[] reverse(Vector[] group) {
    Vector[] reversed = new Vector[group.length];
    for (int i = 0; i < group.length; i++) {
      reversed[i] = group[group.length - 1 - i];
    }
    return reversed;
  }

  protected static Vector[] merge(Vector[] group1, Vector[] group2) {
    Vector[] merged = new Vector[group1.length + group2.length - 1];
    for (int i = 0; i < group1.length; i++) {
      merged[i] = group1[i];
    }

    for (int i = 1; i < group2.length; i++) {
      merged[group1.length + i - 1] = group2[i];
    }

    return merged;
  }

  protected static Vector head(Vector[] group) {
    return group[0];
  }

  protected static Vector tail(Vector[] group) {
    return group[group.length - 1];
  }
}
