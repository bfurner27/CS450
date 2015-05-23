/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Benjamin
 */
public final class KNNClassifier extends Classifier {

    private Instances dataToCompare;
    private int k;
    private int numAttributes;
    private int numClasses;
    private List<Double> kNNVal;
    private List<Instance> nearestNeighbors;
    public List<Double> classes;
    
    /**
     * Ensures the correct data will come in the right way.
     * @param i - this is the training instances
     * @param kNum - this is the number we want k to be
     */
    public KNNClassifier (Instances i, int kNum) {
        k = kNum;
        buildClassifier(i);
    }
    /**
     * Build Classifier ensures the program is trained so that it can predict classes
     * @param i - instances, this is the training data
     */
    @Override
    public void buildClassifier(Instances i)  {
        dataToCompare = i;
        numAttributes = dataToCompare.numAttributes();
        numClasses = dataToCompare.numClasses();
        
        classes = new ArrayList<Double>();
        for (double j = 0.0; j < dataToCompare.numClasses(); j++) {
                boolean add = classes.add(j);
        }
                
    }
        
    /**
     * This is the classifier, it will attempt to classify the data.
     * @param instance - this is the instance that will be classified
     * @return returns the number of the class it has decided
     */
    @Override
    public double classifyInstance(Instance instance) {
        nearestNeighbors = new LinkedList<>();
        kNNVal = new LinkedList<>();
        
        //loop through number of instances in training set
        for (int h = 0; h < dataToCompare.numInstances(); h++) {
            Instance compare = dataToCompare.instance(h);
            int numAttrTrue = 0;
            
            double distance = 0;
            
            /*loop through number of attributes in the instance comparing them and finding if
            an element will be the nearest neighbor*/
            for (int j = 0; j < numAttributes - 1; j++) {
                Attribute attr = instance.attribute(j); //get instance of attribute
                boolean isMatch = true;
                
                /* check for nominal or numerical attribute and call appropriate function */
                //if (attr.isNominal()) {
                //    isMatch = isNomAttrMatch(instance.stringValue(attr), compare.stringValue(attr));
                //} else {
                    double myValue = instance.value(j);
                    double otherValue = compare.value(j);
                    
                    double attributeDistance = Math.pow(myValue - otherValue, 2);
                    distance += attributeDistance;
                    
                    //double standardDeviation = 0;
                    
                    /* this will calculate the deviation I want the compare function to take into
                    account */
                    //standardDeviation = dataToCompare.variance(attr) /
                    //        (dataToCompare.numClasses() * 10000);
                    //isMatch = isNumAttrMatch(instance.value(attr), compare.value(attr), standardDeviation);
                //}
                
                /* increase number of attributes if match, this will be used later to determine 
                   nearest neighbors*/
                if (isMatch) {
                    numAttrTrue++;
                }
                
                
            } // end of foreach attribute j
        
            nearestNeighbors.add(compare);
            kNNVal.add(distance);
            
            //insertInstanceAndValue(compare, numAttrTrue);
        } // end of foreach instance in the training data
        
        double result = classify();
        //double result = findClassification();
        System.out.println(result);
        return result;
    }
    
    private double classify() {
        
        int bestIndex = 0;
        double bestValue = Double.MAX_VALUE;
        for (int i = 0; i < kNNVal.size(); i++) {
            double distance = kNNVal.get(i);
            
            if (distance < bestValue) {
                bestValue = distance;
                bestIndex = i;
            }
        }
        
        // bestIndex now has the index of the closest neighbor in our neareastNeighbors list
        Instance nearest = nearestNeighbors.get(bestIndex);
        return nearest.classValue();
    }
    
//    private double findClassification() {
//        
//        List<Integer> tempList = new ArrayList<>();
//        
//        //add k values to list to be evaluated
//        for (int i = 0; i < k && i < kNNVal.size(); i++) {
//            tempList.add(kNNVal.get(i));
//        }
//       
//        //add any duplicates of the top value, if any exist
//        for (int i = k; i < kNNVal.size(); i++) {
//            if (Objects.equals(tempList.get(0), kNNVal.get(i))) {
//                tempList.add(kNNVal.get(i));
//            } 
//            
//            // will exit loop as soon as there are no duplicates left of the first value
//            if (kNNVal.get(i) < tempList.get(0)) {
//                break;
//            }
//        }
//        
//        /* Out of the top values count the number of each class, to evaluate which class is the 
//           has the most votes*/
//        List<Integer> tempCount = new ArrayList<>();
//        
//        // create a new array to corespond with the number of classes
//        for (Double classe : classes) {
//            tempCount.add(0);
//        }
//        
//        // loop through the array of temp list and count values
//        for (int i = 0; i < tempList.size(); i++) {
//            Instance nN = nearestNeighbors.get(i);
//            
//            //loops through each class value for each instance and compares
//            for (int h = 0; h < classes.size(); h++) {        
//                if (nN.classValue() == classes.get(h)) {
//                    int countCopy = tempCount.get(h);
//                    countCopy++;
//                    tempCount.set(h, countCopy);
//                }
//            }
//        }
//        
//        //now find and return the result
//        int indexHighest = 0;
//        for (int i = 0; i < tempCount.size(); i++) {
//            //tie goes to the first classification
//            if (indexHighest < tempCount.get(i)) {
//                indexHighest = i;
//            }
//        }
//        return (classes.get(indexHighest));
//    }
//    
//    private void insertInstanceAndValue(Instance compare, int numAttrTrue) {
//        if (kNNVal.isEmpty()) {
//                nearestNeighbors.add(compare);
//                kNNVal.add(numAttrTrue);
//                
//        } else {
//            boolean isInserted = false;  //check if value is inserted into list
//            for (int i = 0; i < kNNVal.size(); i++) {
//                // Inserts larger Item in front of previous item
//                if (kNNVal.get(i) < numAttrTrue) {
//                        
//                    nearestNeighbors.add(i, compare);
//                    kNNVal.add(i, numAttrTrue);
//                        
//                    isInserted = true;
//                    break;
//                }
//            }
//                
//            /* appends item onto the end of the list. */
//            if (isInserted == false) { 
//                nearestNeighbors.add(compare);
//                kNNVal.add(numAttrTrue);
//            }
//        }
//    }
    
    private boolean isNomAttrMatch(String nomAttrVal, String compareVal) {
        boolean isMatch = false;
        
        if (nomAttrVal.equals(compareVal)) {
            isMatch = true;
        }
            
        return isMatch;
    }
    
    private boolean isNumAttrMatch(double numAttr, double numCompAttr, double standardDeviation) {
        boolean isMatch = false;
        
        if (numAttr == numCompAttr)
        {
            isMatch = true;
        } else if (numAttr < numCompAttr + standardDeviation
                || numAttr > numCompAttr - standardDeviation) {
            isMatch = true;
        } else {
            isMatch = false;
        }
            
        return isMatch;
    }
}
