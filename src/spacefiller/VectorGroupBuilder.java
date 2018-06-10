package spacefiller;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static spacefiller.VectorGroupHelpers.MergeStrategy;
import static spacefiller.VectorGroupHelpers.MERGE_STRATEGIES;

public class VectorGroupBuilder {
  private List<PVector[]> groups;
  private float tolerance = 0f;

  public VectorGroupBuilder(float tolerance) {
    this.tolerance = tolerance;
    this.groups = new ArrayList<>();
  }

  public VectorGroupBuilder() {
    this(0);
  }

  public void addGroup(PVector[] newGroup) {
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
      PVector[] group1 = groups.get(i);
      for (int j = i + 1; j < groups.size(); j++) {
        PVector[] group2 = groups.get(j);

        float minDistance = 9999;
        MergeStrategy minStrategy = null;

        for (MergeStrategy candidateStrategy : MERGE_STRATEGIES) {
          float distance = candidateStrategy.distance(group1, group2);
          if (distance < minDistance) {
            minDistance = distance;
            minStrategy = candidateStrategy;
          }
        }

        if (minDistance <= tolerance) {
          PVector[] mergedGroup = minStrategy.merge(group1, group2);
          groups.remove(i);
          groups.remove(j - 1);
          groups.add(mergedGroup);
          return true;
        }
      }
    }
    return false;
  }

  public List<PVector[]> getGroups() {
    return groups;
  }
}
