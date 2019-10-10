import neural_networks.*;

PImage bird_img;
PImage pipe_img;
PImage background_img;

float baseX = 640, baseY = 420;
float scaleX, scaleY;

ArrayList<Pipe> pipes = new ArrayList<Pipe>();
Population<Bird> birds;
int generation = 1;
int n = 0;
int cycles = 1;

void setup() {
  //size(640, 420);
  fullScreen(P2D);
  birds = new Population<Bird>(populationSize, new Bird());
  bird_img = loadImage("bird.png");
  pipe_img = loadImage("pipe.png");
  background_img = loadImage("background.jpg");
  bird_img.resize(24, 24);
  imageMode(CENTER);

  scaleX = width/baseX;
  scaleY = height/baseY;
}

void draw() {
  scale(scaleX, scaleY);
  background(0);
  image(background_img, baseX/2, baseY/2, baseX, baseY);
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


    birds.think(pipes.toArray(new Pipe[1]));
    birds.kill(pipes.toArray(new Pipe[0]));
    n++;
    if (birds.isDead()) {
      birds.nextGen();
      generation++;
      n = 0;
      pipes.clear();
    }
  }
  for (Bird b : birds.getAlive())
  {
    b.show();
  }
  for (Pipe p : pipes) {
    p.show();
  }
  text(n, 20, 20);
  text(generation, 80, 20);
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
  }
}