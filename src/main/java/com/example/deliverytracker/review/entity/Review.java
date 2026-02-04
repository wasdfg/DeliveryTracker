package com.example.deliverytracker.review.entity;

import com.example.deliverytracker.common.BaseEntity;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.review.dto.ReviewUpdateRequest;
import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id")
    private Store store;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private int rating;

    @Column(length = 1000)
    private String content;

    private String imageUrl;

    private boolean deleted = false;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ReviewReply reviewReply;

    @Builder
    public Review(User user, Store store, Order order, int rating, String content, String imageUrl) {
        this.user = user;
        this.store = store;
        this.order = order;
        this.rating = rating;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public void changeInfo(ReviewUpdateRequest request,String imageUrl) {
        if(request.getContent() != null){
            this.content = request.getContent();
        }
        
        if(request.getRating() != null){
            this.rating = request.getRating();
        }
        
        this.imageUrl = imageUrl;
    }

    public void delete(){
        this.deleted = true;
    }
}
