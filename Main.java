import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;

    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    static int[][][] profit = new int[MONTHS][DAYS][COMMS];

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                for (int c = 0; c < COMMS; c++) {
                    profit[m][d][c] = 0;
                }
            }
        }

        for (int m = 0; m < MONTHS; m++) {
            String filePath = "Data_Files" + File.separator + months[m] + ".txt";

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(filePath));
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length != 3) continue;

                    int day;
                    int p;
                    String comm;

                    try {
                        day = Integer.parseInt(parts[0].trim());
                        comm = parts[1].trim();
                        p = Integer.parseInt(parts[2].trim());
                    } catch (Exception ignore) {
                        continue;
                    }

                    if (day < 1 || day > DAYS) continue;

                    int cIndex = -1;
                    for (int c = 0; c < COMMS; c++) {
                        if (commodities[c].equals(comm)) {
                            cIndex = c;
                            break;
                        }
                    }
                    if (cIndex == -1) continue;

                    profit[m][day - 1][cIndex] = p;
                }
            } catch (Exception ignore) {
            } finally {
                if (br != null) {
                    try { br.close(); } catch (Exception ignore) {}
                }
            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========
    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 1 || month > 12) return "INVALID_MONTH";
        int monthIdx = month - 1;

        String mostProfit = commodities[0];
        int bestprofit = -9999;

        for (int c = 0; c < COMMS; c++) {
            int totalProfit = 0;

            for (int d = 0; d < DAYS; d++) {
                totalProfit = totalProfit + profit[monthIdx][d][c];
            }

            if (bestprofit < totalProfit) {
                bestprofit = totalProfit;
                mostProfit = commodities[c];
            }
        }

        return mostProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 1 || month > 12) return -99999;
        if (day < 1 || day > 28) return -99999;

        int monthIdx = month - 1;
        int dayIdx = day - 1;

        int totalProfit = 0;
        for (int c = 0; c < COMMS; c++) {
            totalProfit = totalProfit + profit[monthIdx][dayIdx][c];
        }

        return totalProfit;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        if (from < 1 || to < 1 || from > 28 || to > 28 || from > to) return -99999;

        int profitSum = 0;

        int commInt = -1;
        for (int c = 0; c < COMMS; c++) {
            if (commodities[c].equals(commodity)) { commInt = c; break; }
        }
        if (commInt == -1) return -99999;

        int fromIdx = from - 1;
        int toIdx = to - 1;

        for (int m = 0; m < MONTHS; m++) {
            for (int x = fromIdx; x <= toIdx; x++) {
                profitSum = profitSum + profit[m][x][commInt];
            }
        }

        return profitSum;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 1 || month > 12) return -1;
        int monthIdx = month - 1;

        int bestday = 0;
        int bestTotal = -999999;

        for (int d = 0; d < DAYS; d++) {
            int dayTotal = 0;

            for (int c = 0; c < COMMS; c++) {
                dayTotal = dayTotal + profit[monthIdx][d][c];
            }

            if (dayTotal > bestTotal) {
                bestTotal = dayTotal;
                bestday = d;
            }
        }

        return bestday + 1;
    }

    public static String bestMonthForCommodity(String comm) {
        int bestProfit = -99999;
        int bestMonth = 0;
        int commInt = -1;

        for (int c = 0; c < COMMS; c++) {
            if (commodities[c].equals(comm)) { commInt = c; break; }
        }
        if (commInt == -1) return "INVALID_COMMODITY";

        for (int m = 0; m < MONTHS; m++) {
            int totalProfit = 0;

            for (int d = 0; d < DAYS; d++) {
                totalProfit = totalProfit + profit[m][d][commInt];
            }

            if (bestProfit < totalProfit) {
                bestMonth = m;
                bestProfit = totalProfit;
            }
        }

        return months[bestMonth];
    }

    public static int consecutiveLossDays(String comm) {
        int commInt = -1;
        int longestStreak = 0;
        int totalStreak = 0;

        for (int c = 0; c < COMMS; c++) {
            if (commodities[c].equals(comm)) { commInt = c; break; }
        }
        if (commInt == -1) return -1;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                int dailyprofit = profit[m][d][commInt];
                if (dailyprofit < 0) {
                    totalStreak++;
                } else {
                    if (longestStreak < totalStreak) {
                        longestStreak = totalStreak;
                    }
                    totalStreak = 0;
                }
            }
        }
        return longestStreak;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        int daysAbove = 0;
        int commInt = -1;

        for (int c = 0; c < COMMS; c++) {
            if (commodities[c].equals(comm)) { commInt = c; break; }
        }
        if (commInt == -1) return -1;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                int dailyprofit = profit[m][d][commInt];
                if (dailyprofit > threshold) {
                    daysAbove++;
                }
            }
        }

        return daysAbove;
    }

    public static int biggestDailySwing(int month) {
        if (month < 1 || month > 12) return -99999;
        int monthIdx = month - 1;

        int biggestSwing = 0;

        int dayBefore = 0;
        for (int c = 0; c < COMMS; c++) {
            dayBefore += profit[monthIdx][0][c];
        }

        for (int d = 1; d < DAYS; d++) {
            int totalProfit = 0;
            for (int c = 0; c < COMMS; c++) {
                totalProfit += profit[monthIdx][d][c];
            }

            int calculation = Math.abs(totalProfit - dayBefore);
            if (biggestSwing < calculation) {
                biggestSwing = calculation;
            }

            dayBefore = totalProfit;
        }

        return biggestSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        int c1Int = -1;
        int c2Int = -1;
        int c1Profit = 0;
        int c2Profit = 0;

        for (int c = 0; c < COMMS; c++) {
            if (commodities[c].equals(c1)) c1Int = c;
            if (commodities[c].equals(c2)) c2Int = c;
        }
        if (c1Int == -1 || c2Int == -1) return "INVALID_COMMODITY";

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                c1Profit = c1Profit + profit[m][d][c1Int];
                c2Profit = c2Profit + profit[m][d][c2Int];
            }
        }

        if (c1Profit > c2Profit) {
            return c1 + " is better by " + (c1Profit - c2Profit);
        } else if (c2Profit > c1Profit) {
            return c2 + " is better by " + (c2Profit - c1Profit);
        } else {
            return "Equal";
        }
    }

    public static String bestWeekOfMonth(int month) {
        if (month < 1 || month > 12) return "INVALID_MONTH";
        int monthIdx = month - 1;

        int week1Profit = 0;
        int week2Profit = 0;
        int week3Profit = 0;
        int week4Profit = 0;

        for (int d = 0; d < DAYS; d++) {
            int dailyprofit = 0;
            for (int c = 0; c < COMMS; c++) {
                dailyprofit = dailyprofit + profit[monthIdx][d][c];
            }

            if (d < 7) {
                week1Profit += dailyprofit;
            } else if (d < 14) {
                week2Profit += dailyprofit;
            } else if (d < 21) {
                week3Profit += dailyprofit;
            } else {
                week4Profit += dailyprofit;
            }
        }

        int[] profits = {week1Profit, week2Profit, week3Profit, week4Profit};
        int biggest = 0;
        int weekint = -1;

        for (int x = 0; x < profits.length; x++) {
            if (profits[x] > biggest) {
                biggest = profits[x];
                weekint = x;
            }
        }

        String week = "";
        switch (weekint) {
            case 0: week = "Week 1"; break;
            case 1: week = "Week 2"; break;
            case 2: week = "Week 3"; break;
            case 3: week = "Week 4"; break;
        }
        return week;
    }

    public static void main(String[] args) {
        loadData();


        System.out.println(totalProfitOnDay(12, 28));


    }
}
