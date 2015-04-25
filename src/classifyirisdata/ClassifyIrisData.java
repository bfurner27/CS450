/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifyirisdata;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

/**
 *
 * @author Benjamin
 */
public class ClassifyIrisData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ClassifyIrisData instance = new ClassifyIrisData();
        try {
            instance.programInterface();
        } catch (Exception ex) {
           System.out.println("This failed to work! Will now exit" + ex);
           System.exit(0);
        }
    }
    
    /**
     * This is the program interface, it will read in the file, randomize it and split the data into
     * two sets, test and train. This also will call the other function that are needed to classify
     * the data correctly.
     * @throws Exception
     */
    public void programInterface() throws Exception {
            String filename = "C:\\Users\\Benjamin\\Documents\\Brigham Young University Idaho"
                    + "\\Computer Science\\CS450\\iris.csv";
            DataSource src = new DataSource(filename);
            
            Instances data = src.getDataSet();
            

            
            data.randomize(data.getRandomNumberGenerator(25));
            

            /*
            This section has a problem because I could not get the inverse property to work so 
            it actually does not really split the items but choses out 30% and 70% out of the data
            set individually. 
            */
            
            //set aside 70 percent of data for train set
            RemovePercentage rmv = new RemovePercentage();
            rmv.setInputFormat(data);            
            rmv.setPercentage(30);
            Instances trainSet = Filter.useFilter(data, rmv);
            
            //set aside 30 percent of data for test set 
            rmv.setInputFormat(data);
            rmv.setPercentage(30);
            rmv.setInvertSelection(true);
            Instances testSet = Filter.useFilter(data, rmv);
            
            System.out.println("size data: " + data.numInstances() + "size trainSet: " 
                    + trainSet.numInstances() + "size testSet: " + testSet.numInstances());
            
            for (int i = 0; i < trainSet.numInstances(); i++) {
                System.out.println(trainSet.instance(i).toString());
            }
           
            System.out.println("");
            for (int i = 0; i < testSet.numInstances(); i++) {
                System.out.println(testSet.instance(i).toString());
            }
            
            ClassifierTest(trainSet);
    } 
    
    public void ClassifierTest(Instances irisSet) {
        int numCorrect = 0;
        int totalNum = irisSet.numInstances();
        int irisTypePredicted = 0;
        int irisTypeActual = 0;
        for (int i = 0; i < irisSet.numInstances(); i++) {
            String type = irisSet.instance(i).toString();
            String data[] = type.split(",");
            if (data[4].equals("Iris-setosa")) {
                irisTypeActual = 0;
            } else if (data[4].equals("Iris-versicolor")) {
                irisTypeActual = 1;
            } else if (data[4].equals("Iris-virginica")) {
                irisTypeActual = 2;
            } else {
                irisTypeActual = 3;
            }
                
            irisTypePredicted = classify(irisSet.instance(i));
            
            if (irisTypePredicted == irisTypeActual) {
                numCorrect++;
            }
        }
        
        reportResults(numCorrect, totalNum);
    }
    
    public void reportResults(int correct, int total) {
        float result = (float)correct/total;
        System.out.println("The probability of correct was: " + correct + "/" + total
                + " or " + result * 100 + "%");
    }
    
    public int classify(Instance iris) {
        return 0;
    }
}