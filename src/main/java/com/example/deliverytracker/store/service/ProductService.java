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
import com.example.image.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    private final ImageService imageService;
    
    @Transactional
    public void registProduct(Long storeId, ProductRequest productRequest, User user, MultipartFile imageFile){

        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("상품을 등록할 권한이 없습니다.");
        }

        ProductCategory category = ProductCategory.valueOf(productRequest.getCategory().toUpperCase());

        String imageUrl;

        try {
            imageUrl = imageService.upload(imageFile);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }

        Product product = Product.builder()
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .name(productRequest.getName())
                .category(category)
                .description(productRequest.getDescription())
                .imageUrl(imageUrl)
                .isAvailable(true)
                .store(store)
                .build();

        productRepository.save(product);
    }

    public Page<ProductResponse> getProductList(Long storeId, Pageable pageable){
        Page<Product> productList = this.productRepository.findListByStoreId(storeId,pageable);

        return productList.map(ProductResponse::from);
    }

    @Transactional
    public void changeProductInfo(Long productId, ProductUpdateRequest productRequest, User user,MultipartFile imageFile){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("물품 정보가 없습니다."));
        
        if(!product.getStore().getOwner().equals(user)){
            throw new AccessDeniedException("상품은 점장만 등록 할 수 있습니다.");
        }

        String newImageUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {

                if (product.getImageUrl() != null) {
                    imageService.delete(product.getImageUrl());
                }

                newImageUrl = imageService.upload(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 수정에 실패했습니다.", e);
            }
        }

        product.updateInfo(productRequest,newImageUrl);

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
