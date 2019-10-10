static final String PATH_BASE = "";
static final String PATH_NN = PATH_BASE + "nn.ai";
static final String PATH_TRAINING_INPUTS = PATH_BASE + "training_inputs.ai";
static final String PATH_TRAINING_OUTPUTS = PATH_BASE + "training_outputs.ai";
static final String PATH_TESTING_INPUTS = PATH_BASE + "testing_inputs.ai";
static final String PATH_TESTING_OUTPUTS = PATH_BASE + "testing_outputs.ai";

SNN nn;

ArrayList<float[]> training_inputs = new ArrayList<float[]>();
ArrayList<float[]> training_outputs = new ArrayList<float[]>();
ArrayList<float[]> test_inputs = new ArrayList<float[]>();
ArrayList<float[]> test_outputs = new ArrayList<float[]>();
int counter = 0;

void train() {
    while (true) {
        for (int i = 0; i < 10000; i++) {
            int j = int(random(training_inputs.size()));
            float[] input = (float[])training_inputs.get(j);
            float[] output = (float[])training_outputs.get(j);
            nn.train(input, output);
        }
        nn.save(PATH_NN);
    }
}

void error() {
    int correct = 0;
    for (int i = 0; i < test_inputs.size(); i++) {
        int guess = int(SNN.getMax(nn.guess(test_inputs.get(i)))[0]);
        if (test_outputs.get(i)[guess] == 1) {
            correct += 1;
        }
    }
    println(((float)correct/test_inputs.size()));
}

void guess() {
    float[] grid_flat = new float[GAMESIZE*GAMESIZE+1];
    for (int i = 0; i < GAMESIZE; i++) {
        for (int j = 0; j < GAMESIZE; j++) {
            grid_flat[i*GAMESIZE + j] = map(game_cells[i][j], -5, 1, 0, 1);
        }
    }
    grid_flat[grid_flat.length-1] = direction;
    float[] prediction = nn.guess(grid_flat);
    int guessed_direction = int(SNN.getMax(prediction)[0]);
    direction = guessed_direction;
}

void write (String filename, ArrayList<float[]> x) {
    try {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(x);
        objectOut.close();
        fileOut.close();
    } 
    catch (IOException i) {
        i.printStackTrace();
    }
}

ArrayList<float[]> read(String filename) {
    ArrayList res = null;
    try {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        res = (ArrayList) objectIn.readObject();
        objectIn.close();
        fileIn.close();
    } 
    catch (IOException e) {
        e.printStackTrace();
        return new ArrayList<float[]>();
    } 
    catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    return res;
}