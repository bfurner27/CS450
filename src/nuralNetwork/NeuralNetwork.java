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
public class NeuralNetwork  {
    List<NeuralLayer> network;
    
    /**
     * This is the constructor for this network it will construct the neural network based on
     * the user parameters
     * @param numLayersAndNeurons - The size of the list is the number of layers, and the number
     *                              are the number of neurons within that layer 
     *                              NOTE: the last value in the List should be the number of classes
     * @param numAttributes - this is the number of attributes in a given instance that will be
     *                        tested
     */
    public NeuralNetwork(List<Integer> numLayersAndNeurons, int numAttributes) {
        network = new ArrayList<>();
       //create each layer with the number of nodes that the user specified
        network.add(new NeuralLayer(numLayersAndNeurons.get(0), numAttributes));
        for (int i = 1; i < numLayersAndNeurons.size(); i++) {
           /*This statement will create a new neural layer, will pass the number of neurons in 
            a layer and the number of inputs which happens to be the previous item in the list or
            the number of nodes*/
            network.add(new NeuralLayer(numLayersAndNeurons.get(i), numLayersAndNeurons.get(i - 1)));
        }
    }
    
    /**
     * This is the class to evaluate an instance to determine which class is the correct one
     * @param inst - this is the instance to be classified
     * @return - the class value as a double
     */
    public double classifyInstance(Instance inst) {
        //loop through all the layers and call their individual update instances for the class
        List<Double> layerOutput = network.get(0).classifyInstance(inst);
        for (int i = 1; i < network.size(); i++) {
            layerOutput = network.get(i).classifyInstance(layerOutput);
        }
        
        return findClassValue(layerOutput);
    }
    
    
    /**
     * This finds the largest value and returns its class value by finding the correct index
     * which means the final layer will correspond to the classes starting at index 0 will be 
     * the first class index 1 will be next and so on
     * @param layerOutput - This is the final output from the classifyInstance function
     * @return - this returns the final classification as a double
     */
    private double findClassValue(List<Double> layerOutput) {
                Integer largestIndx = -1;
        double largestVal = -10202.0;
        //loops through final output from previous loop to find largest value
        for (int i = 0; i < layerOutput.size(); i++) {
            //System.out.println("check: " + largestVal + " < " + layerOutput.get(i));
            if (largestVal < layerOutput.get(i)) {
                //System.out.println("largest node value: " + layerOutput.get(i));
                largestIndx = i;
                largestVal = layerOutput.get(i);
            }
        }
        System.out.println("In NeuralNetwork findClassValue The Class value: " + largestIndx);
        return largestIndx.doubleValue();
    }
}
