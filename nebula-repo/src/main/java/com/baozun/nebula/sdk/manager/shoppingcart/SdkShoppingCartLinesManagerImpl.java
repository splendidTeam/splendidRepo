package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitRuleFilterManager;

@Transactional
@Service("sdkShoppingCartLinesManager")
public class SdkShoppingCartLinesManagerImpl implements SdkShoppingCartLinesManager {
	
	private final static Logger log = LoggerFactory.getLogger(SdkShoppingCartLinesManagerImpl.class);
	
	@Autowired
	private SdkEngineManager sdkEngineManager;

	@Autowired
	private SdkPurchaseLimitRuleFilterManager sdkPurchaseRuleFilterManager;

	
	/**
	 * 获取当前购物车中的直推的赠品行是否有效
	 * @param allGroupedLines
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<Integer, List<ShoppingCartLineCommand>> getShoppingCartForceSendGiftLines(List<ShoppingCartLineCommand> allGroupedLines, List<LimitCommand> purchaseLimitationList, ShoppingCartCommand shoppingCart){
		
		if (null==allGroupedLines || allGroupedLines.size()==0){
			return null;
		}
		
		//List<ShoppingCartLineCommand> outOfStockForceSendGiftLines = new ArrayList<ShoppingCartLineCommand>();
		// 获取购物车中所有的直推赠品行
		List<ShoppingCartLineCommand> giftLinesList = new ArrayList<ShoppingCartLineCommand>();
		for(ShoppingCartLineCommand line : allGroupedLines){
			if(null == line || line.isCaptionLine()){
				continue;
			}
			
			if(line.isGift() && GiftChoiceType.NoNeedChoice.equals(line.getGiftChoiceType())){
				giftLinesList.add(line);
			}
		}
		
		Integer retval = 0;
		
		Map<Integer, List<ShoppingCartLineCommand>> outOfStockForceGiftMap = new HashMap<Integer, List<ShoppingCartLineCommand>>();
		
		for(ShoppingCartLineCommand line : giftLinesList){
				
			retval = sdkEngineManager.doEngineGiftCheck(line, false, shoppingCart, purchaseLimitationList);
			// 检查商品有效性
			if (Constants.CHECK_INVENTORY_FAILURE.equals(retval)) {
				// 库存不足
				processOutOfStockForceGiftMap(Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM, line, outOfStockForceGiftMap);
			} 
			else if (Constants.CHECK_ITEM_SKU_FAILURE.equals(retval)) {
				// 订单或购物车的商品包含已暂停销售的尺寸或颜色
				processOutOfStockForceGiftMap(Constants.CONTAINS_NOTVALID_ITEM, line, outOfStockForceGiftMap);
			}
			else if (new Integer(1).equals(line.getValidType()) && !line.isValid()) {
				// 有效性检验失败
				processOutOfStockForceGiftMap(Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM, line, outOfStockForceGiftMap);
			}
		}
		
		log.debug("processOutOfStockForceGiftMap have {}", outOfStockForceGiftMap);
		return outOfStockForceGiftMap;
	}
	
	/**
	 * 获取当前购物车中库存不足的赠品行
	 * @param allGroupedLines
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ShoppingCartLineCommand> getOutOfStockShoppingCartUserChoiceGiftLines(List<ShoppingCartLineCommand> allGroupedLines)
	{
		if (null==allGroupedLines || allGroupedLines.size()==0)
			return null;
		List<ShoppingCartLineCommand> outOfStockLines = getOutOfStockShoppingCartLines(allGroupedLines);
		if (null==outOfStockLines || outOfStockLines.size()==0)
			return null;
		List<ShoppingCartLineCommand> outOfStockGiftLines = new ArrayList<ShoppingCartLineCommand>();
		for(ShoppingCartLineCommand line:allGroupedLines)
		{
			if (line.isCaptionLine())
				continue;
			if (line.isGift()&&!GiftChoiceType.NoNeedChoice.equals(line.getGiftChoiceType()))
				outOfStockGiftLines.add(line);
		}
		return outOfStockGiftLines;
	}
	
	/**
	 * 获取当前购物车中库存不足的行
	 * @param allGroupedLines
	 * @return
	 */
	@Override
	public List<ShoppingCartLineCommand> getOutOfStockShoppingCartLines(List<ShoppingCartLineCommand> allGroupedLines)
	{
		List<ShoppingCartLineCommand> linesOutOfStock = new ArrayList<ShoppingCartLineCommand>();
		for(ShoppingCartLineCommand line:allGroupedLines)
		{
			if (line.isCaptionLine())
				continue;

			if (line.getQuantity()==null ||line.getQuantity()==0)
				linesOutOfStock.add(line);
			else if (line.getStock()==null ||line.getStock()==0)
				linesOutOfStock.add(line);
			else if (line.getQuantity()>line.getStock())
				linesOutOfStock.add(line);
		}
		return linesOutOfStock;
	}
	
	
	/**
	 * 
	 * @param errorCode
	 * @param line
	 * @param outOfStockForceGiftMap
	 */
	private Map<Integer, List<ShoppingCartLineCommand>> processOutOfStockForceGiftMap(Integer errorCode, ShoppingCartLineCommand line, Map<Integer, List<ShoppingCartLineCommand>> outOfStockForceGiftMap){
		List<ShoppingCartLineCommand> tmpLineList = outOfStockForceGiftMap.get(errorCode);
		if( tmpLineList != null){
			tmpLineList.add(line);
		}else{
			tmpLineList = new ArrayList<ShoppingCartLineCommand>();
			tmpLineList.add(line);
		}
		outOfStockForceGiftMap.put(errorCode, tmpLineList);
		return outOfStockForceGiftMap;
	}
}
