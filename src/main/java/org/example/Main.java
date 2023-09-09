package org.example;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String nickname = null;
        String userinput = null;
        String[] columnMapping = {"timestamp", "nickname", "message"};
        String[] settingsColumnMapping = {"server", "port"};
        String settingsFileName = "Settings.txt";

        String fileName = "log.csv";

        List<Settings> settings = parseSettingsCSV(settingsColumnMapping, settingsFileName);

        String host;
        int port;

        host = settings.get(0).server;
        port = settings.get(0).port;

        System.out.println("host: " + host);
        System.out.println("post: " + port);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (nickname == null) {
                System.out.println("Введите ваш nickname");
                nickname = scanner.nextLine();
            }
            System.out.println("Для выхода, введите: /exit");
            System.out.println("Получить историю сообщений, введите : /refresh");
            System.out.println("Либо введите сообщение для отправки");
            userinput = scanner.nextLine();
            if (userinput.equals("/exit")) {
                System.out.println("До новых встреч");
                break;
            } else {
                sendandreceiveMessage(host, port, nickname, userinput);
                readLog(columnMapping, fileName);
            }

        }

    }

    public static void readLog(String[] columnMapping, String fileName) {
        List<Message> list = parseCSV(columnMapping, fileName);
        Collections.sort(list);
        list.forEach(System.out::println);
    }

    public static void printLog(String log) {
        try (FileWriter writer = new FileWriter("log.csv", true)) {
            writer.write(log);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void sendandreceiveMessage(String host, int port, String nickname, String message) {
        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new
                     PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new
                     InputStreamReader(clientSocket.getInputStream()))) {
            out.println();

            long unixTime = System.currentTimeMillis() / 1000L;
            out.println("log.csv," + unixTime + "," + nickname + "," + message);

            Thread.sleep(1000);

            String line = null;
            PrintWriter writer = new PrintWriter("log.csv");
            writer.print("");
            writer.close();
            while ((line = in.readLine()) != null) {
                printLog(line + "\r\n");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<Message> parseCSV(String[] columnMapping, String fileName) {

        List<Message> messages = null;

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Message> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Message.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Message> csv = new CsvToBeanBuilder<Message>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            messages = csv.parse();

            // Массив считанных строк
            String[] nextLine;
            // Читаем CSV построчно
            while ((nextLine = reader.readNext()) != null) {
                // Работаем с прочитанными данными.
                System.out.println(Arrays.toString(nextLine));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private static List<Settings> parseSettingsCSV(String[] columnMapping, String fileName) {

        List<Settings> messages = null;

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Settings> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Settings.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Settings> csv = new CsvToBeanBuilder<Settings>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            messages = csv.parse();

            // Массив считанных строк
            String[] nextLine;
            // Читаем CSV построчно
            while ((nextLine = reader.readNext()) != null) {
                // Работаем с прочитанными данными.
                System.out.println(Arrays.toString(nextLine));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return messages;
    }

}