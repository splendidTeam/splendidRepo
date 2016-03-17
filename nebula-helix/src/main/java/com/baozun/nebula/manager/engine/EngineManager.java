package com.baozun.nebula.manager.engine;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.EngineMemberCommand;

public interface EngineManager extends BaseManager {
	public EngineMemberCommand findEngineMemberDataByMemberId(Long memberId);
}
