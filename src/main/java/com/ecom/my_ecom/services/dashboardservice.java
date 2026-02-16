package com.ecom.my_ecom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecom.my_ecom.entities.orders;
import com.ecom.my_ecom.entities.myentity;
import com.ecom.my_ecom.productrepository.Repository;
import com.ecom.my_ecom.productrepository.orderrepo;
import com.ecom.my_ecom.productrepository.productrepo;

@Service
public class dashboardservice {

    @Autowired
    private orderrepo orderRepo;

    @Autowired
    private productrepo productRepo;

    @Autowired
    private Repository userRepo;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> result = new HashMap<>();

        long totalOrders = orderRepo.count();
        Double revenue = orderRepo.getTotalRevenue();
        long totalProducts = productRepo.count();
        long totalCustomers = userRepo.count();

        result.put("totalOrders", totalOrders);
        result.put("totalRevenue", revenue != null ? revenue : 0.0);
        result.put("totalCustomers", totalCustomers);
        result.put("totalProducts", totalProducts);

        double conversionRate = totalCustomers > 0
                ? Math.round((totalOrders * 100.0 / totalCustomers) * 10) / 10.0
                : 0.0;
        result.put("conversionRate", conversionRate);

        List<orders> recentOrdersList = orderRepo.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")))
                .getContent();

        List<Map<String, Object>> recentOrders = new ArrayList<>();
        for (orders o : recentOrdersList) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId", o.getreferenceID());
            orderMap.put("amount", o.getTotal());
            orderMap.put("status", o.getStatus() != null ? o.getStatus() : "pending");
            orderMap.put("createdAt", o.getCreatedAt());

            String customerName = "Guest";
            if (o.getUserId() != null) {
                customerName = userRepo.findById(o.getUserId())
                        .map(myentity::getName)
                        .orElse("User #" + o.getUserId());
            }
            orderMap.put("customerName", customerName);

            recentOrders.add(orderMap);
        }
        result.put("recentOrders", recentOrders);

        return result;
    }
}
