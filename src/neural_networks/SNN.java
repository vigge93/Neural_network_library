package neural_networks;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SNN extends Neural_network implements Serializable {

	protected float dsigmoid(float val) {
		return val * (1 - val);
	}

	protected Matrix dSoftmax(Matrix m) {
		Matrix res = m.copy();
		res = softmax(res);
		for (int i = 0; i < res.rows; i++) {
			float val = res.table[i][0];
		}
		return res;
	}

	protected float dtanh(float val) {
		return 1 - val * val;
	}

	protected float crossentropy_loss(Matrix answer, Matrix predicted) {
		float sum = 0;
		for (int i = 0; i < answer.rows; i++) {
			sum += answer.table[i][0] * (float) Math.log(predicted.table[i][0]);
		}
		return -sum;
	}

	protected int kronecker_delta(int a, int b) {
		return a == b ? 1 : 0;
	}

	private static final long serialVersionUID = 1L;

	public float learningrate = (float) 0.01;

	public SNN(int inputs_, int[] hidden_, int outputs_, activationFunction activation) {
		super(inputs_, hidden_, outputs_, activation);
	}

	protected float[] guessTraining(float[] in) {
		inputs = new Matrix(in);

		// Input to hidden
		Matrix tempIH = Matrix.multiply(weightsIH, inputs);
		tempIH = Matrix.add(tempIH, weightBI);

		switch (activation) {
		case SIGMOID:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden[0].table[i][0] = sigmoid(tempIH.table[i][0]);
			}
			break;
		case SOFTMAXSIG:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden[0].table[i][0] = sigmoid(tempIH.table[i][0]);
			}
			break;
		case TANH:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden[0].table[i][0] = tanh(tempIH.table[i][0]);
			}
			break;
		case SOFTMAXTANH:
			break;
		case GAUSSIAN:
			break;
		case SOFTMAXGAUSSIAN:
			break;
		default:
			for (int i = 0; i < tempIH.rows; i++) {
				hidden[0].table[i][0] = sigmoid(tempIH.table[i][0]);
			}
			break;
		}

		// Hidden to hidden
		for (int n = 0; n < weightsHH.length; n++) {
			Matrix tempHH = Matrix.multiply(weightsHH[n], hidden[n]);
			tempHH = Matrix.add(tempHH, weightsBH[n]);

			switch (activation) {
			case SIGMOID:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden[n + 1].table[i][0] = sigmoid(tempHH.table[i][0]);
				}
				break;
			case SOFTMAXSIG:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden[n + 1].table[i][0] = sigmoid(tempHH.table[i][0]);
				}
				break;
			case TANH:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden[n + 1].table[i][0] = tanh(tempHH.table[i][0]);
				}
				break;
			case SOFTMAXTANH:
				break;
			case GAUSSIAN:
				break;
			case SOFTMAXGAUSSIAN:
				break;
			default:
				for (int i = 0; i < tempHH.rows; i++) {
					hidden[n + 1].table[i][0] = sigmoid(tempHH.table[i][0]);
				}
				break;
			}
		}

		// Hidden to outputs
		Matrix tempHO = Matrix.multiply(weightsHO, hidden[hidden.length - 1]);
		tempHO = Matrix.add(tempHO, weightBO);

		switch (activation) {
		case SIGMOID:
			for (int i = 0; i < tempHO.rows; i++) {
				outputs.table[i][0] = sigmoid(tempHO.table[i][0]);
			}
			break;
		case SOFTMAXSIG:
			outputs = softmax(outputs);
			break;
		case TANH:
			for (int i = 0; i < tempHO.rows; i++) {
				outputs.table[i][0] = tanh(tempHO.table[i][0]);
			}
			break;
		case SOFTMAXTANH:
			outputs = softmax(outputs);
			break;
		case GAUSSIAN:
			break;
		case SOFTMAXGAUSSIAN:
			outputs = softmax(outputs);
			break;
		default:
			for (int i = 0; i < tempHO.rows; i++) {
				outputs.table[i][0] = sigmoid(tempHO.table[i][0]);
			}
			break;
		}
		return outputs.toArray();
	}

	/**
	 * 
	 * @param inputs_  Input vector
	 * @param answers_ Expected output vector
	 */
	public void train(float[] inputs_, float[] answers_) {
		float[] predicted = guessTraining(inputs_);
		Matrix predictions = new Matrix(predicted);
		Matrix answers = new Matrix(answers_);

		// Calc errors
		// Output to Hidden

		Matrix errorsO = Matrix.subtract(answers, predictions);
		Matrix transposedHO = Matrix.transpose(weightsHO);
		Matrix errorsHLast = Matrix.multiply(transposedHO, errorsO);

		// Hidden to Hidden
		Matrix[] errorsH = new Matrix[hidden.length];
		errorsH[hidden.length - 1] = errorsHLast;

		for (int n = hidden.length - 2; n >= 0; n--) {
			Matrix transposedHH = Matrix.transpose(weightsHH[n]);
			errorsH[n] = Matrix.multiply(transposedHH, errorsH[n + 1]);
		}

		// Adjust weights
		// Output to hidden
		Matrix gradient = new Matrix(outputs.rows, outputs.cols);
		switch (activation) {
		case SIGMOID:
			for (int i = 0; i < outputs.rows; i++) {
				for (int j = 0; j < outputs.cols; j++) {
					gradient.table[i][j] = dsigmoid(outputs.table[i][j]);// outputs.table[i][j]*(1-outputs.table[i][j]);
				}
			}
			break;
		case SOFTMAXSIG:
			break;
		case TANH:
			for (int i = 0; i < outputs.rows; i++) {
				for (int j = 0; j < outputs.cols; j++) {
					gradient.table[i][j] = dtanh(outputs.table[i][j]);// outputs.table[i][j]*(1-outputs.table[i][j]);
				}
			}
			break;
		case SOFTMAXTANH:
			outputs = softmax(outputs);
			break;
		case GAUSSIAN:
			break;
		case SOFTMAXGAUSSIAN:
			outputs = softmax(outputs);
			break;
		default:
			for (int i = 0; i < outputs.rows; i++) {
				for (int j = 0; j < outputs.cols; j++) {
					gradient.table[i][j] = dsigmoid(outputs.table[i][j]);// outputs.table[i][j]*(1-outputs.table[i][j]);
				}
			}
			break;
		}
		

		// Multiply error and lr
		gradient.multiply(errorsO);
		gradient.multiply(learningrate);
		// Adjust weights
		weightBO = Matrix.add(weightBO, gradient);
		Matrix hiddenLastT = Matrix.transpose(hidden[hidden.length - 1]);
		Matrix deltaWeightsHO = Matrix.multiply(gradient, hiddenLastT);
		weightsHO = Matrix.add(weightsHO, deltaWeightsHO);

		// Hidden to hidden
		for (int n = hidden.length - 1; n >= 1; n--) {
			gradient = new Matrix(hidden[n].rows, hidden[n].cols);
			switch (activation) {
			case SIGMOID:
				for (int i = 0; i < gradient.rows; i++) {
					for (int j = 0; j < gradient.cols; j++) {
						gradient.table[i][j] = dsigmoid(hidden[n].table[i][j]);// hidden[n].table[i][j] * (1 -
																				// hidden[n].table[i][j]);
					}
				}
				break;
			case SOFTMAXSIG:
				break;
			case TANH:
				for (int i = 0; i < gradient.rows; i++) {
					for (int j = 0; j < gradient.cols; j++) {
						gradient.table[i][j] = dtanh(hidden[n].table[i][j]);
					}
				}
				break;
			case SOFTMAXTANH:
				outputs = softmax(outputs);
				break;
			case GAUSSIAN:
				break;
			case SOFTMAXGAUSSIAN:
				outputs = softmax(outputs);
				break;
			default:
				for (int i = 0; i < gradient.rows; i++) {
					for (int j = 0; j < gradient.cols; j++) {
						gradient.table[i][j] = dsigmoid(hidden[n].table[i][j]);// hidden[n].table[i][j] * (1 -
																				// hidden[n].table[i][j]);
					}
				}
				break;
			}
			
			gradient.multiply(errorsH[n]);
			gradient.multiply(learningrate);
			weightsBH[n - 1] = Matrix.add(weightsBH[n - 1], gradient);
			weightsHH[n - 1] = Matrix.add(weightsHH[n - 1], Matrix.multiply(gradient, Matrix.transpose(hidden[n - 1])));
		}

		// Hidden to inputs
		gradient = new Matrix(hidden[0].rows, hidden[0].cols);
		switch (activation) {
		case SIGMOID:
			for (int i = 0; i < gradient.rows; i++) {
				for (int j = 0; j < gradient.cols; j++) {
					gradient.table[i][j] = dsigmoid(hidden[0].table[i][j]);// hidden[0].table[i][j] * (1 -
																			// hidden[0].table[i][j]);
				}
			}
			break;
		case SOFTMAXSIG:
			break;
		case TANH:
			for (int i = 0; i < gradient.rows; i++) {
				for (int j = 0; j < gradient.cols; j++) {
					gradient.table[i][j] = dtanh(hidden[0].table[i][j]);// hidden[0].table[i][j] * (1 -
																			// hidden[0].table[i][j]);
				}
			}
			break;
		case SOFTMAXTANH:
			outputs = softmax(outputs);
			break;
		case GAUSSIAN:
			break;
		case SOFTMAXGAUSSIAN:
			outputs = softmax(outputs);
			break;
		default:
			for (int i = 0; i < gradient.rows; i++) {
				for (int j = 0; j < gradient.cols; j++) {
					gradient.table[i][j] = dsigmoid(hidden[0].table[i][j]);// hidden[0].table[i][j] * (1 -
																			// hidden[0].table[i][j]);
				}
			}
			break;
		}
		

		// Multiply error and lr
		gradient.multiply(errorsH[0]);
		gradient.multiply(learningrate);

		// Adjust weights
		weightBI = Matrix.add(weightBI, gradient);
		weightsIH = Matrix.add(weightsIH, Matrix.multiply(gradient, Matrix.transpose(inputs)));
	}

	public static SNN load(String filename) {
		SNN res = null;
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			res = (SNN) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void save(String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(this);
			objectOut.close();
			fileOut.close();

		} catch (IOException i) {
			i.printStackTrace();
		}
	}

}
