
/*-----------------------------------------------------------------------------------------------------------*/	
function seamless_horizontal(ctr, opt){
	var cfg = {
		'container': ctr,			// 效果类结构总容器
		'elements' : '',			// 效果类的主体结构对象
		'visibles' : 5,				// 当前显示的个数
		'scrolls'  : 1,				// 滚动的元素个数
		'focus'	   : 'cur', 		// 当元素获取焦点时应用的样式
		'prev'	   : '',			// 上一个的按钮
		'next'     : '',			// 下一个的按钮
		'inside'   : false, 		// 判断按钮是否在容器内查找
		'auto'	   : 0,				// 是否自动播放
		'speed'	   : 1000,		// 动画播放速度
		'elementsEvent': 'click',	// 滚动元素是否追加事件(滚动至当前元素),默认为点击触发,如果不需要则传入 null|false
		'preventDefault': true,		// 是否阻止默认动作(超链接默认跳转)
		'isSeamless' : true,		// 是否无缝滚动
		'lastIndex'	 : 0,			// 最后一个的下标
		'listeners': {} 			// 事件
	};
	$j.extend(cfg, opt);
	// 将参数转成必要的DOM对象
	var _container = $j(cfg.container),				// 主容器对象
		_elements  = $j(cfg.elements, _container),	// 滚动元素列表
		_visibles  = cfg.visibles,					// 当屏显示元素个数
		_scrolls   = cfg.scrolls,					// 一次滚动多少个
		_focus     = cfg.focus, 					// 滚动时切换的class
		_inside	   = cfg.inside,					// 用于在获取上下(左右)按钮时,是否在容器内部查找(默认不在容器内部查找)
		_prev	   = $j(cfg.prev, _inside ? _container : document.body),	// 上一项按钮
		_next      = $j(cfg.next, _inside ? _container : document.body),	// 下一项按钮
		_elementsEvent  = cfg.elementsEvent,		// 指定元素列表追加事件
		_preventDefault = cfg.preventDefault,		// 在元素列表追加的事件中是否阻止元素默认动作
		_auto	   = cfg.auto,						// 是否开启自动播放
		_speed	   = cfg.speed,						// 动画的执行速度
		_listeners = cfg.listeners,					// 自定义回调管理对象
		_lastIndex = cfg.lastIndex,
		_isSeamless = cfg.isSeamless;
	var length	 = _elements.length,					// 列表元素总个数
		curIndex = length,								// 初始化时的当前索引
		once	 = _elements.first().outerWidth(true),	// 一次滚动的距离
		listItemParent = _elements.parent(),			// 列表元素的父对象
		animateElement = _elements.parent().parent(),	// 执行滚动动作的容器对象
		newElementList,		// 无疑滚动结构初始化后的列表元素集合
		interval,			// 定时器对象
		_index = 0;			// 当前下标

	var _this = this;
	// 初始化函数
	_this.init = function(){
		// 在原有列表的前后复制一份,以达到无缝滚动的效果
		listItemParent.prepend(_elements.clone(true).removeClass(_focus)).append(_elements.clone(true).removeClass(_focus));
		// 结构复制完成后,设置其宽度
		listItemParent.width(length * 3 * once);
		// 获取最新的滚动元素列表
		newElementList = listItemParent.children();
		// 初始化位置
		animateElement.scrollLeft(curIndex * once);
		// 处理设定的滚动数是否大于一屏的显示总个数
		_scrolls > _visibles && (_scrolls = _visibles);
		// 返回当前函数本身
		return arguments.callee;
	}();
	// 设置样式至当前位置
	_this.toggleClass = function(i){
		$j(newElementList).removeClass(_focus).eq(i).addClass(_focus);
	}
	// 滚动方法 => 滚动于当前索引处
	_this.moveTo = function(i){
		// 调用执行回调方法
		_listeners.begin && _listeners.begin.call(_this, curIndex - length, i);
		// 滚动动画开始执行
		$j(animateElement).stop(true, true).animate({'scrollLeft': i * once}, {'duration': _speed, 'complete': function(){
			// 此处为动画执行完毕后所调用的回调函数
			if (i < length){
				// 表示向上滚动, 如果滚动到头了, 则让其跳转到最后一个元素后对应的位置
				curIndex = length * 2 - 1;
				$j(animateElement).scrollLeft(curIndex * once);
			}
			else if (i > length * 2 - 1){
				// 表示向下滚动, 如果滚动到尾了, 则让其跳转到第一个元素前对应的位置
				curIndex = i - length;
				$j(animateElement).scrollLeft(curIndex * once);
			}
			// 设置当前元素的选中样式
			_this.toggleClass(curIndex);	
			// 调用执行回调方法
			_index = curIndex - length;
			_listeners.done && _listeners.done.call(_this, _index, curIndex, i);
		}});
	}
	// 滚动至上一个
	_this.prev = function(e){
		e.preventDefault();
		// 如果当前动画未结束, 则不执行
		// animateElement 执行动画的元素
		if (animateElement.filter(':animated').length > 0) return false;
		
		if(!_isSeamless && _index == 0 && !interval) return false;

		curIndex -= _scrolls;
		_this.moveTo(curIndex);
	}
	// 滚动至下一个
	_this.next = function(e){
		e && e.preventDefault();
		// 如果当前动画未结束, 则不执行
		// animateElement 执行动画的元素
		if (animateElement.filter(':animated').length > 0) return false;

		if(!_isSeamless && _index == _lastIndex && !interval) return false;

		curIndex += _scrolls;
		_this.moveTo(curIndex);
	}
	// 开启自动播放
	_this.start = function(){
		if (_auto){
			interval = setInterval(_this.next, 6000);
		}
	}
	// 中止自动播放
	_this.stop = function(){
		clearInterval(interval);
		// 停止定时器调用上面方法就行了
		// 此处赋值是为下面委托hover使用,
		// 因为即使定时器清除了, 其返回值不为空, 为内存中为当前定时器分配的一个ID
		interval = null;
	}
	// 用于绑定用户自定义回调函数, 如: begin  done
	// type: 	函数名称
	// fn: 		函数主体
	_this.on = function(type, fn){
		_listeners[type] = fn;
	}
	// 可以在任意的地方调用自定义函数
	// type:　函数名称
	_this.fire = function(type){
		_listeners[type] && typeof _listeners[type] == 'function' && _listeners[type].call(_this);
	}

	_prev.bind({
		'click': _this.prev,
		'mouseenter': _this.stop,
		'mouseleave': _this.start
	});
	_next.bind({
		'click': _this.next,
		'mouseenter': _this.stop,
		'mouseleave': _this.start
	});

	// 滚动的元素是否追加事件处理
	// 由于考虑到列表元素数量数题,此处采用委托处理
	if (_elementsEvent){
		listItemParent.delegate('li', _elementsEvent, function(e){
			_preventDefault && e.preventDefault();
			curIndex = $j(this).index();
			_this.moveTo(curIndex);
		});
		listItemParent.delegate('li', 'hover', function(){
			// 在委托中, hover 只能指定这一个函数
			// 需要在此处处理 mouseenter  mouseleave 时所执行的代码
			// 开启了自动播放才处理 => 如果当前是关闭的则开启, 否则停止
			_auto && (interval ? _this.stop() : _this.start());
		});
	}
	// 效果类实例完成后,处理自动播放
	_auto && _this.start();
}


function fixedAdElement(){
	if ($j.browser.msie && $j.browser.version == 6){
		$j(window).scroll(function(){
			var top = $j(window).height() - $j('#relate_img').height() - 20 + $j(window).scrollTop();
			$j('#relate_img').css('top', top);
		});
	}
	$j(window).bind('load resize', function(){
		var centerElement = $j('.nav_ta_all'), ceX = centerElement.offset().left, ceW = centerElement.width(), wW = $j(window).width(), curElement = $j('#relate_img');
		/*curElement.css('left', (ceX + ceW > wW ? wW - curElement.width() : ceX + ceW + 45));*/
		curElement.css('left', (ceX - curElement.width() < 0 ? 0 : ceX - curElement.width()-10));
		$j(curElement)[wW < ceW + curElement.width() * 2 ? 'hide' : 'show']();
	});
}
