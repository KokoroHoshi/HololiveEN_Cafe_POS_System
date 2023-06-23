package models;

import java.io.File;
import java.util.Formatter;
import java.util.Scanner;
import java.util.TreeMap;

public class CategoryProducts {
    public static TreeMap<String, Product> readDrinkProduct() {
        TreeMap<String, Product> product_dict = new TreeMap();
        
        String[][] product_drinks = {
            {"p-d-001", "Drink", "Watson名偵探特調", "170", "Amelia_Drink.png", "奶蓋紅茶"},
            {"p-d-002", "Drink", "duzzem!Baelz的混沌蘇打", "180", "Baelz_Drink.png", "蔓越莓汁蘇打"},
            {"p-d-003", "Drink", "千杯不醉死神的紅酒替代品", "160", "Calliope_Drink.png", "櫻桃可樂"},
            {"p-d-004", "Drink", "德魯伊的治療藥劑", "180", "Fauna_Drink.png", "情人果冰沙"},
            {"p-d-005", "Drink", "Gura愛喝的泡泡浴", "190", "Gura_Drink.png", "香草冰淇淋、柑橘蘇打"},
            {"p-d-006", "Drink", "混血天使IRyS的行動要領", "180", "IRyS_Drink.png", "石榴葡萄蘇打"},
            {"p-d-007", "Drink", "古神指定!少女祭司的觸手果昔", "180", "Ina_Drink.png", "藍莓果昔"},
            {"p-d-008", "Drink", "店長的無限復活藥水", "180", "Kiara_Drink.png", "鹽漬柳橙蘇打"},
            {"p-d-009", "Drink", "時光典獄長的黑暗回憶", "160", "Kronii_Drink.png", "仙草風味飲"},
            {"p-d-010", "Drink", "不眠15小!貓頭鷹的提神特調", "170", "Mumei_Drink.png", "玄米拿鐵(含咖啡)"}
        };
        
        try{
            Formatter out = new Formatter(new File("DrinkProduct.csv"), "utf-8");
            for (String[] item : product_drinks) {
                for(int i = 0; i < item.length; i++){
                    out.format("%s", item[i]);
                    if(i < item.length-1){
                        out.format(",");
                    }
                }
                out.format("\n");
            }
            out.flush();
            out.close();
        } catch (Exception e){
            System.out.println(e);
        }

        for (String[] item : product_drinks) {
            
            Product product = new Product(
                    item[0], 
                    item[1], 
                    item[2], 
                    Integer.parseInt(item[3]),
                    item[4], 
                    item[5]);
            product_dict.put(product.getId(), product);
        }
        
        return product_dict; 
    }
    
    public static TreeMap<String, Product> readDrinkProductFromFile() {
        TreeMap<String, Product> product_dict = new TreeMap();
        
        try{
            Scanner in = new Scanner(new File("DrinkProduct.csv"), "utf-8");
            while(in.hasNextLine()) {
                String[] item = in.nextLine().split(",");
                Product product = new Product(
                        item[0], 
                        item[1], 
                        item[2], 
                        Integer.parseInt(item[3]),
                        item[4], 
                        item[5]);
                product_dict.put(product.getId(), product);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return product_dict;
    }
    
    public static TreeMap<String, Product> readFoodProductFromFile() {
        TreeMap<String, Product> product_dict = new TreeMap();
        
        try{
            Scanner in = new Scanner(new File("FoodProduct.csv"), "utf-8");
            while(in.hasNextLine()) {
                String[] item = in.nextLine().split(",");
                Product product = new Product(
                        item[0], 
                        item[1], 
                        item[2], 
                        Integer.parseInt(item[3]),
                        item[4], 
                        item[5]);
                product_dict.put(product.getId(), product);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return product_dict;
    }
    
    public static TreeMap<String, Product> readMerchProductFromFile() {
        TreeMap<String, Product> product_dict = new TreeMap();
        
        try{
            Scanner in = new Scanner(new File("MerchProduct.csv"), "utf-8");
            while(in.hasNextLine()) {
                String[] item = in.nextLine().split(",");
                Product product = new Product(
                        item[0], 
                        item[1], 
                        item[2], 
                        Integer.parseInt(item[3]),
                        item[4], 
                        item[5]);
                product_dict.put(product.getId(), product);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return product_dict;
    }
}

