/* 促销范围 */
var PROMOTION_SCOPE = {
	OR: "|",
	AND: "&",
	AND_OR: "&|",
	EXP_PID: "pid",	// 产品
	EXP_CID: "cid",	// 分类
	EXP_CMB: "cmbid",	// 组合
	EXP_CALL: "call"	// 全场
};

/* 促销条件类型 */
var CONDITION_TYPE = {
	EXP_NORMAL: "Normal",	//nolmt
	EXP_STEP: "Step",	//ordamt(200)整单金额大于等于200
	EXP_CHOICE: "Choice",	//ordpcs(5)整单件数大于等于5件
	EXP_NORMALSTEP: "NormalStep",	//scpordamt(500,cid:188)男鞋整单金额大于等于500
	EXP_NORMALCHOICE: "NormalChoice"	//scpordpcs(3,cid:188)男鞋整单件数大于等于3
};

/* 促销条件 */
var PROMOTION_CONDITION = {
	OR: "|",
	AND: "&",
	AND_OR: "&|",
	NOT_AND: "&!",

	EXP_NOLIMIT: "nolmt",	//nolmt
	EXP_ORDAMT: "ordamt",	//ordamt(200)整单金额大于等于200
	EXP_ORDPCS: "ordpcs",	//ordpcs(5)整单件数大于等于5件
	EXP_SCPORDAMT: "scpordamt",	//scpordamt(500,cid:188)男鞋整单金额大于等于500
	EXP_SCPORDPCS: "scpordpcs",	//scpordpcs(3,cid:188)男鞋整单件数大于等于3
	EXP_SCPPRDAMT: "scpprdamt",	//scpprdamt(300,cid:188)男鞋单品金额大于等于300
	EXP_SCPPRDPCS: "scpprdpcs",	//scpprdpcs(2,cid:188)男鞋单品件数大于等于2

	EXP_ORDCOUPON: "ordcoupon",	//ordcoupon(2)整单5元券
	EXP_SCPCOUPON: "scpcoupon",	//scpcoupon(1,cid:188)男鞋类10元券
	
	EXP_CHOICEPRIMPRD: "prmprd",	//选购中主商品
	EXP_CHOICEADDTPRD: "addtprd"	//选购中选购商品
};

/* 促销优惠设置 */
var PROMOTION_SETTING = {
		OR: "|",
		AND: "&",
		AND_OR: "&|",
		NOT_AND: "&!",

		// 判断逻辑代号
		EXP_FREESHIP: "freeship",	//免运费
		EXP_ORDDISC: "orddisc",	//整单优惠
		EXP_ORDRATE: "ordrate",	//整单折扣
		EXP_SCPORDDISC: "scporddisc",	//范围整单优惠
		EXP_SCPORDRATE: "scpordrate",	//范围整单折扣
		EXP_SCPPRDDISC: "scpprddisc",	//范围单品优惠
		EXP_SCPPRDRATE: "scpprdrate",	//范围单品折扣

		EXP_SCPPCSDISC: "scppcsdisc",	//范围单件优惠
		EXP_SCPPCSRATE: "scppcsrate",	//范围单件折扣
		EXP_ORDCOUPON: "ordcoupon",	//整单优惠券
		EXP_SCPCOUPON: "scpcoupon",	//范围优惠券
		EXP_SCPGIFT: "scpgift"	//范围优惠券		
};
