/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuralNetwork;

import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;

/**
 *
 * @author Benjamin
 */
public class NeuralLayer {
    List<Neuron> neurons; 
    List<Double> outputs;
    
    public NeuralLayer(int numNeurons, int numInputs) {
        neurons = new ArrayList<>();
        outputs = new ArrayList<>();
        
        for (int i = 0; i < numNeurons; i++) {
            neurons.add(new Neuron(numInputs));
        }
    }
    
    public void calculateOutputs(Instance instance) {
        for (int i = 0; i < neurons.size() - 1; i++) {
            
        }
        for (Neuron n : neurons) {
            outputs.add(n.calculateInputResults(instance));
        }
            
    }
    
    public List<Double> getOutputs() {
        return outputs;
    }
}
