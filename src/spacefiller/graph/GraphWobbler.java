package spacefiller.graph;

import toxi.math.noise.PerlinNoise;
import java.util.List;

/**
 * Created by miller on 10/2/17.
 */
public class GraphWobbler extends Graph {
  private Graph graph;
  private PerlinNoise perlinNoise;
  private float t;

  public float updateSpeed = 0.01f;
  public float jitter = 0;
  public float waveAmp = 0;
  public float waveFreq = 0.1f;

  public GraphWobbler(Graph graph) {
    this.graph = graph;
    this.perlinNoise = new PerlinNoise();
  }

  public void update() {
    t += updateSpeed;
  }

  public List<Node> getNodes() {
    Graph copy = graph.copy();
    doJitter(copy);

    return copy.getNodes();
  }

  public List<Edge> getEdges() {
    Graph copy = graph.copy();
    doJitter(copy);

    return copy.getEdges();
  }

  private void doJitter(Graph graph) {
    for (Node n : graph.getNodes()) {
      n.position.x += perlinNoise.noise(n.position.x / 100f, n.position.y / 100f, t) * jitter - jitter/2;
      n.position.y += perlinNoise.noise(n.position.x / 100f, n.position.y / 100f, t + 100) * jitter - jitter/2;
      n.position.y += Math.sin(n.position.x * waveFreq + t * 10) * waveAmp;
    }
  }
}
