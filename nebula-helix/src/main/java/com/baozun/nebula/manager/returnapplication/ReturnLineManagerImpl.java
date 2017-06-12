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
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.returnapplication.SoReturnApplicationManager;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnLineViewCommand;

/**
 * @author yaohua.wang@baozun.cn
 *
 */
@Service("returnLineManager")
public class ReturnLineManagerImpl implements ReturnLineManager{

    @Autowired
    private SdkOrderLineDao sdkOrderLineDao;

    @Autowired
    private SdkSkuManager sdkSkuManager;

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private SoReturnApplicationManager soReturnApplicationManager;

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
                Integer count = soReturnApplicationManager.countCompletedAppsByPrimaryLineId(line.getId());
                //剩余可退数量
                lineView.setCount(line.getCount() - count);
                lineView.setOrderLineCommand(line);
                soReturnLineViews.add(lineView);
            }
        }
        return soReturnLineViews;

    }

}
