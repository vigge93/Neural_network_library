package neural_networks;

import java.io.Serializable;

abstract class Neural_network implements Serializable {

	private static final long serialVersionUID = 1L;

	protected float sigmoid(float val) {
		return 1 / (1 + (float) Math.exp(-val));
	}

	protected float tanh(float val) {
		return (float)(Math.tanh(val));
	}
	
	protected Matrix softmax(Matrix m) {
		Matrix res = m.copy();
		float total = 0;
		for (int i = 0; i < m.rows; i++) {
			float exp_value = (float) Math.exp(m.table[i][0]);
			res.table[i][0] = exp_value;
			total += exp_value;
		}
		for (int i = 0; i < res.rows; i++) {
			res.table[i][0] /= total;
		}
		return res;
	}

	protected float softmax(int val, float total) {
		return (float) (Math.exp(val) / total);
	}

	protected float neatSigmoid(float val) {
		return 1 / (1 + (float) Math.exp(-4.9 * val));
	}
	
	public enum activationFunction {
		SIGMOID,
		SOFTMAXSIG,
		TANH,
		SOFTMAXTANH,
		GAUSSIAN,
		SOFTMAXGAUSSIAN,
		NEAT
	}

	protected Matrix inputs;
	protected Matrix[] hidden;
	protected Matrix outputs;

	protected Matrix weightsIH;
	protected Matrix[] weightsHH;
	protected Matrix weightsHO;

	protected Matrix weightBI;
	protected Matrix[] weightsBH;
	protected Matrix weightBO;

	protected activationFunction activation;

	/**
	 * 
	 * @param inputs_    Number of input nodes
	 * @param hidden_    Array of hidden nodes per layer
	 * @param outputs_   Number of output nodes
	 * @param activation Which activation function/s to use
	 * 
	 */
	protected Neural_network(int inputs_, int[] hidden_, int outputs_, activationFunction activation) {
		this.activation = activation;
		hidden = new Matrix[hidden_.length];

		inputs = new Matrix(inputs_, 1);
		for (int i = 0; i < hidden.length; i++) {
			hidden[i] = new Matrix(hidden_[i], 1);
		}
		outputs = new Matrix(outputs_, 1);

		weightsHH = new Matrix[hidden_.length - 1];
		weightsBH = new Matrix[hidden_.length - 1];

		weightsIH = new Matrix(hidden_[0], inputs_);
		weightsIH.randomize();
		for (int i = 0; i < hidden.length - 1; i++) {
			weightsHH[i] = new Matrix(hidden[i + 1].rows, hidden[i].rows);
			weightsHH[i].randomize();
			weightsBH[i] = new Matrix(hidden[i + 1].rows, 1);
			weightsBH[i].randomize();
		}
		weightsHO = new Matrix(outputs_, hidden_[hidden_.length - 1]);
		weightsHO.randomize();

		weightBI = new Matrix(hidden_[0], 1);
		weightBI.randomize();
		weightBO = new Matrix(outputs_, 1);
		weightBO.randomize();
	}

	protected Neural_network() {
	}

	/**
	 * Makes prediction based on inputs
	 * 
	 * @param in Input vector
	 * @return float[] Output vector
	 */
	public float[] guess(float[] in) {
		Matrix inputs_ = new Matrix(in);
		Matrix[] hidden_ = new Matrix[hidden.length];
		for (int i = 0; i < hidden_.length; i++) {
			hidden_[i] = hidden[i].copy();
		}
		Matrix outputs_ = outputs.copy();

		Matrix weightsIH_ = weightsIH.copy();
		Matrix weightBI_ = weightBI.copy();
		Matrix weightBO_ = weightBO.copy();

		Matrix[] weightsHH_ = new Matrix[weightsHH.length];
		for (int i = 0; i < weightsHH_.length; i++) {
			weightsHH_[i] = weightsHH[i].copy();
		}
		Matrix[] weightsBH_ = new Matrix[weightsBH.length];
		for (int i = 0; i < weightsBH_.length; i++) {
			weightsBH_[i] = weightsBH[i].copy();
		}
		Matrix weightsHO_ = weightsHO.copy();
		// Input to hidden
		Matrix tempIH = Matrix.multiply(weightsIH_, inputs_);
		tempIH = Matrix.add(tempIH, weightBI_);

		// Activation
		switch (activation) {
		case SIGMOID:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden_[0].table[i][0] = sigmoid(tempIH.table[i][0]);
			}
			break;
		case SOFTMAXSIG:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden_[0].table[i][0] = sigmoid(tempIH.table[i][0]);
			}
			break;
		case TANH:
			for(int i = 0; i < tempIH.rows; i++) {
				hidden_[0].table[i][0] = tanh(tempIH.table[i][0]);
			}
			break;
		case SOFTMAXTANH:
			break;
		case GAUSSIAN:
			break;
		case SOFTMAXGAUSSIAN:
			break;
		case NEAT:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden_[0].table[i][0] = neatSigmoid(tempIH.table[i][0]);
			}
			break;
		default:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden_[0].table[i][0] = sigmoid(tempIH.table[i][0]);
			}
			break;
		}

		// Hidden to hidden
		for (int n = 0; n < weightsHH_.length; n++) {
			Matrix tempHH = Matrix.multiply(weightsHH_[n], hidden_[n]);
			tempHH = Matrix.add(tempHH, weightsBH_[n]);

			// Activation

			switch (activation) {
			case SIGMOID:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden_[n + 1].table[i][0] = sigmoid(tempHH.table[i][0]);
				}
				break;
			case SOFTMAXSIG:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden_[n + 1].table[i][0] = sigmoid(tempHH.table[i][0]);
				}
				break;
			case TANH:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden_[n + 1].table[i][0] = tanh(tempHH.table[i][0]);
				}
				break;
			case SOFTMAXTANH:
				break;
			case GAUSSIAN:
				break;
			case SOFTMAXGAUSSIAN:
				break;
			case NEAT:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden_[n + 1].table[i][0] = neatSigmoid(tempHH.table[i][0]);
				}
				break;
			default:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden_[n + 1].table[i][0] = sigmoid(tempHH.table[i][0]);
				}
				break;
			}
		}

		// Hidden to outputs
		Matrix tempHO = Matrix.multiply(weightsHO_, hidden_[hidden_.length - 1]);
		tempHO = Matrix.add(tempHO, weightBO_);
		for (int i = 0; i < tempHO.rows; i++) {

			// Activation

			switch (activation) {
			case SIGMOID:
				for (int j = 0; j < tempHO.cols; j++) {
					outputs_.table[i][j] = sigmoid(tempHO.table[i][j]);
				}
				break;
			case SOFTMAXSIG:
				outputs_ = softmax(outputs_);
				break;
			case TANH:
				for (int j = 0; j < tempHO.cols; j++) {
					outputs_.table[i][j] = tanh(tempHO.table[i][j]);
				}
				break;
			case SOFTMAXTANH:
				outputs_ = softmax(outputs_);
				break;
			case GAUSSIAN:
				break;
			case SOFTMAXGAUSSIAN:
				outputs_ = softmax(outputs_);
				break;
			case NEAT:
				for (int j = 0; j < tempHO.cols; j++) {
					outputs_.table[i][j] = neatSigmoid(tempHO.table[i][j]);
				}
				break;
			default:
				for (int j = 0; j < tempHO.cols; j++) {
					outputs_.table[i][j] = sigmoid(tempHO.table[i][j]);
				}
				break;
			}
		}
		return outputs_.toArray();
	}

	/**
	 * 
	 * @param arr Array to analyze
	 * @return float[2] [index, value at index]
	 */
	public static float[] get_max(float[] arr) {
		int index = -1;
		float max = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				index = i;
			}
		}
		float[] res = { index, arr[index] };
		return res;
	}
}