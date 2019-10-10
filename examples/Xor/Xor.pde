import neural_networks.*;

SNN nn;
int[] layers = {4};
float[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
float[][] result = {{0}, {1}, {1}, {0}};

int rows = 20;
int cols = 20;
float w;
float h;



void settings() {
  size(700, 700, P2D);
}

void setup() {
  w = width/cols;
  h = height/rows;
  nn = new SNN(2, layers, 1, Neural_network.activationFunction.SIGMOID);
  colorMode(RGB, 1.0);
  noStroke();
}

void draw() {
  background(0);
  // Train
  for (int i = 0; i < 1000; i++) {
    int j = int(random(inputs.length));
    nn.train(inputs[j], result[j]);
  }
  // Show
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      float[] input = {(float)i/cols, (float)j/rows};
      float res = nn.guess(input)[0];
      
      fill(res);
      rect(i*w, j*h, w, w);
    }
  }
}

void keyPressed() {
  // Reset
  if (key == ' ') {
    nn = new SNN(2, layers, 1, Neural_network.activationFunction.SIGMOID);
  }
}