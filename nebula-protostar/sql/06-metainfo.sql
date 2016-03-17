INSERT INTO "t_sys_mata_info" VALUES (nextval('S_T_SYS_MATA_INFO'), 'hasStyle', 1, 'false', now());

INSERT INTO "t_sys_mata_info" VALUES (nextval('S_T_SYS_MATA_INFO'), 'hasCache', 1, 'false', now());

-- 哪些商品应该做为促销进行计算 ALL、CHECKED、AVAILABLE、CHECKEDANDAVAILABLE，默认为CHECKED
INSERT INTO "t_sys_mata_info" VALUES (nextval('S_T_SYS_MATA_INFO'), 'SC_CALC_LEVEL', 1, 'CHECKED', now());

-- 返回价格，数量，是否包含未进行促销的行,TRUE以及FALSE，默认为FALSE
INSERT INTO "t_sys_mata_info" VALUES (nextval('S_T_SYS_MATA_INFO'), 'SC_RETURN_PRICE_NUM_SUM', 1, 'FALSE', now());

-- 是否通过商品图片类型分组显示
INSERT INTO "t_sys_mata_info" VALUES (nextval('S_T_SYS_MATA_INFO'), 'IS_IMAGE_TYPE_GROUP', 1, 'false', now());

--商品可见性
INSERT INTO "t_sys_mata_info" VALUES (nextval('S_T_SYS_MATA_INFO'), 'product.visibility', 1, 'true', now());

--国际化开关
INSERT INTO "t_sys_mata_info" VALUES (nextval('S_T_SYS_MATA_INFO'), 'i18n.on.off', 1, 'false', now());