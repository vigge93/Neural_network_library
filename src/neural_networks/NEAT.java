package neural_networks;

import java.io.Serializable;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class NEAT implements Serializable {

	private static final long serialVersionUID = 1L;
	int specie;
	
	Node[] inputNodes;
	Node[] hiddenNodes;
	Node[] outputNodes;
	
	Node bias;
	
	Connection[] connections;
	
	
	NEAT(int inputs, int outputs){
		//Generate nodes
		inputNodes = new Node[inputs];
		for(int i = 0; i < inputNodes.length; i++) {
			inputNodes[i] = new Node(0);
		}
		hiddenNodes = new Node[0];
		outputNodes = new Node[outputs];
		for(int i = 0; i < outputNodes.length; i++) {
			outputNodes[i] = new Node(inputNodes.length);
		}
		bias = new Node(0);
		bias.value = 1;
		
		//Generate connections
		connections = new Connection[(inputNodes.length+1)*outputNodes.length];
		for(int i = 0; i < outputNodes.length; i++) {
			for(int j = 0; j < inputNodes.length; j++) {
				connections[i*outputNodes.length + j] = new Connection(j, 0, i, 2);
			}
		}
	}
	
	/**
	 * To be added
	 * @param old
	 * @throws NotImplementedException
	 * @return
	 */
	NEAT[] crossover(NEAT[] old) {
		throw new NotImplementedException();
	}
	
}

class Node{
	int[] connectionsIn;
	boolean active = true;
	double value;
	boolean evaluated = false;
	
	Node(int sizeConnectionsIn){
		connectionsIn = new int[sizeConnectionsIn];
	}
}

class Connection {
	static final int sensor = 0;
	static final int hidden = 1;
	static final int output = 2;
	static int nextInnovation = 0;
	int innovation;
	int nodeIn;
	int nodeInType;
	int nodeOut;
	int nodeOutType;
	double weight;
	boolean active = true;
	
	Connection(int nodeIn, int typeIn, int nodeOut, int typeOut){
		this.nodeIn = nodeIn;
		nodeInType = typeIn;
		this.nodeOut = nodeOut;
		nodeOutType = typeOut;
		this.weight = Math.random()*2-1;
		innovation = nextInnovation++;
	}
}