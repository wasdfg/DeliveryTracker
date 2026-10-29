package com.example.deliverytracker.payment.service;

import com.example.deliverytracker.payment.dto.PaymentApprovalResult;
import com.example.deliverytracker.payment.entity.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnProperty(
        name = "deliverytracker.payment.provider",
        havingValue = "toss"
)
public class TossPaymentGateway implements PaymentGateway {

    private final RestClient restClient;

    private final String secretKey;

    public TossPaymentGateway(RestClient.Builder builder, @Value("${deliverytracker.payment.toss.base-url:https://api.tosspayments.com/v1}") String baseUrl, @Value("${deliverytracker.payment.toss.secret-key:}") String secretKey) {
        this.restClient = builder.baseUrl(baseUrl).build();

        this.secretKey = secretKey;
    }

    @Override
    public PaymentApprovalResult approve(Payment payment, String providerPaymentKey) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("토스페이먼츠 Secret Key가 설정되지 않았습니다.");
        }

        if (providerPaymentKey == null || providerPaymentKey.isBlank()) {
            throw new IllegalArgumentException("PG에서 전달받은 결제 키가 필요합니다.");
        }

        TossConfirmRequest request = new TossConfirmRequest(providerPaymentKey, payment.getGatewayOrderId(), payment.getAmount());

        TossConfirmResponse response;

        try {
            response = restClient.post()
                    .uri("/payments/confirm")
                    .headers(headers -> headers.setBasicAuth(secretKey, "", StandardCharsets.UTF_8))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(TossConfirmResponse.class);

        } catch (RestClientResponseException e) {
            throw new IllegalStateException("PG 승인 요청에 실패했거나 승인 결과 확인이 필요합니다.", e);

        } catch (RestClientException e) {
            throw new IllegalStateException("PG 승인 결과를 확인할 수 없습니다.", e);
        }

        if (response == null) {
            throw new IllegalStateException("PG 승인 응답이 비어 있습니다.");
        }

        if (!providerPaymentKey.equals(response.paymentKey()) || !payment.getGatewayOrderId().equals(response.orderId())) {
            throw new IllegalStateException("PG 승인 응답의 결제 정보가 일치하지 않습니다.");
        }

        if (!"DONE".equals(response.status())) {
            return PaymentApprovalResult.failed("PG 결제가 승인 완료 상태가 아닙니다.");
        }

        if (response.totalAmount() == null) {
            throw new IllegalStateException("PG 승인 금액이 응답에 없습니다.");
        }

        return PaymentApprovalResult.approved(response.paymentKey(), response.orderId(), BigDecimal.valueOf(response.totalAmount()));
    }

    private record TossConfirmRequest(String paymentKey, String orderId, BigDecimal amount) {
    }

    private record TossConfirmResponse(String paymentKey, String orderId, Long totalAmount, String status, String method) {
    }
}
