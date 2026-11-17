package com.example;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * ЛР №1 — консольний конвертер одиниць (варіант 1 — Довжина: mm, cm, m, km, mile).
 * Валідація введення БЕЗ винятків (Scanner.hasNextDouble / перевірка набору одиниць).
 * Пакується у виконуваний fat-jar (maven-shade-plugin).
 */
public final class Main {

    /** Коефіцієнти переведення кожної одиниці в базову (метри). */
    private static final Map<String, Double> UNITS = new LinkedHashMap<>();
    static {
        UNITS.put("mm", 0.001);
        UNITS.put("cm", 0.01);
        UNITS.put("m", 1.0);
        UNITS.put("km", 1000.0);
        UNITS.put("mile", 1609.344);
    }

    public static void main(String[] args) {
        // Явне кодування UTF-8 для коректної кирилиці на введенні та виведенні.
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        out.println("=== Конвертер одиниць (Довжина) ===");
        out.println("Одиниці: mm, cm, m, km, mile");
        out.println("(введіть значення 0, щоб вийти)");

        while (true) {
            // 1) Значення з валідацією без винятків.
            out.print("Значення: ");
            if (!sc.hasNextDouble()) {
                String bad = sc.hasNext() ? sc.next() : "";
                out.println("-> Помилка: введіть число. Спробуйте ще раз. (отримано: \"" + bad + "\")");
                continue;
            }
            double value = sc.nextDouble();
            if (value == 0) {
                out.println("-> Вихід. Дякуємо за використання!");
                break;
            }

            // 2) Одиниця "з" з перевіркою належності набору (теж без винятків).
            String from = readUnit(sc, out, "З (mm/cm/m/km/mile): ");
            String to   = readUnit(sc, out, "У (mm/cm/m/km/mile): ");

            // 3) Конвертація через базову одиницю (метри).
            double meters = value * UNITS.get(from);
            double result = meters / UNITS.get(to);
            out.printf("-> %s %s = %s %s%n", trim(value), from, trim(result), to);
        }
    }

    /** Читає одиницю, доки вона не належатиме набору — без винятків. */
    private static String readUnit(Scanner sc, PrintStream out, String prompt) {
        while (true) {
            out.print(prompt);
            String u = sc.hasNext() ? sc.next().trim().toLowerCase() : "";
            if (UNITS.containsKey(u)) return u;
            out.println("-> Помилка: невідома одиниця \"" + u + "\". Доступні: mm, cm, m, km, mile.");
        }
    }

    /** Акуратний вивід числа: без зайвих нулів. */
    private static String trim(double d) {
        if (d == Math.rint(d)) return String.valueOf((long) d) + ".0";
        return String.valueOf(d);
    }
}
