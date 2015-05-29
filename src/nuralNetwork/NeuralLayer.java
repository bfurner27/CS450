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
        
        //create a new layer of neurons
        for (int i = 0; i < numNeurons; i++) {  
            neurons.add(new Neuron(numInputs));
        }
    }
    
    public List<Double> classifyInstance(Instance instance) {
        resetOutput();
        for (Neuron n : neurons) {
            outputs.add(n.calculateInputResults(instance));
        }
        return outputs;
    }
    
    public List<Double> classifyInstance(List<Double> instance) {
        resetOutput();
        for (Neuron n : neurons) {
            outputs.add(n.calculateInputResults(instance));       
        }
        return outputs;
    }
    
    private void resetOutput() {
        outputs = new ArrayList<>();
    }
    
    public List<Double> getOutputs() {
        return outputs;
    }
}
