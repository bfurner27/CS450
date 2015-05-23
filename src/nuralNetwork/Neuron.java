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
        // checks if the attributes in the input are the right number
        if (inputs.size() + 1 != inputWeights.size())
        {
            System.out.println("ERROR: inputs size does not match predetermined size");
            System.exit(0);
        }
        
        //sums the inputs multiplied by their respective weight
        double sum = inputs.get(inputs.size() - 1) * -1.0; //bias node
        
        for (int i = 0; i < inputWeights.size(); i++) {
            sum += inputs.get(i) * inputWeights.get(i);
        }
        
        //tells neuron to fire if the threshold is smaller than the sum
        if (sum > threshold)
        {
            return 1.0;
        }
        return 0.0;
    }
    
    /**
     * This calculates if the neuron will fire or return a 1 based on the inputs from the instance
     * and the weights that were specified in the constructor, it will take into account the bias 
     * node which will always be a -1.
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
            System.out.println("attribute value: " + inputs.value(i) + " * weight: " + inputWeights.get(i));
            sum += inputs.value(i) * inputWeights.get(i);
        }
        System.out.println();
        
        /* 
        This will fire th neuron if the threshold is less than the sum
        */
        if (sum > threshold)
        {
            return 1.0;
        }
        return 0.0;
    }
    
    
}
