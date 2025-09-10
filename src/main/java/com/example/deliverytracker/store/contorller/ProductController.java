package com.example.deliverytracker.store.contorller;

import com.example.deliverytracker.store.dto.ProductRequest;
import com.example.deliverytracker.store.dto.ProductResponse;
import com.example.deliverytracker.store.dto.ProductUpdateRequest;
import com.example.deliverytracker.store.service.ProductService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

    private ProductService productService;

    @PostMapping(value = "/stores/{storeId}/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registProduct(@PathVariable Long storeId, @RequestPart("request") @Valid ProductRequest productRequest, @AuthenticationPrincipal UserDetailsImpl userDetail, @RequestPart("image") MultipartFile imageFile){
        this.productService.registProduct(storeId,productRequest,userDetail.getUser(),imageFile);

        return ResponseEntity.status(HttpStatus.CREATED).body("상품 등록이 완료되었습니다.");
    }

    @GetMapping("/stores/{storeId}/products")
    public ResponseEntity<Page<ProductResponse>> getProdcutInfo(@PathVariable Long storeId, Pageable pageable){

        Page<ProductResponse> productResponse = this.productService.getProductList(storeId,pageable);

        return ResponseEntity.ok(productResponse);
    }

    @PatchMapping(value = "/products/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> changeProductInfo(@PathVariable Long productId, @RequestPart("request") @Valid ProductUpdateRequest productRequest, @AuthenticationPrincipal UserDetailsImpl userDetail,@RequestPart("image") MultipartFile imageFile){

        this.productService.changeProductInfo(productId,productRequest,userDetail.getUser(),imageFile);

        return ResponseEntity.ok("상품 변경이 완료되었습니다.");
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetail){

        this.productService.deleteProduct(productId,userDetail.getUser());

        return ResponseEntity.noContent().build();
    }
}
