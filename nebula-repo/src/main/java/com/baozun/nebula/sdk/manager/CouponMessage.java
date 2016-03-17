package com.baozun.nebula.sdk.manager;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.system.CouponSendUserLog;
import com.baozun.nebula.model.system.InstationSendMessageRef;

/** 
* @ClassName: ItemColorValueRefManager 
* @Description: (发送优惠券和站内信息) 
* @author gewei.lu <gewei.lu@baozun.cn> 
* @date 2016年1月7日 下午2:17:50 
*  
*/

public interface CouponMessage extends BaseManager{
	
	/** 
	* @Title: saveInstationSendMessageRef 
	* @Description:(记录发送站内信的数据) 
	* @param @param instationSendMessageRef
	* @param @return    设定文件 
	* @return InstationSendMessageRef    返回类型 
	* @throws 
	* @date 2016年1月16日 上午11:16:31 
	* @author GEWEI.LU   
	*/
	public InstationSendMessageRef saveInstationSendMessageRef(InstationSendMessageRef instationSendMessageRef);
	
	
	/** 
	* @Title: findCouponSendUserLog 
	* @Description:(查询已发送的记录) 
	* @param @param promotioncouponid
	* @param @return    设定文件 
	* @return List<CouponSendUserLog>    返回类型 
	* @throws 
	* @date 2016年1月18日 上午11:04:33 
	* @author GEWEI.LU   
	*/
	List<CouponSendUserLog> findCouponSendUserLog(Long promotioncouponid);
	
	
	
	/** 
	* @Title: saveInstationSendMessageRef 
	* @Description:(记录发送优惠券的记录) 
	* @param @param instationSendMessageRef
	* @param @return    设定文件 
	* @return InstationSendMessageRef    返回类型 
	* @throws 
	* @date 2016年1月18日 下午12:27:14 
	* @author GEWEI.LU   
	*/
	public void saveCouponSendUserLog(Long Memberid,Long promotioncouponcodeid,String promotioncouponcode,Long promotioncouponid,String promotioncouponname);
	
	
	/**
	 * 根据会员ids查询会员
	 * @param memberIds
	 * @return
	 */
	List<Member> findMemberList(String Long[],String type);
	
	
	
	/** 
	* @Title: synthesizeCouponoperation 
	* @Description:(派发优惠券的操作) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月19日 上午11:31:54 
	* @author GEWEI.LU   
	*/
	public  Map<Object, Object>   synthesizeCouponoperation(Map<String,String> parameter);
	
	
	/** 
	* @Title: synthesizeCouponoperation 
	* @Description:(记录发送站内信信息操作) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月19日 上午11:31:54 
	* @author GEWEI.LU   
	*/
	public  Map<Object, Object>   synthesizeInstationSendMessage(Map<String,String> parameter);
	
}
