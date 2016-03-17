INSERT INTO "t_au_user"("id","create_time","email","latest_access_time","latest_update_time","lifecycle","memo","mobile","password","realname","user_name","version","org_id") VALUES (nextval('s_t_au_user'), now(), 'admin@baozun.cn', now(), now(), 1, null, '15121133000', 'e0b766b04875ba02b82d90133bf2ecae7b4e76840e3179281a1123144cea4019', '系统管理员', 'admin', now(), '1');

INSERT INTO "t_au_role"("id","description","lifecycle","name","version","org_type_id") VALUES (nextval('S_T_AU_ROLE'), '系统默认的系统管理员', 1, '系统管理员', now(), 1);

INSERT INTO "t_au_role_privilege"("id","version","pri_id","role_id") VALUES (nextval('S_T_AU_ROLE_PRIVILEGE'), now(), (select id from t_au_privilege where acl='ACL_SYS_ROLE_MANAGE'), currval('S_T_AU_ROLE'));
INSERT INTO "t_au_role_privilege"("id","version","pri_id","role_id") VALUES (nextval('S_T_AU_ROLE_PRIVILEGE'), now(), (select id from t_au_privilege where acl='ACL_SYS_USER_MANAGE'), currval('S_T_AU_ROLE'));

INSERT INTO "t_au_user_role"("id","version","org_id","role_id","user_id") VALUES (nextval('S_T_AU_USER_ROLE'), now(), 1, currval('S_T_AU_ROLE'), currval('s_t_au_user'));

INSERT INTO "public"."t_prm_customizefilterclasses" ("id", "cache_second", "cacheversion", "lifecycle", "scope_name", "scope_type", "service_name", "shop_id", "version") VALUES (nextval('t_prm_customizefilterclasses'), '0', '2015-07-13 13:15:28.264', '1', '自定义Voucher条件', '3', 'uaCustomCouponCondition', '1', now());
INSERT INTO "public"."t_prm_customizefilterclasses" ("id", "cache_second", "cacheversion", "lifecycle", "scope_name", "scope_type", "service_name", "shop_id", "version") VALUES (nextval('t_prm_customizefilterclasses'), '0', '2015-07-10 11:40:03.721', '1', '自定义Voucher优惠设置', '4', 'uaCustomCouponSetting', '1', now());
