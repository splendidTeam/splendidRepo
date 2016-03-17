package com.baozun.nebula.web.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class FindebackAccountCmd {

	@NotNull(message="{member.findback.warn.acc}")
	@Pattern(regexp = "^[A-Za-z0-9_]+$", message="{member.findback.warn.acc}")
	private String loginName;

	@NotNull(message="{member.findback.warn.vcode}")
	@Pattern(regexp = "^[\\d]{4}$", message="{member.findback.warn.vcode}")
	private String vcode;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
}
