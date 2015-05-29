/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import weka.core.Instance;

/**
 *
 * @author Benjamin
 */
public class Neuron {
    private List<Double> inputWeights;
    private double threshold = 0.0;
    
    /**
     * The constructor it will take the number of inputs that the user has specified and will create
     * a new node with weights. It also includes the bias input in its calculations so the user does
     * not have to manually input that value
     * @param numInputs - this is the number of inputs the node will take
     */
    public Neuron(int numInputs) {
        //set the input Weights
        inputWeights = new ArrayList<>();

        //randomize the input values for a number between -1 and 1 scaled by .5
        //accounts for bias node by adding one to the number of inputs
        Random rndm = new Random();
        
        double rangeMax = .5;
        double rangeMin = -.25;  //these two numbers are to scale the random number
        for (int i = 0; i < numInputs + 1; i++) {
            inputWeights.add(rndm.nextDouble() * rangeMax + rangeMin);
        }
    }
    
    /**
     * This function will calculate the result and return a one or a zero based on the weights and
     * the threshold which is set to 0
     *
     * @param inputs - the array of inputs they have to match the value previously entered when
     * the class was initialized
     * @return
     */
    public double calculateInputResults(List<Double> inputs) {
        // checks if the attributes in the input are the right number accounts for bias input
        if (inputs.size() != inputWeights.size() - 1)
        {
            System.out.println("ERROR: inputs size does not match predetermined size calculateInputResults(Double)");
            System.exit(0);
        }
        
        //sums the inputs multiplied by their respective weight
        double sum = inputs.get(inputs.size() - 1) * -1.0; //bias node
        
        for (int i = 0; i < inputWeights.size() - 1; i++) {
            sum += inputs.get(i) * inputWeights.get(i);
        }
        
        //tells neuron to fire if the threshold is smaller than the sum
        return calculateThreshold(sum);
    }
    
    /**
     * This calculates if the neuron will fire or return a 1 based on the inputs from the instance
     * and the weights that were specified in the constructor, it will take into account the bias 
     * node which will always be a -1. 
     * This function takes into account that weka has a class count as an attribute so that if there
     * is no class value in there it will not actually work, to overcome this one could add a dummy
     * attribute at the end and this function will then ignore that last input
     * 
     * @param inputs - this is a weka instance with attribute values to be checked
     * @return
     */
    public double calculateInputResults(Instance inputs) {
        //checks that the number of attributes is the right size
        
        //this might give problems if there is no class specified because it takes into account
        //that the weka instance might have a class attribute as part of its attributes
        if (inputs.numAttributes() != inputWeights.size())
        {
            System.out.println("ERROR: inputs size does not match predetermined size");
            System.exit(0);
        }
        
        //This will sum the attribute values by their respective input weight
        double sum = -1.0 * inputWeights.get(inputWeights.size() - 1);
        for (int i = 0; i < inputWeights.size() - 1; i++) {
            sum += inputs.value(i) * inputWeights.get(i);
        }
        
        return calculateThreshold(sum);
    }
    
    /**
     * This will calculate the threshold function which is 1/1+e^-n
     * @param sum - this is the sum that was calculated for the node
     * @return
     */
    private double calculateThreshold(double sum) {
        return 1 / (1 + Math.pow(Math.E, -sum));
    }
    
}
