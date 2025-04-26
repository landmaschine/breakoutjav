package breakout.com;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL11.*;

public class UIRenderer {
  private final long vg;
  private final int windowWidth = 800;
  private final int windowHeight = 600;
  private int font;

  public UIRenderer() {
    vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
    if (vg == 0) {
      throw new RuntimeException("Could not init NanoVG.");
    }
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    font = nvgCreateFont(vg, "retro", "src/main/res/fonts/DepartureMono-Regular.otf");
    if (font == -1) {
      throw new RuntimeException("Could not load font 'DepartureMono-Regular.otf'.");
    }
  }

  public void renderScore(int score) {
    nvgBeginFrame(vg, windowWidth, windowHeight, 1);

    NVGColor textColor = NVGColor.calloc();
    nvgRGBA((byte)255, (byte)255, (byte)255, (byte)255, textColor);
    nvgFontSize(vg, 16.0f);
    nvgFontFace(vg, "retro");
    nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_BOTTOM);
    nvgFillColor(vg, textColor);
    String msg = "Score: " + score;
    nvgText(vg, 20, windowHeight - 20, msg);

    nvgEndFrame(vg);
  }

  public void destroy() {
    nvgDelete(vg);
  }
}

