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
	
	public float mutationRate = (float)0.01;
    
	public float fitness = 0;
	
	/**
	 * 
	 * @param inputs_ No. input nodes
	 * @param hidden_ Array of hidden layer nodes
	 * @param outputs_ No. output nodes
	 * @param activation Activation/loss function to use
	 */
    public ENN(int inputs_, int[] hidden_, int outputs_, String activation) {
        super(inputs_, hidden_, outputs_, activation);
    }
    
    protected ENN(ENN parent) {
        super();
        this.inputs = parent.inputs.copy();
        this.hidden = new Matrix[parent.hidden.length];
        for (int i = 0; i < this.hidden.length; i++) {
            this.hidden[i] = parent.hidden[i].copy();
        }
        this.outputs = parent.outputs.copy();

        this.weightsIH = parent.weightsIH.copy();
        this.weightsHH = new Matrix[parent.weightsHH.length];
        for (int i = 0; i < this.weightsHH.length; i++) {
            this.weightsHH[i] = parent.weightsHH[i].copy();
        }
        this.weightsHO = parent.weightsHO.copy();

        this.weightBI = parent.weightBI.copy();
        this.weightsBH = new Matrix[parent.weightsBH.length];
        for (int i = 0; i < this.weightsBH.length; i++) {
            this.weightsBH[i] = parent.weightsBH[i].copy();
        }
        this.weightBO = parent.weightBO.copy();

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
        mutate(weightsIH);
        for (int i = 0; i < weightsHH.length; i++) {
            mutate(weightsHH[i]);
        }
        mutate(weightsHO);

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
    	ENN[] res = normalizeFitness(networks);
    	for(int i = 0; i < res.length; i++) {
    		ENN child = pickOne(res);
    		child.mutate();
    		res[i] = child;
    	}
    	return res;
    }
    
    protected static ENN[] normalizeFitness(ENN[] networks) {
    	ENN[] res = new ENN[networks.length];
    	float totalFitness = 0;
        for (ENN n : networks) {
          totalFitness += n.fitness;
        }
        for (int i = 0; i < res.length; i++) {
          res[i] = networks[i].copy();
          res[i].fitness = networks[i].fitness/totalFitness;
        }
        return res;
    }
    
    protected static ENN pickOne(ENN[] networks) {
    	int index = 0;
        float r = (float)Math.random();
        
        while (r > 0 && index < networks.length) {
          r = r - networks[index].fitness;
          index++;
        }
        index--;
        ENN child = networks[index].copy();
        return child;
    }
    
    /**
     * 
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