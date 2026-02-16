package com.ecom.my_ecom.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecom.my_ecom.entities.orderitems;
import com.ecom.my_ecom.entities.orders;
import com.ecom.my_ecom.entities.products;
import com.ecom.my_ecom.productrepository.Repository;
import com.ecom.my_ecom.productrepository.orderrepo;
import com.ecom.my_ecom.productrepository.productrepo;
import com.ecom.my_ecom.reviewsdao.ordercreateddto;
import com.ecom.my_ecom.reviewsdao.orderdao;
import com.ecom.my_ecom.reviewsdao.requestorder;



@Service
public class orderservices {

	@Autowired
	private productrepo prorepo;

	@Autowired
	private orderrepo ordrepo;

	@Autowired
	private Repository userRepo;

	public ordercreateddto createorder(requestorder requestorder) {
		if(requestorder.getOrderdao()==null||requestorder.getOrderdao().isEmpty()) {
			 throw	new RuntimeException("order is empty");
		}
		orders order = new orders();
		order.setStatus("pending"); 
		double totalitemamount=0;
		for (orderdao dao : requestorder.getOrderdao()) {
		    
		    orderitems item = new orderitems();

		    item.setName(dao.getName());
		    item.setImage(dao.getImage());
		    item.setPrice(dao.getPrice());
		    item.setQuantity(dao.getQuantity());

		    
		    
		    products product = prorepo.findById(dao.getProductid())
		    	    .orElseThrow(() -> new RuntimeException("product id not found"));

		    item.setProduct(product);
		   

		    totalitemamount += dao.getPrice() * dao.getQuantity();

		    order.getOrderitems().add(item);
		}

		
		
		
		order.setOrderitemtotal(totalitemamount);
		double totalamount=0;
		double gst=10;
		totalamount=totalitemamount+gst;
		order.setTotal(totalamount);
		order.setGst(gst);
		order.setCreatedAt(Instant.now());
		String refid = UUID.randomUUID().toString();
		order.setreferenceId(refid);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
			String username = ((UserDetails) auth.getPrincipal()).getUsername();
			userRepo.findByusername(username).ifPresent(u -> order.setUserId(u.getId()));
		}

		ordrepo.save(order);
		return new ordercreateddto(refid);

	}

	

	public orders getorder(String referenceID) {
		return ordrepo.findByReferenceIdWithItems(referenceID)
				.orElseThrow(() -> new RuntimeException("Order not found: " + referenceID));
	}

	public List<orders> getOrdersByUserId(Long userId) {
		return ordrepo.findByUserIdWithItems(userId);
	}

	public List<orders> getAllOrders() {
		return ordrepo.findAllWithItems();
	}

}
