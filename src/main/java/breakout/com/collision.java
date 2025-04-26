package breakout.com;

import breakout.com.Types.*;
import breakout.com.Renderer.*;

public class collision {
  public static void checkBounds(Types.Paddle paddle, int windowHeight, int windowWidth) {
    if(paddle.x - paddle.width / 2.f < 0) {
      paddle.x = paddle.width / 2.f;
    }

    if(paddle.x + paddle.width / 2.f > windowWidth) {
      paddle.x = windowWidth - paddle.width / 2.f;
    }
  }
}
