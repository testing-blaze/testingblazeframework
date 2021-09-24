package com.testingblaze.report;

public class RunReportAnalyzer {
    private static ReportAnalyzer reportAnalyzer;

    public static ReportAnalyzer perform(){
        if(reportAnalyzer == null){
            reportAnalyzer = new ReportAnalyzer();
        }
        return reportAnalyzer;
    }
}
