/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import at.tugraz.cgv.multiviewva.model.enums.dimReductionType;
import at.tugraz.cgv.multiviewva.utility.*;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorFactory;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double.EuclideanDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures.NumericalFeatureVectorDescriptor;
import com.github.TKnudsen.ComplexDataObject.model.transformations.dimensionalityReduction.IDimensionalityReduction;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.FeatureVectorCluster;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical.NumericalFeatureVectorClusterResult;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.AbstractActiveLearningModel;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.uncertaintySampling.SmallestMarginActiveLearning;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;
import com.github.TKnudsen.DMandML.model.supervised.classifier.impl.numericalFeatures.RandomForest;
import com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction.MDS;
import com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction.PCA;
import com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction.TSNE;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.INumericalClusteringAlgorithm;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl.HierarchicalClustering;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl.KMeans;
import com.jujutsu.tsne.barneshut.TSneConfiguration;
import com.jujutsu.utils.TSneUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import test.com.github.TKnudsen.DMandML.model.supervised.classifier.ClassificationTest;

/**
 * FileModel stores the information in a .CSV file (High dimensional data set)
 *
 * @author Lin Shao
 */
public final class DataModel {

    /**
     * what is the delimiter
     */
    private String delimiter;

    /**
     * number of attributes in the header
     */
    private int nrOfAttributes;
    /**
     * Header of data
     */
    private ArrayList<String> dataHeader;

    /**
     * index of all numeric dimensions in dataHeader
     */
    private ArrayList<Integer> numericDimensionsIndex = new ArrayList();

    /**
     * Whole data set is stored in this Array of ArrayList. to access each
     * dimension do dataSet[n] and to access a point do a for on all dimensions
     * dataSet should have categories at the end
     */
    private ArrayList<Object>[] dataSet;

    /**
     * ObservableList of all items
     */
    private ObservableList<Item> items = FXCollections.observableArrayList();

    /**
     * complex object for machine learning and stuff (records including class
     * names)
     * getDescription: true if labelled, false if not
     * getAttribute("class"), get class of the record
     */
    private List<ComplexDataObject> complexObjList = new ArrayList<>();

    /**
     *
     */
    private int nrItems = 0;

    /**
     * determines index of name dimension (if any)
     */
    private int nameDimIndex = -1;

    /**
     * ObservableList of selected items
     */
    private ObservableSet<Integer> selectedItems = FXCollections.observableSet(new HashSet<Integer>());

    /**
     * selected items that are selected individually and not by lasso
     */
    private ObservableSet<Integer> selectedItemsIndividual = FXCollections.observableSet(new HashSet<Integer>());

    /**
     * Set of dimensions that the selection happen on
     */
    private Set<String> dimensionSelected = new HashSet();

    /**
     *
     */
    private ObservableList<Dimension> dimensions = FXCollections.observableArrayList();

    /**
     * Min and Max value of every dimension. First value in Double[] is min and
     * second is max
     */
    private ArrayList<Double[]> minMaxValues;

    /**
     * which attributes are string and therefore should not be treated like data
     */
    private ArrayList<Integer> classIndex;

    /**
     * Number of different categories (for example a data set can have 2
     * categories and each category includes 3 clusters)
     */
    private int nrOfCatDim;

    /**
     * name of clusters
     */
    private ArrayList<LabelModel> classNames = new ArrayList<>();

    /**
     * map of records and initil original labels
     */
    Map<Integer, String> originalLables = new HashMap<>();

    /**
     * Constructor for creating a new DataModel, e.g. new
     * DataModel(file.getAbsolutePath(), ";", true);
     *
     * @param path      the file path
     * @param delimiter e.g. ; or :
     * @param normalize whether normalize (0-1) or not the points
     */
    public DataModel(String path, String delimiter, boolean normalize) {

        try {
            this.delimiter = delimiter;

            BufferedReader in = new BufferedReader(new FileReader(path));
            readHeader(in.readLine());
            readData(in);
            if (normalize) {
                dataSet = normalizeData(dataSet);
            }
            createDimensions();

        } catch (IOException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * add items
         */
        for (int i = 0; i < nrItems; i++) {
            items.add(getNodeByIndex(i));
        }

        /**
         * create complex objects
         */
        for (int i = 0; i < nrItems; i++) {
            ComplexDataObject cdo = new ComplexDataObject(i);
            //if true means labelled, if not means not labelled yet!
            cdo.setDescription("false");
            int nAtt = getNodeByIndex(i).getValues().size();

            for (int j = 0; j < nAtt; j++) {
                cdo.add(dataHeader.get(j), getNodeByIndex(i).getValues().get(j));
            }

            complexObjList.add(cdo);

            //just adding it to original data to keep track
            originalLables.put(i, (String) cdo.getAttribute("class"));

            //just for labelling demo, it delete all labells from records
            if (DataLoadUtility.labellingDemo) {
                complexObjList.stream().forEach(cdoStream -> {
                    cdoStream.removeAttribute("class");
                    cdoStream.add("class", "unknown");
                });
            }
        }


        //init all values for dim red
        dimensionReduction(dimReductionType.TSNE);
        dimensionReduction(dimReductionType.PCA);
        dimensionReduction(dimReductionType.MDS);

        //create dimensions objects
    }

    /**
     * update categories of the dataset (new clusters), same records. Based on
     * the selected points from the user
     *
     * @param model
     */
    public void updateCategories(LabelModel model) {

        if (selectedItems.size() == items.size()
                && (!model.getName().equals("unknown") && !model.getName().equals("Unknown"))) {
            return;
        }

        //update the featureVector
        selectedItems.forEach(item -> {

            String cls = (String) complexObjList.get(item).removeAttribute("class");
            complexObjList.get(item).removeAttribute("class");
            complexObjList.get(item).add("class", model.getName());
//                classNames.add(model);
//                dataSet[dataSet.length - 2].set(item, cls + "_" + model.getName());
            //TODO for items (pacoord)
//                items.get(item).
//                System.out.println(items.get(item));
            //true means the object is labelled
            model.getItems().add(item);
            if (!model.getName().equals("unknown") && !model.getName().equals("Unknown")) {
                complexObjList.get(item).setDescription("true");
            } else {
                complexObjList.get(item).setDescription("false");
            }

        });

    }

    /**
     * remove a category and add all instances to unknown
     *
     * @param model label that should be removed
     */
    public void deleteCategory(LabelModel model) {
        //cannot remove unkown category from the alphabet
        if (model.getName().equalsIgnoreCase("unknown")) {
            return;
        }
        //
        for (ComplexDataObject cdo : complexObjList) {
            if (cdo.getAttribute("class").equals(model.getName())) {
                cdo.setDescription("false");
                cdo.removeAttribute("class");
                cdo.add("class", "unknown");
            }
        }

        ArrayList<LabelModel> tempRm = new ArrayList();
        for (LabelModel tempModel : classNames) {
            if (model.getName().equals(tempModel.getName()) && !model.getName().equalsIgnoreCase("unknown")) {
                tempRm.add(tempModel);
            }
        }
        for (LabelModel tempModel : tempRm) {
            classNames.remove(tempModel);
        }

    }

    /**
     * do dimension reduction of the current data set to 2D
     *
     * @param type of the algorithm used for dimension reduction
     * @return
     */
    public void dimensionReduction(dimReductionType type) {

        //make featureVector of the dataset
        NumericalFeatureVectorDescriptor descriptor = new NumericalFeatureVectorDescriptor();
        List<NumericalFeatureVector> featureVectors = descriptor.transform(complexObjList);

        IDimensionalityReduction<NumericalFeatureVector> dimRed = null;

        //number of dimensions at the end
        int outputDimensionality = 2;

//        dimRed = new PCA(featureVectors, outputDimensionality);
        switch (type) {
            case PCA:
                dimRed = new PCA(featureVectors, outputDimensionality);
                break;
            case TSNE:
                dimRed = new TSNE(featureVectors, outputDimensionality);
                ((TSNE) dimRed).setIterationsMax(4000);
                ((TSNE) dimRed).setPerplexity(20);
                break;
            case MDS:
                dimRed = new MDS<>(featureVectors, new com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.featureVector.EuclideanDistanceMeasure(), 2);
                ((MDS<NumericalFeatureVector>) dimRed).setMaxIterations(50);
                break;
        }

        try {
            dimRed.calculateDimensionalityReduction();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<NumericalFeatureVector, NumericalFeatureVector> highDimToLowDim = dimRed.getMapping();

//        System.out.println(highDimToLowDim.size());
        //highDimToLowDim.get(highDim).getVector() new dimensions vector
        //highDim.getVector() old dimension vector
//        XYChart.Series<Number, Number> series = new XYChart.Series();
        highDimToLowDim.keySet().forEach((highDim) -> {
//            series.getData().add(new XYChart.Data(highDimToLowDim.get(highDim).getVector()[0],
//                    highDimToLowDim.get(highDim).getVector()[1]));

            items.forEach(item -> {
                if (item.equals(highDim.getVector())) {
                    switch (type) {
                        case PCA:
                            item.getPcaValues()[0] = highDimToLowDim.get(highDim).getVector()[0];
                            item.getPcaValues()[1] = highDimToLowDim.get(highDim).getVector()[1];
                            break;
                        case TSNE:
                            item.getTsneValues()[0] = highDimToLowDim.get(highDim).getVector()[0];
                            item.getTsneValues()[1] = highDimToLowDim.get(highDim).getVector()[1];
                            break;
                        case MDS:
                            item.getMdsValues()[0] = highDimToLowDim.get(highDim).getVector()[0];
                            item.getMdsValues()[1] = highDimToLowDim.get(highDim).getVector()[1];
                            break;
                    }
                }
            });

        });

//        for (int i = 0; i < series.getData().size(); i++) {
//            System.out.println(series.getData().get(i).getXValue() + " " + series.getData().get(i).getYValue());
//        }
    }

    /**
     * calls after the user performs clustering.All unlabelled data will be
     * clustered
     *
     * @return
     */
    public ArrayList<LabelModel> updateClustersData() {
        //result of labels
        final ArrayList<LabelModel> result = new ArrayList<>();

        NumericalFeatureVectorDescriptor descriptor = new NumericalFeatureVectorDescriptor();

        // create a objectComplexList just with unlabelled data
        List<ComplexDataObject> colTemp = new ArrayList<>();
        // map the new temp col with the original one (for indexing)
        // first int is for original and second is for new temp col
        Map<Integer, Integer> mapcol = new HashMap();

        for (int i = 0; i < complexObjList.size(); i++) {
            if (complexObjList.get(i).getDescription().equals("false")) {
                colTemp.add(complexObjList.get(i));
                mapcol.put(i, colTemp.size() - 1);
            }
        }

        List<NumericalFeatureVector> featureVectors = descriptor.transform(colTemp);

        //for automatic dimension selection feature, removing all irrelevant dimesnions
        for (Dimension dimension : dimensions) {
            if (!dimension.isActiveML()) {
                for (NumericalFeatureVector fv : featureVectors) {
                    fv.removeFeature(dimension.getName());
                }
            }
        }

        INumericalClusteringAlgorithm clusteringAlgorithm = null;

        switch (MLUtility.clusterType) {
            case KMEANS:
                clusteringAlgorithm = new KMeans(SearchUtility.numClusters.getValue());
                break;
            case HIERARCHIAL:
                clusteringAlgorithm = new HierarchicalClustering(SearchUtility.numClusters.getValue());
                break;

        }

        clusteringAlgorithm.setFeatureVectors(featureVectors);

        clusteringAlgorithm.calculateClustering();

        NumericalFeatureVectorClusterResult clusteringResult = clusteringAlgorithm.getClusteringResult();

        for (int i = 0; i < clusteringResult.getClusters().size(); i++) {
            MLUtility.clusterCount++;
            clusteringResult.getClusters().get(i).setName("k-means " + MLUtility.clusterCount);
            clusteringResult.getClusters().get(i).getCentroid();
        }

        for (int j = 0; j < complexObjList.size(); j++) {
//            System.out.println(clusteringResult.getCluster(featureVectors.get(j)));
            if (complexObjList.get(j).getDescription().equals("false")) {
                complexObjList.get(j).removeAttribute("class");
                complexObjList.get(j).add("class", clusteringResult.getCluster(featureVectors.get(mapcol.get(j))).getName());
            }
//            });
        }

        for (FeatureVectorCluster<NumericalFeatureVector> cluster : clusteringResult.getClusters()) {
            LabelModel temp = new LabelModel(cluster.getName(), ChartStyleUtility.colorsString.get(classNames.size() + 1));
            addClassName(temp);
            result.add(temp);
        }
        return result;

    }

    /**
     * when the user approve the result of clustering/classification some
     * changes are need to be done
     *
     * @param model
     */
    public void updateApprovedData(LabelModel model) {
        if (model.getName().equalsIgnoreCase("unknown")) {
            return;
        }
        for (int j = 0; j < complexObjList.size(); j++) {
            if (complexObjList.get(j).getAttribute("class").equals(model.getName())) {
                complexObjList.get(j).setDescription("true");
            }
        }
    }


    /**
     * clear all the data that is not labelled from a model
     *
     * @param model
     */
    public void updateClearData(LabelModel model) {
        for (int j = 0; j < complexObjList.size(); j++) {
            if (complexObjList.get(j).getAttribute("class").equals(model.getName())
                    && complexObjList.get(j).getDescription().equals("false")) {
                complexObjList.get(j).removeAttribute("class");
                complexObjList.get(j).add("class", "unknown");
            }
        }
    }

    public ArrayList<LabelModel> getLabelModels() {
        return classNames;
    }

    /**
     * search for a label model based on name
     *
     * @param label name of the label
     * @return
     */
    public LabelModel getLabelModel(String label) {

        for (int i = 0; i < classNames.size(); i++) {
            if (classNames.get(i).getName().equals(label)) {
                return classNames.get(i);
            }
        }
        return null;
    }

    /**
     * calls after the user perform classification
     */
    public void updateClassificationData() {
        NumericalFeatureVectorDescriptor descriptor = new NumericalFeatureVectorDescriptor();
        List<NumericalFeatureVector> featureVectors = descriptor.transform(complexObjList);

        // add target variable: the "CLASSID" (1-3) the passengers traveled with.
        // by design, the class attribute (target variable) is not part of the content
        // of the NumericalFeatureVector, but is an additional attribute stored
        // in an embodied key-value pair metadata structure.
        for (int i = 0; i < complexObjList.size(); i++) {
            featureVectors.get(i).add("class", complexObjList.get(i).getAttribute("class").toString());
        }

        //for automatic dimension selection feature, removing all irrelevant dimesnions
        for (Dimension dimension : dimensions) {
            if (!dimension.isActiveML()) {
                for (NumericalFeatureVector fv : featureVectors) {
                    fv.removeFeature(dimension.getName());
                }
            }
        }

        // split into training and testing data
        List<NumericalFeatureVector> trainingVectors = new ArrayList<>();
        List<NumericalFeatureVector> testingVectors = new ArrayList<>();

        for (int i = 0; i < featureVectors.size(); i++) {

            if (complexObjList.get(i).getDescription().equals("true")) {
                trainingVectors.add(featureVectors.get(i));
                trainingVectors.get(trainingVectors.size() - 1).setName(Integer.toString(i));
            } else {
                testingVectors.add(featureVectors.get(i));
                testingVectors.get(testingVectors.size() - 1).setName(Integer.toString(i));
            }
        }

        IClassifier<NumericalFeatureVector> classifier = new RandomForest();

        // (1) train classification model
        classifier.train(trainingVectors);

        // (2) test the model
        IClassificationResult<NumericalFeatureVector> classificationResult = classifier
                .createClassificationResult(testingVectors);

//        for (NumericalFeatureVector fv
//                : classificationResult.getFeatureVectors()) {
//            System.out.println(fv.getName() + ", predicted CLASS = " + classificationResult.getClass(fv)
//                    + ", true CLASS = " + fv.getAttribute("class") + ", Probabilities: "
//                    + classificationResult.getLabelDistribution(fv));
//        }
        for (NumericalFeatureVector fv
                : classificationResult.getFeatureVectors()) {
            //check first if it is similar enough to a class
            if (classificationResult.getLabelDistribution(fv).getValueDistribution().get(classificationResult.getClass(fv))
                    > (SearchUtility.classSimilarity.getValue() / 100.00)) {
                complexObjList.get(Integer.parseInt(fv.getName())).removeAttribute("class");
                complexObjList.get(Integer.parseInt(fv.getName())).add("class", classificationResult.getClass(fv));
            }
        }

    }

    /**
     * calls after the system performs active learning
     */
    public void updateAL() {

        NumericalFeatureVectorDescriptor descriptor = new NumericalFeatureVectorDescriptor();
        List<NumericalFeatureVector> featureVectors = descriptor.transform(complexObjList);

        for (int i = 0; i < complexObjList.size(); i++) {
            featureVectors.get(i).add("class", complexObjList.get(i).getAttribute("class").toString());
        }
        // please note that in an AL process you usually have
        // 1) already labeled instances (training data)
        // 2) unlabeled instances (candidate data)
        // 3) testing data
        List<NumericalFeatureVector> trainingVectors = new ArrayList<>();
        List<NumericalFeatureVector> candidateVectors = new ArrayList<>();
//        List<NumericalFeatureVector> testingVectors = new ArrayList<>();

        for (int i = 0; i < featureVectors.size(); i++) {

            if (complexObjList.get(i).getDescription().equals("true")) {
                trainingVectors.add(featureVectors.get(i));
                trainingVectors.get(trainingVectors.size() - 1).setName(Integer.toString(i));
            } else {
                candidateVectors.add(featureVectors.get(i));
                candidateVectors.get(candidateVectors.size() - 1).setName(Integer.toString(i));
            }
        }

//        for (int i = 0; i < featureVectors.size(); i++) {
//            switch (i % 3) {
//                case 0:
//                    trainingVectors.add(featureVectors.get(i));
//                    break;
//                case 1:
//                    candidateVectors.add(featureVectors.get(i));
//                    break;
//                default:
////                    testingVectors.add(featureVectors.get(i));
//                    break;
//            }
//        }
        // Classifier - target variable is fixed to "class"
        IClassifier<NumericalFeatureVector> classifier = new RandomForest();

        // (1) train classification model
        classifier.train(trainingVectors);

//		// (2) test the model (optional)
//		IClassificationResult<NumericalFeatureVector> classificationResult = classifier
//				.createClassificationResult(testingVectors);
//
//		for (NumericalFeatureVector fv : classificationResult.getFeatureVectors()) {
//			System.out.println(fv.getName() + ", predicted CLASS = " + classificationResult.getClass(fv)
//					+ ", true CLASS = " + fv.getAttribute("class") + ", Probabilities: "
//					+ classificationResult.getLabelDistribution(fv));
//		}
        // AL PART - note that AL will never train classifiers.
        // It is a use-only relation.
        AbstractActiveLearningModel<NumericalFeatureVector> alModel = new SmallestMarginActiveLearning<NumericalFeatureVector>(
                classifier::createClassificationResult);

        alModel.setCandidates(candidateVectors);

        // assigns an interestingness score to every FV (interestingness = AL
        // applicability) - all candidates
        Map<NumericalFeatureVector, Double> candidateScores = alModel.getCandidateScores();

        // asking the AL model for most applicable FVs for the AL process.
        // Winning  FVs
        List<NumericalFeatureVector> mostApplicableFVsList = alModel.suggestCandidates(MLUtility.activeLearningNumber);

        // print the probability distributions of candidates predicted by the
        // classifier. These probability distributions have most likely been the
        // criterion for the AL model
        // to identify interesting FVs (e.g., SmallestMargin).
        IClassificationResult<NumericalFeatureVector> classificationResult = classifier
                .createClassificationResult(candidateVectors);

        // For SmallestMargin AL look at the two most likely class probabilities. the
        // winning FV has the smallest margin between best and second best in the entire
        // candidate set. That's the SmallestMargin criterion.
        mostApplicableFVsList.forEach((fv) -> {
            complexObjList.get(Integer.parseInt(fv.getName())).removeAttribute("class");
            complexObjList.get(Integer.parseInt(fv.getName())).add("class", classificationResult.getClass(fv));
        });

    }

    public void printCmplxObject() {
        ComplexDataContainer container = new ComplexDataContainer(complexObjList);
        Iterator<ComplexDataObject> iterator = container.iterator();
        while (iterator.hasNext()) {
            ComplexDataObject cdo = iterator.next();

            for (String attribute : container.getAttributeNames()) {
                System.out.print(attribute + ": ");
                System.out.print(cdo.getAttribute(attribute) + "   ");
            }
            System.out.println("");
        }
    }

    /**
     * Read header of the file
     *
     * @param in
     */
    public void readHeader(String in) {
        String[] header = in.split(delimiter);
        nrOfAttributes = header.length;

        dataHeader = new ArrayList<>();
        dataSet = new ArrayList[nrOfAttributes];
        classIndex = new ArrayList<>();

        //set category and name dimensions
        for (int i = 0; i < header.length; i++) {
            if (header[i].contains("Class") || header[i].contains("class") || header[i].contains("(c)") || header[i].contains("(C)")) {
                nrOfCatDim++;
                classIndex.add(i);
                header[i] = "class";
            } else if (header[i].contains("name") || header[i].contains("tag") || header[i].contains("Name") || header[i].contains("NAME")) {
                nameDimIndex = i;
            } else {
                numericDimensionsIndex.add(i);
            }
            this.dataHeader.add(header[i]);
            this.dataSet[i] = new ArrayList<>();
        }

        //add a class header if it does not exist
        if (classIndex.isEmpty()) {
            header = Arrays.copyOf(header, header.length + 1);
            header[header.length - 1] = "class";
            nrOfCatDim++;
            classIndex.add(header.length - 1);
            this.dataHeader.add(header[header.length - 1]);

            //add category (class) at the end of the dataset
            dataSet = Arrays.copyOf(dataSet, dataSet.length + 1);
            this.dataSet[dataSet.length - 1] = new ArrayList<>();

        }

    }

    /**
     * read data from the file and store it in dataSet
     *
     * @param in
     */
    private void readData(BufferedReader in) {
        try {
            String next;
            String values[];

            minMaxValues = new ArrayList<>();

            /**
             * initialize minMaxValues
             */
            for (int i = 0; i < dataHeader.size(); i++) {
                Double[] minMax = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
                minMaxValues.add(minMax);
            }

            while ((next = in.readLine()) != null) {
                nrItems++;
                values = next.split(delimiter);
                for (int i = 0; i < values.length; i++) {
                    if (values[i].length() > 0 && !values[i].contains("?")) {    // && values[i].matches("([0-9]*)\\\\.[0]")
                        if (isNumeric(values[i])) {
                            double value = Double.parseDouble(values[i]);
                            dataSet[i].add(value);
                            minMaxValues.get(i)[0] = (minMaxValues.get(i)[0] > value) ? value : minMaxValues.get(i)[0];     //min
                            minMaxValues.get(i)[1] = (minMaxValues.get(i)[1] < value) ? value : minMaxValues.get(i)[1];     //max

                        } else {
                            dataSet[i].add(values[i]);
                        }
                    } //for iris data set no class information!
                    else {
                        dataSet[i].add(null);
                    }
                }
                //add unknown when values are missing (especially when
                //there is no class header and we added it manually in readHeader();
                if (values.length < dataHeader.size()) {
                    for (int e = values.length; e < dataHeader.size(); e++) {
                        dataSet[e].add("unknown");
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DataModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createDimensions() {

        for (Integer dimIndex : numericDimensionsIndex) {
            Dimension temp = new Dimension(dataHeader.get(dimIndex));
            dimensions.add(temp);
            temp.setMinMax(new Double[]{minMaxValues.get(dimIndex)[0], minMaxValues.get(dimIndex)[1]});
        }
        //create dimension

    }

    /**
     * Normalize the points in data set (scale all the points to 0-1)
     *
     * @param data
     * @return
     */
    private ArrayList<Object>[] normalizeData(ArrayList<Object>[] data) {
        ArrayList<Object>[] normalizedData = new ArrayList[data.length];

        for (int i = 0; i < data.length; i++) {
            normalizedData[i] = new ArrayList<>();
            for (int j = 0; j < data[i].size(); j++) {

                if (data[i].get(j) != null) {
                    if (isNumeric(data[i].get(j).toString())) {
                        double val = (((Double) data[i].get(j)) - minMaxValues.get(i)[0]) / (minMaxValues.get(i)[1] - minMaxValues.get(i)[0]);
                        normalizedData[i].add(val);
                    } else {
                        // string
                        normalizedData[i].add(data[i].get(j));
                    }
                } else {
                    // null
                    normalizedData[i].add(data[i].get(j));
                }
            }
        }
        return normalizedData;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public List<ComplexDataObject> getComplexObjList() {
        return complexObjList;
    }

    public Map<Integer, String> getOriginalLables() {
        return originalLables;
    }

    /**
     * return color of a class (category)
     *
     * @param cls
     * @return
     */
    public String colorofClass(String cls) {

        for (LabelModel cn : classNames) {
            if (cn.getName().equals(cls)) {
                return cn.getColor();
            }
        }
        return "#FFFFFF";
    }

    /**
     * function to say if the class is visible
     *
     * @param cls
     * @return
     */
    public boolean isClassVisible(String cls) {
        for (LabelModel cn : classNames) {
            if (cn.getName().equals(cls)) {
                return cn.isVisible();
            }
        }
        return false;
    }

    /**
     * index number of a category
     *
     * @param cls
     * @return
     */
    public int indexofClass(String cls) {

        for (int i = 0; i < classNames.size(); i++) {
            if (classNames.get(i).getName().equals(cls)) {
                return i;
            }
        }
        return -1;
    }

    public int getNrOfAttributes() {
        return nrOfAttributes;
    }

    public int getNrOfCatDims() {
        return nrOfCatDim;
    }

    //labels or category names (e.g. u.s. europe japan)
    public ArrayList<Integer> getClassIndex() {
        return classIndex;
    }

    public ObservableList<Dimension> getDimensions() {
        return dimensions;
    }

    public ArrayList<String> getDataHeader() {
        return dataHeader;
    }

    public int getNameDimIndex() {
        return nameDimIndex;
    }

    public Set<String> getDimensionSelected() {
        return dimensionSelected;
    }

    public String getHeaderAt(int index) {
        return dataHeader.get(index);
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public ObservableSet<Integer> getSelectedItems() {
        return selectedItems;
    }

    public ArrayList<Double[]> getMinMaxValues() {
        return minMaxValues;
    }

    /**
     * get specific min max of a dimension
     *
     * @param DimName
     * @return
     */
    public Double[] getMinMaxValue(String DimName) {
        for (Dimension dim : dimensions) {
            if (dim.getName().equals(DimName)) {
                return dim.getMinMax();
            }
        }
        return new Double[]{-1.0, -1.0};
    }

    public void setItems(ObservableList<Item> items) {
        this.items = items;
    }

    public void setSelectedItems(ObservableSet<Integer> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public ObservableSet<Integer> getSelectedItemsIndividual() {
        return selectedItemsIndividual;
    }

    public void setSelectedItemsIndividual(ObservableSet<Integer> selectedItemsIndividual) {
        this.selectedItemsIndividual = selectedItemsIndividual;
    }

    public int getNrItems() {
        return nrItems;
    }

    public int getHeaderIndex(String name) {
        for (int i = 0; i < dataHeader.size(); i++) {
            if (name.equals(dataHeader.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Object>[] getDataSet() {
        return dataSet;
    }

    /**
     * return a copy of class names
     *
     * @return
     */
    public ArrayList<String> getCopyClassNames() {
        ArrayList<String> result = new ArrayList<>();
        for (LabelModel label : classNames) {
            result.add(label.getName());
        }
        return result;
    }

    /**
     * return original classNames
     *
     * @return
     */
    public ArrayList<LabelModel> getClassNames() {
        return classNames;
    }

    /**
     * add class to classNames
     */
    public void addClassName(LabelModel model) {
        classNames.add(model);
    }

    /**
     * initialize all class names and create models based on that
     */
    public void initClassNames() {
//        ArrayList<LabelModel> labels = new ArrayList<>();
        ArrayList<String> labelNames = new ArrayList<>();

        //initiate unknown label
        addClassName(new LabelModel("unknown", ChartStyleUtility.colorsString.get(0)));
        labelNames.add("unknown");

        int counter = 0;
        counter++;

        for (LabelModel label : getClassNames()) {
            labelNames.add(label.getName());
        }

        ArrayList<Object>[] data = getDataSet();
        int classIndex = getClassIndex().get(0);      // 0 -> only 1 class label

        for (int i = 0; i < data[classIndex].size(); i++) {

            if (!labelNames.contains(data[classIndex].get(i))) {
                LabelModel label = new LabelModel(String.valueOf(data[classIndex].get(i)),
                        ChartStyleUtility.colorsString.get(counter));
                addClassName(label);
                labelNames.add(String.valueOf(data[classIndex].get(i)));
                counter++;
            }
        }

    }


    /**
     * this is a temporary class generation for digit dataset
     * should be removed afterward
     */
    /*
    public void initClassNamesDigitDataset() {
        ArrayList<LabelModel> labels = new ArrayList<>();
//        ArrayList<String> labelNames = new ArrayList<>();

        //initiate unknown label
        int counter = 0;

        //initiate unknown label
        labels.add(new LabelModel("unknown", ChartStyleUtility.colorsString.get(0)));
        counter++;
        for (int i = 0; i < 10; i++) {
            labels.add(new LabelModel(String.valueOf(i + 1), ChartStyleUtility.colorsString.get(i + 1)));
//            labelNames.add(String.valueOf(i + 1));
            counter++;
        }

//        for (LabelModel label : labels) {
//            labelNames.add(label.getName());
//        }

        ArrayList<Object>[] data = getDataSet();
        int classIndex = getClassIndex().get(0);      // 0 -> only 1 class label

//        for (int i = 0; i < data[classIndex].size(); i++) {
//
//            if (!labelNames.contains(data[classIndex].get(i))) {
//                LabelModel label = new LabelModel(String.valueOf(data[classIndex].get(i)),
//                        ChartStyleUtility.colorsString.get(counter));
//                labels.add(label);
//                labelNames.add(String.valueOf(data[classIndex].get(i)));
//                counter++;
//            }
//        }

        setClassNames(labels);
    }
     */
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * this is a function to perform classification after user clicks on one single
     * record (it's for demo labelling version, not useful for normal usecase)
     */
    public void singleItemClickHandeling(int item) {
        //label one single data point (record)
        if (DataLoadUtility.labellingDemo) {
            getSelectedItems().clear();
            getSelectedItems().add(item);
            LabelModel model = getLabelModel(
                    (String) SearchUtility.dataModel.getOriginalLables().get(item)
            );
            updateCategories(model);
            getSelectedItems().clear();
            BrushMngUtility.highLightRecordTemp(false,
                    getItembyIndex(item));

            //do classification after each selection
            updateClassificationData();
        }
    }

    public void printHeader() {
        System.out.print("Header: ");
        getDataHeader().stream().forEach(header -> {
            System.out.print(header + " ");
        });
    }

    public void printDataSet() {
        System.out.print("Data Set: ");

        int size = getDataSet().length;
        for (int i = 0; i < size; i++) {
            {
                getDataSet()[i].stream().forEach(data -> {
                    System.out.print(data + "   ");
                });
            }
            System.out.println();
        }
    }

    /**
     * return an item based on the index
     *
     * @param index
     * @return
     */
    public Item getItembyIndex(Integer index) {
        for (int i = 0; i < items.size(); i++) {
            if (index == items.get(i).getIndex()) {
                return items.get(i);
            }
        }
        return null;
    }

    /**
     * get a node by it's index, including categories IMPORTANT: this method
     * just return a COPY not original from Items list
     *
     * @param index index of the node
     * @return ArrayList of attributes
     */
    public Item getNodeByIndex(int index) {

        int size = getDataSet().length;

        ObservableList<Object> result = FXCollections.observableArrayList();
        for (int i = 0; i < size; i++) {
            result.add(getDataSet()[i].get(index));
        }
        //get name of data
        String tempName = "N/A";
        if (nameDimIndex != -1) {
            tempName = (String) getDataSet()[this.nameDimIndex].get(index);
        }

        return new Item(nrOfAttributes, nrOfCatDim, result, tempName, index);

    }

    /**
     * get a node by it's index, without categories IMPORTANT: this method just
     * return a COPY not original from Items list
     *
     * @param index
     * @return
     */
    public Item getNodeWithoutCategory(int index) {

        ObservableList<Object> result = FXCollections.observableArrayList();
        for (int i = 0; i < getNumberAttributes(); i++) {
            result.add(getDataSet()[i].get(index));
        }
        //get name of data
        String tempName = "N/A";
        if (nameDimIndex != -1) {
            tempName = (String) getDataSet()[this.nameDimIndex].get(index);
        }
        return new Item(nrOfAttributes, nrOfCatDim, result, tempName, index);
    }

    /**
     * @return number of dataSet attributes (without category dimension)
     */
    public int getNumberAttributes() {
        if (nameDimIndex == -1) {
            return this.getDataHeader().size() - this.getNrOfCatDims();
        } else {
            return this.getDataHeader().size() - this.getNrOfCatDims() - 1;
        }
    }

    /**
     * find if there is any item in list of items that have the same feature
     * vector as the input
     *
     * @param fv
     * @return index of the item in list of items
     */
    public int findEqualItem(double[] fv) {

        for (int i = 0; i < fv.length; i++) {
            for (Item item : items) {
                if (item.equals(fv[i])) {
                    return item.getIndex();
                }
            }
        }
        return -1;
    }

    /**
     * @param label name of the partition (cluster) to return the centroid for
     * @return the centroid of the cluster, based on the DR technique with extra
     * value which is the label name in string format
     */
    public Data<Number, Number> centroidValue(String label) {

        double x = 0.0;
        double y = 0.0;
        int numberOfCluster = 0;

        int size = complexObjList.size();
        for (int i = 0; i < size; i++) {
            if (complexObjList.get(i).getAttribute("class").equals(label)) {
                switch (MLUtility.dimeRedType) {
                    case PCA:
                        x = x + items.get(i).getPcaValues()[0];
                        y = y + items.get(i).getPcaValues()[1];
                        break;
                    case TSNE:
                        x = x + items.get(i).getTsneValues()[0];
                        y = y + items.get(i).getTsneValues()[1];
                        break;
                    case MDS:
                        x = x + items.get(i).getMdsValues()[0];
                        y = y + items.get(i).getMdsValues()[1];
                        break;
                }
                numberOfCluster++;
            }
        }
        if (numberOfCluster == 0) {
            return null;
        }
        x = x / numberOfCluster;
        y = y / numberOfCluster;
        Data<Number, Number> result = new Data<>(x, y);
        result.setExtraValue(label);
        return result;

    }

    public int clusterSize(String label) {
        int size = complexObjList.size();
        int numberOfCluster = 0;
        for (int i = 0; i < size; i++) {
            if (complexObjList.get(i).getAttribute("class").equals(label)) {
                numberOfCluster++;
            }
        }
        return numberOfCluster;
    }

    /**
     * @param label
     * @return ratio of a cluster in the total number (between 3 to 10)
     */
    public double ratioofCluster(String label) {
        int size = complexObjList.size();
        int numberOfCluster = 0;
        for (int i = 0; i < size; i++) {
            if (complexObjList.get(i).getAttribute("class").equals(label)) {
                numberOfCluster++;
            }
        }
        double percent = (1.0 * numberOfCluster) / size;
        percent = (percent * 10) + 3;
        return percent;
    }

    /**
     * Calculate if there is any intersection between two set of labels
     *
     * @param first  label
     * @param second label
     * @return the list of dimensions they share
     */
    public Set<String> intersectionLabelsDimnsions(LabelModel first, LabelModel second) {

        Set<String> intersection = new HashSet(first.getDimeInteraction());
        intersection.retainAll(second.getDimeInteraction());

        return intersection;

    }

    /**
     * @return specific dimension that the name is, null if it doesn't exist
     */
    public Dimension getDimensionByName(String name) {
        for (Dimension dim : dimensions) {
            if (dim.getName().equals(name)) {
                return dim;
            }
        }
        return null;
    }

}
