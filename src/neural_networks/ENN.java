package neural_networks;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import processing.core.PApplet;

public class ENN extends Neural_network implements Serializable {

	private static final long serialVersionUID = 1L;
	
    /**
     * Probability of mutating each gene
     */
	public float mutationRate = (float)0.01;
    
    /**
     * The current fitness of the model
     */
	public float fitness = 0;
	
	/**
	 * Constructor
	 * @param inputs_ No. input nodes
	 * @param hidden_ Array of hidden layer nodes
	 * @param outputs_ No. output nodes
	 * @param activation Activation/loss function to use
	 */
    public ENN(int inputs_, int[] hidden_, int outputs_, activationFunction activation) {
        super(inputs_, hidden_, outputs_, activation);
    }
    
    /**
     * Creates a deep copy of a network
     */
    protected ENN(ENN parent) {
        super();
        // Copy input layer
        this.inputs = parent.inputs.copy();

        // Copy the hidden layers
        this.hidden = new Matrix[parent.hidden.length];
        for (int i = 0; i < this.hidden.length; i++) {
            this.hidden[i] = parent.hidden[i].copy();
        }

        // Copy the output layer
        this.outputs = parent.outputs.copy();

        // Copy the weights between input and hidden layers
        this.weightsIH = parent.weightsIH.copy();

        // Copy weights between hidden layers
        this.weightsHH = new Matrix[parent.weightsHH.length];
        for (int i = 0; i < this.weightsHH.length; i++) {
            this.weightsHH[i] = parent.weightsHH[i].copy();
        }

        // Copy the weights between hidden and output layers
        this.weightsHO = parent.weightsHO.copy();

        // Copy the biases
        this.weightBI = parent.weightBI.copy();
        this.weightsBH = new Matrix[parent.weightsBH.length];
        for (int i = 0; i < this.weightsBH.length; i++) {
            this.weightsBH[i] = parent.weightsBH[i].copy();
        }
        this.weightBO = parent.weightBO.copy();

        // Copy properties
        this.mutationRate = parent.mutationRate;
        this.activation = parent.activation;
    }
    
    /**
     * Copies the network
     * @return Copy of network
     */
    public ENN copy() {
        return new ENN(this);
    }
    
    /**
     * Mutates the network
     */
    public void mutate() {
        // Mutate the weights between the layers
        mutate(weightsIH);
        for (int i = 0; i < weightsHH.length; i++) {
            mutate(weightsHH[i]);
        }
        mutate(weightsHO);

        // Mutate the biases
        mutate(weightBI);
        for (int i = 0; i < weightsBH.length; i++) {
            mutate(weightsBH[i]);
        }
        mutate(weightBO);
    }

    protected void mutate(Matrix m) {
    	Random r = new Random();
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                if ((float)Math.random() < mutationRate) {
                    // Uses gaussian distribution for the random numbers
                    // Because it biases the mutations to smaller changes
                    // while still allowing big changes every once in a while
                    m.table[i][j] += ((float)r.nextGaussian()*0.3);//(float)Math.random() * 2 - 1;
                }
            }
        }
    }
    
    /**
     * Performs crossover on a population (currently copies networks and mutates them)
     * @param networks Selection pool
     * @return Mutated offsprings
     */
    public static ENN[] crossover(ENN[] networks) {
        // Make sure that the fitness is between 0 and 1
        ENN[] res = normalizeFitness(networks);
        
        // Generate the next generation, with equal population size
    	for(int i = 0; i < res.length; i++) {
    		ENN child = pickOne(res);
    		child.mutate();
    		res[i] = child;
    	}
    	return res;
    }
    
    /**
     * Make sure the fitness values are between 0 and 1, and that the sum of all fitness values are 1
     */
    protected static ENN[] normalizeFitness(ENN[] networks) {
    	ENN[] res = new ENN[networks.length];
        float totalFitness = 0;
        
        // Sum all the fitness values
        for (ENN n : networks) {
          totalFitness += n.fitness;
        }

        // Normalize fitness
        for (int i = 0; i < res.length; i++) {
          res[i] = networks[i].copy();
          res[i].fitness = networks[i].fitness/totalFitness;
        }
        return res;
    }
    
    /**
     * Pick a random network, with higher fitness resulting in higher probability of being choosen
     */
    protected static ENN pickOne(ENN[] networks) {
    	int index = 0;
        float r = (float)Math.random();
        
        // Choose a network by removing their fitness from r until r < 0 or the end is reached.
        while (r > 0 && index < networks.length) {
          r = r - networks[index].fitness;
          index++;
        }
        // Prevent off by one error
        index--;

        ENN child = networks[index].copy();
        return child;
    }
    
    /**
     * Load a network from a file
     * @param filename Filename
     * @return {@link ENN}
     */
    public static ENN load(String filename) {
    	ENN res = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            res = (ENN) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * Save a network to a file
     * @param filename Filename
     */
    public void save(String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            fileOut.close();
        } 
        catch (IOException i) {
            i.printStackTrace();
        }
    }
}