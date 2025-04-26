package breakout.com;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    Renderer renderer;
    Types.Paddle paddle;
    Types.Ball ball;
    UIRenderer uiRenderer;
    int score = 0;

    float baseSpeed = 2.0f;

    boolean ballAttached = true;

    List<Types.Block> blocks = new ArrayList<>();

    public static void main(String[] args) {
        new Main().run();
    }

    private void initBlocks() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                float x = 85 + col * 70;
                float y = 430 + row * 30;
                blocks.add(new Types.Block(x, y, 60, 20));
            }
        }
    }

    private void resetBall() {
        ballAttached = true;
        ball = new Types.Ball(0, 0, 10);
        ball.x = paddle.x;
        ball.y = paddle.y - paddle.height / 2f - ball.radius;
        ball.velocityX = 0;
        ball.velocityY = 0;
    }

    public void run() {
        renderer = new Renderer(800, 600, "Breakout");
        uiRenderer = new UIRenderer();
        paddle = new Types.Paddle(400, 30);

        resetBall();

        initBlocks();

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
            paddle.x -= baseSpeed * speed;
        }

        if(glfwGetKey(renderer.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
            paddle.x += baseSpeed * speed;
        }

        if(ballAttached) {
            ball.x = paddle.x;
            if(glfwGetKey(renderer.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
                ballAttached = false;
                ball.velocityY = 2.0f;
                ball.velocityX = -3.0f;
            }
        }

        glfwPollEvents();
    }

    private void update() {
        ball.x += ball.velocityX;
        ball.y += ball.velocityY;

        physics.checkBounds(paddle, 800, 600);
        physics.checkBounds(ball, 800, 600);
        physics.checkCollision(ball, paddle);

        for(Types.Block block : blocks) {
            if(!block.destroyed && physics.checkCollision(ball, block)) {
                block.destroyed = true;
                score += 10;
                break;
            }
        }

        if(blocks.stream().allMatch(b -> b.destroyed)) {
            initBlocks();
            resetBall();
        }

        if(ball.getTop() <= 0) {
            System.out.println("Game Over!");
            score = 0;
            blocks.clear();
            initBlocks();
            resetBall();
        }
    }

    private void render() {
        renderer.beginFrame();
        paddle.render(renderer);
        ball.render(renderer);
        for(Types.Block block : blocks) {
            block.render(renderer);
        }
        uiRenderer.renderScore(score);
        renderer.endFrame();
    }
}

