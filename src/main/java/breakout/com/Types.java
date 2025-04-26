package breakout.com;

public class Types {
  public static class Paddle {
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
  }
}
