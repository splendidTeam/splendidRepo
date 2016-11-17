package com.baozun.nebula.utilities.integration.payment.chinaPnR;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayPaymentRequest;

public abstract class AbstractChinaPnRPaymentAdaptor implements PaymentAdaptor {

	@Override
	public abstract String getServiceProvider();

	@Override
	public String getServiceType() {
		return "chinaPnR";
	}

	@Override
	public String getServiceVersion() {
		return "10";
	}

	@Override
	public PaymentRequest newPaymentRequest(String httpType,
			Map<String, String> addition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResult(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PaymentResult getPaymentResultForMobileCreateDirect(Map<String,String> resultStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResultFromNotification(
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult closePaymentRequest(Map<String, String> parm) {
		PaymentResult result = new PaymentResult();
		result.setMessage("NOT SUPPORT");
		result.setPaymentServiceSatus(PaymentServiceStatus.NOT_SUPPORT);
		return result;
	}

	@Override
	public boolean isSupportClosePaymentRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileCreateDirect(Map<String, String> addition) {
		
		return null;
	}
	
	@Override
	public PaymentRequest newPaymentRequestForMobileAuthAndExecute(Map<String, String> addition) {
		AlipayPaymentRequest request = new AlipayPaymentRequest();
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(HttpServletRequest request){
		PaymentResult paymentResult = new PaymentResult();
		
		return paymentResult;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteASY(HttpServletRequest request){
		PaymentResult paymentResult = new PaymentResult();
		
		return paymentResult;
	}
	
	@Override
	public PaymentResult unifiedOrder(Map<String, String> addition) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.utilities.integration.payment.PaymentAdaptor#getCreateResponseToken()
	 */
	@Override
	public PaymentRequest getCreateResponseToken(PaymentRequest paymentRequest) {
		return paymentRequest;
	}
}
