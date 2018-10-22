package spacefiller;

import java.util.ArrayList;
import java.util.List;

import static spacefiller.VectorGroupHelpers.MergeStrategy;
import static spacefiller.VectorGroupHelpers.MERGE_STRATEGIES;

public class VectorGroupBuilder {
  private List<Vector[]> groups;
  private float tolerance = 0f;

  public VectorGroupBuilder(float tolerance) {
    this.tolerance = tolerance;
    this.groups = new ArrayList<>();
  }

  public VectorGroupBuilder() {
    this(0);
  }

  public void addGroup(Vector[] newGroup) {
    groups.add(newGroup);
  }

  public void merge() {
    boolean updated = true;
    while (updated) {
      updated = doMerge();
    }
  }

  private boolean doMerge() {
    // Find two closest points
    for (int i = 0; i < groups.size(); i++) {
      Vector[] group1 = groups.get(i);

      float minDistance = 9999;
      MergeStrategy minStrategy = null;
      int groupIndex = -1;

      for (int j = i + 1; j < groups.size(); j++) {
        Vector[] group2 = groups.get(j);


        for (MergeStrategy candidateStrategy : MERGE_STRATEGIES) {
          float distance = candidateStrategy.distance(group1, group2);
          if (distance < minDistance) {
            minDistance = distance;
            minStrategy = candidateStrategy;
            groupIndex = j;
          }
        }
      }

      if (minDistance <= tolerance) {
        Vector[] mergedGroup = minStrategy.merge(group1, groups.get(groupIndex));
        groups.remove(i);
        groups.remove(groupIndex - 1);
        groups.add(mergedGroup);
        return true;
      }

    }
    return false;
  }

  public List<Vector[]> getGroups() {
    return groups;
  }
}
