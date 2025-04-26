package breakout.com;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer {
  private final long window;
  private int shaderProgram;
  private int vao;

  private final float[] projectionMatrix;

  public long getWindow() {
    return window;
  }

  public Renderer(int width, int height, String windowName) {
    System.out.println("Renderer init");
    GLFWErrorCallback.createPrint(System.err).set();

    if(!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

    window = glfwCreateWindow(width, height, windowName, NULL, NULL);
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true);
      }
    });

    try(MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1);
      IntBuffer pHeight = stack.mallocInt(1);

      glfwGetWindowSize(window, pWidth, pHeight);
      GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      assert vidMode != null;
      glfwSetWindowPos(
              window,
              (vidMode.width() - pWidth.get(0)) / 2,
              (vidMode.height() - pHeight.get(0)) / 2
      );
    }

    glfwMakeContextCurrent(window);
    glfwSwapInterval(1);
    glfwShowWindow(window);

    GL.createCapabilities();

    glViewport(0, 0, width, height);
    projectionMatrix = createOrthoProjection(0, 800, 600, 0);

    initShaders();
    initPaddle();
  }

  public void beginFrame() {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  public void render(Types.Paddle paddle) {
    glUseProgram(shaderProgram);

    int projectionLocation = glGetUniformLocation(shaderProgram, "uProjection");
    glUniformMatrix4fv(projectionLocation, false, projectionMatrix);

    int positionLocation = glGetUniformLocation(shaderProgram, "uPosition");
    glUniform2f(positionLocation, paddle.x, paddle.y);

    int scaleLocation = glGetUniformLocation(shaderProgram, "uScale");
    glUniform2f(scaleLocation, paddle.width, paddle.height);

    glBindVertexArray(vao);
    glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    glBindVertexArray(0);

    glUseProgram(0);
  }

  public void render(Types.Ball ball) {
    glUseProgram(shaderProgram);

    int projectionLocation = glGetUniformLocation(shaderProgram, "uProjection");
    glUniformMatrix4fv(projectionLocation, false, projectionMatrix);

    int positionLocation = glGetUniformLocation(shaderProgram, "uPosition");
    glUniform2f(positionLocation, ball.x, ball.y);

    int scaleLocation = glGetUniformLocation(shaderProgram, "uScale");
    glUniform2f(scaleLocation, ball.radius * 2.f, ball.radius * 2.f);

    glBindVertexArray(vao);
    glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    glBindVertexArray(0);

    glUseProgram(0);
  }

  public void endFrame() {
    glfwSwapBuffers(window);
  }

  private static final String vertexShaderSource =
          """
                  #version 330 core
                  layout(location = 0) in vec2 aPos;
                  uniform mat4 uProjection;
                  uniform vec2 uPosition;
                  uniform vec2 uScale;
                  void main() {
                      vec4 pos = vec4(aPos * uScale + uPosition, 0.0, 1.0);
                      gl_Position = uProjection * pos;
                  }""";

  private static final String fragmentShaderSource =
          """
                  #version 330 core
                  out vec4 FragColor;
                  void main() {
                      FragColor = vec4(1.0, 1.0, 1.0, 1.0);
                  }""";

  private void initShaders() {
    int vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, vertexShaderSource);
    glCompileShader(vertexShader);
    if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
      throw new RuntimeException("Vertex shader compile error: " + glGetShaderInfoLog(vertexShader));
    }

    int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader, fragmentShaderSource);
    glCompileShader(fragmentShader);
    if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
      throw new RuntimeException("Fragment shader compile error: " + glGetShaderInfoLog(fragmentShader));
    }

    shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    glLinkProgram(shaderProgram);
    if(glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
      throw new RuntimeException("Link shader compile error: " + glGetProgramInfoLog(shaderProgram));
    }

    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);
  }

  private void initPaddle() {
    float[] vertices = {
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f
    };

    int vbo = glGenBuffers();
    vao = glGenVertexArrays();

    glBindVertexArray(vao);

    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }

  public void render(Types.Block block) {
    if (block.destroyed) return;

    glUseProgram(shaderProgram);

    int projectionLocation = glGetUniformLocation(shaderProgram, "uProjection");
    glUniformMatrix4fv(projectionLocation, false, projectionMatrix);

    int positionLocation = glGetUniformLocation(shaderProgram, "uPosition");
    glUniform2f(positionLocation, block.x, block.y);

    int scaleLocation = glGetUniformLocation(shaderProgram, "uScale");
    glUniform2f(scaleLocation, block.width, block.height);

    glBindVertexArray(vao);
    glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    glBindVertexArray(0);

    glUseProgram(0);
  }

  private static float[] createOrthoProjection(float left, float right, float top, float bottom) {
    float[] mat = new float[16];

    mat[0] = 2.0f / (right - left);
    mat[5] = 2.0f / (top - bottom);
    mat[10] = -1.0f;
    mat[12] = -(right + left) / (right - left);
    mat[13] = -(top + bottom) / (top - bottom);
    mat[15] = 1.0f;

    return mat;
  }

}