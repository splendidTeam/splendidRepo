package com.baozun.nebula.web.controller.returnapplication.resolver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.api.returnapplication.ReturnApplicationCodeCreatorManager;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;
import com.feilong.core.Validator;

/**
 * nebula默认的builder，根据页面提交的表单信息创建退换货订单。默认退货单和换货单是分开创建。如果一个用户订单同时退货和换货会创建一个退货单和一个换货单。
 * @author zl.shi
 * @since 2017.8.21
 */
@Service("defaultReturnApplicationBuilder")
public class DefaultReturnApplicationBuilderImpl implements ReturnApplicationBuilder {
	
	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;
	
    /** The ReturnApplication code creator. */
    @Autowired(required = false)
    private ReturnApplicationCodeCreatorManager returnApplicationCodeCreatorManager;


	@Override
	public List<ReturnApplicationCommand> buildReturnApplicationCommands(
			MemberDetails memberDetails,ReturnOrderForm returnOrderForm, SalesOrderCommand salesOrder) {
		
		List<ReturnApplicationCommand> returnApplicationComs = new ArrayList<ReturnApplicationCommand>();
		
		ReturnApplicationCommand returnAppCommand = buildReturnReturnApplication( returnOrderForm, salesOrder, memberDetails.getMemberId() );
		ReturnApplicationCommand changeAppCommand = buildChangeReturnApplication( returnOrderForm, salesOrder, memberDetails.getMemberId() );
		
		returnApplicationComs.add( returnAppCommand );
		returnApplicationComs.add( changeAppCommand );
		
		return returnApplicationComs;
	}
	
	
	
	/**
	 * 构建退货订单，退货单涉及到退款问题
	 * @return
	 */
	public ReturnApplicationCommand buildReturnReturnApplication( ReturnOrderForm returnOrderForm, SalesOrderCommand salesOrder, Long memberId ) {
		
		ReturnApplication returnApplication = buildSimpleReturnApplication( returnOrderForm, salesOrder, memberId, ReturnApplication.SO_RETURN_TYPE_RETURN, "" );
		
		
		List<ReturnApplicationLine> returnLineList = buildReReturnAppLine( returnOrderForm );
		setReimburse( returnApplication, returnLineList );	// 计算退款设置到退货单
		
		
		ReturnApplicationCommand returnApplicationCom = buildReturnAppCommand( returnApplication, returnLineList, returnOrderForm );
		
		return returnApplicationCom;
	}
	
	
	
	/**
	 * 构建换货的订单
	 * @return
	 */
	public ReturnApplicationCommand buildChangeReturnApplication( ReturnOrderForm returnOrderForm, SalesOrderCommand salesOrder, Long memberId ) {
		
		ReturnApplication returnApplication = buildSimpleReturnApplication( returnOrderForm, salesOrder, memberId, ReturnApplication.SO_RETURN_TYPE_EXCHANGE, "" );
		
		List<ReturnApplicationLine> returnLineList = buildChangeReturnAppLine( returnOrderForm );
		
		
		returnApplication.setReturnPrice(new BigDecimal(0));
		
		ReturnApplicationCommand returnApplicationCom = buildReturnAppCommand( returnApplication, returnLineList, returnOrderForm );
		
		return returnApplicationCom;
	}
	
	
	/**
	 * 构建ReturnApplicationCommand对象
	 * @param returnApplication
	 * @param returnLineList
	 * @param returnOrderForm
	 * @return
	 */
	public ReturnApplicationCommand buildReturnAppCommand( ReturnApplication returnApplication, List<ReturnApplicationLine> returnLineList, ReturnOrderForm returnOrderForm ) {
		
		ReturnApplicationCommand returnAppCommand = new ReturnApplicationCommand();
		returnAppCommand.setReturnLineList(returnLineList);
		returnAppCommand.setSoReturnApplicationDeliveryInfo(returnOrderForm.getReturnDeliveryInfo());
		returnAppCommand.setReturnApplication(returnApplication);
		
		String code = returnApplicationCodeCreatorManager.createReturnApplicationCodeBySource(returnAppCommand);
		
        if( Validator.isNotNullOrEmpty(returnAppCommand) && Validator.isNotNullOrEmpty(returnAppCommand.getReturnApplication()) ){
            returnAppCommand.getReturnApplication().setReturnApplicationCode(code);
        }
        
        return returnAppCommand;
	}
	
	
	
	/**
	 * 对退换货
	 */
	public void setReimburse( ReturnApplication returnApplication, List<ReturnApplicationLine> returnLineList ) {
		
		BigDecimal returnTotalMoney = new BigDecimal(0);
		for ( ReturnApplicationLine returnAppLine : returnLineList ) {
			returnTotalMoney = returnTotalMoney.add(returnAppLine.getReturnPrice());
		}
		returnApplication.setReturnPrice(returnTotalMoney);
	}
	
	
	/**
	 * 构建退货的订单行集合
	 * @param returnOrderForm
	 * @return
	 */
	public List<ReturnApplicationLine> buildReReturnAppLine( ReturnOrderForm returnOrderForm ) {
		List<ReturnApplicationLine> returnLineList = new ArrayList<ReturnApplicationLine>();
		for ( int i = 0; i < returnOrderForm.getLineIdSelected().length; i++ ) {
			
			// 必须是退货申请
			if ( !ReturnApplication.SO_RETURN_TYPE_RETURN.equals(returnOrderForm.getReturnType()[i]) ) {
				continue;
			}
			
			OrderLine line = sdkOrderLineDao.getByPrimaryKey(returnOrderForm.getLineIdSelected()[i]);
			ReturnApplicationLine returnLine = buildBaseReturnAppLine( returnOrderForm, i );
			
			returnLine.setRtnExtentionCode(line.getExtentionCode());
			returnLine.setChgExtentionCode(null);
			
			// 退款 = 订单行总额 / 订单行数量 * 退货数量
			returnLine.setReturnPrice(line.getSubtotal().divide(new BigDecimal(line.getCount())).multiply(new BigDecimal(returnOrderForm.getSumSelected()[i])));
			
			returnLineList.add(returnLine);
			
		}
		
		return returnLineList;
	}
	
	
	/**
	 * 构建换货的订单行集合
	 * @param returnOrderForm
	 * @return
	 */
	public List<ReturnApplicationLine> buildChangeReturnAppLine( ReturnOrderForm returnOrderForm ) {
		List<ReturnApplicationLine> returnLineList = new ArrayList<ReturnApplicationLine>();
		for ( int i = 0; i < returnOrderForm.getLineIdSelected().length; i++ ) {
			
			// 必须是换货
			if ( !ReturnApplication.SO_RETURN_TYPE_EXCHANGE.equals(returnOrderForm.getReturnType()[i]) ) {
				continue;
			}
			
			ReturnApplicationLine returnLine = buildBaseReturnAppLine( returnOrderForm, i );
			

			//换货，不涉及到金额
			returnLine.setRtnExtentionCode(null);
			returnLine.setChgExtentionCode(returnOrderForm.getExtentionCode()[i]);
			returnLineList.add(returnLine);
		}
		
		return returnLineList;
	}
	
	
	/**
	 * 构建订单行的基础信息，退货跟换货通用的逻辑
	 * @param returnOrderForm
	 * @param index
	 * @return
	 */
	public ReturnApplicationLine buildBaseReturnAppLine( ReturnOrderForm returnOrderForm, Integer index ) {
		
		ReturnApplicationLine returnLine = new ReturnApplicationLine();
		Long lineId = returnOrderForm.getLineIdSelected()[index];
		String returnReason = returnOrderForm.getReasonSelected()[index].trim();
		returnLine.setReturnReason(returnReason);
		returnLine.setQty(returnOrderForm.getSumSelected()[index]);
		returnLine.setSoLineId(lineId);
		returnLine.setMemo(returnOrderForm.getMemo());
		returnLine.setType(returnOrderForm.getReturnType()[index]);
		returnLine.setCreateTime( new Date() );
		
		return returnLine;
	}
	
	
	/**
	 * 通过form表单的信息创建对于的ReturnApplication，这里没有设置退款。调用后需要自行计算退款
	 * @param returnOrderForm
	 * @param salesOrder
	 * @param memberId
	 * @param index
	 * @return
	 */
	public ReturnApplication buildSimpleReturnApplication( ReturnOrderForm returnOrderForm, SalesOrderCommand salesOrder, Long memberId, Integer type, String reason ) {
		ReturnApplication returnApplication = new ReturnApplication();
		Date date = new Date();
		returnApplication.setCreateTime(date);
		returnApplication.setMemberId( memberId );
		
		returnApplication.setRemark(returnOrderForm.getMemo());
		returnApplication.setRefundAccount(returnOrderForm.getAccount());
		
		returnApplication.setVersion(date);
		returnApplication.setRefundBankBranch(returnOrderForm.getBranch());
		returnApplication.setRefundBank(returnOrderForm.getBank());
		returnApplication.setType( type );
		returnApplication.setSoOrderCode(returnOrderForm.getOrderCode());
		returnApplication.setSoOrderId(returnOrderForm.getOrderId());
		returnApplication.setRefundType(String.valueOf(salesOrder.getPayment()));// 退款方式
		returnApplication.setIsNeededReturnInvoice(ReturnApplication.SO_RETURN_NEEDED_RETURNINVOICE);
		returnApplication.setReturnReason( reason );
		returnApplication.setStatus(ReturnApplication.SO_RETURN_STATUS_AUDITING);
		
		return returnApplication;
	}
	

}
