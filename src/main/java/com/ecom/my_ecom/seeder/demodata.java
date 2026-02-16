package com.ecom.my_ecom.seeder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ecom.my_ecom.entities.products;
import com.ecom.my_ecom.productrepository.productrepo;

@Component
public class demodata implements CommandLineRunner {

    @Autowired
    private productrepo repo;

    @Override
    public void run(String... args) throws Exception {

        if (repo.count() == 0) {
            List<products> demoProducts = new ArrayList<>();

            // ===================== SUPPLEMENTS =====================
            products p1 = new products(null, "Whey Protein 2kg", 59.0,
                    "High-quality whey protein for muscle growth and recovery",
                    "Supplements", 4.7, "MuscleBlaze", 40,
                    List.of("https://placehold.co/400x400/png?text=Whey+Protein+2kg"));
            p1.setDiscount(20);
            demoProducts.add(p1);

            products p2 = new products(null, "Creatine Monohydrate 300g", 24.0,
                    "Improves strength, power, and workout performance",
                    "Supplements", 4.8, "Optimum Nutrition", 35,
                    List.of("https://placehold.co/400x400/png?text=Creatine+300g"));
            p2.setDiscount(15);
            demoProducts.add(p2);

            products p3 = new products(null, "Pre-Workout Energy Booster", 29.0,
                    "Boosts energy, focus, and endurance during workouts",
                    "Supplements", 4.6, "GNC", 30,
                    List.of("https://placehold.co/400x400/png?text=Pre+Workout"));
            demoProducts.add(p3);

            products p4 = new products(null, "BCAA Amino Acids", 22.0,
                    "Supports muscle recovery and reduces fatigue",
                    "Supplements", 4.5, "MyProtein", 28,
                    List.of("https://placehold.co/400x400/png?text=BCAA"));
            p4.setDiscount(25);
            demoProducts.add(p4);

            // ===================== CLOTHING =====================
            products p5 = new products(null, "Men Gym T-Shirt", 19.0,
                    "Breathable and sweat-resistant gym t-shirt",
                    "Clothing", 4.6, "Nike", 45,
                    List.of("https://placehold.co/400x400/png?text=Gym+T-Shirt"));
            p5.setDiscount(30);
            demoProducts.add(p5);

            products p6 = new products(null, "Women Workout Leggings", 25.0,
                    "Stretchable leggings for yoga and gym workouts",
                    "Clothing", 4.7, "Adidas", 38,
                    List.of("https://placehold.co/400x400/png?text=Leggings"));
            demoProducts.add(p6);

            products p7 = new products(null, "Men Training Shorts", 17.0,
                    "Lightweight shorts for intense training sessions",
                    "Clothing", 4.5, "Puma", 42,
                    List.of("https://placehold.co/400x400/png?text=Training+Shorts"));
            p7.setDiscount(10);
            demoProducts.add(p7);

            // ===================== EQUIPMENT =====================
            products p8 = new products(null, "Adjustable Dumbbells Set", 99.0,
                    "Adjustable dumbbells for effective home workouts",
                    "Equipment", 4.8, "Decathlon", 15,
                    List.of("https://placehold.co/400x400/png?text=Dumbbells"));
            p8.setDiscount(15);
            demoProducts.add(p8);

            products p9 = new products(null, "Olympic Barbell Rod", 129.0,
                    "Heavy-duty barbell for strength training",
                    "Equipment", 4.7, "Strauss", 10,
                    List.of("https://placehold.co/400x400/png?text=Barbell"));
            demoProducts.add(p9);

            // ===================== ACCESSORIES =====================
            products p10 = new products(null, "Gym Gloves", 14.0,
                    "Anti-slip gloves for better grip during workouts",
                    "Accessories", 4.6, "Boldfit", 60,
                    List.of("https://placehold.co/400x400/png?text=Gym+Gloves"));
            p10.setDiscount(20);
            demoProducts.add(p10);

            products p11 = new products(null, "Gym Shaker Bottle", 9.0,
                    "Leak-proof shaker bottle for protein shakes",
                    "Accessories", 4.7, "MuscleXP", 80,
                    List.of("https://placehold.co/400x400/png?text=Shaker"));
            demoProducts.add(p11);

            products p12 = new products(null, "Gym Backpack", 34.0,
                    "Spacious backpack for gym and travel use",
                    "Accessories", 4.6, "Puma", 18,
                    List.of("https://placehold.co/400x400/png?text=Backpack"));
            p12.setDiscount(35);
            demoProducts.add(p12);

            repo.saveAll(demoProducts);
            System.out.println("✅ Demo products inserted successfully (with placeholders)");
        } else {
            System.out.println("ℹ️ Products already exist. Skipping seeding.");
        }
    }
}
