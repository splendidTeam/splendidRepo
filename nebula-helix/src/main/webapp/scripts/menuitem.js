var menus = [{label:"商品管理", icon:"product"},
    {label:"系统管理", icon:"manage"}];

//商品管理：1
menus[0].children=[{label:"商品维护",url:"pages/template-column1.html"},
    {label:"商品维护",url:"pages/error.html"},
    {label:"商品分类维护",url:"pages/tree.html"},
    {label:"商品标签维护",url:"http://www.nikestore.com.cn"},
	{label:"行业维护",url:"pages/industry.html"},
	
	{label:"属性维护",url:"pages/list-properties.html"},
	{label:"店铺管理",url:"pages/shop.html"},
	{label:"商品分类管理",url:"pages/categoryManager.html"}
	];
	

//系统管理：2
menus[1].children=[{label:"用户管理",url:"pages/list-user.html",children:[
    {label:"用户管理",url:"pages/template-column1.html"},
    {label:"用户管理",url:"pages/template-column1.html"},
    {label:"用户管理",url:"pages/template-column1.html", children:[
        {label:"用户管理",url:"pages/template-column1.html"},
        {label:"用户管理",url:"pages/template-column1.html"}
        ]}
    ]},
    {label:"角色管理",url:"pages/template-column1.html"},
    {label:"权限管理",url:"pages/template-column1.html"}];