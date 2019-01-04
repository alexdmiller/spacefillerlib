#version 410 core

layout (points) in;
layout (triangle_strip) out;
layout (max_vertices = 9) out;

uniform sampler2D texture;

void main(void)
{
    int i;

    for (i = 0; i < gl_in.length(); i++)
    {
        if (texture(texture, gl_in[i].gl_Position.xy).r == 0) {
            gl_Position = gl_in[i].gl_Position;
            EmitVertex();

            gl_Position = gl_in[i].gl_Position + vec4(0.0, 10.0, 0.0, 0.0);
            EmitVertex();

            gl_Position = gl_in[i].gl_Position + vec4(10.0, 0.0, 0.0, 0.0);
            EmitVertex();

            gl_Position = gl_in[i].gl_Position + vec4(10.0, 10.0, 0.0, 0.0);
            EmitVertex();

            EndPrimitive();
        }
    }

}