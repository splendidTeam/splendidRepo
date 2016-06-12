-- Privilege 是按照功能项一个个定义的，定义按功能分组分块，每块中再分系统和店铺两种组织类型
-- 这里记录的都是用户必须登录后并分配访问权限的功能项，初始化方式按先初始化功能项，再初始化对应URL的方式进行
-- 功能URL需符合设计规范
-- 选项CODE定义规范为：ACL_[SYS/STO]_[ENTITY]_[ACTION]
-- SYS代表对应组织类型为系统，STO代表对应组织为店铺
-- ENTITY代表你要操作的实体类型，如果是短语使用简写或者_分割方式，如 SKU_CAT代表商品分类
-- ACTION代表功能行为，几个标准行为：ADD/DELETE/EDIT/MANAGE/VIEW, 其余非标准行为另外定义，采用_分割描述，如 QUICK_REVIEW
-- Privilege URL中对每个URL需在描述中写明使用场景，如 异步加载会员列表数据
-- 同一URL数据可能在多个Privilege对应的URL数据中存在，因为多个功能可能会复用一个URL请求

-- ####系统
-- ##系统：系统组织
-- 系统角色管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_ROLE_MANAGE','系统角色管理','管理PTS系统中的角色，可以在此功能中新增/修改/删除/查看角色',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/role/list.htm','查看角色列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/role/list.json','查询/刷新角色列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/role/create.htm','进入创建角色页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/role/update.htm','进入修改角色页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/role/delete.json','删除指定角色/角色列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/role/save.json','创建/修改角色',currval('s_t_au_privilege'));
-- 系统用户管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_USER_MANAGE','系统用户管理','管理PTS系统中的用户，可以在此功能中新增/修改/删除/查看用户',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/list.htm','查看用户列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/list.json','查询/刷新用户列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/to-create.htm','进入创建用户页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/to-update.htm','进入修改用户页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/view.htm','查看用户信息',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/enable-or-disable.json','启用/禁用用户',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/create.htm','创建用户',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/update.htm','修改用户',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/save-user-role.json','增加/修改用户角色关系',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/user/remove-user-role.json','删除用户角色关系',currval('s_t_au_privilege'));

-- 通用选项管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_OPTION_MANAGE','通用选项管理','管理PTS系统中的选项列表和数据',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/option/optionGroupList.htm','查看通用选项列表',currval('s_t_au_privilege'));
-- TODO 补充通用选项管理权限

-- 自定义筛选器条件注册管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_CUSTOMFILTERCLS_MANAGER','自定义筛选器条件注册管理','自定义筛选器条件注册管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/rule/csfilterclseslist.htm','自定义筛选器条件注册管理',currval('s_t_au_privilege'));

-- ##系统：店铺组织

-- ####基础信息
-- ##基础信息：系统组织
-- 行业管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_INDUSTRY_MANAGE','行业管理','管理行业列表，一个商城至少需要一个行业',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/industry/industryList.htm','查看行业列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/industry/saveIndustry.json','新增/修改行业',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/industry/removeIndustry.json','删除行业',currval('s_t_au_privilege'));

-- 行业商品属性管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_PROD_PROP_MANAGE','行业商品属性管理','定义行业相关的商品属性，相当于为一个行业的商品数据定义数据模板',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/property/propertyList.htm','查看行业商品属性列表',currval('s_t_au_privilege'));
-- TODO 补充行业商品属性管理权限

-- 店铺管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_STORE_MANAGE','店铺管理','管理店铺列表，一个商城至少需要一个店铺，如果存在多个店铺购物车会根据店铺拆单',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/shopList.htm','查看店铺列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/shopList.json','查询/刷新店铺列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/createShop.htm','进入创建店铺页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/updateShop.htm','进入修改店铺页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/enableOrDisableShop.json','启用/禁用店铺',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/shopPropertymanager.htm','设置店铺自定义行业属性',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/saveShop.json','创建/修改店铺信息',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/propertyList.json','查询/刷新店铺自定义行业属性列表',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/createProperty.htm','进入创建店铺自定义行业属性页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/updateProperty.htm','进入修改店铺自定义行业属性页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/removeProperty.json','删除店铺自定义行业属性',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/enableOrDisableProperty.json','启用/禁用店铺自定义行业属性',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/saveProperty.json','创建/修改店铺自定义行业属性',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/propertyValueList.htm','进入设置店铺自定义行业属性可选值页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/shop/savePropertyValue.json','创建/修改店铺自定义行业属性可选值',currval('s_t_au_privilege'));

-- 商城导航管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_NAVIGATION_MANAGE','商城导航管理','管理商城的导航菜单',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/base/navigation.htm','进入导航管理页',currval('s_t_au_privilege'));
--TODO 补充商城导航管理权限

-- ##基础信息：店铺组织

-- ####商品
-- ##商品：系统组织
-- 商品分类管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_SYS_PROD_CATEGORY_MANAGE','商品分类管理','管理商城全局的商品分类，是一个典型的树状结构',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/category/manager.htm','进入商品分类管理页',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/category/updateCategory.json','修改商品分类',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/category/removeCategory.json','删除商品分类',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/category/dropCategory.json','拖曳商品分类',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/category/addLeafCategory.json','增加子商品分类',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/category/insertSiblingCategory.json','增加同级商品分类',currval('s_t_au_privilege'));

-- 商品排序管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_SYS_PROD_SORT_MANAGE','商品排序管理','基于商品分类定义商品显示顺序',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemCategorySort.htm','进入商品排序管理页',currval('s_t_au_privilege'));
-- TODO 补充商品排序管理权限

--搜索条件管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_ITEM_SEARCHCONDITION_MANAGER','商品搜索条件管理','商品搜索条件管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/manager.htm','进入商品搜索条件管理页',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/searchConditionList.json','进入商品搜索条件分页查询',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/findPropertyByIndustryId.json','查询对应行业ID下的Property',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/endisableSearchCondition.json','启用/禁用搜索条件',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/removeSearchCondition.json','删除搜索条件',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/removeSearchConditionByIds.json','批量删除对应ID的搜索条件',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/managerSetting.htm','搜索条件选项设置页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/searchConditionItemList.json','搜索条件选项分页查询',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/endisableSearchConditionItem.json','启用/禁用搜索条件',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/removeSearchConditionItem.json','删除搜索条件',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/removeSearchConditionItemByIds.json','批量删除搜索条件',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/managerAddCondition.htm','添加/修改搜索条件页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/managerAddConditionItem.htm','添加/修改搜索条件选项页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/managerSaveCondition.htm','保存搜索条件页面',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSearchCondition/managerSaveConditionItem.htm','保存搜索条件选项',currval('s_t_au_privilege'));

--索引管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品相关','ACL_STO_ITEM','索引管理','索引管理',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemSolrSetting/manager.htm','索引管理',currval('s_t_au_privilege'));

-- ##商品：店铺组织
-- 商品管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_STO_PROD_MANAGE','商品管理','编辑商品',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemList.htm','查看店铺对应的商品列表',currval('s_t_au_privilege'));
-- TODO 补充商品管理权限

-- 分类商品
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_STO_SEGMENT_PROD','分类商品','确定每个商品的类别所属',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/itemCategory.htm','进入分类商品的初始页面',currval('s_t_au_privilege'));
-- TODO 补充分类商品权限

-- ####会员
-- ##会员：系统组织
-- 会员分组管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'会员','ACL_SYS_MEMBER_GROUP_MANAGE','会员分组管理','管理会员分组和调整会员所属的分组',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/group/memberGroup.htm','进入会员分组管理的初始页面',currval('s_t_au_privilege'));
-- TODO 补充会员分组管理权限

-- 会员筛选器管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'会员','ACL_SYS_MEMBER_COMBO_QUERY','人群筛选器管理','人群筛选器管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/member/membercombolist.htm','进入人群筛选器查询的初始页面',currval('s_t_au_privilege'));



insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'会员','ACL_SYS_MEMBER_COMBOEDIT_MANAGE','会员筛选器创建','会员筛选器创建',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/member/membercomboedit.htm','进入会员筛选器创建的初始页面',currval('s_t_au_privilege'));

insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'会员','ACL_SYS_MEMBER_COMBOUPDATE_MANAGE','会员筛选器编辑','会员筛选器编辑',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/member/combo/update.htm','进入会员筛选器编辑的初始页面',currval('s_t_au_privilege'));


-- 商品筛选器管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_SYS_PRODUCT_COMBO_QUERY','商品筛选器管理','商品筛选器管理',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/productcombolist.htm','进入人群筛选器查询的初始页面',currval('s_t_au_privilege'));

insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_SYS_PRODUCT_COMBOEDIT_MANAGE','商品筛选器编辑','商品筛选器编辑',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/productcomboedit.htm','进入商品筛选器编辑的初始页面',currval('s_t_au_privilege'));


-- 促销活动管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'促销','ACL_SYS_PROMOTION_EDIT_LIST','促销活动编辑管理','促销活动编辑管理',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/promotion/promotionEdit.htm','进入商品筛选器编辑的初始页面',currval('s_t_au_privilege'));

insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'促销','ACL_SYS_PROMOTION_PUBLISH_LIST','促销活动启用管理','促销活动启用管理',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/promotion/promotionList.htm','进入商品筛选器编辑的初始页面',currval('s_t_au_privilege'));

insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'促销','ACL_SYS_PROMOTION_ADJUSTPRIORITY_LIST','促销活动优先级调整','促销活动优先级调整',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/promotion/promotionpriorityadjust.htm','进入商品筛选器编辑的初始页面',currval('s_t_au_privilege'));

-- 优惠码管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'促销','ACL_SYS_PROMOTION_COUPON_IMPORT','优惠码导入','优惠码导入',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/promotion/couponimport.htm','进入优惠码导入',currval('s_t_au_privilege'));
-- 优惠劵类型菜单
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'促销','ACL_SYS_PROMOTION_COUPON_TYPE_LIST','优惠劵类型管理','优惠劵类型管理',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/coupon/coupon.htm','进入优惠劵类型管理',currval('s_t_au_privilege'));
-- 限购管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'限购管理','ACL_SYS_LIMITATION_EDIT_LIST','限购编辑列表','限购编辑列表',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/limitation/limitationEditList.htm','进入限购编辑列表初始页面',currval('s_t_au_privilege'));

insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'限购管理','ACL_SYS_LIMITATION_PUBLISH_LIST','限购启用列表','限购启用列表',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/limitation/limitationPublishList.htm','进入限购启用列表初始页面',currval('s_t_au_privilege'));

-- 会员管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'会员','ACL_SYS_MEMBER_MANAGE','会员管理','查看会员信息',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/member/memberList.htm','查看会员列表',currval('s_t_au_privilege'));
-- TODO 补充会员管理权限

-- ##会员：店铺组织

-- ####销售
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'交易','ACL_SYS_ORDER_MANAGE','订单列表','查看订单列表',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/order/orderList.htm','查看订单列表',currval('s_t_au_privilege'));

-- ####活动

-- ####运营服务
-- ##运营服务：系统组织
-- 评价管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'运营服务','ACL_SYS_COMMENT_MANAGE','评价管理','审核用户评价',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/product/itemEvaluateListe.htm','进入评价管理的初始页面',currval('s_t_au_privilege'));
-- TODO 补充评价管理权限

-- 咨询管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'运营服务','ACL_SYS_CONSULTANT_MANAGE','咨询管理','回复用户咨询和发布用户咨询',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/consultant/consultantList.htm','进入咨询管理的初始页面',currval('s_t_au_privilege'));
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'运营服务','ACL_SYS_CONSULTANT_OPERATION','咨询管理操作','回复用户咨询和发布用户咨询',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/consultant/resolveConsultant.json','回复咨询',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/consultant/batchPublishConsultant.json','批量公示',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/consultant/batchUnpublishConsultant.json','批量取消公示',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/consultant/unpublishConsultant.json','取消公示',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/consultant/publishConsultant.json','公示',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/consultant/updateConsultant.json','更新回复',currval('s_t_au_privilege'));
-- TODO 补充咨询管理权限

-- ##运营服务：店铺组织

-- ####其他
-------------------推荐商品管理
-- 公共推荐管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_PUBLIC_RECOMMAND','公共推荐管理','公共推荐管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/system/publicRecommandManager.htm','公共推荐管理',currval('s_t_au_privilege'));
--分类推荐管理 
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_CATEGORY_RECOMMAND','分类推荐管理','分类推荐管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/system/categoryRecommandManager.htm','分类推荐管理',currval('s_t_au_privilege'));
--商品搭配管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_ITEM_RECOMMAND','商品搭配管理','商品搭配管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/system/itemRecommandManager.htm','商品搭配管理',currval('s_t_au_privilege'));

insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'会员','ACL_SYS_SCHEDULER_TASK_QUERY','定时任务管理','定时任务管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/system/schedulerTask/manager.htm','进入定时任务查询的初始页面',currval('s_t_au_privilege'));

-- 页面生成器
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_PAGE_TEMPLATE','页面生成器','页面生成器',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/newcms/pageTemplateList.htm','页面生成器',currval('s_t_au_privilege'));

-- 物流方式
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_LOGISTICS','物流方式','物流方式',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/freight/distributionMode.htm','物流方式',currval('s_t_au_privilege'));
-- 运费管理
insert into t_au_privilege("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_SHIPPING','运费模板管理','运费模板管理',1,now(),2);
insert into t_au_privilege_url("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/freight/shippingTemeplateList.htm','运费模板管理',currval('s_t_au_privilege'));

--默认排序引擎管理 菜单
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品相关','ACL_SYS_ITEM_SORT_SCORE_LIST','默认排序引擎管理','默认排序引擎管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/item/sortScore.htm','默认排序引擎管理',currval('s_t_au_privilege'));

--邮件模板管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'基础信息','ACL_SYS_EMAIL_TEMPLATE_LIST','邮件模板管理','邮件模板管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/email/list.htm','邮件模板管理',currval('s_t_au_privilege'));


--告警设置
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_WARNING_CONFIG','告警设置','告警设置',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/warningConfig/list.htm','告警设置',currval('s_t_au_privilege'));
--系统参数配置管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_META_CONFIG_LIST','系统参数配置管理','系统参数配置管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/mataInfo/list.htm','系统参数配置管理',currval('s_t_au_privilege'));
-- 模块生成器
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_MODULE_LIST','模块生成器','模块生成器',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/newcms/moduleTemplateList.htm','模块生成器',currval('s_t_au_privilege'));
-- 商品可见性管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'商品','ACL_ITEM_VISIBILITY','商品可见性管理','商品可见性管理',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/itemVisibility/list.htm','商品可见性管理',currval('s_t_au_privilege'));

-- 菜单管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_MENU','菜单管理','菜单管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/menu/list.htm','菜单管理',currval('s_t_au_privilege'));
-- 权限管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_SYS_AUTH','权限管理','权限管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/auth/list.htm','权限管理',currval('s_t_au_privilege'));


-- 国际化语言管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'系统','ACL_I18N_MANAGE','国际化语言管理','国际化语言管理',1,now(),1);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/i18nLang/list.htm','国际化语言管理',currval('s_t_au_privilege'));

-- 日志管理
insert into "t_au_privilege"("id","group_name","acl","name","description","lifecycle","version","org_type_id") values(nextval('s_t_au_privilege'),'运营服务','ACL_BACKLOG_SKUINVENTORY_CHANGE','库存变更日志管理','库存变更日志管理',1,now(),2);
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/backlog/skuInventoryChangeLog/list.htm','库存变更日志管理',currval('s_t_au_privilege'));
insert into "t_au_privilege_url"("id","url","description","pri_id") values(nextval('s_t_au_privilege_url'),'/backlog/skuInventoryChangeLog/page.json','库存变更日志列表',currval('s_t_au_privilege'));

