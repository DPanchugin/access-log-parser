package ru.courses.main.main;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static javax.swing.UIManager.put;

public class Statistics {
    private int countVisits = 0;
    private int countErrors = 0;
    private int totalTraffic = 0;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> ipRealUsers = new HashSet<>();
    private HashSet<String> allPages = new HashSet<>();
    private HashMap<String, Integer> occurrenceOs = new HashMap<>();

    private HashSet<String> noExistPages = new HashSet<>();
    private HashMap<String, Integer> occurrenceBrow = new HashMap<>();
    private HashMap<String, Double> fractionOs = new HashMap<>();
    private HashMap<String, Double> fractionBrow = new HashMap<>();
    private HashSet<String> domains = new HashSet<>();
    private HashMap<String, Integer> countVisitsRealUsers = new HashMap<>();
    private HashMap<Integer, Integer> countRequestPerSec = new HashMap<>();

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
                this.allPages.add(logEntry.getPath());
            }
            if (logEntry.getResponseCode() == 404) {
                this.noExistPages.add(logEntry.getPath());
            }
            if (!(logEntry.getUserAgent().getTypeOs().equals("-"))) {
                if (occurrenceOs.containsKey(logEntry.getUserAgent().getTypeOs())) {
                    occurrenceOs.put(logEntry.getUserAgent().getTypeOs(), occurrenceOs.get(logEntry.getUserAgent().getTypeOs()) + 1);
                } else occurrenceOs.put(String.valueOf(logEntry.getUserAgent().getTypeOs()), 1);
            }
            if (!(logEntry.getUserAgent().getTypeBrowser().equals("-"))) {
                if (occurrenceBrow.containsKey(logEntry.getUserAgent().getTypeBrowser())) {
                    occurrenceBrow.put(logEntry.getUserAgent().getTypeBrowser(), occurrenceBrow.get(logEntry.getUserAgent().getTypeBrowser()) + 1);
                } else occurrenceBrow.put(String.valueOf(logEntry.getUserAgent().getTypeBrowser()), 1);
            }
            if (!logEntry.getUserAgent().isBot()) {
                countVisits++;
                this.ipRealUsers.add(logEntry.getIpAddr());
            }

            ArrayList<String> uniqueCodeSec = new ArrayList<>(Arrays.asList(
                    Integer.toString(logEntry.getTime().getMonth().getValue()),
                    Integer.toString(logEntry.getTime().getDayOfMonth()),
                    Integer.toString(logEntry.getTime().getHour()),
                    Integer.toString(logEntry.getTime().getMinute()),
                    Integer.toString(logEntry.getTime().getSecond())
            ));
            Integer secCode = Integer.parseInt(uniqueCodeSec.stream().collect(Collectors.joining()));
            if (countRequestPerSec.containsKey(secCode)) {
                this.countRequestPerSec.put(secCode, countRequestPerSec.get(secCode) + 1);
            } else this.countRequestPerSec.put(secCode, 1);

            if (countVisitsRealUsers.containsKey(logEntry.getIpAddr())) {
                countVisitsRealUsers.put(logEntry.getIpAddr(), countVisitsRealUsers.get(logEntry.getIpAddr()) + 1);
            } else countVisitsRealUsers.put(logEntry.getIpAddr(), 1);
        }

        if (Integer.toString(logEntry.getResponseCode()).charAt(0) == '4' || Integer.toString(logEntry.getResponseCode()).charAt(0) == '5') {
            countErrors++;
        }

        if (!logEntry.getReferer().equals("-") && logEntry.getReferer().contains("https://")) {
            String referer = logEntry.getReferer().replaceAll("https://", "");
            this.domains.add(referer.substring(0, referer.indexOf('/')));
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
        List<String> pages = new ArrayList<>(allPages);
        return new ArrayList<>(pages);
    }

    public HashMap<String, Double> getStatisticsOs() {
        return getStatisticsHashMap(occurrenceOs, fractionOs);
    }

    public HashMap<String, Double> getStatisticsBrow() {
        return getStatisticsHashMap(occurrenceBrow, fractionBrow);
    }

    private HashMap<String, Double> getStatisticsHashMap(HashMap<String, Integer> occurrence, HashMap<String, Double> fraction) {
        Integer countRequest = 0;
        for (Integer value : occurrence.values()) {
            countRequest += value;
        }
        for (Map.Entry<String, Integer> entry : occurrence.entrySet()) {
            double fractionValue = entry.getValue().doubleValue() / countRequest.doubleValue();
            fractionValue = Math.round(fractionValue * 1000.0) / 1000.0;
            fraction.put(entry.getKey(), fractionValue);
        }
        return fraction;
    }


    public ArrayList<String> getNoExistPages() {
        List<String> pages = new ArrayList<>(noExistPages);
        return new ArrayList<>(pages);
    }

    public int getAvgCountVisits() {
        int hour = getAmountHours();
        return countVisits / hour;
    }

    public int getAvgCountErrors() {
        int hour = getAmountHours();
        return countErrors / hour;
    }

    public int getAvgVisitsOneUser() {
        return countVisits / ipRealUsers.size();
    }

    public int getMaxCountRequestPerSec() {
        return countRequestPerSec.values().stream().mapToInt(sec -> sec).max().orElse(0);
    }

    public ArrayList<String> getWebsiteWithLinksCurrentSite() {
        List<String> pages = new ArrayList<>(domains);
        return new ArrayList<>(pages);
    }

    public int getMaxCountVisitsRealUser() {
        return countVisitsRealUsers.values().stream().mapToInt(countRequests -> countRequests).max().orElse(0);
    }

}


