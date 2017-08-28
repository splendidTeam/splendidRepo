package com.baozun.nebula.web.controller.returnapplication.resolver;

import java.util.List;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;

/**
 * 用于构建退换货的ReturnApplicationCommand对象
 * @author zl.shi 处理退换货构建
 *
 */
public interface ReturnApplicationBuilder {

	List<ReturnApplicationCommand> buildReturnApplicationCommands(MemberDetails memberDetails,
			ReturnOrderForm returnOrderForm, SalesOrderCommand salesOrder);

}
