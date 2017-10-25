package com.example.macbook.box2homeclient.models;

/**
 * Created by macbook on 20/10/2017.
 */

public class Order {

    private String dateCreation;
    private String heureCourse;
    private String coursePaid;
    private String montantMax;
    private String statut;
    private String depart;
    private String arrivee;
    private String nomClient;
    private String prenomClient;
    private String telClient;
    private String photoClient;

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getHeureCourse() {
        return heureCourse;
    }

    public void setHeureCourse(String heureCourse) {
        this.heureCourse = heureCourse;
    }

    public String getCoursePaid() {
        return coursePaid;
    }

    public void setCoursePaid(String coursePaid) {
        this.coursePaid = coursePaid;
    }

    public String getMontantMax() {
        return montantMax;
    }

    public void setMontantMax(String montantMax) {
        this.montantMax = montantMax;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getTelClient() {
        return telClient;
    }

    public void setTelClient(String telClient) {
        this.telClient = telClient;
    }

    public String getPhotoClient() {
        return photoClient;
    }

    public void setPhotoClient(String photoClient) {
        this.photoClient = photoClient;
    }
}
