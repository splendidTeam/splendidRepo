package com.baozun.nebula.manager.product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.dao.product.ItemRateDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.product.ItemRate;
import com.baozun.nebula.sdk.command.ItemRateCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.solr.utils.Validator;

@Transactional
@Service("ItemRateManager")
public class ItemRateManagerImpl implements ItemRateManager {

	@Autowired
	private SdkMemberManager sdkMemberManager;

	@Autowired
	private SdkItemManager sdkItemManager;
	
	@Value("#{meta['item.rate.check.order']}")
	private final String IS_ITEM_RATE_CHECK_ORDER = "true";

	@Override
	public void createRate(RateCommand rateCommand ) {
		// 验证用户是否存在
		MemberCommand memberCommand = sdkMemberManager
				.findMemberById(rateCommand.getMemberId());
		if (memberCommand == null) {
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
		}  
		if(Boolean.parseBoolean(IS_ITEM_RATE_CHECK_ORDER)){
			List<OrderLineCommand> skucompletion= sdkMemberManager.findOrderLineCompletionByItemIdOrUserId(rateCommand.getMemberId(),
					rateCommand.getItemId()	);
			if (!(null != skucompletion && skucompletion.size() > 0)) {
				throw new BusinessException(ErrorCodes.MEMBER_ITEMRATE_ORDERLINECOMPLETION);
			}
		}
		rateCommand.setMemberId(memberCommand.getId()); 
		ItemRate itemRate=sdkMemberManager.createRate(rateCommand);  
		/*if (itemRate != null) {
			// 修改订单评论状态
			Integer row = orderManager.updateOrderLineEvaulationStatus(itemRate
					.getItemId(), itemRate.getOrderLineId());
			if (row <= 0) {
				throw new BusinessException(
						ErrorCodes.ORDERLINE_EVALUSTIONSTATUSE_ERROE);
			}
		}*/
	}

	@Override
	public Pagination<ItemRateCommand> findItemRateList(Page page,
			Long memberId, Integer evaluationStatus, String beginTime,
			String endTime) {
		Sort[] sorts=Sort.parse("ss.create_time desc"); 
		Pagination<ItemRateCommand> itemRateCommand =new Pagination<ItemRateCommand> ();
		try {
			Map<String, Object> searchParam = new HashMap<String, Object>();
			searchParam.put("evaluationStatus", evaluationStatus);
			searchParam.put("memberId", memberId);
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd");
		   if (Validator.isNotNullOrEmpty(beginTime)) {
			searchParam.put("beginTime", format.parse(beginTime));
		   }
			if (Validator.isNotNullOrEmpty(endTime)) {
				searchParam.put("endTime", format.parse(endTime));
			} 
		  itemRateCommand = sdkMemberManager.findItemRateList(page, sorts, searchParam);
		} catch (ParseException e) { 
			throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
		} 

		return itemRateCommand;
	}

	@Override
	public RateCommand findItemRateById(Long rateId ,Long memberId) {
		Map<String, Object> result = new HashMap<String, Object>();  
		// 验证用户是否存在
		MemberCommand memberCommand = sdkMemberManager.findMemberById(memberId);
		RateCommand rateCommand=null;
		if (memberCommand != null) {
			rateCommand=sdkMemberManager.findItemRateById(rateId); 
		}   
		return  rateCommand;
	}

	@Override
	public Pagination<RateCommand> findItemRateListByItemId(Page page,
			Long itemId, Sort[] sorts) {
		Pagination<RateCommand> itemRateCommandPage = sdkItemManager.findItemRateListByItemId(page, itemId, sorts);
		return itemRateCommandPage;
	}

	@Override
	public Integer findRateCountByItemCode(String itemCode) {
		return sdkItemManager.findRateCountByItemCode(itemCode);
	}
}
