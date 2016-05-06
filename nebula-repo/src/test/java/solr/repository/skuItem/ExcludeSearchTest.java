package solr.repository.skuItem;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.baozun.nebula.search.command.ExcludeSearchCommand;
import com.feilong.core.Validator;

public class ExcludeSearchTest {
	
	
	@Test
	public void test1() {

		List<ExcludeSearchCommand> excludeList = new ArrayList<>();
		
		for(int i=0;i<3;i++){
			ExcludeSearchCommand command = new ExcludeSearchCommand();
			command.setFieldName("f_"+i);
			List<String> values = new ArrayList<String>();
			for(int j=0;j<=i;j++){
				values.add(j+"-"+j);
			}
			command.setValues(values);
			excludeList.add(command);
		}
		
		
		for(ExcludeSearchCommand excludeCommand : excludeList){
			StringBuilder strBuf = new StringBuilder();
			if(Validator.isNotNullOrEmpty(excludeCommand.getFieldName()) && Validator.isNotNullOrEmpty(excludeCommand.getValues())){
				strBuf.append("-").append(excludeCommand.getFieldName()).append(":(");
				List<String> values = excludeCommand.getValues();
				for(int i=0;i<values.size();i++){
					strBuf.append(values.get(i));
					if(i < values.size()-1){
						strBuf.append(" OR ");
					}
					if(i == values.size()-1){
						strBuf.append(")");
					}
				}
			}
			
			System.out.println(strBuf.toString());
		}
	
	}
}
