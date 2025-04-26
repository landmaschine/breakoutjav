package breakout.com;

import breakout.com.collision.*;
import breakout.com.Renderer.*;
import breakout.com.Types.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    Renderer renderer;
    Types.Paddle paddle;


    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        renderer = new Renderer(800, 600, "Breakout");
        paddle = new Types.Paddle(400, -600 + 20.f);
        paddle.height = 20.f;
        paddle.width = 100.f;
        loop();
    }

    public void loop() {
        while(!glfwWindowShouldClose(renderer.getWindow())) {
            input();
            update();
            render();
        }
    }

    private void input() {
        float speed = 1.f;

        if(glfwGetKey(renderer.getWindow(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            speed = 2.f;
        }

        if(glfwGetKey(renderer.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
            paddle.x -= 1.5f * speed;
        }

        if(glfwGetKey(renderer.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
            paddle.x += 1.5f * speed;
        }


        glfwPollEvents();
    }

    private void update() {
        collision.checkBounds(paddle, 800, 600);
    }

    private void render() {
        renderer.beginFrame();
        renderer.render(paddle);
        renderer.endFrame();
    }

}

