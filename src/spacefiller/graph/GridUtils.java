package spacefiller.graph;

import spacefiller.mapping.Surface;
import spacefiller.mapping.Grid;
import spacefiller.mapping.Quad;

public class GridUtils {
  public static Surface createSurface(int rows, int cols, float spacing) {
    return new Surface(createTriangleGrid(rows, cols, spacing));
  }

  public static Grid createSimpleGrid(int rows, int cols, float xSpacing, float ySpacing) {
    rows++; cols++;

    Node[][] nodes = new Node[rows][cols];
    Grid grid = new Grid();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        nodes[row][col] = grid.createNode(col * xSpacing, row * ySpacing);
      }
    }

    grid.setBoundingQuad(new Quad(
        nodes[0][0].copy(),
        nodes[0][cols - 1].copy(),
        nodes[rows - 1][cols - 1].copy(),
        nodes[rows - 1][0].copy()));

    for (int row = 0; row < rows - 1; row++) {
      for (int col = 0; col < cols - 1; col++) {
        grid.addTriangle(nodes[row][col], nodes[row + 1][col], nodes[row][col + 1]);
        grid.addTriangle(nodes[row + 1][col + 1], nodes[row + 1][col], nodes[row][col + 1]);
      }
    }

    return grid;
  }

  public static Grid createTriangleGrid(int rows, int cols, float spacing) {
    rows *= 2;
    rows += 1;
    cols += 1;
    Node[][] nodes = new Node[rows][cols];
    Grid grid = new Grid();

    grid.setRows(rows);
    grid.setColumns(cols);
    grid.setCellSize(spacing);

    for (int row = 0; row < rows; row += 2) {
      float yPos = row/2 * spacing;
      for (int col = 0; col < cols; col++) {
        nodes[row][col] = grid.createNode(col * spacing, yPos);
      }

      if (row < rows - 2) {
        for (int col = 0; col < cols - 1; col++) {
          nodes[row + 1][col] = grid.createNode(col * spacing + spacing / 2, yPos + spacing / 2);
        }
      }
    }

    grid.setBoundingQuad(new Quad(
        nodes[0][0].copy(),
        nodes[0][cols - 1].copy(),
        nodes[rows - 1][cols - 1].copy(),
        nodes[rows - 1][0].copy()));

    for (int row = 0; row < rows; row += 2) {
      for (int col = 0; col < cols; col++) {
        // top left to top right
        if (col < cols - 1) {
          grid.createEdge(nodes[row][col], nodes[row][col + 1]);
        }

        // top left to bottom left
        if (row < rows - 2) {
          grid.createEdge(nodes[row][col], nodes[row + 2][col]);
        }

        if (row < nodes.length - 1 && nodes[row + 1][col] != null) {
          // middle to top left
          grid.createEdge(nodes[row + 1][col], nodes[row][col]);

          // middle to top right
          if (col < cols - 1) {
            grid.createEdge(nodes[row + 1][col], nodes[row][col + 1]);
          }

          // middle to bottom left
          if (row < rows - 2) {
            grid.createEdge(nodes[row + 1][col], nodes[row + 2][col]);
          }

          // middle to bottom right
          if (row < rows - 2 && col < cols - 1) {
            grid.createEdge(nodes[row + 1][col], nodes[row + 2][col + 1]);
          }
        }

        if (col < cols - 1 && row < nodes.length - 1 && nodes[row + 1][col] != null) {
          // top triangle
          grid.addTriangle(nodes[row][col], nodes[row][col + 1], nodes[row + 1][col]);
        }

        if (col < cols - 1 && row < rows - 2) {
          // bottom triangle
          grid.addTriangle(nodes[row + 2][col], nodes[row + 2][col + 1], nodes[row + 1][col]);
        }

        if (col < cols - 1 && row < rows - 2 && nodes[row + 1][col] != null) {
          // right triangle
          grid.addTriangle(nodes[row][col + 1], nodes[row + 2][col + 1], nodes[row + 1][col]);
        }

        if (row < rows - 2 && nodes[row + 1][col] != null) {
          // left triangle
          grid.addTriangle(nodes[row][col], nodes[row + 2][col], nodes[row + 1][col]);
        }

        if (row < rows - 2 && col < cols - 1) {
          grid.addSquare(nodes[row][col], nodes[row][col + 1], nodes[row + 2][col + 1], nodes[row + 2][col], nodes[row + 1][col]);
        }
      }
    }

    return grid;
  }
}
