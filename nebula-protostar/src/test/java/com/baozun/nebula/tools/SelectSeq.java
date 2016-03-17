package com.baozun.nebula.tools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SelectSeq {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Test
	public void selectSeq(){
		String seq = "S_T_SYS_EMAILSENDLOG";
		int  num = 650; 
		for (int i = 0; i < num+1; i++) {
			int result = jdbcTemplate.queryForInt("select nextval ('"+seq+"')");
			if(i< result){
				i = result;
			}
			if(result >= num){
				System.out.println("result:"+result);
				break;
			}
		}
		
	}
	
}

