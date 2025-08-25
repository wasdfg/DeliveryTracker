package com.example.deliverytracker.store.service;

import com.example.deliverytracker.store.dto.ProductRequest;
import com.example.deliverytracker.store.dto.ProductResponse;
import com.example.deliverytracker.store.dto.ProductUpdateRequest;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.entity.ProductCategory;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.ProductRepository;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;
    
    @Transactional
    public void registProduct(Long storeId, ProductRequest productRequest, User user){

        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("상품을 등록할 권한이 없습니다.");
        }

        ProductCategory category = ProductCategory.valueOf(productRequest.getCategory().toUpperCase());

        Product product = Product.builder()
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .name(productRequest.getName())
                .category(category)
                .description(productRequest.getDescription())
                .isAvailable(true)
                .build();

        store.addProduct(product);

        productRepository.save(product);
    }

    public Page<ProductResponse> getProductList(Long storeId, Pageable pageable){
        Page<Product> productList = this.productRepository.findListByStoreId(storeId,pageable);

        return productList.map(ProductResponse::from);
    }

    @Transactional
    public void changeProductInfo(Long productId, ProductUpdateRequest productRequest, User user){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("물품 정보가 없습니다."));
        
        if(!product.getStore().getOwner().equals(user)){
            throw new AccessDeniedException("상품은 점장만 등록 할 수 있습니다.");
        }

        product.updateInfo(productRequest);

    }

    @Transactional
    public void deleteProduct(Long productId, User user){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("물품 정보가 없습니다."));

        if(!product.getStore().getOwner().equals(user)){
            throw new AccessDeniedException("상품은 점장만 삭제 할 수 있습니다.");
        }

        product.delete();
    }
}
