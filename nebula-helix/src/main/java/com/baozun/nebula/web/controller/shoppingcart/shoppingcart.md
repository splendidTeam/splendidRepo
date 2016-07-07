购物车

# 种类

## 普通的购物车

### 操作

#### 显示 (showShoppingCart)

步骤:

1. **step1:拿到购物车原始数据**

	其中,会员使用 `memberShoppingcartResolver`,从DB 获取当前登陆用户的`ShoppingCartLineCommand list`
	
	而游客,使用 `guestShoppingcartResolver` 从Cookie中拿到 `ShoppingCartLineCommand list`

2. **step2:**基于 `ShoppingCartLineCommand list` 封装 `ShoppingCartCommand`

3. **step3:**将 `ShoppingCartCommand` 转换成 `ShoppingCartViewCommand` 用于显示

#### 加入购物车 (addShoppingCart)

##### 游客 

##### 会员 


#### 删除购物车 (deleteShoppingCartLine)

##### 游客 

##### 会员 


#### 修改购物车 (updateShoppingCartCount)

##### 游客 

##### 会员 


#### 选中不选中(单行) (toggleShoppingCart)

##### 游客 

##### 会员 


#### 全选/全不选 (toggleAllShoppingCartLineCheckStatus)

##### 游客 

##### 会员 


## 立即购买

