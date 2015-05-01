/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Benjamin
 */
public class HardCodedClassifier extends Classifier{

    @Override
    public void buildClassifier(Instances i) throws Exception {
        
    }

    @Override
    public double classifyInstance(Instance i) {
        return 0;
    }
}
