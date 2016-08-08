package com.baozun.nebula.web.command;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 订单概要
 * 
 * @author dongliang
 *
 */
public class PtsSalesOrderCommand extends SalesOrderCommand implements Command{

    private static final long serialVersionUID = 896331295074752205L;

    /**
     * 来源
     */

    private String sourceLabel;

    /**
     * 支付方式
     */
    private String paymentLabel;

    /**
     * 物流状态
     */
    private String logisticsLabel;

    /**
     * 财务状态
     */
    private String financialLabel;

    /** 总价+运费 */
    private BigDecimal actotal;

    public BigDecimal getActotal(){
        return actotal;
    }

    public void setActotal(BigDecimal actotal){
        this.actotal = actotal;
    }

    public String getSourceLabel(){
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel){
        this.sourceLabel = sourceLabel;
    }

    public String getPaymentLabel(){
        return paymentLabel;
    }

    public void setPaymentLabel(String paymentLabel){
        this.paymentLabel = paymentLabel;
    }

    public String getLogisticsLabel(){
        return logisticsLabel;
    }

    public void setLogisticsLabel(String logisticsLabel){
        this.logisticsLabel = logisticsLabel;
    }

    public String getFinancialLabel(){
        return financialLabel;
    }

    public void setFinancialLabel(String financialLabel){
        this.financialLabel = financialLabel;
    }

}
