package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberCryptoguard;
import com.baozun.nebula.model.member.MemberFavorites;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.product.ItemRate;
import com.baozun.nebula.model.sns.Consultants;
import com.baozun.nebula.sdk.command.ItemRateCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;

public interface SdkCustomMemberManager extends SdkCustomizeFilterExecuteManager{

	public List<Long> findMemberIdsByComsumeeAmtPromotionId(Long shopId,
			BigDecimal amt,String prmId);
}
