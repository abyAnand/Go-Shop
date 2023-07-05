package io.ecommerce.GoShop.DTO;

import io.ecommerce.GoShop.model.Status;

import java.util.UUID;

public class OrderDTO {

    UUID orderId;

    Status status;


    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
