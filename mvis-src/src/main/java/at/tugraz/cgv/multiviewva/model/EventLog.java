/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;

/**
 * A class to record all events happening in the software
 * @author mohammad
 */
public class EventLog {

    /**
     * start time of the event
     */
    private int startTime;
    
    /**
     * end time of the event
     */
    private int endTime;
    
    /**
     * duration of the event
     */
    private int duration;
    
    /**
     * saving the event itself, can be TouchEvent, MouseEvent etc.
     */
    private Event event;

    /**
     * type of action that happened
     */
    enum TypeOfAction {
        NA,
        selectSP,
        over,

    }

    /**
     * type of Modality, like eye-tracking, mouse etc.
     */
    enum Modality {
        NA,
        gaze,
        mouse,
        touch,
        keyboard,
        speech
    }
    
    /**
     * what kind of object in affected by the action
     */
    enum AffectedObjectType{
        NA,
        attribute,
        record,
        view,
    }
    
    /**
     * In which view the action is happened
     */
    enum View{
        NA,
        scatterplot,
        SPM,
        paco,
        table,
    }

    private View view = View.NA;
    private AffectedObjectType affectedObjectType = AffectedObjectType.NA;
    private TypeOfAction typeofAction = TypeOfAction.NA;
    private Modality modality = Modality.NA;
    
    /**
     * all the attributes affected by the action
     */
    private ObservableList<String> attributesAffected = FXCollections.observableArrayList();
    
    /**
     * all records affected by the action
     */
    private ObservableList<Integer> recordsAffected = FXCollections.observableArrayList();

    public EventLog() {

    }

    /**
     * constructing the event log, giving the information about the event
     * @param startTime
     * @param endTime
     * @param event
     * @param attributesAffected
     * @param recordsAffected
     * @param action
     * @param modal
     * @param view
     * @param affectedObjectType 
     */
    public EventLog(int startTime, int endTime, Event event,
            ObservableList<String> attributesAffected, ObservableList<Integer> recordsAffected,
            TypeOfAction action, Modality modal, View view, AffectedObjectType affectedObjectType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
        this.duration = this.endTime - this.startTime;
        this.attributesAffected = attributesAffected;
        this.recordsAffected = recordsAffected;
        this.typeofAction = action;
        this.modality = modal;
        this.view = view;
        this.affectedObjectType = affectedObjectType;
    }

}
