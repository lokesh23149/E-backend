package com.ecom.my_ecom.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.ecom.my_ecom.entities.products;

public class productsepc {

	public static Specification<products> findcategory(String category) {

		return (root, query, callback) -> category == null ? null : callback.equal(root.get("category"), category);

	}

	public static Specification<products> findbyprice(Double min, Double max) {

		return (root, query, callback) -> {
			if (min == null && max == null)
				return null;
			if (min == null)
				return callback.lessThanOrEqualTo(root.get("price"), max);
			if (max == null)
				return callback.greaterThanOrEqualTo(root.get("price"), min);

			return callback.between(root.get("price"), min, max);

		};

	}

	public static Specification<products> findbynameordiscripsion(String keyword) {
		
		return (root, query, callback) -> {
			if (keyword == null || keyword.trim().isEmpty() ) return null;
			else {
			return callback.or
					(callback.like(root.get("name"), "%" + keyword.toLowerCase() + "%"),
					callback.like(root.get("discripsion"), "%" + keyword.toLowerCase() + "%"));

			}	};

	}
	
	public static Specification<products> findratings(Double rating) {
		return (root,query,callback)->{
			if(rating == null|| rating.isNaN()) return null;
			return callback.greaterThanOrEqualTo(root.get("ratings"), rating);
			
		};
		
	}

}
