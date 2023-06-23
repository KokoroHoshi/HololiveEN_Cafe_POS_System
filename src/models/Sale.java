package models;

import db.CacheDAO;

public class Sale { 
    private String id;
    private int totalPrice;
    private String paymentMethod;
    private String LINE_PayCode = "";
    private String creditCardCode = "";
    private String discountCode = "";
    private String carrierCode = "";
    
    private CacheDAO cacheDAO = new CacheDAO();

    public Sale(int totalPrice) {
        this.setId();
        this.id = this.getId();
        this.totalPrice = totalPrice;
        this.paymentMethod = "Cash";
        cacheDAO.updateSaleNum(cacheDAO.getSaleNum()+1);
    }
    
    public Sale(int totalPrice, String paymentMethod, String LINE_PayCode, String creditCardCode, String discountCode, String carrierCode) {
        this.setId();
        this.id = this.getId();
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.LINE_PayCode = LINE_PayCode;
        this.creditCardCode = creditCardCode;
        this.discountCode = discountCode;
        this.carrierCode = carrierCode;
        cacheDAO.updateSaleNum(cacheDAO.getSaleNum()+1);
    }
    
    public String getId() {
        return id;
    }

    public void setId() {
        this.id = "s-id-" + cacheDAO.getDate() + "-" + String.valueOf(cacheDAO.getSaleNum()+1);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getLINE_PayCode() {
        return LINE_PayCode;
    }

    public void setLINE_PayCode(String LINE_PayCode) {
        this.LINE_PayCode = LINE_PayCode;
    }

    public String getCreditCardCode() {
        return creditCardCode;
    }

    public void setCreditCardCode(String creditCardCode) {
        this.creditCardCode = creditCardCode;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }
}
