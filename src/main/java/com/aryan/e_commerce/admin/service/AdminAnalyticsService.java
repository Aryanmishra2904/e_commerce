package com.aryan.e_commerce.admin.service;

import com.aryan.e_commerce.admin.dto.DashboardOverviewDto;
import com.aryan.e_commerce.admin.dto.OrderStatusStatsDto;
import com.aryan.e_commerce.admin.dto.TopProductDto;
import com.aryan.e_commerce.admin.dto.UserStatsDto;
import com.aryan.e_commerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    @Cacheable(value = "admin-dashboard", key = "'overview'")
    public DashboardOverviewDto getOverview() {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("status").is("PAID")),
                Aggregation.group()
                        .sum("totalAmount").as("revenue")
                        .count().as("orders")
        );

        Document doc = mongoTemplate
                .aggregate(agg, "orders", Document.class)
                .getUniqueMappedResult();

        double revenue = doc == null ? 0 : doc.getDouble("revenue");
        long orders = doc == null ? 0 : doc.getLong("orders");
        long users = userRepository.count();

        return new DashboardOverviewDto(revenue, orders, users);
    }
    @Cacheable(value = "admin-orders", key = "'status'")
    public List<OrderStatusStatsDto> getOrderStatusStats() {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("status")
                        .count().as("count"),
                Aggregation.project("count")
                        .and("_id").as("status")
        );

        return mongoTemplate.aggregate(
                agg, "orders", OrderStatusStatsDto.class
        ).getMappedResults();
    }
    @Cacheable(value = "admin-products", key = "'top'")
    public List<TopProductDto> getTopSellingProducts() {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("items"),
                Aggregation.group("items.productId")
                        .sum("items.quantity").as("soldQuantity"),
                Aggregation.sort(Sort.Direction.DESC, "soldQuantity"),
                Aggregation.limit(5),
                Aggregation.project("soldQuantity")
                        .and("_id").as("productId")
        );

        return mongoTemplate.aggregate(
                agg, "orders", TopProductDto.class
        ).getMappedResults();
    }
    @Cacheable(value = "admin-users", key = "'count'")
    public UserStatsDto getUserStats() {
        return new UserStatsDto(userRepository.count());
    }



}
