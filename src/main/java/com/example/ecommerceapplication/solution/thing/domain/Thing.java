package com.example.ecommerceapplication.solution.thing.domain;

import com.example.ecommerceapplication.domainprimitives.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
public class Thing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Float size;

    @Getter
    @Setter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "purchase_price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "purchase_price_currency"))
    })
    private Money purchasePrice;

    @Getter
    @Setter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "sell_price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "sell_price_currency"))
    })
    private Money sellPrice;

    @Getter
    @Setter
    private UUID thingId;
    @Getter
    @Setter
    private Integer thingStock = 0;


    public Thing() {}
    public Thing(UUID thingId,String name, String description, Float size, Money purchasePrice, Money sellPrice) {
        this.thingId = thingId;
        this.name = name;
        this.description = description;
        this.size = size;
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;

    }

    public void addToStock(int addedQuantity) {
        if(getThingStock() == null) {
            setThingStock(0);
        }
        setThingStock(getThingStock() + addedQuantity);
    }
}
