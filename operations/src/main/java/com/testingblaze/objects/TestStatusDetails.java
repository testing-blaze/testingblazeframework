package com.testingblaze.objects;

public class TestStatusDetails {


    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
    private String status;

    public String getTag() {
        return tag;
    }

    private String tag;
    private String details;
    public TestStatusDetails(String status, String details, String tag){
        this.status=status;
        this.details=details;
        this.tag = tag;
    }
}
