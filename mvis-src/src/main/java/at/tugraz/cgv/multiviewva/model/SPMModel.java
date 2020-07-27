/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mchegini
 */
public class SPMModel {

    private HashMap<Integer, String> combinations = new HashMap<>();
    private String parentPath = new String();
    private DataModel model;
    private ArrayList<String> header;
    private ArrayList<Pattern> patternList = new ArrayList<>();
    private String filename;

    public void setFilename(String filename) {
        this.filename = filename;
    }
    

    public String getFilename() {
        return filename;
    }

    public SPMModel(HashMap<Integer, String> combinations, String parentPath, DataModel model, ArrayList<String> header, ArrayList<Pattern> patternList, String filename) {
        this.combinations = combinations;
        this.parentPath = parentPath;
        this.model = model;
        this.header = header;
        this.patternList = patternList;
        this.filename = filename;
    }

    public void setCombinations(HashMap<Integer, String> combinations) {
        this.combinations = combinations;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public void setHeader(ArrayList<String> header) {
        this.header = header;
    }

    public void setPatternList(ArrayList<Pattern> patternList) {
        this.patternList = patternList;
    }


    public HashMap<Integer, String> getCombinations() {
        return combinations;
    }

    public String getParentPath() {
        return parentPath;
    }

    public DataModel getModel() {
        return model;
    }

    public ArrayList<String> getHeader() {
        return header;
    }

    public ArrayList<Pattern> getPatternList() {
        return patternList;
    }

}
