package com.example;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * ЛР №1 — консольний конвертер одиниць (варіант 5 — Швидкість: m/s, km/h, mph).
 * Валідація введення БЕЗ винятків (Scanner.hasNextDouble / перевірка набору одиниць).
 * Пакується у виконуваний fat-jar (maven-shade-plugin).
 */
public final class Main {

    /** Коефіцієнти переведення кожної одиниці в базову (метр за секунду, m/s). */
    private static final Map<String, Double> UNITS = new LinkedHashMap<>();
    static {
        UNITS.put("m/s", 1.0);
        UNITS.put("km/h", 1000.0 / 3600.0);     // 1 км/год = 0.27778 м/с
        UNITS.put("mph", 1609.344 / 3600.0);    // 1 миля/год = 0.44704 м/с
    }

    public static void main(String[] args) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        out.println("=== Конвертер одиниць (Швидкість) ===");
        out.println("Одиниці: m/s, km/h, mph");
        out.println("(введіть значення 0, щоб вийти)");

        while (true) {
            out.print("Значення: ");
            if (!sc.hasNextDouble()) {                    // валідація БЕЗ винятків
                String bad = sc.hasNext() ? sc.next() : "";
                out.println("-> Помилка: введіть число. Спробуйте ще раз. (отримано: \"" + bad + "\")");
                continue;
            }
            double value = sc.nextDouble();
            if (value == 0) {
                out.println("-> Вихід. Дякуємо за використання!");
                break;
            }
            String from = readUnit(sc, out, "З (m/s/km/h/mph): ");
            String to   = readUnit(sc, out, "У (m/s/km/h/mph): ");

            double base = value * UNITS.get(from);        // у базову одиницю (m/s)
            double result = base / UNITS.get(to);
            out.printf("-> %s %s = %s %s%n", trim(value), from, trim(result), to);
        }
    }

    private static String readUnit(Scanner sc, PrintStream out, String prompt) {
        while (true) {
            out.print(prompt);
            String u = sc.hasNext() ? sc.next().trim().toLowerCase() : "";
            if (UNITS.containsKey(u)) return u;
            out.println("-> Помилка: невідома одиниця \"" + u + "\". Доступні: m/s, km/h, mph.");
        }
    }

    private static String trim(double d) {
        if (d == Math.rint(d)) return String.valueOf((long) d) + ".0";
        return String.valueOf(d);
    }
}
