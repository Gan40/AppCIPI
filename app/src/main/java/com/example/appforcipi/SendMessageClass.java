package com.example.appforcipi;

import java.util.ArrayList;

public class SendMessageClass  {
    private String provider;
    private ArrayList<String> siteHashCodeArray;
    private ArrayList<Integer> statusArray;
    private ArrayList<Long> timestampArray;

    public SendMessageClass(String provider, ArrayList<String> siteHashCodeArray, ArrayList<Integer> statusArray, ArrayList<Long> timestampArray) {
        this.provider = provider;
        this.siteHashCodeArray = siteHashCodeArray;
        this.statusArray = statusArray;
        this.timestampArray = timestampArray;
    }

    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public ArrayList<String> getSiteHashCodeArray() {
        return siteHashCodeArray;
    }
    public void setSiteHashCodeArray(ArrayList<String> siteHashCodeArray) {
        this.siteHashCodeArray = siteHashCodeArray;
    }
    public ArrayList<Integer> getStatusArray() {
        return statusArray;
    }
    public void setStatusArray(ArrayList<Integer> statusArray) {
        this.statusArray = statusArray;
    }
    public ArrayList<Long> getTimestampArray() {
        return timestampArray;
    }
    public void setTimestampArray(ArrayList<Long> timestampArray) {
        this.timestampArray = timestampArray;
    }

    @Override
    public String toString() throws NullPointerException {
        StringBuilder out = new StringBuilder();
        out.append("{");

        out.append("\"provider\": \"").append(getProvider()).append("\",");
        out.append("\"sites\": {");
        for (int i = 0; i < siteHashCodeArray.size(); i++){
            out.append("\"").append(siteHashCodeArray.get(i)).append("\": {");
            out.append("\"statusCode\": ").append(statusArray.get(i)).append(",");
            out.append("\"timestamp\": \"").append(timestampArray.get(i)).append("\"");
            if(i != siteHashCodeArray.size()-1)
                out.append("},");
            else
                out.append("}");
        }
        out.append("}");

        out.append("}");
        return out.toString().replaceAll("\\\\","");
    }
}

