INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'cancelOrderTaskManager', 'cancel_orders', '45分钟取消订单', 1, 'cancelOrders', '0 0/5 * * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'syncInventoryManager', 'sync_inventory_inc', '库存增量同步', 1, 'syncIncrementInventory', '0 3/5 * * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'syncSalesOrderManager', 'sync_order_status', 'oms订单状态同步到商城', 1, 'syncSoStatus', '0 4/5 * * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'messageCompensateManager', 'message_Compensate', '消息补偿机制', 1, 'timeMessageCompensate', '0 0/5 * * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'commonTimingManager', 'propelling_on_sale_product', '添加在售商品补偿记录的定时器', 1, 'addOnSalesProductRecord', '0 0 1 * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'syncItemManager', 'sync_product_price', '同步商品价格 SCM推送商品价格到商城', 1, 'syncItemPrice', '0 2/5 * * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'syncItemManager', 'sync_sku_base_info', '同步商品基础信息 scm推送商品信息到商城', 1, 'syncBaseInfo', '0 1/5 * * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'solrRefreshManager', 'solr_refresh', 'solr定时刷新', 1, 'refresh', '0 0 3 * * ?', NULL);
INSERT INTO "t_sys_scheduler_task" VALUES (nextval('S_T_SYS_SCHEDULER_TASK'), 'syncLogisticManager', 'sync_logistic_info', 'SCM同步物流信息到商城', 1, 'syncLogisticInfo', '0 0 2 * * ?', NULL);
