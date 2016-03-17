///**
// * Copyright (c) 2010 Jumbomart All Rights Reserved.
// *
// * This software is the confidential and proprietary information of Jumbomart.
// * You shall not disclose such Confidential Information and shall use it only in
// * accordance with the terms of the license agreement you entered into
// * with Jumbo.
// *
// * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
// * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
// * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
// * THIS SOFTWARE OR ITS DERIVATIVES.
// *
// */
//package com.baozun.nebula.api.shoppingcart;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.baozun.nebula.api.utils.ConvertUtils;
//import com.baozun.nebula.dao.product.SkuDao;
//import com.baozun.nebula.dao.shoppingcart.ShoppingCartDao;
//import com.baozun.nebula.dao.shoppingcart.ShoppingCartLineDao;
//import com.baozun.nebula.exception.BusinessException;
//import com.baozun.nebula.model.product.Sku;
//
//@Transactional
//@Service("shoppingCartService")
//public class ShoppingCartServiceImpl implements ShoppingCartService {
//
//	private static final Logger log = LoggerFactory
//			.getLogger(ShoppingCartServiceImpl.class);
//
//	/** 程序返回结果 **/
//	private static final Integer SUCCESS = 1;
//
//	private static final Integer FAILURE = 0;
//
//	@Autowired
//	private ShoppingCartDao shoppingCartDao;
//
//	@Autowired
//	private ShoppingCartLineDao shoppingCartLineDao;
//
//	@Autowired
//	private SkuDao skuDao;
//
//	@Override
//	public ShoppingCart findShoppingCart(Long userId) {
//		if (null != userId) {
//			// 根据用户id查询出购物车信息
//			com.baozun.nebula.model.shoppingcart.ShoppingCart shoppingCart = shoppingCartDao
//					.findShoppingCartByMemberId(userId);
//			if (null == shoppingCart) {
//				return null;
//			} else {
//				Long shoppingCartId = shoppingCart.getId();
//				List<ShoppingCartLine> apiShoppingCartLines = new ArrayList<ShoppingCartLine>();
//				ShoppingCart apiShoppingCart = new ShoppingCart();
//				apiShoppingCart.setId(shoppingCartId);
//				apiShoppingCart.setTotalDiscount(shoppingCart
//						.getTotalDiscount());
//				apiShoppingCart.setTotalPrice(shoppingCart.getTotalPrice());
//				// 根据购物车id查找出购物车行信息
//				List<com.baozun.nebula.model.shoppingcart.ShoppingCartLine> shoppingCartLines = shoppingCartLineDao
//						.findShopCartLineByCartId(shoppingCartId);
//				if (null != shoppingCartLines && shoppingCartLines.size() > 0) {
//					for (com.baozun.nebula.model.shoppingcart.ShoppingCartLine shoppingCartLine : shoppingCartLines) {
//						// sku信息先从数据库读取，之后可能改成从缓存里读取
//						Sku sku = skuDao.findSkuById(shoppingCartLine
//								.getItemId());
//						ShoppingCartLine apiShoppingCartLine = new ShoppingCartLine();
//						// shoppingCartLine info
//						ConvertUtils.convertTwoObject(apiShoppingCartLine,
//								shoppingCartLine);
//						// sku info
//						apiShoppingCartLine.setUnitPrice(sku.getPrice());
//
//						apiShoppingCartLines.add(apiShoppingCartLine);
//					}
//					apiShoppingCart.setShoppingCartLines(apiShoppingCartLines);
//					return apiShoppingCart;
//				}
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public int saveShoppingCart(Long userId, ShoppingCartLine shoppingCartLine) {
//		int result=merageShoppingCartLine(userId, shoppingCartLine, true);
//		return result;
//	}
//
//	@Override
//	public int updateShoppingCart(Long userId, String extentionCode, Integer count) {
//		if (null == extentionCode) {
//			// TODO throw BussinessException ?
//			return FAILURE;
//		}
//
//		Long itemId = getItemIdByextentionCode(extentionCode);
//		ShoppingCartLine shoppingCartLine = new ShoppingCartLine();
//		shoppingCartLine.setExtentionCode(extentionCode);
//		shoppingCartLine.setItemId(itemId);
//		shoppingCartLine.setQuantity(count);
//	
//		int result=merageShoppingCartLine(userId, shoppingCartLine, false);
//		return result;
//	}
//
//	@Override
//	public int removeShoppingCartLine(Long userId, String extentionCode) {
//		if (null != userId) {
//			com.baozun.nebula.model.shoppingcart.ShoppingCart shoppingCart = shoppingCartDao
//					.findShoppingCartByMemberId(userId);
//			try {
//				shoppingCartLineDao.deleteByExtentionCode(shoppingCart.getId(),extentionCode);
//			} catch (Exception e) {
//				log.error("removeShoppingCartLine error");
//				return FAILURE;
//			}
//			return SUCCESS;
//		} else {
//			return FAILURE;
//		}
//
//	}
//
//	@Override
//	public int emptyShoppingCart(Long userId) {
//		if (null != userId) {
//			com.baozun.nebula.model.shoppingcart.ShoppingCart shoppingCart = shoppingCartDao
//					.findShoppingCartByMemberId(userId);
//			
//			try {
//				shoppingCartLineDao.deleteByShoppingCartId(shoppingCart.getId());
//				shoppingCartDao.delete(shoppingCart);
//			} catch (Exception e) {
//				log.error("emptyShoppingCart error");
//				return FAILURE;
//			}
//
//			return SUCCESS;
//		} else {
//			return FAILURE;
//		}
//	}
//
//	/**
//	 * 计算购物车里边商品总价格
//	 * 
//	 * @return
//	 */
//	private BigDecimal getCartTotalPrice() {
//		BigDecimal total = new BigDecimal(-1);
//		// TODO
//		return total;
//	}
//
//	/**
//	 * 计算购物车里商品的折扣
//	 * 
//	 * @return
//	 */
//	private BigDecimal getCartTotalDiscount() {
//		BigDecimal total = new BigDecimal(-1);
//		// TODO
//		return total;
//	}
//
//	private com.baozun.nebula.model.shoppingcart.ShoppingCartLine saveCartLine(
//			Long shoppingCartId, ShoppingCartLine shoppingCartLine) {
//
//		com.baozun.nebula.model.shoppingcart.ShoppingCartLine cartLine = new com.baozun.nebula.model.shoppingcart.ShoppingCartLine();
//		ConvertUtils.convertModelToApi(cartLine, shoppingCartLine);
//
//		com.baozun.nebula.model.shoppingcart.ShoppingCartLine savedCartLine = shoppingCartLineDao
//				.save(cartLine);
//
//		return savedCartLine;
//	}
//
//	/**
//	 * 根据extentionCode来查找商品Id
//	 * 
//	 * @param extentionCode
//	 * @return
//	 */
//	private Long getItemIdByextentionCode(String extentionCode) {
//		// TODO
//		return 1L;
//	}
//
//	/**
//	 * 新增或者修改购物车行
//	 * @param userId
//	 * @param shoppingCartLine
//	 * @param saveFlage
//	 * @return
//	 */
//	private Integer merageShoppingCartLine(Long userId,
//			ShoppingCartLine shoppingCartLine, boolean saveFlage) {
//		// 是否考虑数目限制？ 是否考虑数据库原来木有这条记录的情况？ 超过商品库存？ 负数？
//		// TODO　要考虑库存，暂时不做，记录下来
//		String extentionCode = shoppingCartLine.getExtentionCode();
//		if (null == extentionCode) {
//			// TODO throw BussinessException ?
//			return FAILURE;
//		}
//		/**
//		 * TODO 1.按照次序调用 有效性检查 true/false， 2.限购true/false， 3.促销引擎 .
//		 * 传给他们购物车对象，他们处理之后，再将他们返回的对象转化为我们使用的对象，再进行赋值
//		 */
//
//		Long cartId = null;
//
//		// 先查询是否已经有购物车的信息
//		ShoppingCart queriedCart = findShoppingCart(userId);
//
//		boolean existFlag = false;
//		boolean updateResultFlag = false;
//
//		if (null != queriedCart) {// 如果有
//			cartId = queriedCart.getId();
//
//			// 如果有，
//			// 如果有，就修改数量，同时修改价格
//			// 如果没有，就添加一条记录，同时修改价格
//
//			// 判断加入的该商品是否已经在购物车中，
//			for (ShoppingCartLine line : queriedCart.getShoppingCartLines()) {
//				if (extentionCode.equals(line.getExtentionCode())) {// 如果在的话，就修改数量
//					existFlag = true;
//					Integer curQuantity = 0;
//					if (saveFlage) {// 如果是新增 ， 就合并
//						curQuantity = line.getQuantity() + shoppingCartLine.getQuantity();
//					} else {// 如果是更新，就直接替换值
//						curQuantity = shoppingCartLine.getQuantity();
//					}
//					Integer effectedRows = shoppingCartLineDao
//							.updateCartLineQuantity(cartId, line.getItemId(),
//									curQuantity);
//
//					if (1 == effectedRows) {
//						updateResultFlag = true;
//						line.setQuantity(curQuantity);
//					} else {
//						updateResultFlag = false;
//					}
//
//					break;
//				}
//			}
//
//			if (existFlag) {
//				if (updateResultFlag) {
//					// 修改 购物车总价和总促销信息
//					Integer result = shoppingCartDao
//							.updateShoppingCart(cartId, userId,
//									getCartTotalPrice(), getCartTotalDiscount());
//					if (result == 1) {
//						return SUCCESS;
//					} else {//修改的行数和期望的行数不一致，抛出运行时异常，事务回滚
//						Object[] args = {result,1};
//						throw new BusinessException(10,args);
//					}
//
//				} else {// 存在，但是未修改成功
//					return FAILURE;
//				}
//			} else {// 不存在
//					// 添加
//				com.baozun.nebula.model.shoppingcart.ShoppingCartLine savedCartLine = saveCartLine(
//						cartId, shoppingCartLine);
//				if (null != savedCartLine) {
//					Integer result = shoppingCartDao
//							.updateShoppingCart(cartId, userId,
//									getCartTotalPrice(), getCartTotalDiscount());
//					if (result == 1) {
//						return SUCCESS;
//					} else {//修改的行数和期望的行数不一致，抛出运行时异常，事务回滚
//						Object[] args = {result,1};
//						throw new BusinessException(10,args);
//					}
//				} else {
//					return FAILURE;
//				}
//			}
//
//		} else {// 如果表中没有购物车，那么 创建购物车,同时计算价格
//			com.baozun.nebula.model.shoppingcart.ShoppingCart cart = new com.baozun.nebula.model.shoppingcart.ShoppingCart();
//
//			cart.setMemberId(userId);
//			cart.setTotalDiscount(getCartTotalDiscount());
//			cart.setTotalPrice(getCartTotalPrice());
//
//			// 保存购物车
//			com.baozun.nebula.model.shoppingcart.ShoppingCart savedCart = shoppingCartDao
//					.save(cart);
//
//			if (null != savedCart) {
//				cartId = savedCart.getId();
//
//				// 保存 shoppingCartLine
//				com.baozun.nebula.model.shoppingcart.ShoppingCartLine savedCartLine = saveCartLine(
//						cartId, shoppingCartLine);
//
//				if (null != savedCartLine) {
//					return SUCCESS;
//				} else {//修改的行数和期望的行数不一致，抛出运行时异常，事务回滚
//					Object[] args = {0,1};
//					throw new BusinessException(10,args);
//				}
//
//			} else {
//				return FAILURE;
//			}
//		}
//	}
//}
