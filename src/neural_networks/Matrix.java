/**
 * A simple neural network library.
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		##author##
 * @modified	##date##
 * @version		##version##
 */

package neural_networks;

import java.io.Serializable;
import java.util.Random;
import processing.core.PApplet;

public class Matrix implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int rows;
	public int cols;
	public float[][] table;

	/**
	 * 
	 * @param rows    Number of rows
	 * @param columns Number of columns
	 */
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.cols = columns;
		table = new float[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				table[i][j] = 0;
			}
		}
	}

	/**
	 * 
	 * @param m Two dimensional array [rows][cols]
	 */
	public Matrix(float[][] m) {
		rows = m.length;
		cols = m[0].length;
		table = new float[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				table[i][j] = m[i][j];
			}
		}
	}

	/**
	 * 
	 * @param m Array of rows
	 */
	public Matrix(float[] m) {
		rows = m.length;
		cols = 1;
		table = new float[rows][1];
		for (int i = 0; i < rows; i++) {
			table[i][0] = m[i];
		}
	}

	/**
	 * 
	 * @param m - Matrix to copy
	 */
	public Matrix copy() {
		return new Matrix(this.table);
	}

	public void randomize() {
		Random r = new Random();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				table[i][j] = r.nextFloat() * 2 - 1;
			}
		}
	}

	/**
	 * 
	 * @param scalar Scalar
	 * @return Matrix
	 */
	public Matrix multiply(float scalar) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				table[i][j] *= scalar;
			}
		}
		return this;
	}

	/**
	 * 
	 * @param m Matrix to multiply with
	 * @return Matrix
	 */
	public Matrix multiply(Matrix m) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				table[i][j] *= m.table[i][j];
			}
		}
		return this;
	}

	/**
	 * 
	 * @param a Matrix a
	 * @param b Matrix b
	 * @return Matrix
	 */
	public static Matrix multiply(Matrix a, Matrix b) {
		Matrix res = new Matrix(a.rows, b.cols);
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < b.cols; j++) {
				float sum = 0;
				for (int k = 0; k < a.cols; k++) {
					sum += a.table[i][k] * b.table[k][j];
				}
				res.table[i][j] = sum;
			}
		}
		return res;
	}

	/**
	 * 
	 * @param scalar Scalar
	 * @return Matrix
	 */
	public Matrix add(float scalar) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				table[i][j] += scalar;
			}
		}
		return this;
	}

	/**
	 * 
	 * @param a Matrix a
	 * @param b Matrix b
	 * @return Matrix
	 */
	public static Matrix add(Matrix a, Matrix b) {
		Matrix res = new Matrix(a.rows, a.cols);
		for (int i = 0; i < res.rows; i++) {
			for (int j = 0; j < res.cols; j++) {
				res.table[i][j] = a.table[i][j] + b.table[i][j];
			}
		}
		return res;
	}

	/**
	 * 
	 * @param a Matrix a
	 * @param b Matrix b
	 * @return Matrix
	 */
	public static Matrix subtract(Matrix a, Matrix b) {
		Matrix res = new Matrix(a.rows, a.cols);
		for (int i = 0; i < res.rows; i++) {
			for (int j = 0; j < res.cols; j++) {
				res.table[i][j] = a.table[i][j] - b.table[i][j];
			}
		}
		return res;
	}

	/**
	 * 
	 * @param m Matrix to transpose
	 * @return Matrix
	 */
	public static Matrix transpose(Matrix m) {
		Matrix res = new Matrix(m.cols, m.rows);
		for (int i = 0; i < m.rows; i++) {
			for (int j = 0; j < m.cols; j++) {
				res.table[j][i] = m.table[i][j];
			}
		}
		return res;
	}

	public void print() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				PApplet.print(table[i][j] + "\t");
			}
			PApplet.println();
		}
	}

	/**
	 * 
	 * @return float[]
	 */
	public float[] toArray() {
		float[] res = new float[rows * cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				res[cols * i + j] = table[i][j];
			}
		}
		return res;
	}

	public final static String VERSION = "##version##";

	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
}
