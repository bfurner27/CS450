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
    Node<Instance> head;
    @Override
    public void buildClassifier(Instances inst) {
        //find the most common class and format all the class values
        double mostCommon = 0.0;

        //call the function to create the tree and hopefully display the results
        String treeRep = " ";
        head = makeTree(inst, head, treeRep);
        head.displayTree(treeRep);
    }
    
    
    public Node makeTree(Instances trainingData, Node head, String treeRep) {
        //gets an array with the class in its index position and number of instances of class
        List<Double> classesCount = findClassesCount(trainingData);
        
        //calculate the total entropy
        double totalEntropy = findTotalEntropy(classesCount);
        
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
            head = new Node();
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
            
            //set the attribute value for the node
            head.setAttributeIndx(trainingData.attribute(bestAttributeIndx));
            
            //find attribute values
            if (trainingData.attribute(bestAttributeIndx).isNumeric()) {
                List<Double> values = findAttributeValuesNumeric(trainingData, bestAttributeIndx);
                
                //loop through the values of the attribute
                for (Double value : values) {
                    Instances smallerTrainData = new Instances(trainingData);
                    smallerTrainData.delete();
                    // loops through the data and separates into a group based on the attribute
                    for (int i = 0; i < trainingData.numInstances(); i++) {
                        if (trainingData.instance(i).value(bestAttributeIndx) == value) {
                            smallerTrainData.add(trainingData.instance(i));
                        }
                    }
                    
                    //remove the attribute so it cannot be used late in the recursive process
                    smallerTrainData.deleteAttributeAt(bestAttributeIndx);
                    
                    // recursive call to go to the next level of recursion
                    Node subTree = makeTree(smallerTrainData, head, treeRep);
                    

                    // set the tree's index so that the attribute in the node above it will be able
                    // to know which value is in that particular tree.
                    subTree.setCheckIndxDouble(value);
                    subTree.setAttributeIndx(trainingData.attribute(bestAttributeIndx));
                    
                    //link the trees
                    head.addChild(subTree); 
                }
                return head;
            } else {
                List<String> values = findAttributeValuesNominal(trainingData, bestAttributeIndx);
                
                //loop through the values of the attribute
                for (String value : values) {
                    Instances smallerTrainData = new Instances(trainingData);
                    smallerTrainData.delete();
                    
                    for (int i = 0; i < trainingData.numInstances(); i++) {
                        if (trainingData.instance(i).stringValue(bestAttributeIndx).equals(value)) {
                            smallerTrainData.add(trainingData.instance(i));
                        }
                    }
                    
                    //remove the attribute so it cannot be used later in the recursive process
                    smallerTrainData.deleteAttributeAt(bestAttributeIndx);
                   
                    
                    // recursive call to go to the next level of recursion
                    Node subTree = makeTree(smallerTrainData, head, treeRep);
                    
                    // set the tree's index so that the attribute in the node above it will be able
                    // to know which value is in that particular tree.
                    subTree.setCheckIndx(trainingData.attribute(bestAttributeIndx).indexOfValue(value));
                    subTree.setAttributeIndx(trainingData.attribute(bestAttributeIndx));
                  
                    
                    //link the trees
                    head.addChild(subTree); 
                }
                return head;
            }
        }
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
        if (head.getData() < 0) {
            Node value = head;
            while (true) {
                List<Node> children = value.getChildren();
                if (instance.isMissing(value.getAttributeIndx())) {
                    if (children.isEmpty()) {
                        return 0.0;
                    } else {
                        value = children.get(0);
                    }
                //case for numbers
                } else if (head.getAttributeIndx().isNumeric()) {
                   
                    boolean isNextLayer = false;
                    for (Node child : children) {
                        //check if the data is greater =than one and if the two values are equal
                        if (child.getData() >= 0.0 
                            && instance.value(child.getAttributeIndx()) <=
                                child.getCheckIndxDouble()) {
   
                            return child.getData();
                        } else if (instance.value(child.getAttributeIndx()) <=
                                child.getCheckIndxDouble()) {
                            
                            value = child;
                            isNextLayer = true;
                            break;
                        }
                    }
                    
                    if (!isNextLayer) {
                        if (children.isEmpty()) {
                            return 0.0;
                        } else {
                            value = children.get(children.lastIndexOf(children));
                        }
                    } 
                //check string case      
                } else {
                    boolean isNextLayer = false;
                    for (Node child : children) {
                        //check if the data is greater =than one and if the two values are equal
                        if (child.getData() >= 0.0 
                            && instance.stringValue(child.getCheckIndx()).equals(
                               child.getAttributeIndx().value(child.getCheckIndx()))) {
   
                            return child.getData();
                        } else if (instance.stringValue(child.getCheckIndx()).equals(
                                child.getAttributeIndx().value(child.getCheckIndx()))) {
                            
                            value = child;
                            isNextLayer = true;
                            break;
                        }
                    }
                    
                    if (!isNextLayer) {
                        return 0.0;
                    } 
                }
            
            }
        } else {
            return head.getData();
        }
    }
    
    
}
