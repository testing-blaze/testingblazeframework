package com.testingblaze.report;

import com.testingblaze.enums.GenerateReportAnalytics;

import java.io.IOException;

public class RunReportAnalyzer {
    private static ReportAnalyzer reportAnalyzer;
    /**
     * publish or generate Report Analytics
     *
     * @param localOrPublish select using GenerateReportAnalytics
     * @author nauman.shahid
     */
    public static void performAnalysis(GenerateReportAnalytics localOrPublish) {
        if (reportAnalyzer == null) {
            reportAnalyzer = new ReportAnalyzer();
        }
        if (GenerateReportAnalytics.valueOf("onLocal").equals(localOrPublish)) {
            try {
                reportAnalyzer.executeAnalysis();
            } catch (IOException e) {
                System.out.println("Report not generated");
                e.printStackTrace();
            }
        } else if (GenerateReportAnalytics.valueOf("publishOnPortal").equals(localOrPublish)) {
            System.setProperty("publishReport","yes");
            try {
                reportAnalyzer.publishReportAnalytics();
                ReportAnalyzer.isReportPublished=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
