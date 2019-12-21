uniform ivec2 size;

uniform sampler2D mask;
uniform sampler2D image;

void main() {
  vec2 position = (gl_FragCoord.xy / size.xy);

  vec4 maskPixel = texture2D(mask, position);
  vec4 imagePixel = texture2D(image, position);

  gl_FragColor = mix(vec4(0, 0, 0, 0), imagePixel, maskPixel.r);
}