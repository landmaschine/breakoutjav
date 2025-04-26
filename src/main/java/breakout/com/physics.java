package breakout.com;

public class physics {
  public static void checkBounds(Entity ent, int windowWidth, int windowHeight) {
    ent.moveInsideBounds(windowWidth, windowHeight);

    if(ent instanceof Types.Ball ball) {
      if(ball.getLeft() <= 0 || ball.getRight() >= windowWidth) {
        ball.velocityX = -ball.velocityX;
      }

      if(ball.getBottom() >= windowHeight) {
        ball.velocityY = -ball.velocityY;
      }
    }
  }



    public static boolean checkCollision(Entity ballEntity, Entity otherEntity) {
      if (!(ballEntity instanceof Types.Ball ball)) return false;

      boolean collisionX = ball.getRight() >= otherEntity.getLeft()
              && ball.getLeft()  <= otherEntity.getRight();
      boolean collisionY = ball.getBottom()>= otherEntity.getTop()
              && ball.getTop()   <= otherEntity.getBottom();

      if (collisionX && collisionY) {
        if (otherEntity instanceof Types.Paddle) {
          if (ball.velocityY > 0) {
            ball.y = otherEntity.getTop() - ball.radius;
          } else {
            ball.y = otherEntity.getBottom() + ball.radius;
          }
          ball.velocityY = -ball.velocityY;
          return true;
        }

        float leftOverlap  = ball.getRight()  - otherEntity.getLeft();
        float rightOverlap = otherEntity.getRight() - ball.getLeft();
        float topOverlap   = ball.getBottom() - otherEntity.getTop();

        float overlapX = Math.min(leftOverlap, rightOverlap);

        if (overlapX < topOverlap) {
          if (leftOverlap < rightOverlap) {
            ball.x = otherEntity.getLeft() - ball.radius;
            ball.velocityX = -Math.abs(ball.velocityX);
          } else {
            ball.x = otherEntity.getRight() + ball.radius;
            ball.velocityX = Math.abs(ball.velocityX);
          }
        } else {
          ball.y = otherEntity.getTop() - ball.radius;
          ball.velocityY = -Math.abs(ball.velocityY);
        }
        return true;
      }
      return false;
    }
  }
