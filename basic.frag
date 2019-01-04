#version 330

out vec4 fragColor;

uniform sampler2D texture;

void main()
{

    if (texture(texture, gl_Position.xy).r == 0) {
        fragColor = vec4(1.0, 0.0, 0.0, 1.0);
    } else {
        fragColor = vec4(1.0, 0.0, 0.0, 1.0);
    }
}