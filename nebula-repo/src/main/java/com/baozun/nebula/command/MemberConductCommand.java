package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Date;


public class MemberConductCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 228221051958999914L;
	
	//5.3.2.18 客户端识别码规定绑定名称
    public static final String CLIENT_IDENTIFICATION_MECHANISMS = MemberConductCommand.class.getName()+"clientIdentificationMechanisms";

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 登录次数
	 */
	private Integer				loginCount;

	/**
	 * 注册时间
	 */
	private Date				registerTime;

	/**
	 * 登录时间(最近)
	 */
	private Date				loginTime;

	/**
	 * 付款时间(最近)
	 */
	private Date				payTime;

	/**
	 * 注册ip
	 */
	private String				registerIp;

	/**
	 * 登录ip(最近)
	 */
	private String				loginIp;

	/**
	 * 累积消费金额
	 */
	private BigDecimal			cumulativeConAmount;
	
	/**
     * 客户端识别码
     * 类似ip一样的信息
     * @since 5.3.2.18
     */
    private String clientIdentificationMechanisms;

	public MemberConductCommand(){
		super();
	}

	public MemberConductCommand(Date loginTime, String loginIp){
		super();
		this.loginTime = loginTime;
		this.loginIp = loginIp;
	}

	public MemberConductCommand(Integer loginCount, Date registerTime, String registerIp){
		super();
		this.loginCount = loginCount;
		this.registerTime = registerTime;
		this.registerIp = registerIp;
	}

	public MemberConductCommand(Long id, Integer loginCount, Date registerTime, Date loginTime, String registerIp, String loginIp){
		super();
		this.id = id;
		this.loginCount = loginCount;
		this.registerTime = registerTime;
		this.loginTime = loginTime;
		this.registerIp = registerIp;
		this.loginIp = loginIp;
	}

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Integer getLoginCount(){
		return loginCount;
	}

	public void setLoginCount(Integer loginCount){
		this.loginCount = loginCount;
	}

	public Date getRegisterTime(){
		return registerTime;
	}

	public void setRegisterTime(Date registerTime){
		this.registerTime = registerTime;
	}

	public Date getLoginTime(){
		return loginTime;
	}

	public void setLoginTime(Date loginTime){
		this.loginTime = loginTime;
	}

	public Date getPayTime(){
		return payTime;
	}

	public void setPayTime(Date payTime){
		this.payTime = payTime;
	}

	public String getRegisterIp(){
		return registerIp;
	}

	public void setRegisterIp(String registerIp){
		this.registerIp = registerIp;
	}

	public String getLoginIp(){
		return loginIp;
	}

	public void setLoginIp(String loginIp){
		this.loginIp = loginIp;
	}

	public BigDecimal getCumulativeConAmount(){
		return cumulativeConAmount;
	}

	public void setCumulativeConAmount(BigDecimal cumulativeConAmount){
		this.cumulativeConAmount = cumulativeConAmount;
	}
	
	/**
     * 获得 客户端识别码
     * @return clientIdentificationMechanisms
     * @since 5.3.2.18
     */
    public String getClientIdentificationMechanisms(){
        return clientIdentificationMechanisms;
    }

    /**
     * 设置 客户端识别码
     * @param clientIdentificationMechanisms
     * @since 5.3.2.18
     */
    public void setClientIdentificationMechanisms(String clientIdentificationMechanisms){
        this.clientIdentificationMechanisms = clientIdentificationMechanisms;
    }

}
