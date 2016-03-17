package com.baozun.nebula.manager.i18n;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.baozun.nebula.model.i18n.MutlLangResource;
public class MutlLangResourceRowMapper implements  RowMapper<MutlLangResource> {

	@Override
	public MutlLangResource mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		MutlLangResource mutlLangResource = new MutlLangResource();
		mutlLangResource.setId(rs.getLong("id"));
		mutlLangResource.setClassName(rs.getString("class_name"));
		mutlLangResource.setTableName(rs.getString("table_name"));
		mutlLangResource.setFieldName(rs.getString("field_name"));
		mutlLangResource.setColumnName(rs.getString("column_name"));
		mutlLangResource.setIdColumnName(rs.getString("id_column_name"));
		mutlLangResource.setIdFieldName(rs.getString("id_field_name"));
		return mutlLangResource;
	}

}

