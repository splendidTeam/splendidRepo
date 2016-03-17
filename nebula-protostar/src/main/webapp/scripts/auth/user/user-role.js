
var userRoleList={
	//用于绘制表格的列表数据
	urList:new Array(),		
	//组织map id:name
	orgMap:new Object(),	
	//角色map id:name
	roleMap:new Object(),
	 //组织类型map id:name
	orgTypeMap:new Object(),
	
	/**
	 * 添加元素(userRole id,机构类型id,角色id,组织id数组,机构类型名称,角色名称,组织名称数组)
	 */
	addElement:function(urId,orgTypeId,roleId,orgIds,orgs){
		this.urList[this.urList.length]={"urId":urId,"orgTypeId":orgTypeId,"roleId":roleId,"orgIds":orgIds,"orgType":this.orgTypeMap[orgTypeId],"role":this.roleMap[roleId],"orgs":orgs.join(",")}
	},
	/**
	 * 删除元素
	 * urId:只是一个序号
	 */
	deleteElement:function(urId){
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].urId==urId){
				this.urList.splice(i,1);
				return ;
			}
		}
	},

	/**
	 * 修改数据
	 * 返回true表示修改成功
	 */
	modifyElement:function(urId,orgTypeId,roleId,orgIds,orgs){
		
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].urId==urId){
				this.urList[i]={"urId":urId,"orgTypeId":orgTypeId,"roleId":roleId,"orgIds":orgIds,"orgType":this.orgTypeMap[orgTypeId],"role":this.roleMap[roleId],"orgs":orgs.join(",")};
				return true;
			}
		}
		return false;
	},
	/**
	 * 获取所有用户角色表格数据
	 */
	findAll:function(){
		return this.urList;
	},
	/**
	 * 通过序号来获取用户角色表格数据
	 */
	findByUrId:function(urId){
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].urId==urId){
				
				return this.urList[i];
			}
		}
	},
	/**
	 * 通过角色id来获取用户角色表格数据
	 */
	findByRoleId:function(roleId){
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].roleId==roleId){
				
				return this.urList[i];
			}
		}
	},
	/**
	 * 初始化数据结构
	 */
	init:function(data,orgTypeList,sysRoleList,shopRoleList){
		this.urList=data;
		for(var o in orgTypeList){
			this.orgTypeMap[orgTypeList[o].id]=orgTypeList[o].name;
		}
		for(var o in sysRoleList){
			this.roleMap[sysRoleList[o].id]=sysRoleList[o].name;
		}
		for(var o in shopRoleList){
			this.roleMap[shopRoleList[o].id]=shopRoleList[o].name;
		}
	}
		
}
