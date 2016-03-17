var menus = [{label:"商品管理", icon:"product"},{label:"订单管理", icon:"member"},
             {label:"会员管理", icon:"member"},{label:"内容管理", icon:"content"},
             {label:"系统管理", icon:"manage"},{label:"评价管理", icon:"manage"}];

//商品管理：0
menus[0].children=[{label:"商品管理",url:"pages/item-list.html"},
   
    
    {label:"商品标签管理",url:"pages/tagManage.html"},
	{label:"行业管理",url:"pages/industry.html"},
	{label:"商品分类",url:"pages/categoryProduct.html"},
	{label:"商品属性管理",url:"pages/list-properties.html"},
	{label:"店铺管理",url:"pages/shop.html"},
	{label:"商品分类管理",url:"pages/categoryManager.html"}
	];

//订单管理：1
menus[1].children=[{label:"订单列表",url:"pages/order-list.html"},
    {label:"申请退换货列表",url:"pages/ext-order-list.html"},
    {label:"订单",url:"pages/order.html"},
    {label:"取消订单列表",url:"pages/cancel-order-list.html"}
    ];

//会员管理：2
menus[2].children=[{label:"会员列表",url:"pages/member-list.html"},
    {label:"会员分组管理",url:"pages/edit-group.html"}
    ];

//内容管理：3
menus[3].children=[{label:"站点地图",url:"pages/cms-sitemap.html"},
    {label:"数字资产管理",url:"pages/cms/digitalAssets.html"},
    {label:"模板定义管理",url:"pages/cms-template.html"},
    {label:"区域管理",url:"pages/cms-area-list.html"},
    {label:"组件管理",url:"pages/cms-comp-list.html"},
    {label:"资源管理",url:"pages/cms-resources.html"},
    {label:"安装模板",url:"pages/cms-install-component.html"},
    {label:"属性定义管理",url:"pages/cms-dialog.html"}];

//系统管理：4
menus[4].children=[{label:"用户管理",url:"pages/list-user.html",children:[
    {label:"用户管理",url:"pages/list-user.html"},
    {label:"用户管理",url:"pages/list-user.html"},
    {label:"用户管理",url:"pages/list-user.html", children:[
        {label:"用户管理",url:"pages/list-user.html"},
        {label:"用户管理",url:"pages/list-user.html"}
        ]}
    ]},
    {label:"角色管理",url:"pages/list-role.html"},
    {label:"权限管理",url:"pages/template-column1.html"},
    
    {label:"通用选项管理",url:"pages/choose-option-group.html"}];

menus[5].children=[{label:"评价管理",url:"pages/item-evaluate.html"},
                   {label:"咨询管理",url:"pages/question-list.html"}];


