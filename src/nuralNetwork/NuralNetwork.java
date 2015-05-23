/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuralNetwork;

import weka.core.Instance;

/**
 *
 * @author Benjamin
 */
public class NuralNetwork {
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
        nL.calculateOutputs(test);
        
        System.out.println("\n\nNow show the results of the calculations");
        for (double output : nL.getOutputs()) {
            System.out.println(output);
        }
    }
}
