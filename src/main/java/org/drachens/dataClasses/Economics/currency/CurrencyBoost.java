package org.drachens.dataClasses.Economics.currency;

public class CurrencyBoost {
    private final CurrencyTypes currencyTypes;
    private float boost;
    public CurrencyBoost(CurrencyTypes currencyTypes, float boost){
        this.currencyTypes = currencyTypes;
        this.boost = boost;
    }
    public CurrencyBoost(CurrencyBoost currencyBoost){
        this.currencyTypes=currencyBoost.currencyTypes;
        this.boost=currencyBoost.boost;
    }
    public void addBoost(float boost){
        this.boost+=boost;
    }
    public void minusBoost(float boost){
        this.boost-=boost;
    }
    public CurrencyTypes getCurrencyTypes(){
        return currencyTypes;
    }
    public float getBoost(){
        return boost;
    }
}
