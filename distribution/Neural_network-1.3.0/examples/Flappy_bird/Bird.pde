class Bird {
  float x;
  float y;
  float r;
  float gravity;
  float lift;
  float velocity;
  ENN brain;

  Bird() {
    y = height/2;
    x = 64;
    r = 12;

    gravity = 0.8;
    lift= -12;
    velocity = 0;
    brain  = new ENN(5, new int[]{32, 16, 8}, 2, "sigmoid");
    brain.mutationRate = 0.1;
    brain.fitness = 0;
  }

  Bird(ENN brain) {
    y = height/2;
    x = 64;
    r = 12;

    gravity = 0.8;
    lift= -12;
    velocity = 0;
    this.brain = brain.copy();
    this.brain.mutate();
  }

  Bird copy() {
    return new Bird(brain);
  }

  void show() {
    stroke(255);
    fill(255, 100);
    ellipse(x, y, r*2, r*2);
  }

  void up() {
    velocity += lift;
  }

  void think(ArrayList<Pipe> pipes) {
    Pipe closest = null;
    float closestD = Float.MAX_VALUE;
    for (Pipe p : pipes) {
      float D = (p.x + p.w) - x;
      if (D < closestD && D > 0) {
        closest = p;
        closestD = D;
      }
    }
    if (closest != null) {
      float[] inputs = {
        map(y, 0, height, 0, 1), 
        map(closest.top, 0, height, 0, 1), 
        map(closest.bottom, 0, height, 0, 1), 
        map(closest.x, x, width, 0, 1), 
        map(velocity, -5, 5, 0, 1)  
      };
      float[] output = brain.guess(inputs);
      if (output[0] > output[1]) {
        up();
      }
    }
  }

  void update() {
    velocity += gravity;
    y += velocity;

    brain.fitness++;
  }
}