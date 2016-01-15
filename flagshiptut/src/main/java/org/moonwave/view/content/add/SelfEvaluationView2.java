package org.moonwave.view.content.add;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * SelfEvaluation View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class SelfEvaluationView2 {

    private boolean evalPart1_1s; // student
    private boolean evalPart1_1t; // tutor
    private boolean evalPart1_2s;
    private boolean evalPart1_2t;
    private boolean evalPart1_3s;
    private boolean evalPart1_3t;
    private String evalPart1_comments;

    private boolean evalPart2_1s;
    private boolean evalPart2_1t;
    private boolean evalPart2_2s;
    private boolean evalPart2_2t;
    private boolean evalPart2_3s;
    private boolean evalPart2_3t;
    private String evalPart2_comments;

    private boolean evalPart3_1s;
    private boolean evalPart3_1t;
    private boolean evalPart3_2s;
    private boolean evalPart3_2t;
    private boolean evalPart3_3s;
    private boolean evalPart3_3t;
    private String evalPart3_comments;

    private String[] selectedConsoles;
    private String[] selectedCities; 
    private List<String> cities;

    @PostConstruct
    public void init() {
        cities = new ArrayList<String>();
        cities.add("San Francisco");
        cities.add("London");
        cities.add("Paris");
        cities.add("Istanbul");
        cities.add("Berlin");
        cities.add("Barcelona");
        cities.add("Rome");
        cities.add("Sao Paulo");
        cities.add("Amsterdam");
    }

    public String[] getSelectedConsoles() {
        return selectedConsoles;
    }

    public void setSelectedConsoles(String[] selectedConsoles) {
        this.selectedConsoles = selectedConsoles;
    }

    public String[] getSelectedCities() {
        return selectedCities;
    }

    public void setSelectedCities(String[] selectedCities) {
        this.selectedCities = selectedCities;
    }

    public List<String> getCities() {
        return cities;
    }

    public boolean isEvalPart1_1s() {
        return evalPart1_1s;
    }

    public void setEvalPart1_1s(boolean evalPart1_1s) {
        this.evalPart1_1s = evalPart1_1s;
    }

    public boolean isEvalPart1_1t() {
        return evalPart1_1t;
    }

    public void setEvalPart1_1t(boolean evalPart1_1t) {
        this.evalPart1_1t = evalPart1_1t;
    }

    public boolean isEvalPart1_2s() {
        return evalPart1_2s;
    }

    public void setEvalPart1_2s(boolean evalPart1_2s) {
        this.evalPart1_2s = evalPart1_2s;
    }

    public boolean isEvalPart1_2t() {
        return evalPart1_2t;
    }

    public void setEvalPart1_2t(boolean evalPart1_2t) {
        this.evalPart1_2t = evalPart1_2t;
    }

    public boolean isEvalPart1_3s() {
        return evalPart1_3s;
    }

    public void setEvalPart1_3s(boolean evalPart1_3s) {
        this.evalPart1_3s = evalPart1_3s;
    }

    public boolean isEvalPart1_3t() {
        return evalPart1_3t;
    }

    public void setEvalPart1_3t(boolean evalPart1_3t) {
        this.evalPart1_3t = evalPart1_3t;
    }

    public boolean isEvalPart2_1s() {
        return evalPart2_1s;
    }

    public void setEvalPart2_1s(boolean evalPart2_1s) {
        this.evalPart2_1s = evalPart2_1s;
    }

    public boolean isEvalPart2_1t() {
        return evalPart2_1t;
    }

    public void setEvalPart2_1t(boolean evalPart2_1t) {
        this.evalPart2_1t = evalPart2_1t;
    }

    public boolean isEvalPart2_2s() {
        return evalPart2_2s;
    }

    public void setEvalPart2_2s(boolean evalPart2_2s) {
        this.evalPart2_2s = evalPart2_2s;
    }

    public boolean isEvalPart2_2t() {
        return evalPart2_2t;
    }

    public void setEvalPart2_2t(boolean evalPart2_2t) {
        this.evalPart2_2t = evalPart2_2t;
    }

    public boolean isEvalPart2_3s() {
        return evalPart2_3s;
    }

    public void setEvalPart2_3s(boolean evalPart2_3s) {
        this.evalPart2_3s = evalPart2_3s;
    }

    public boolean isEvalPart2_3t() {
        return evalPart2_3t;
    }

    public void setEvalPart2_3t(boolean evalPart2_3t) {
        this.evalPart2_3t = evalPart2_3t;
    }

    public boolean isEvalPart3_1s() {
        return evalPart3_1s;
    }

    public void setEvalPart3_1s(boolean evalPart3_1s) {
        this.evalPart3_1s = evalPart3_1s;
    }

    public boolean isEvalPart3_1t() {
        return evalPart3_1t;
    }

    public void setEvalPart3_1t(boolean evalPart3_1t) {
        this.evalPart3_1t = evalPart3_1t;
    }

    public boolean isEvalPart3_2s() {
        return evalPart3_2s;
    }

    public void setEvalPart3_2s(boolean evalPart3_2s) {
        this.evalPart3_2s = evalPart3_2s;
    }

    public boolean isEvalPart3_2t() {
        return evalPart3_2t;
    }

    public void setEvalPart3_2t(boolean evalPart3_2t) {
        this.evalPart3_2t = evalPart3_2t;
    }

    public boolean isEvalPart3_3s() {
        return evalPart3_3s;
    }

    public void setEvalPart3_3s(boolean evalPart3_3s) {
        this.evalPart3_3s = evalPart3_3s;
    }

    public boolean isEvalPart3_3t() {
        return evalPart3_3t;
    }

    public void setEvalPart3_3t(boolean evalPart3_3t) {
        this.evalPart3_3t = evalPart3_3t;
    }

    public String getEvalPart1_comments() {
        return evalPart1_comments;
    }

    public void setEvalPart1_comments(String evalPart1_comments) {
        this.evalPart1_comments = evalPart1_comments;
    }

    public String getEvalPart2_comments() {
        return evalPart2_comments;
    }

    public void setEvalPart2_comments(String evalPart2_comments) {
        this.evalPart2_comments = evalPart2_comments;
    }

    public String getEvalPart3_comments() {
        return evalPart3_comments;
    }

    public void setEvalPart3_comments(String evalPart3_comments) {
        this.evalPart3_comments = evalPart3_comments;
    }

    public String save() {
    	return "";
    }
    
}
