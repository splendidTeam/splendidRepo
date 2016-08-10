-- 菜单是一个树林的模式，需分层定义，由于菜单只有初始化数据，没有界面维护，因此这里采用定制id的模式，定制规则为：
-- 一级菜单编号为个位数， 二级菜单编号为三位数, 依此类推

INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (1, 1, 1, '', now(), null, 'product', '商品管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (2, 1, 2, '', now(), null, 'sales', '交易管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (3, 1, 3, '', now(), null, 'promotion', '活动管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (4, 1, 4, '', now(), null, 'member', '会员管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (5, 1, 5, '', now(), null, 'operation', '运营管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (6, 1, 6, '', now(), null, 'baseinfo', '基础信息管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (7, 1, 7, '', now(), null, 'system', '系统管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (8, 1, 8, '', now(), null, 'log', '日志管理');


-- 商品管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (110, 1, 1, '/product/category/manager.htm', now(), 1, null, '商品分类管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (111, 1, 2, '/item/itemList.htm', now(), 1, null, '商品管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (112, 1, 3, '/item/itemCategory.htm', now(), 1, null, '商品归类');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (113, 1, 4, '/item/itemCategorySort.htm', now(), 1, null, '商品排序');


--商品搜索条件管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (114, 1, 5, '/item/itemSearchCondition/manager.htm', now(), 1, null, '商品搜索条件管理');
--索引管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (115, 1, 2, '/item/itemSolrSetting/manager.htm', now(), 1, null, '索引管理');
-- 商品筛选器管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (116, 1, 5, '/product/productcombolist.htm', now(), 1, 'product', '商品筛选器管理');



-- 交易管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (210, 1, 1, '/order/orderList.htm', now(), 2, null, '订单列表');


-- 活动管理

-- 会员管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (410, 1, 1, '/group/memberGroup.htm', now(), 4, null, '会员分组管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (411, 1, 2, '/member/memberList.htm', now(), 4, null, '会员管理');

-- 会员筛选器管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (412, 1, 1, '/member/membercombolist.htm', now(), 4, 'member', '人群筛选器管理');


-- 运营管
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (510, 1, 1, '/product/itemEvaluateListe.htm', now(), 5, null, '评价管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (511, 1, 2, '/consultant/consultantList.htm', now(), 5, null, '咨询管理');

--线下店铺管理
insert into t_au_menu(id, label, lifecycle, sort_no, url, version, parent_id) values(nextval('s_t_au_menu'), '线下店铺管理', 1, 1, '/offlineStore/store.htm', now(), 5);
insert into t_au_privilege(id, acl, description, group_name, lifecycle, name, version, org_type_id) values(nextval('s_t_au_privilege'), 'ACL_SYS_OFFLINE-SHOP_MANAGE', '线下店铺管理', '运营管理', 1, '线下店铺管理', now(), 2);
insert into t_au_privilege_url(id, description, url, pri_id) values(nextval('s_t_au_privilege_url'), '线下店铺管理', '/offlineStore/store.htm', (SELECT MAX(id) FROM t_au_privilege));
insert into t_au_role_privilege(id, pri_id, role_id, version) values(nextval('s_t_au_role_privilege'), (SELECT MAX(id) FROM t_au_privilege), 2, now());


-- 基础信息管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (610, 1, 1, '/industry/industryList.htm', now(), 6, null, '行业管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (611, 1, 2, '/property/propertyList.htm', now(), 6, null, '行业商品属性管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (612, 1, 3, '/shop/shopList.htm', now(), 6, null, '店铺管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (613, 1, 4, '/base/navigation.htm', now(), 6, null, '菜单导航管理');

INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (698, 1, 8, '', now(), 6, null, '推荐管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (617, 1, 8, '/system/publicRecommandManager.htm', now(), 698, null, '公共推荐管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (619, 1, 9, '/system/categoryRecommandManager.htm', now(), 698, null, '分类推荐管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (620, 1, 10, '/system/itemRecommandManager.htm', now(), 698, null, '商品搭配管理');

INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (621, 1, 11, '/newcms/pageTemplateList.htm', now(), 6, null, '页面生成器');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (622, 1, 12, '/freight/distributionMode.htm', now(), 6, null, '物流方式');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (623, 1, 1, '/freight/shippingTemeplateList.htm', now(), 6, null, '运费模板管理');


-- 系统管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (710, 1, 1, '', now(), 7, 'sys-auth', '系统权限管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (711, 1, 2, '/option/optionGroupList.htm', now(), 7, null, '通用选项管理');

-- 定时任务管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (712, 1, 3, '/system/schedulerTask/manager.htm', now(), 7, null, '定时任务管理');

-- 自定义筛选器条件注册管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (713, 1, 4, '/rule/csfilterclseslist.htm', now(), 7, null, '自定义筛选器条件注册管理');

-- 系统权限管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (71010, 1, 1, '/user/list.htm', now(), 710, null, '用户管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (71011, 1, 2, '/role/list.htm', now(), 710, null, '角色管理');

-- 促销活动管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (512, 1, 1, '', now(), 5, 'system', '促销活动管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (51210, 1, 1, '/promotion/promotionEdit.htm', now(), 512, null, '促销活动编辑管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (51211, 1, 2, '/promotion/promotionList.htm', now(), 512, null, '促销活动启用管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (51212, 1, 3, '/promotion/promotionpriorityadjust.htm', now(), 512, null, '促销活动优先级管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (51213, 1, 4, '/promotion/couponimport.htm', now(), 512, null, '优惠码导入维护');

-- 限购管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (513, 1, 1, '', now(), 5, 'system', '限购管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (51310, 1, 1, '/limitation/limitationEditList.htm', now(), 513, null, '限购编辑管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (51311, 1, 2, '/limitation/limitationPublishList.htm', now(), 513, null, '限购启用管理');
-- 插入 优惠劵类型菜单 菜单 挂在促销活动管理 下512
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (51214, 1, 5, '/coupon/coupon.htm', now(), 512, null, '优惠劵类型管理');

--默认排序引擎管理 菜单
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (120, 1, 12, '/item/sortScore.htm', now(), 1, null, '默认排序引擎管理');
--邮件模板管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (615, 1, 10, '/email/list.htm', now(), 6, null, '邮件模板管理');


--告警设置
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (720, 1, 20, '/warningConfig/list.htm', now(), 7, null, '告警设置');

--系统参数配置管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (721, 1, 21, '/mataInfo/list.htm', now(), 7, null, '系统参数配置管理');
-- 模块生成器
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (631, 1, 20, '/newcms/moduleTemplateList.htm', now(), 6, null, '模块生成器');
-- 商品可见性管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (121, 1, 20, '/itemVisibility/list.htm', now(), 1, null, '商品可见性管理');

-- 菜单管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (71020, 1, 20, '/menu/list.htm', now(), 710, null, '菜单管理');
-- 权限管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (71021, 1, 20, '/auth/list.htm', now(), 710, null, '权限管理');
-- 国际化语言管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (741, 1, 20, '/i18nLang/list.htm', now(), 7, null, '国际化语言管理');

-- 日志管理
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (810, 1, 1, '/backlog/skuInventoryChangeLog/list.htm', now(), 8, null, '库存变更日志管理');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (811, 1, 2, '/backlog/sysAuditLog/list.htm', now(), 8, null, '审计日志查询列表');
INSERT INTO "t_au_menu"("id" ,"lifecycle","sort_no","url","version","parent_id","icon","label") VALUES (nextval('s_t_au_menu'), 1, 4, '/backlog/schedulerLog/list.htm', now(), 14, null, '定时任务运行日志');