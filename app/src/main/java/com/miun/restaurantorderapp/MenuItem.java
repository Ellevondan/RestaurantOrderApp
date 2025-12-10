package com.miun.restaurantorderapp;

import java.util.List;
import java.util.ArrayList;

public class MenuItem {

    public int id;
    public String name;
    public double price;
    public String description;
    public int cookTime;
    public int idleTime;
    public String type;
    public boolean isMeat;

    public MenuItem(int id, String name, double price, String description,
                    int cookTime, int idleTime, String type, boolean isMeat) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.cookTime = cookTime;
        this.idleTime = idleTime;
        this.type = type;
        this.isMeat = isMeat;
    }



    private static final String[] NAMES = {
            "Bruschetta","Toast Skagen","Tomatsoppa","Vitlöksbröd","Carpaccio",
            "Entrecôte","Köttbullar","Laxfilé","Kycklingpasta","Vegolasagne",
            "Chokladfondant","Pannacotta","Äppelpaj","Glass Trio","Tiramisu",
            "Cola","Mineralvatten","Rödvin","Vitt vin","Öl"
    };

    private static final double[] PRICES = {
            89, 99, 79, 59, 119,
            229, 159, 189, 149, 139,
            89, 79, 69, 59, 99,
            25, 25, 79, 79, 69
    };

    private static final String[] DESCRIPTIONS = {
            "Tomat, basilika, bröd",
            "Räkröra på toast",
            "Krämig tomatsoppa",
            "Rostat bröd med vitlök",
            "Rå biff med parmesan",

            "Stekt kött med pommes",
            "Klassiska köttbullar",
            "Lax med citron och potatis",
            "Pastarätt med kyckling",
            "Lasagne utan kött",

            "Chokladkaka med varm kärna",
            "Vaniljpannacotta",
            "Paj med äpplen och vaniljsås",
            "Tre sorters glass",
            "Italiensk dessert",

            "Kall läsk",
            "Bubbligt vatten",
            "Rött vinglas",
            "Vitt vinglas",
            "Lageröl"
    };

    private static final int[] COOK_TIMES = {
            5, 7, 10, 3, 8,
            20, 15, 18, 12, 14,
            12, 5, 10, 1, 6,
            0, 0, 0, 0, 0
    };

    private static final int[] IDLE_TIMES = {
            1, 1, 1, 1, 1,
            2, 2, 2, 2, 2,
            1, 1, 1, 1, 1,
            0, 0, 0, 0, 0
    };

    private static final String[] TYPES = {
            "starter","starter","starter","starter","starter",
            "main","main","main","main","main",
            "dessert","dessert","dessert","dessert","dessert",
            "drink","drink","drink","drink","drink"
    };

    private static final boolean[] IS_MEAT = {
            false, true, false, false, true,
            true, true, false, true, false,
            false, false, false, false, false,
            false, false, false, false, false
    };

    public static List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        for (int i = 0; i < NAMES.length; i++) {
            items.add(new MenuItem(
                    i + 1,                 // ID starts at 1
                    NAMES[i],
                    PRICES[i],
                    DESCRIPTIONS[i],
                    COOK_TIMES[i],
                    IDLE_TIMES[i],
                    TYPES[i],
                    IS_MEAT[i]
            ));
        }
        return items;
    }
}
