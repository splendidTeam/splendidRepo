package com.baozun.nebula.manager.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * 游客操作购物车
 * 
 * @author jumbo
 *
 */
@Component("guestShoppingcartResolver")
public class GuestShoppingcartResolver extends AbstractShoppingcartResolver {
	/** The Constant log. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GuestShoppingcartResolver.class);

	@Override
	public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(HttpServletRequest request,
			MemberDetails memberDetails) {
		try {
			// 获取cookie中的购物车行集合
			List<CookieShoppingCartLine> cookieShoppingCartLineList = getCookieShoppingCartLines(request);

			if (Validator.isNullOrEmpty(cookieShoppingCartLineList)) {
				return null;
			}

			return toShoppingCartLineCommandList(cookieShoppingCartLineList);

		} catch (EncryptionException e) {
			LOGGER.error("EncryptionException e :", e);
			throw new IllegalArgumentException(e);// TODO 换成更好的 runtimeexception
		}
	}

	@Override
	protected ShoppingcartResult doAddShoppingCart(MemberDetails memberDetails,
			List<ShoppingCartLineCommand> shoppingCartLineCommandList, ShoppingCartLineCommand currentLine,
			HttpServletRequest request, HttpServletResponse response) {
		// 主賣品(剔除 促銷行 贈品) 剔除之后 下次load会补全最新促销信息 只有游客需要有这个动作 所以放在这里
		List<ShoppingCartLineCommand> mainLines = CollectionsUtil.select(shoppingCartLineCommandList,
				new MainLinesPredicate());
		addGuestIndentifyCartCookie(response, toCookieShoppingCartLineList(mainLines));
		return null;
	}

	@Override
	protected ShoppingcartResult doUpdateShoppingCart(MemberDetails memberDetails,
			List<ShoppingCartLineCommand> shoppingCartLineCommandList, ShoppingCartLineCommand currentLine,
			HttpServletRequest request, HttpServletResponse response) {
		// 主賣品(剔除 促銷行 贈品) 剔除之后 下次load会补全最新促销信息 只有游客需要有这个动作 所以放在这里
		List<ShoppingCartLineCommand> mainLines = CollectionsUtil.select(shoppingCartLineCommandList,
				new MainLinesPredicate());
		addGuestIndentifyCartCookie(response, toCookieShoppingCartLineList(mainLines));
		return null;
	}

	/**
	 * 把cookie购物车行对象加入cookie当中
	 * 
	 * @param guestIndentify
	 * @param response
	 * @param cartLineList
	 */
	private void addGuestIndentifyCartCookie(HttpServletResponse response, List<CookieShoppingCartLine> cartLineList) {
		String json = JSON.toJSONString(cartLineList);
		CookieGenerator cookieGenerator = new CookieGenerator();
		cookieGenerator.setCookieName(CookieKeyConstants.GUEST_COOKIE_GC);
		cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
		try {
			String encrypt = EncryptUtil.getInstance().encrypt(json);
			cookieGenerator.addCookie(response, encrypt);
		} catch (EncryptionException e) {
			LOGGER.error("EncryptionException e:", e);
		}
	}

	/**
	 * 获取cookie中的购物车行集合
	 * 
	 * @param request
	 * @return
	 * @throws EncryptionException
	 */
	private List<CookieShoppingCartLine> getCookieShoppingCartLines(HttpServletRequest request)
			throws EncryptionException {
		Cookie cookie = WebUtils.getCookie(request, CookieKeyConstants.GUEST_COOKIE_GC);

		if (null == cookie) {
			return null;
		}

		if (StringUtils.isBlank(cookie.getValue())) {
			return null;
		}

		String decrypt = EncryptUtil.getInstance().decrypt(cookie.getValue());
		return JSON.parseObject(decrypt, new TypeReference<ArrayList<CookieShoppingCartLine>>() {
		});
	}

	/**
	 * 获取cookie中的购物车行信息.将cookie中的购物车 转换为 shoppingCartLineCommand
	 * 
	 * @param callType
	 * @param validedLines
	 * @param effectEngine
	 * @param memberContext
	 * @param cookieShoppingCartLineList
	 * @return
	 */
	private List<ShoppingCartLineCommand> toShoppingCartLineCommandList(
			List<CookieShoppingCartLine> cookieShoppingCartLineList) {
		List<ShoppingCartLineCommand> shoppingCartLines = new ArrayList<ShoppingCartLineCommand>();
		for (CookieShoppingCartLine cookieShoppingCartLine : cookieShoppingCartLineList) {

			// 将cookie中的购物车 转换为 shoppingCartLineCommand
			ShoppingCartLineCommand shoppingLineCommand = new ShoppingCartLineCommand();
			PropertyUtil.copyProperties(shoppingLineCommand, cookieShoppingCartLine, "createTime", "skuId", "quantity",
					"extentionCode", "settlementState", "shopId", "promotionId", "lineGroup", "id");
			shoppingLineCommand
					.setGift(null == cookieShoppingCartLine.getIsGift() ? false : cookieShoppingCartLine.getIsGift());
			shoppingCartLines.add(shoppingLineCommand);
		}
		return shoppingCartLines;
	}

	/**
	 * 将ShoppingCartLineCommand对象转换为CookieShoppingCartLine对象
	 * 
	 * @param shoppingCartLine
	 * @return
	 */
	private List<CookieShoppingCartLine> toCookieShoppingCartLineList(List<ShoppingCartLineCommand> shoppingCartLines) {
		// 将ShoppingCartLineCommand对象转换为CookieShoppingCartLine对象
		List<CookieShoppingCartLine> cookieLines = new ArrayList<CookieShoppingCartLine>();
		if (Validator.isNotNullOrEmpty(shoppingCartLines)) {
			for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLines) {
				CookieShoppingCartLine cookieShoppingCartLine = new CookieShoppingCartLine();

				PropertyUtil.copyProperties(cookieShoppingCartLine, shoppingCartLineCommand, "extentionCode", "skuId",
						"quantity", "createTime", "settlementState", "shopId", "promotionId", "lineGroup");
				cookieShoppingCartLine.setIsGift(shoppingCartLineCommand.isGift());
				// TODO bundle 以后再考虑 id
				cookieShoppingCartLine.setId(null == shoppingCartLineCommand.getId() ? shoppingCartLines.size()
						: shoppingCartLineCommand.getId());

				cookieLines.add(cookieShoppingCartLine);
			}
		}
		return cookieLines;
	}

	@Override
	protected ShoppingcartResult doDeleteShoppingCartLine(MemberDetails memberDetails,
			List<ShoppingCartLineCommand> shoppingCartLineCommandList, ShoppingCartLineCommand currentLine,
			HttpServletRequest request, HttpServletResponse response) {
		List<ShoppingCartLineCommand> mainLines = new ArrayList<ShoppingCartLineCommand>();// 主賣品行
		List<ShoppingCartLineCommand> shoppingCartLineCommands = getShoppingCartLineCommandList(request, memberDetails);
		for (int i = 0; i < shoppingCartLineCommands.size(); i++) {
			ShoppingCartLineCommand line = shoppingCartLineCommands.get(i);
			if (!line.isCaptionLine() && !line.isGift()) {// 促銷行 & 贈品行 不參與遍曆
				if (!currentLine.getId().equals(line.getId())) {
					mainLines.add(line);
				}
			}
		}
		// 将修改后的购物车保存cookie
		addGuestIndentifyCartCookie(response, toCookieShoppingCartLineList(mainLines));
		return null;
	}

	@Override
	protected ShoppingcartResult doSelectShoppingCartLine(MemberDetails memberDetails, Integer settlementState,
			List<String> extentionCodeList, List<ShoppingCartLineCommand> shoppingCartLineCommandList,
			HttpServletRequest request, HttpServletResponse response) {
		// 将修改后的购物车保存cookie
		addGuestIndentifyCartCookie(response, toCookieShoppingCartLineList(shoppingCartLineCommandList));
		return null;
	}

}
