package spacefiller;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContourSpace {
  private int width;
  private int height;
  private float cellSize;
  private float[][] grid;
  private List<LineSegment> lineSegments;

  public ContourSpace(int width, int height, float cellSize) {
    this.width = width;
    this.height = height;
    this.cellSize = cellSize;
    this.grid = new float[(int) (this.height / this.cellSize)][(int) (this.width / this.cellSize)];
    this.lineSegments = new ArrayList<>();
  }

  public List<LineSegment> getLineSegments() {
    return lineSegments;
  }

  public List<PVector[]> getVertexGroups() {
    VectorGroupBuilder builder = new VectorGroupBuilder();
    for (LineSegment segment : lineSegments) {
      builder.addGroup(new PVector[] { segment.p1, segment.p2 });
    }

    return builder.getGroups();
  }

  public void clearLineSegments() {
    lineSegments.clear();
  }

  public void resetGrid() {
    this.grid = new float[(int) (this.height / this.cellSize)][(int) (this.width / this.cellSize)];
    this.lineSegments.clear();
  }

  public void addMetaBall(PVector position, float radius) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        PVector sample = new PVector(col * cellSize, row * cellSize);
        float distance = position.dist(sample);
        //grid[row][col] = grid[row][col] + Math.min(50f / distance, 1000);
        //grid[row][col] = (float) (grid[row][col] + Math.pow((1 - distance * distance), 2));
        grid[row][col] += Math.min(Math.pow(radius, 2) / (Math.pow(sample.x - position.x, 2) + Math.pow(sample.y - position.y, 2)), 1000);
      }
    }
  }

  public void drawIsoContour(float height) {
    drawGridPlaneIntersection(grid, height, cellSize);
  }

  void drawGridPlaneIntersection(float[][] heightMap, float planeHeight, float gridSize) {
    for (int r = 0; r < heightMap.length - 1; r++) {
      for (int c = 0; c < heightMap[r].length - 1; c++) {
        PVector p1 = new PVector(c * gridSize, r * gridSize, heightMap[r][c]);
        PVector p2 = new PVector((c + 1) * gridSize, r * gridSize, heightMap[r][c + 1]);
        PVector p3 = new PVector((c + 1) * gridSize, (r + 1) * gridSize, heightMap[r + 1][c + 1]);
        PVector p4 = new PVector(c * gridSize, (r + 1) * gridSize, heightMap[r + 1][c]);
        drawRectPlaneIntersection(p1, p2, p3, p4, planeHeight);
      }
    }
  }

  PVector average(PVector p1, PVector p2, PVector p3, PVector p4) {
    PVector m = new PVector();
    m.add(p1);
    m.add(p2);
    m.add(p3);
    m.add(p4);
    m.div(4);
    return m;
  }

  void drawRectPlaneIntersection(PVector p1, PVector p2, PVector p3, PVector p4, float planeHeight) {
    PVector m = average(p1, p2, p3, p4);
    drawTrianglePlaneIntersection(p1, p2, m, planeHeight);
    drawTrianglePlaneIntersection(p2, p3, m, planeHeight);
    drawTrianglePlaneIntersection(p3, p4, m, planeHeight);
    drawTrianglePlaneIntersection(p4, p1, m, planeHeight);
  }

  void drawTrianglePlaneIntersection(PVector p1, PVector p2, PVector p3, float planeHeight) {
    List<PVector> intersections = new ArrayList();
    intersections.add(intersection(p1, p2, planeHeight));
    intersections.add(intersection(p2, p3, planeHeight));
    intersections.add(intersection(p1, p3, planeHeight));
    intersections.removeAll(Collections.singleton(null));
    if (intersections.size() == 2) {
      PVector l1 = intersections.get(0);
      PVector l2 = intersections.get(1);
      vline(l1, l2);
    }
  }

  PVector intersection(PVector p1, PVector p2, float planeHeight) {
    float t = (planeHeight - p1.z) / (p2.z - p1.z);
    float x = (p2.x - p1.x) * t + p1.x;
    float y = (p2.y - p1.y) * t + p1.y;
    if (t < 0 || t > 1) {
      return null;
    } else {
      return new PVector(x, y);
    }
  }

  void vline(PVector p1, PVector p2) {
    lineSegments.add(new LineSegment(p1, p2));
  }
}
