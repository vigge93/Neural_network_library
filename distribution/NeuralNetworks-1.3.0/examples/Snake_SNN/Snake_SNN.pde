import neural_networks.*; //<>//
import java.io.*;

int[] layers = {50};
boolean collect_data = true;

static final int GAMESIZE = 5;
static final int SPEED = 2;
static final int FRAMERATE = 60;

void setup() {
    noStroke();
    size(700, 700);
    textSize(TEXTSIZE);
    frameRate(FRAMERATE);
    
    nn = SNN.load(PATH_NN);
    training_inputs = read(PATH_TRAINING_INPUTS);
    training_outputs = read(PATH_TRAINING_OUTPUTS);
    test_inputs = read(PATH_TESTING_INPUTS);
    test_outputs = read(PATH_TESTING_OUTPUTS);
    println("Training size: " + training_inputs.size() + "\tTesting size: " + test_inputs.size());
    if (nn == null) {
        nn = new SNN(GAMESIZE*GAMESIZE+1, layers, 4, "sigmoid");
    }
    nn.learningrate = 0.001;
    reset();
    if (!collect_data) {
        thread("train");
    }
}

void draw() {
    if (frameCount%floor(FRAMERATE/SPEED) == 0 || !collect_data) {
        background(BACKGROUNDCOLOR);
        key_pressed = false;
        if (!collect_data) {
            guess();
        }
        update();
        for (int y = 0; y<GAMESIZE; y++) {
            for (int x = 0; x<GAMESIZE; x++) {
                switch(game_cells[y][x]) {
                case BLANK:
                    fill(BACKGROUNDCOLOR);
                    break;
                case APPLE:
                    fill(APPLECOLOR);
                    break;
                case SNAKEHEAD:
                    fill(HEADCOLOR);
                    break;
                default:
                    fill(SNAKECOLOR);
                    break;
                }
                rect(map(x, 0, GAMESIZE, 0, width), map(y, 0, GAMESIZE, 0, height), width/GAMESIZE, height/GAMESIZE);
            }
        }
        fill(0, 0, 0);
        text(score, width-2*TEXTSIZE, TEXTSIZE);
        text("Highscore: " + high_score, width-7.5*TEXTSIZE, 2*TEXTSIZE);

        // Data for NN
        if (collect_data) {
            float[] grid_flat = new float[GAMESIZE*GAMESIZE+1];
            for (int i = 0; i < GAMESIZE; i++) {
                for (int j = 0; j < GAMESIZE; j++) {
                    grid_flat[i*GAMESIZE + j] = map(game_cells[i][j], -5, 1, 0, 1);
                }
            }
            grid_flat[grid_flat.length-1] = direction;
            float[] user_direction = {0, 0, 0, 0};
            user_direction[direction] = 1;
            if (counter % 5 == 0) {
                test_inputs.add(grid_flat);
                test_outputs.add(user_direction);
                write(PATH_TESTING_INPUTS, test_inputs);
                write(PATH_TESTING_OUTPUTS, test_outputs);
            } else {
                training_inputs.add(grid_flat);
                training_outputs.add(user_direction);
                write(PATH_TRAINING_INPUTS, training_inputs);
                write(PATH_TRAINING_OUTPUTS, training_outputs);
            }
        } else {
            error();
        }
        counter++;
    }
}

void keyPressed() {
    if (key == 'w' || keyCode == UP)
        if (direction != DIRDOWN && !key_pressed) {
            direction = DIRUP;
            key_pressed = true;
        }
    if (key == 'd' || keyCode == RIGHT)
        if (direction != DIRLEFT && !key_pressed) {
            direction = DIRRIGHT;
            key_pressed = true;
        }
    if (key == 's' ||keyCode == DOWN)
        if (direction != DIRUP && !key_pressed) {
            direction = DIRDOWN;
            key_pressed = true;
        }
    if (key == 'a' ||keyCode == LEFT)
        if (direction != DIRRIGHT && !key_pressed) {
            direction = DIRLEFT;
            key_pressed = true;
        }
    if (key == ' ') {
        reset();
    }
}