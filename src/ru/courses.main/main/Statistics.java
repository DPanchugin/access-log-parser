package ru.courses.main.main;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

public class Statistics {
    private int totalTraffic = 0;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> allPages = new HashSet<>();
    private HashMap<String, Integer> occurrenceOs = new HashMap<>();
    private HashMap<String, Double> fractionOs = new HashMap<>();
    public Statistics() {
    }

    public int getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public void addEntry(LogEntry logEntry) {

        this.totalTraffic += logEntry.getResponseSize() / 1024; // переводим в Кб

        if (minTime == null) {
            this.minTime = logEntry.getTime();
        } else {
            if (minTime.isAfter(logEntry.getTime())) {
                this.minTime = logEntry.getTime();
            }
        }

        if (maxTime == null) {
            this.maxTime = logEntry.getTime();
        } else {
            if (maxTime.isBefore(logEntry.getTime())) {
                this.maxTime = logEntry.getTime();
            }
            if (logEntry.getResponseCode() == 200) {
                this.allPages.add(logEntry.getPathUrl());
            }
            if (!(logEntry.getUserAgent().getTypeOs().equals("-"))) {
                if (occurrenceOs.containsKey(logEntry.getUserAgent().getTypeOs())) {
                    occurrenceOs.put(logEntry.getUserAgent().getTypeOs(), occurrenceOs.get(logEntry.getUserAgent().getTypeOs()) + 1);
                } else occurrenceOs.put(String.valueOf(logEntry.getUserAgent().getTypeOs()), 1);
            }
        }

    }
    public int getTrafficRate() {
        int hour = getAmountHours();
        return totalTraffic / hour;
    }
    private int getAmountHours() {
        int hour = 0;
        if (maxTime.getMonth().getValue() > minTime.getMonth().getValue()) {
            int countDaysMonthMin = YearMonth.of(minTime.getYear(), minTime.getMonth().getValue()).lengthOfMonth();
            int countFullDays = countDaysMonthMin - minTime.getDayOfMonth() + maxTime.getDayOfMonth() - 2; // дни полные
            hour += countFullDays * 24;
            hour += 24 - minTime.getHour() + maxTime.getHour();
        } else if (maxTime.getDayOfMonth() > minTime.getDayOfMonth()) {
            int diffDay = maxTime.getDayOfMonth() - minTime.getDayOfMonth();
            if (diffDay == 1) {
                hour += 24 - minTime.getHour() + maxTime.getHour();
            }
            if (diffDay > 1) {
                hour += 24 - minTime.getHour() + maxTime.getHour() + 24 * (diffDay - 1);
            }
        } else {
            if (maxTime.getHour() - minTime.getHour() == 0) {
                hour += 1;
            } else hour += maxTime.getHour() - minTime.getHour();
        }
        return hour;
    }
    public ArrayList<String> getAllExistPages() {
        List<String> pages = new ArrayList<>();
        for (String page : allPages) {
            pages.add(page);
        }
        return new ArrayList<>(pages);
    }
    public HashMap<String, Double> getStatisticsOs() {
        Integer countRequest = 0;
        for (Map.Entry<String, Integer> entry : occurrenceOs.entrySet()) {
            countRequest += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : occurrenceOs.entrySet()) {
            fractionOs.put(entry.getKey(), entry.getValue().doubleValue() / countRequest.doubleValue());
        }
        return fractionOs;
    }
}
