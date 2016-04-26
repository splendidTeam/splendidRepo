/**
 * 
 */
package com.baozun.nebula.command.sales;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.command.Command;

/**
 * @author xianze.zhang
 * @creattime 2013-11-28
 * @deprecated 搜索了一遍貌似没有被引用
 */
@Deprecated
public class GiftBean implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1806900765664974442L;

	// 赠品列表
	private List<ShoppingCartLineCommand>	giftList	= new ArrayList<ShoppingCartLineCommand>();

	/**
	 * 若为-1，表示用户不可挑选。由系统默认给予赠品
	 *  最大可挑选数量
	 */
	int										maxNum;
	
	
	public List<ShoppingCartLineCommand> getGiftList(){
		return giftList;
	}

	
	public void setGiftList(List<ShoppingCartLineCommand> giftList){
		this.giftList = giftList;
	}

	
	public int getMaxNum(){
		return maxNum;
	}

	
	public void setMaxNum(int maxNum){
		this.maxNum = maxNum;
	}
	
}
