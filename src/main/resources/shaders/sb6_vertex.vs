#version 330

layout(location = 0) in vec3 position;

out vec3 outTexCoord;

void main() {

    outTexCoord = position;
    gl_Position = vec4(position, 1.0);
}