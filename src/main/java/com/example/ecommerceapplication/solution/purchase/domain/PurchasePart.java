package com.example.ecommerceapplication.solution.purchase.domain;

import com.example.ecommerceapplication.solution.thing.domain.Thing;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Embeddable
@NoArgsConstructor
public class PurchasePart {
    @Getter
    @Setter
    private Integer quantity;
    @Getter
    @Setter
    @ManyToOne
    private Thing thing;
}
