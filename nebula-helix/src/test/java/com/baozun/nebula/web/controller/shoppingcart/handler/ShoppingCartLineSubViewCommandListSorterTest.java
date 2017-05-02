/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.shoppingcart.handler;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.Status;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.toDate;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.15
 */
public class ShoppingCartLineSubViewCommandListSorterTest{

    private ShoppingCartLineSubViewCommandListSorter shoppingCartLineSubViewCommandListSorter = new DefaultShoppingCartLineSubViewCommandListSorter();

    @Test
    public void testSort(){

        ShoppingCartLineSubViewCommand viewCommand1 = build(1L, Status.OUT_OF_STOCK, toDate("2017-04-29 11:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), null);
        ShoppingCartLineSubViewCommand viewCommand2 = build(2L, Status.NORMAL, toDate("2017-04-29 22:10:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), 1);
        ShoppingCartLineSubViewCommand viewCommand3 = build(3L, Status.ITEM_LIFECYCLE_OFF_SHELF, toDate("2017-04-29 14:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), null);
        ShoppingCartLineSubViewCommand viewCommand4 = build(4L, Status.OUT_OF_STOCK, toDate("2017-04-29 10:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), null);
        ShoppingCartLineSubViewCommand viewCommand5 = build(5L, Status.NORMAL, toDate("2017-04-29 22:20:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), 1);
        ShoppingCartLineSubViewCommand viewCommand6 = build(6L, Status.NORMAL, toDate("2017-04-29 23:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), null);

        ShoppingCartLineSubViewCommand viewCommand7 = build(7L, Status.OUT_OF_STOCK, toDate("2017-04-29 22:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), null);
        ShoppingCartLineSubViewCommand viewCommand8 = build(8L, Status.ITEM_LIFECYCLE_OFF_SHELF, toDate("2017-04-29 16:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), null);

        List<ShoppingCartLineSubViewCommand> shoppingCartLineSubViewCommandList = toList(//
                        viewCommand1,
                        viewCommand2,
                        viewCommand3,
                        viewCommand4,
                        viewCommand5,
                        viewCommand6,
                        viewCommand7,
                        viewCommand8);

        shoppingCartLineSubViewCommandListSorter.sort(shoppingCartLineSubViewCommandList);

        assertThat(
                        shoppingCartLineSubViewCommandList,

                        contains(//
                                        viewCommand5,
                                        viewCommand2,
                                        viewCommand6,

                                        viewCommand7,
                                        viewCommand1,
                                        viewCommand4,

                                        viewCommand8,
                                        viewCommand3
                        //
                        ));
    }

    @Test
    public void testSort1(){

        ShoppingCartLineSubViewCommand viewCommand5 = build(5L, Status.NORMAL, toDate("2017-04-29 22:20:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), 1);
        ShoppingCartLineSubViewCommand viewCommand6 = build(6L, Status.NORMAL, toDate("2017-04-29 12:20:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND), null);

        List<ShoppingCartLineSubViewCommand> shoppingCartLineSubViewCommandList = toList(viewCommand5, viewCommand6);

        shoppingCartLineSubViewCommandListSorter.sort(shoppingCartLineSubViewCommandList);

        assertThat(shoppingCartLineSubViewCommandList, contains(viewCommand5, viewCommand6));
    }

    /**
     * @param id
     * @param status
     * @param addTime
     * @param group
     * @return
     */
    private ShoppingCartLineSubViewCommand build(Long id,Status status,Date addTime,Integer group){
        ShoppingCartLineSubViewCommand shoppingCartLineSubViewCommand = new ShoppingCartLineSubViewCommand();
        shoppingCartLineSubViewCommand.setId(id);
        shoppingCartLineSubViewCommand.setStatus(status);
        shoppingCartLineSubViewCommand.setAddTime(addTime);
        shoppingCartLineSubViewCommand.setGroup(group);
        return shoppingCartLineSubViewCommand;
    }

}
