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
    centery = random(spacing, height-spacing);
    top = centery-spacing/2;
    bottom = height - (centery+spacing/2);
    x = width;
    w = 80;
    speed = 6;
  }

  boolean hits(Bird bird) {
    if (bird.y - bird.r < top || bird.y + bird.r > height - bottom) {
      if (bird.x > x && bird.x < x + w) {
        return true;
      }
    }
    if (bird.y > height || bird.y < 0) {
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
    fill(250);
    stroke(255);
    rect(x, 0, w, top);
    rect(x, height-bottom, w, bottom);
  }

  void update() {
    x -= speed;
  }
}