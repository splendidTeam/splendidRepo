/**
 * 
 */
package com.baozun.nebula.manager.returnapplication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationManager;
import com.baozun.nebula.web.controller.returnapplication.viewcommand.ReturnLineViewCommand;

/**
 * @author yaohua.wang@baozun.cn
 *
 */
@Service("returnLineManager")
public class ReturnApplicationLineManagerImpl implements ReturnApplicationLineManager{

    @Autowired
    private SdkOrderLineDao sdkOrderLineDao;

    @Autowired
    private SdkSkuManager sdkSkuManager;

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private SdkReturnApplicationManager sdkReturnApplicationManager;

    @Override
    public List<ReturnLineViewCommand> findReturnLineViewCommandByLineIds(List<Long> orderLineIds){
        List<OrderLineCommand> orderLineCommands = sdkOrderLineDao.findOrderDetailListByIds(orderLineIds);
        List<ReturnLineViewCommand> soReturnLineViews = new ArrayList<ReturnLineViewCommand>();
        for (OrderLineCommand line : orderLineCommands){
            ReturnLineViewCommand lineView = new ReturnLineViewCommand();
            String properties = line.getSaleProperty();
            List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
            line.setSkuPropertys(propList);
            Long itemId = line.getItemId();
            //通过itemId查询同款商品的其他尺码
            List<Sku> skuList = skuDao.findSkuByItemId(itemId);
            List<SkuCommand> skuCommandList = new ArrayList<SkuCommand>();
            for (Sku sku : skuList){
                SkuCommand skuCommand = skuDao.findInventoryById(sku.getId());
                skuCommandList.add(skuCommand);
            }
            //可供换的skuCommand集合
            lineView.setChgSkuCommandList(skuCommandList);
            if (null != line.getType() && line.getType() != 0){
                // 查询 当前订单行 已经退过货的商品个数（退换货状态为已完成)
                Integer count = sdkReturnApplicationManager.countCompletedAppsByPrimaryLineId(line.getId(),new Integer[]{ ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE });
                //剩余可退数量
                lineView.setCount(line.getCount() - count);
                lineView.setOrderLineCommand(line);
                soReturnLineViews.add(lineView);
            }
        }
        return soReturnLineViews;

    }

}
