package breakout.com;

public class Types {
  public static class Paddle implements Entity {
    public float x;
    public float y;
    public float width;
    public float height;

    Paddle(float x, float y) {
      this.x = x;
      this.y = y;
      this.width = 100.f;
      this.height = 20.f;
    }

    @Override
    public float getLeft() {
      return x - width / 2.f;
    }

    @Override
    public float getRight() {
      return x + width / 2.f;
    }

    @Override
    public float getTop() {
      return y - height / 2.f;
    }

    @Override
    public float getBottom() {
      return y + height / 2.f;
    }

    @Override
    public void moveInsideBounds(int windowWidth, int windowHeight) {
      if(getLeft() < 0) x = width / 2.f;
      if(getRight() > windowWidth) x = windowWidth - width / 2.f;
      if(getTop() < 0) y = height / 2.f;
      if(getBottom() > windowHeight) y = windowHeight - height / 2.f;
    }

    @Override
    public void render(Renderer renderer) {
      renderer.render(this);
    }

  }

  public static class Ball implements Entity {
    public float x;
    public float y;
    public float velocityX;
    public float velocityY;
    public float radius;

    public Ball(float x, float y, float radius) {
      this.x = x;
      this.y = y;
      this.velocityX = 2.0f;
      this.velocityY = -3.0f;
      this.radius = radius;
    }

    @Override
    public float getLeft() {
      return x - radius;
    }

    @Override
    public float getRight() {
      return x + radius;
    }

    @Override
    public float getTop() {
      return y - radius;
    }

    @Override
    public float getBottom() {
      return y + radius;
    }

    @Override
    public void moveInsideBounds(int windowWidth, int windowHeight) {
      if(getLeft() < 0) x = radius;
      if(getRight() > windowWidth) x = windowWidth - radius;
      if(getTop() < 0) y = radius;
      if(getBottom() > windowHeight) y = windowHeight - radius;
    }

    @Override
    public void render(Renderer renderer) {
      renderer.render(this);
    }
  }

  public static class Block implements Entity {
    public float x, y;
    public float width, height;
    public boolean destroyed = false;

    public Block(float x, float y, float width, float height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

    @Override
    public float getLeft() { return x - width / 2f; }

    @Override
    public float getRight() { return x + width / 2f; }

    @Override
    public float getTop() { return y - height / 2f; }

    @Override
    public float getBottom() { return y + height / 2f; }

    @Override
    public void moveInsideBounds(int windowWidth, int windowHeight) { /* Blocks don't move */ }

    @Override
    public void render(Renderer renderer) {
      renderer.render(this);
    }
  }
}

