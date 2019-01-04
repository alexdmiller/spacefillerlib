package spacefiller.shader;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL4;
import processing.core.PApplet;
import processing.opengl.PGL;
import processing.opengl.PJOGL;
import processing.opengl.PShader;

import static com.jogamp.opengl.GL2.GL_GEOMETRY_INPUT_TYPE_EXT;

public class GeometryShader extends PShader {
  int glGeometry;
  String geometrySrc;

  GeometryShader(PApplet parent, String vertFilename, String geoFilename, String fragFilename) {
    super(parent, vertFilename, fragFilename);
    geometrySrc = PApplet.join(parent.loadStrings(geoFilename), "\n");
  }

  protected void setup() {
    glGeometry = pgl.createShader(GL3.GL_GEOMETRY_SHADER);
    compile(glGeometry, geometrySrc, "Cannot compile geometry shader:\n");
    pgl.attachShader(glProgram, glGeometry);
  }

  void compile(int id, String src, String msg) {
    pgl.shaderSource(id, src);
    pgl.compileShader(id);
    pgl.getShaderiv(id, PGL.COMPILE_STATUS, intBuffer);
    boolean compiled = intBuffer.get(0) == 0 ? false : true;
    if (!compiled) {
      System.out.println(msg + pgl.getShaderInfoLog(id));
    }
  }

  protected void draw(int idxId, int count, int offset) {
    pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, idxId);
    this.pgl.drawElements(PGL.POINTS, count, GL.GL_UNSIGNED_SHORT, offset * Short.SIZE / 8);
    pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
  }
}
