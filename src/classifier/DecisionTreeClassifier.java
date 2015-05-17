/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import static java.lang.Math.log;
import java.util.ArrayList;
import static java.util.Arrays.sort;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Benjamin
 */
public class DecisionTreeClassifier extends Classifier {
    Node<Instance> head = new Node<>();
    @Override
    public void buildClassifier(Instances inst) {
        //find the most common class and format all the class values
        double mostCommon = 0.0;
       

        //gets an array with the class in its index position and number of instances of class
        List<Double> classesCount = findClassesCount(inst);
        //calculate the total entropy
        double totalEntropy = findTotalEntropy(classesCount);
        

    }
    
    
    public Node makeTree(Instances trainingData, List<Double> classesCount, double totalEntropy) {
        
        // put the class values in an array from highest number of classes to lowest
        List<Double> classes = new ArrayList<>();
        for (Double i = 0.0; i < classesCount.size(); i++) {
            if (classes.isEmpty() || classesCount.get(i.intValue()) > classes.get(0)) {
                classes.add(0, i);
            } else {
                classes.add(i);
            }
        }
        double mostLikely = classes.get(0);
        
        // These are the checks to determine the recursion works correctly
        
        //determines if the data or the number of attributes is zero
        if (trainingData.numInstances() == 0 || trainingData.numAttributes() == 0) {
            return (new Node<>(mostLikely));
        // determines if there is only one class left  
        } else if (classesCount.get(classes.get(0).intValue()) == trainingData.numInstances()) {
            return (new Node<>(classes.get(0)));       
        } else {
            
            double gain[] = new double[trainingData.numAttributes()];
            //zero out the array
            for (int i = 0; i < gain.length; i++) {
                gain[i] = 0.0;
            }
            
            //choose best possible attribute to go in tree
            for (int i = 0; i < trainingData.numAttributes(); i++) {
                double g = calculateInfoGain(trainingData, trainingData.attribute(i).index());
                gain[trainingData.attribute(i).index()] = totalEntropy - g;
            }
            
            int bestAttributeIndx = 0;
            for (int i = 0; i < gain.length; i++) {
                if (gain[bestAttributeIndx] < gain[i]) {
                    bestAttributeIndx = i;
                }
            }
            
            //find attribute values
            if (trainingData.attribute(bestAttributeIndx).isNumeric()) {
                List<Double> values = findAttributeValuesNumeric(trainingData, bestAttributeIndx);
                for (Double value : values) {
                    Instances smallerTrainData;
                    int smallerNumAttributes = 0;
                    
                    
                    //makeTree(smallerTrainData, classes, smallerNumAttributes, totalEntropy);
                }
            } else {
                List<String> values = findAttributeValuesNominal(trainingData, bestAttributeIndx);
                
                for (String value : values) {
                    Instances smallerTrainData = new Instances(trainingData);
                    smallerTrainData.delete();
           
                    int smallerNumAttributes = 0;
                    
                    for (int i = 0; i < trainingData.numInstances(); i++) {
                        if (trainingData.instance(i).stringValue(bestAttributeIndx).equals(value)) {
                            smallerTrainData.add(trainingData.instance(i));
                        }
                    }
                    
                    smallerTrainData.deleteAttributeAt(bestAttributeIndx);
                    classesCount = findClassesCount(smallerTrainData);
                    totalEntropy = findTotalEntropy(classesCount);
                    
                    makeTree(smallerTrainData, classesCount, totalEntropy);
                    
                    
                }
                Node subNode = new Node<>();
                return subNode;
            }
        }
        return null;
    }
    
    
    public double findTotalEntropy(List<Double> classesCount) {
        double totalEntropy = 0.0;
        //sum the values to get the total number of instances of the class
        double sum = 0.0;
        for (double count : classesCount) {
            sum += count;
        }
        
        for (Double count : classesCount) {
            totalEntropy += calculateEntropy(count / sum);
        }
        return totalEntropy;
    }
    
    
    public List<Double> findClassesCount(Instances trainingData) {
        List<Double> classesCount = new ArrayList<>();
        for (int h = 0; h < trainingData.numClasses(); h++) {
            classesCount.add(0.0);
        }
        for (int i = 0; i < trainingData.numInstances(); i++) {
            for (int h = 0; h < classesCount.size(); h++) {
                if (trainingData.instance(i).classValue() == h) {
                    double tempVal = classesCount.get(h) + 1;
                    classesCount.set(h, tempVal);
                }
            }     
        }
        
        return classesCount;
    }
    
    public List<String> findAttributeValuesNominal(Instances train, int attrbtIndx) {
        List<String> values = new ArrayList<>();
        
        for (int i = 0; i < train.attribute(attrbtIndx).numValues(); i++) {
            values.add(train.attribute(attrbtIndx).value(i));
        }
        return values;
    }
    
    public List<Double> findAttributeValuesNumeric(Instances train, int attrbtIndx) {
        List<Double> attributeValues = new ArrayList<>();
        
        double lower = train.attribute(attrbtIndx).getLowerNumericBound();
        double higher = train.attribute(attrbtIndx).getUpperNumericBound();
        double range = higher - lower;
        
        attributeValues.add(range / 3);
        attributeValues.add(range * 2.0/3.0);
        attributeValues.add(higher);
        
        return attributeValues;
    }
    
    
    /**
     * This function uses the entropy calculation function of -p*log2(p) to find a log value, it 
     * returns 0 if there is no probability passed in.
     * @param probability - this the the probability that the specific event it likely.
     * @return This will return the entropy, a number between 1 and 0 which gives us the measure
     *         of inaccuracy, a 0 being it is purely of one type, a 1 being it is split 50/50
     */
    public double calculateEntropy(double probability) {
        double entropy = 0.0;
        
        if (probability != 0) {
            entropy = -probability * (log(probability)/log(2));
        }
        return entropy;
    }
   
    
    
    
    /**
     * This function will calculate the information gain using the entropy function. It is generally
     * counting all the information that is available to see which feature has the highest 
     * information gain
     * @param instances
     * @param classes
     * @param attributeIndex
     * @return
     */
    public double calculateInfoGain(Instances instances, int attributeIndex) {
        double gain = 0.0;

        
        List<Double> values = new ArrayList<>();
        
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            if (values.contains(instance.value(attributeIndex)))
            {
                values.add(instance.value(attributeIndex));
            }
        }
        
        
        List<Double> numAttributes = new ArrayList<>();
        List<Double> entropy = new ArrayList<>();
        
        for (int i = 0; i < values.size(); i++) {
            numAttributes.add(0.0);
            entropy.add(0.0);
        }
        
        int valueIndex = 0;
        
        for (Double value : values) {
            List<Double> newClasses = new ArrayList<>();
            for (int i = 0; i < instances.numInstances(); i++) {
                if (instances.instance(i).value(attributeIndex) == value)
                {
                    double count = numAttributes.get(valueIndex);
                    numAttributes.add(valueIndex, count++);
                    newClasses.add(instances.instance(i).classValue());
                }
            }
            
            List<Double> classValues = new ArrayList<>();
            for (Double aClass : newClasses) {
                int numClass = 0;
                for (int j = 0; j < classValues.size(); j++) {
                    if (classValues.get(j).equals(aClass)) {
                        numClass++;
                    }
                }
                if (numClass == 0) {
                    classValues.add(aClass);
                }
            }
            
            
            List<Double> classCounts = new ArrayList<>();
            for (int i = 0; i < classValues.size(); i++) {
                classCounts.add(0.0);
            }
            int classIndex = 0;
            
            for (int i = 0; i < classValues.size(); i++) {
                for (Double aClass : newClasses) {
                    if (Objects.equals(aClass, classValues.get(i))) {
                        Double count = classCounts.get(classIndex);
                        classCounts.add(classIndex, count++);
                    }
                }
                classIndex++;
            }
            
            double sumClassCounts = 0; 
            for (int i = 0; i < classCounts.size(); i++) {
                sumClassCounts += classCounts.get(i);
            }
            
            for (int i = classIndex; i < classValues.size(); i++) {
                double entropyTemp = entropy.get(valueIndex);
                entropyTemp += calculateEntropy(classCounts.get(classIndex)/ sumClassCounts);
                entropy.add(valueIndex, entropyTemp);
            }
            
            gain += numAttributes.get(valueIndex)/
                    (instances.numInstances() * entropy.get(valueIndex));
            valueIndex++;
        }
        return gain;
    }
    
    
    
    /**
     *
     * @param instance
     * @return 
     */
    @Override
    public double classifyInstance(Instance instance) {
        return 0;
    }
    
}
