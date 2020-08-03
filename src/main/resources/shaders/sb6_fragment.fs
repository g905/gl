#version 330

in vec3 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;

void main() {

    fragColor = vec4(ambientLight, 1.0) * vec4(0.5, 0.5, 0.5, 1.0);
}