package com.baozun.nebula.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.Type;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品操作日志相关
 * @author jay.yang
 *
 */
@Entity
@Table(name = "T_SYS_OPERATE_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SystemOperateLog extends BaseModel{

	private static final long serialVersionUID = -7129217516762310008L;
	
	private Long id;
	/** 商品备份信息，JSON字符串 */
	private String context;
	/** 操作人ID */
	private Long optId;
	/** 1.单个操作，2.系统推送，3.导入 */
	private Long optType;
	/** 操作数据类型关联键 */
	private String targetId;
	/** 操作时间 */
	private Date createTime;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_OPERATE_LOG", sequenceName = "S_T_SYS_OPERATE_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_OPERATE_LOG")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CONTEXT")
	@Type(type="text")
	public String getContext() {
		return context;
	}
	
	public void setContext(String context) {
		this.context = context;
	}
	
	@Column(name = "OPT_ID")
	public Long getOptId() {
		return optId;
	}
	
	public void setOptId(Long optId) {
		this.optId = optId;
	}
	
	@Column(name = "OPT_TYPE")
	public Long getOptType() {
		return optType;
	}
	
	public void setOptType(Long optType) {
		this.optType = optType;
	}
	
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

    /**
     * @return the targetId
     */
	@Column(name = "TARGET_ID")
    public String getTargetId(){
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(String targetId){
        this.targetId = targetId;
    }

}
