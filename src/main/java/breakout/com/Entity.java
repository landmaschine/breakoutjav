package breakout.com;

public interface Entity {
  float getLeft();
  float getRight();
  float getTop();
  float getBottom();

  void moveInsideBounds(int windowWidth, int windowHeight);
  void render(Renderer renderer);
}
