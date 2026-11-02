package com.example.deliverytracker.payment.service;

import com.example.deliverytracker.payment.dto.PaymentApprovalResult;
import com.example.deliverytracker.payment.dto.PaymentRefundResult;
import com.example.deliverytracker.payment.entity.Payment;

public interface PaymentGateway {

    PaymentApprovalResult approve(Payment payment, String providerPaymentKey);

    PaymentRefundResult refund(Payment payment, String refundReason);
}
