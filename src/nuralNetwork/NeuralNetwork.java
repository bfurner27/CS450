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
    private List<NeuralLayer> network = new ArrayList<>();
    private List< List<Double> > outputs = new ArrayList<>();
    private List< List<Double> > nodeError = new ArrayList<>();
    private double trainRate;
    
    /**
     * This is the constructor for this network it will construct the neural network based on
     * the user parameters
     * @param numLayersAndNeurons - The size of the list is the number of layers, and the number
     *                              are the number of neurons within that layer 
     *                              NOTE: the last value in the List should be the number of classes
     * @param numAttributes - this is the number of attributes in a given instance that will be
     *                        tested
     * @param trainRate
     */
    public NeuralNetwork(List<Integer> numLayersAndNeurons, int numAttributes, double trainRate) {
        this.trainRate = trainRate;
        network = new ArrayList<>();
        outputs = new ArrayList<>();
        nodeError = new ArrayList<>();
            

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
        resetArrays();
        List<Double> layerOutput = network.get(0).classifyInstance(inst);
        //put the outputs in the array of arrays
        outputs.add(layerOutput);
        
        //go through each element and call the classifyInstance in the layer, it will return 
        //the outputs and will put them into the output storage
        for (int i = 1; i < network.size(); i++) {
            layerOutput = network.get(i).classifyInstance(layerOutput);
            outputs.add(layerOutput);
        }
       
        //returns the instance that was classified after putting the output node into the
        //function to compute the actual classification
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
        return largestIndx.doubleValue();
    }
    
    
    public double trainNeuralNetwork(Instance instance) {
        double avgError = 0.0;
        //initialize our lists to new values every time we have a new instance
        resetArrays();
        
        //will send this instance through to compute all the values, it will put all the output 
        //values in the outputs list of lists
        classifyInstance(instance);
        
        //This will compute the error for the output nodes, it ensures that the error list that 
        //corresponds to each node is placed first in the array
        computeErrorOutputNodes(instance);
        
        //Loops through the outputs list and passes the index into the function which will take
        //care of the positioning and will compute the error for each of the values and put it
        //in the error nodeError list. It is a backwards loop because to caclulate the next layer
        //we have to have the error from the previous layer
        for (int i = outputs.size() - 2; i > -1 ; i--) {
            computeErrorHiddenNodes(i);   
        }
        
        updateWeights(instance);
        
            
        return calcAverageError();
    }
    
    
    private double calcAverageError() {
       //this will get the output errors
       List<Double> outputError = nodeError.get(nodeError.size() - 1);
       
       //sum the values for all the errors in the output neurons
       double sum = 0.0; 
       for (double error : outputError) {
           sum += error;
       }
       
       //return the average of the error
       return (sum / outputError.size());
    }
    
    private void updateWeights(Instance instance) {
        //account for the instance and call the instance update weights function
        network.get(0).updateWeights(nodeError.get(0), instance, trainRate);
      
        //this will loop through all the node values and will update the coresponding weights in 
        //the node
        for (int i = 1; i < network.size(); i++) {
            network.get(i).updateWeights(nodeError.get(i), outputs.get(i - 1), trainRate);
        }
    }
    
    /**
     * This will find the expected outputs for the output nodes based on the instance
     * @param instance - this is the information that we are classifying
     * @return - returns a list of the outputs that we are expecting
     */
    private List<Double> calcTargets(Instance instance) {
        List<Double> targets = new ArrayList<>();
        
        //initialize all values to 0.0 because that is the default output
        for (int i = 0; i < instance.numClasses() + 1; i++)
        {
            targets.add(0.0);
        }
        
        // This will update the index of the node with the expected value of one meaning it was the 
        // node that should have fired
        //System.out.println(instance.classValue());
        targets.set(Double.valueOf(instance.classValue()).intValue(), 1.0);
        
        return targets;
    }
    
    /**
     * This will compute the error of the output nodes, It will get the last layer of the nodes
     * from the outputs list of lists
     * @param instance - this is the instance that we are trying to classify
     */
    private void computeErrorOutputNodes(Instance instance) {
        //get the last layer of outputs
        List<Double> outputNodesResults = outputs.get(outputs.size() - 1);
        
        //initialize list for the errors
        List<Double> outputError = new ArrayList<>();
        
        //Get the target values
        List<Double> targets = calcTargets(instance);
        
        //compute the error based on the target value, and the actual outputs
        for (int i = 0; i < outputNodesResults.size(); i++) {
            //get the actual output
            double output = outputNodesResults.get(i);
            //get the expected output
            double target = targets.get(i);
            
            //calculate the error
            double error = output * (1 - output) * (output - target);
            
            //add the error to the output error list
            outputError.add(output);
        }
        
        nodeError.add(outputError);
    }
    
    private void computeErrorHiddenNodes(int hiddenLayerIndex) {
        //get the proper outputs for the given layer
        List<Double> outputsLayer = outputs.get(hiddenLayerIndex);
        
        //create an instance of an array list for the error
        List<Double> errorList = new ArrayList<>();
        
        //loops through each node in the layer and adds the error for that node to the error list
        for (int i = 0; i < outputsLayer.size(); i++) {
           
            //calls the function and makes sure that the node information the function is requesting
            //is accurate
            errorList.add(computeErrorHiddenNode(hiddenLayerIndex + 1, i,
                    nodeError.get(0), outputsLayer.get(i)));
        }
        
        nodeError.add(0, errorList);
    }
    
    private double computeErrorHiddenNode(int nodeLayerIndex, int weightIndex, 
            List<Double> nodeErrors, double output) {
        double error = 0.0;
        
        //get the correct node layer
        NeuralLayer layer = network.get(nodeLayerIndex);
        
        //calls the function that will sum the info for that particular layer
        double sumError = layer.sumErrorLayer(nodeErrors, weightIndex);
        
        //calculates the error for the given node
        error = output * (1 - output) * sumError;
        
        return error;
    }
    
    private void resetArrays() {
        outputs.clear();
        nodeError.clear();
        
    }
}
