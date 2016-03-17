package com.baozun.nebula.mapper.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.baozun.nebula.command.product.ItemInfoCommand;

public class ItemInfoMapper implements RowMapper<Map<String, ItemInfoCommand>> {
	Map<String, ItemInfoCommand> rowMapper = new HashMap<String, ItemInfoCommand>();

	@Override
	public Map<String, ItemInfoCommand> mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		return rowMapper;
	}
}
