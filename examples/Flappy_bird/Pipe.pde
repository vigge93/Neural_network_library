class Pipe {
  float top;
  float bottom;
  float spacing;
  float w;
  float speed;
  float x;
  float centery;

  Pipe() {
    spacing = 75;
    centery = random(spacing, baseY-spacing);
    top = centery-spacing/2;
    bottom = baseY - (centery+spacing/2);
    x = baseX;
    w = 80;
    speed = 6;
  }

  boolean hits(Bird bird) {
    if (bird.y - bird.r < top || bird.y + bird.r > baseY - bottom) {
      if (bird.x > x && bird.x < x + w) {
        return true;
      }
    }
    if (bird.y > baseY || bird.y < 0) {
      return true;
    }

    return false;
  }

  boolean offscreen() {
    if (x + w < 0) {
      return true;
    }
    return false;
  }

  void show() {
    image(pipe_img, x+w/2, baseY-bottom/2, w, bottom);
    pushMatrix();
    translate(x + w/2, top/2);
    rotate(PI);
    image(pipe_img, 0, 0, w, top);
    popMatrix();
  }

  void update() {
    x -= speed;
  }
}
