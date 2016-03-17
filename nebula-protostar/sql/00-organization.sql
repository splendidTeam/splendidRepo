INSERT INTO "t_au_org_type"("id","description","lifecycle","name","version") VALUES ('1', '系统组织类型', '1', '系统', now());
INSERT INTO "t_au_org_type"("id","description","lifecycle","name","version") VALUES ('2', '店铺组织类型', '1', '店铺', now());

INSERT INTO "t_au_organization"("id","code","description","lifecycle","name","parent_id","org_type_id","version") VALUES (1, '1000', '系统', '1', '系统', null, '1', now());

select setval('S_T_AU_ORGANIZATION',10);