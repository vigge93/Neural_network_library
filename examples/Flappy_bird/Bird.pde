int populationSize = 500;
class Bird implements IPopulation<Bird> {
  float x = 64;
  float y = baseY/2;
  float r = 12;
  float gravity = 0.8;
  float lift = -12;
  float velocity = 0;
  ENN brain;

  Bird() {
    brain  = new ENN(5, new int[]{2, 2, 14}, 2, ENN.activationFunction.TANH);
    brain.mutationRate = 0.1;
    brain.fitness = 0;
  }

  Bird factory(Object... args) {
    return new Bird();
  }

  ENN getBrain() {
    return brain;
  }

  float fitness() {
    return brain.fitness;
  }

  void calcFitness(Object... args) {
    brain.fitness = pow(brain.fitness, 2);
  }

  Bird(ENN brain) {
    this.brain = brain.copy();
    this.brain.mutate();
    this.brain.fitness = 0;
  }

  Bird Copy() {
    return new Bird(brain);
  }

  void show() {
    pushMatrix();
    translate(x, y);
    rotate(map(velocity, -10, 10, -QUARTER_PI, QUARTER_PI));
    image(bird_img, 0, 0, r*2, r*2);
    popMatrix();
  }

  void up() {
    velocity += lift;
  }

  void think(Object... pipes) {
    Pipe closest = null;
    float closestD = Float.MAX_VALUE;
    for (Pipe p : (Pipe[])pipes) {
      float D = (p.x + p.w) - x;
      if (D < closestD && D > 0) {
        closest = p;
        closestD = D;
      }
    }
    if (closest != null) {
      float[] inputs = {
        map(y, 0, baseY, -1, 1), 
        map(closest.top, 0, baseY, -1, 1), 
        map(closest.bottom, 0, baseY, -1, 1), 
        map(closest.x, x, baseX, -1, 1), 
        map(velocity, -5, 5, -1, 1)  
      };
      float[] output = brain.guess(inputs);
      if (output[0] > output[1]) {
        up();
      }
    }
    update();
  }


  boolean isAlive(Object... pipes) {
    for (Pipe p : (Pipe[])pipes) {
      if (p.hits(this)) {
        return false;
      }
    }
    return true;
  }

  void update() {
    velocity += gravity;
    y += velocity;

    brain.fitness++;
  }
}
