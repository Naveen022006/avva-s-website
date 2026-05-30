package com.avvahomefoods.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "store_settings")
public class StoreSettings {

    @Id
    private String id;

    private double deliveryCharge;
    private double freeDeliveryThreshold;

    public StoreSettings() {}

    public StoreSettings(double deliveryCharge, double freeDeliveryThreshold) {
        this.deliveryCharge = deliveryCharge;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getDeliveryCharge() { return deliveryCharge; }
    public void setDeliveryCharge(double deliveryCharge) { this.deliveryCharge = deliveryCharge; }

    public double getFreeDeliveryThreshold() { return freeDeliveryThreshold; }
    public void setFreeDeliveryThreshold(double freeDeliveryThreshold) { this.freeDeliveryThreshold = freeDeliveryThreshold; }
}
