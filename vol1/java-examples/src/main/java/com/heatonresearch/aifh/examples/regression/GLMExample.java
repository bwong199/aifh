package com.heatonresearch.aifh.examples.regression;

import com.heatonresearch.aifh.examples.learning.SimpleLearn;
import com.heatonresearch.aifh.general.data.BasicData;
import com.heatonresearch.aifh.general.fns.link.LogitLinkFunction;
import com.heatonresearch.aifh.normalize.DataSet;
import com.heatonresearch.aifh.regression.MultipleLinearRegression;
import com.heatonresearch.aifh.regression.TrainReweightLeastSquares;

import java.io.InputStream;
import java.util.List;

/**
 * Example that uses a GLM to predict the probability of breast cancer.
 */
public class GLMExample extends SimpleLearn {

    /**
     * Run the example.
     */
    public void process() {
        try {
            InputStream istream = this.getClass().getResourceAsStream("/breast-cancer-wisconsin.csv");

            DataSet ds = DataSet.load(istream);
            istream.close();

            ds.deleteUnknowns();
            ds.deleteColumn(0);
            ds.replaceColumn(9, 4, 1, 0);
            List<BasicData> trainingData = ds.extractSupervised(0, 9, 9, 1);

            MultipleLinearRegression reg = new MultipleLinearRegression(9);
            reg.setLinkFunction(new LogitLinkFunction());
            TrainReweightLeastSquares train = new TrainReweightLeastSquares(reg, trainingData);

            int iteration = 0;
            do {
                iteration++;
                train.iteration();
                System.out.println("Iteration #" + iteration + ", Error: " + train.getError());
            } while (iteration < 1000 && train.getError() > 0.01);

            query(reg, trainingData);
            System.out.println("Error: " + train.getError());


        } catch (Throwable t) {
            t.printStackTrace();
        }


    }

    /**
     * The main method.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        GLMExample prg = new GLMExample();
        prg.process();
    }
}
