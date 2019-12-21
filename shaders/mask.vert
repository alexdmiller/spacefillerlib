uniform mat4 transform;

in vec4 position;


void main() {
  gl_Position = transform * position;
}