package com.example.deliverytracker.payment.service;

import com.example.deliverytracker.payment.dto.PaymentApprovalResult;
import com.example.deliverytracker.payment.entity.Payment;

public interface PaymentGateway {

    PaymentApprovalResult approve(Payment payment, String providerPaymentKey);
}
