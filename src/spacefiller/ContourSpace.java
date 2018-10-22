package spacefiller;

import java.util.*;

public class ContourSpace {
  private int width;
  private int height;
  private float cellSize;
  private float[][] grid;
  private SortedMap<Float, List<LineSegment>> layers;
  private OpenSimplexNoise noise;

  public ContourSpace(int width, int height, float cellSize) {
    this.width = width;
    this.height = height;
    this.cellSize = cellSize;
    this.grid = new float[(int) (this.height / this.cellSize)][(int) (this.width / this.cellSize)];
    this.layers = new TreeMap<>();
    this.noise = new OpenSimplexNoise();
  }

  public List<LineSegment> getLineSegmentLayerAtHeight(float height) {
    return layers.get(height);
  }

  public List<List<LineSegment>> getLayers() {
    Collection<List<LineSegment>> values = layers.values();
    return new ArrayList<>(values);
  }

  public List<Vector[]> getVertexGroups() {
    VectorGroupBuilder builder = new VectorGroupBuilder();
    for (List<LineSegment> layer : layers.values()) {
      for (LineSegment segment : layer) {
        builder.addGroup(new Vector[]{segment.p1, segment.p2});
      }
    }

    return builder.getGroups();
  }

  public void clearLineSegments() {
    layers.clear();
  }

  public void resetGrid() {
    this.grid = new float[(int) (this.height / this.cellSize)][(int) (this.width / this.cellSize)];
    this.layers.clear();
  }

  public void addNoise(float scale, float height, float t) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        grid[row][col] += noise.eval(row * scale, col * scale, t) * height;
      }
    }
  }

  public void addMetaBall(Vector position, float radius, float height) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        Vector sample = new Vector(col * cellSize, row * cellSize);
        float distance = (float) position.dist(sample);
        //grid[row][col] = grid[row][col] + Math.min(50f / distance, 1000);
        //grid[row][col] = (float) (grid[row][col] + Math.pow((1 - distance * distance), 2));
        grid[row][col] += Math.min(
            (Math.min(Math.pow(radius, 2) / (Math.pow(sample.x - position.x, 2) + Math.pow(sample.y - position.y, 2)), 1000)), height);
      }
    }
  }

  public void drawIsoContour(float height) {
    drawGridPlaneIntersection(grid, height, cellSize);
  }

  void drawGridPlaneIntersection(float[][] heightMap, float planeHeight, float gridSize) {
    for (int r = 0; r < heightMap.length - 1; r++) {
      for (int c = 0; c < heightMap[r].length - 1; c++) {
        Vector p1 = new Vector(c * gridSize, r * gridSize, heightMap[r][c]);
        Vector p2 = new Vector((c + 1) * gridSize, r * gridSize, heightMap[r][c + 1]);
        Vector p3 = new Vector((c + 1) * gridSize, (r + 1) * gridSize, heightMap[r + 1][c + 1]);
        Vector p4 = new Vector(c * gridSize, (r + 1) * gridSize, heightMap[r + 1][c]);
        drawRectPlaneIntersection(p1, p2, p3, p4, planeHeight);
      }
    }
  }

  Vector average(Vector p1, Vector p2, Vector p3, Vector p4) {
    Vector m = new Vector();
    m.add(p1);
    m.add(p2);
    m.add(p3);
    m.add(p4);
    m.div(4);
    return m;
  }

  void drawRectPlaneIntersection(Vector p1, Vector p2, Vector p3, Vector p4, float planeHeight) {
    Vector m = average(p1, p2, p3, p4);
    drawTrianglePlaneIntersection(p1, p2, m, planeHeight);
    drawTrianglePlaneIntersection(p2, p3, m, planeHeight);
    drawTrianglePlaneIntersection(p3, p4, m, planeHeight);
    drawTrianglePlaneIntersection(p4, p1, m, planeHeight);
  }

  void drawTrianglePlaneIntersection(Vector p1, Vector p2, Vector p3, float planeHeight) {
    List<Vector> intersections = new ArrayList();
    intersections.add(intersection(p1, p2, planeHeight));
    intersections.add(intersection(p2, p3, planeHeight));
    intersections.add(intersection(p1, p3, planeHeight));
    intersections.removeAll(Collections.singleton(null));
    if (intersections.size() == 2) {
      Vector l1 = intersections.get(0);
      Vector l2 = intersections.get(1);
      vline(l1, l2, planeHeight);
    }
  }

  Vector intersection(Vector p1, Vector p2, float planeHeight) {
    float t = (float) ((planeHeight - p1.z) / (p2.z - p1.z));
    float x = (float) ((p2.x - p1.x) * t + p1.x);
    float y = (float) ((p2.y - p1.y) * t + p1.y);
    if (t < 0 || t > 1) {
      return null;
    } else {
      return new Vector(x, y);
    }
  }

  void vline(Vector p1, Vector p2, float planeHeight) {
    if (!layers.containsKey(planeHeight)) {
      layers.put(planeHeight, new ArrayList<>());
    }


    layers.get(planeHeight).add(new LineSegment(p1, p2));
  }
}
