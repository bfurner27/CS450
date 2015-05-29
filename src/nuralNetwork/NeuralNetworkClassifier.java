/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuralNetwork;

import java.util.ArrayList;
import java.util.List;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Benjamin
 */
public class NeuralNetworkClassifier extends Classifier {
    private NeuralNetwork neuralNetwork;
    
    public static void main(String[] args) {
        //build instance of iris 5.1	3.5	1.4	0.2	Iris-setosa
        Instance test = new Instance(5);
        //test.setClassValue("Iris-setosa");
        //test.setClassValue(0.0);
        test.setValue(0, 5.1);
        test.setValue(1, 3.5);
        test.setValue(2, 1.4);
        test.setValue(3, .2);
        test.setValue(4, 0.0);
        Neuron n = new Neuron(test.numAttributes() - 1);
        System.out.println("The result is: " + n.calculateInputResults(test));;
        
        NeuralLayer nL = new NeuralLayer(5,test.numAttributes() - 1);
        nL.classifyInstance(test);
        
        System.out.println("\n\nNow show the results of the calculations");
        for (double output : nL.getOutputs()) {
            System.out.println(output);
        }
    }

    @Override
    public void buildClassifier(Instances i) {
        
        //build the number of neuron layers that are needed for this problem
        List<Integer> numNodes = new ArrayList<>();
        numNodes.add(i.numClasses() + 4); //add number of classes plus 4
        numNodes.add(i.numClasses() + 2); //adds number of classes plus 2
        numNodes.add(i.numClasses()); //adds number of classes, this layer will return our class
        neuralNetwork = new NeuralNetwork(numNodes, i.numAttributes() - 1);
        
        
    }
    
    @Override
    public double classifyInstance(Instance instance) {
        
        return neuralNetwork.classifyInstance(instance);
    }
}
