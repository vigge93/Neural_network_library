import neural_networks.ENN;

ArrayList<Pipe> pipes = new ArrayList<Pipe>();

Population birds;
int populationSize = 500;

int n = 0;
int cycles = 1;

boolean showBest = false;


void setup() {
  size(640, 420);
  birds = new Population(populationSize);
}

void draw() {
  background(0);

  for (int k = 0; k < cycles; k++) {
    if (n % 75 == 0) {
      pipes.add(new Pipe());
    }
    for (int i = pipes.size()-1; i >= 0; i--) {
      pipes.get(i).update();
      if (pipes.get(i).offscreen()) {
        pipes.remove(pipes.get(i));
      }
    }


    if (showBest) {
      birds.best.think(pipes);
      birds.best.update();
    } else {
      birds.think(pipes); 
      birds.update(pipes);
      birds.calcBest();
    }
    n++;
    if (showBest) {
      for (int i = 0; i < pipes.size(); i++) {
        if (pipes.get(i).hits(birds.best)) {
          n = 0;
          pipes.clear();
          birds.calcBest();
        }
      }
    } else if (birds.alive.size() < 1) {
      birds.nextGen();
      n = 0;
      pipes.clear();
    }
  }
  if (!showBest) {
    birds.show();
  } else {
    birds.best.show();
  }
  for (Pipe p : pipes) {
    p.show();
  }
  text(n, 20, 20);
}

void keyPressed() {
  if (keyCode == UP) {
    cycles += 1;
    if (cycles > 10) {
      cycles = 10;
    }
  } else if (keyCode == DOWN) {
    cycles -= 1;
    if (cycles < 1) {
      cycles = 1;
    }
  } else if (key == 'b') {
    n = 0; 
    pipes.clear();
    if (showBest == true) {
      birds.nextGen();
    }
    showBest = !showBest;
  }
}