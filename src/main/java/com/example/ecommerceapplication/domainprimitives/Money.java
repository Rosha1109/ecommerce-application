package com.example.ecommerceapplication.domainprimitives;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import jakarta.persistence.Embeddable;
import lombok.*;



import java.util.Objects;
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PROTECTED)
public class Money implements MoneyType {
    Float amount;
    String currency;

    
    
    @Override
    public Float getAmount() {
        return  amount;
    }

    @Override
    public String getCurrency() {
        return this.currency;
    }

    @Override
    public MoneyType add(MoneyType otherMoney) {
        if(otherMoney == null){
            throw new ShopException("");
        }
        if(!Objects.equals(this.currency, otherMoney.getCurrency())){
            throw new ShopException("wrong currency");
        }
        return new Money(amount+otherMoney.getAmount(), currency);
    }

    @Override
    public MoneyType subtract(MoneyType otherMoney) {
        if(otherMoney == null){
            throw new ShopException("");
        }
        if(!Objects.equals(this.currency, otherMoney.getCurrency())){
            throw new ShopException("wrong currency");
        }
        if(this.amount < otherMoney.getAmount()){
            throw new ShopException("wrong amount");
        }
        return new Money(amount-otherMoney.getAmount(), currency);
    }

    @Override
    public MoneyType multiplyBy(int factor) {
        if(factor < 0 ){
            throw new ShopException("");
        }
        return new Money(amount*factor, currency);
    }

    @Override
    public boolean largerThan(MoneyType otherMoney) {
        if(otherMoney == null){
            throw new ShopException("");
        }
        if(!Objects.equals(this.currency, otherMoney.getCurrency())){
            throw new ShopException("wrong currency");
        }
        if(this.amount > otherMoney.getAmount()){
            return true;
        }else{
            return false;
        }
    }

    public static MoneyType of (Float amount, String currency) {
        if(amount == null || amount < 0  ){
            throw new ShopException("");
        }
        if( currency == null || !currency.equals("EUR") && !currency.equals("CHF") ){
            throw new ShopException("wrong currency");
        }
        return new Money(amount, currency);

    }
}
