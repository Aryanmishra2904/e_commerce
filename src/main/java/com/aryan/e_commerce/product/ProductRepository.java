package com.aryan.e_commerce.product;
import com.aryan.e_commerce.product.Product;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {


    List<Product> findBy(TextCriteria criteria);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByPriceBetween(Double min, Double max);
}
