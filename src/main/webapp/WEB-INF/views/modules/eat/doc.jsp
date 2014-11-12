<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>文档</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script src="${ctxStatic}/jquery.mobile/jquery.mobile-1.4.0.js"></script>
<script src="${ctxStatic}/jquery.plugins/jquery.form.js"></script>
<script src="${ctxStatic}/jquery.plugins/jsrender.min.js"></script>
<script src="${ctxStatic}/json/json2.js"></script>
<script src="${ctxStatic}/modules/eat/doc.js"></script>
<link rel="stylesheet"
	href="${ctxStatic}/jquery.mobile/jquery.mobile-1.4.0.min.css" />
<script id="demoTemplate" type="text/x-jsrender">
    <div data-role="collapsible" data-collapsed="false">
		<h3>例子</h3>
		<form {{if isupload}}enctype="multipart/form-data"
			{{/if}} action="${pctx}/{{>url}}" data-ajax="false" method="post">
			{{for fileds}}
			<div class="ui-field-contain">
				<label for="{{:name}}">{{:name}}:</label> <input name="{{:name}}"
					{{if file}}type="file" {{else}}type="text" {{/if}} />
			</div>
			{{/for}}
			<div data-role="fieldcontain">
				<input type="submit" value="提交" />
			</div>
		</form>
	</div>
</script>
</head>
<body>
	<div data-role="page" id="doc" class="type-interior"
		style="background-color: #eeeeee">
		<div data-role="header">
			<h1>文档</h1>
		</div>
		<div class="ui-grid-a">
			<div style="width: 30%" class="ui-block-a">
				<ul data-role="listview" data-inset="true">
					<li><a>总体介绍</a></li>
					<li><a>1.取得类目</a></li>
					<li><a>2.取得菜品</a></li>
					<li><a>3.商家主帐号登录</a></li>
					<li><a>4.商家主帐号登出</a></li>
					<li><a>5.初始化数据 </a></li>
					<li><a>6.开台 </a></li>
					<li><a>6.1离台 </a></li>
					<li><a>6.2换台 </a></li>
					<li><a>7.取得桌台状态 </a></li>
					<li><a>8.取得订单详情(根据orderId) </a></li>
					<li><a>9.取得订单详情(根据tableId) </a></li>
					<li><a>10.下单 </a></li>
					<li><a>11.追单 </a></li>
					<li><a>12.结账 </a></li>
				</ul>
			</div>
			<div id="testBody" style="width: 70%; padding-left: 20px"
				class="ui-block-b">
				<!-- 总体说明 -->
				<div>
					<h3>返回对象</h3>
					<p>{code:0,msg:"",data:null}</p>
					<h3>code说明</h3>
					<p>0正常，1未登录，9其他提示错误信息,10订单需要更新</p>‘
					<h3>state说明</h3>
					<p>1下台，2下单，3离台，6已结账</p>
					<h3>测试用户</h3>
					<p>白鹿点餐员（test/test）</p>
					<p>外婆家点餐员（test2/test2）</p>
					<p>外婆家营销员（test3/test3）</p>
				</div>
				<!-- 问卷列表 -->
				<div>
					<h3>取得类目</h3>
					<p>api/getCategory.json</p>
					<h3>参数</h3>
					<p>无</p>
					<!-- <p>username(require),password(require)</p> -->
				</div>
				<!-- 问卷详情 -->
				<div>
					<h3>取得某一类的菜品</h3>
					<p>api/getDishes</p>
					<h3>参数</h3>
					<p>categoryId(require)</p>
				</div>
				<!-- 商家主帐号登录 -->
				<div>
					<h3>商家主帐号登录</h3>
					<p>api/doLogin</p>
					<h3>参数</h3>
					<p>username(require),password(require)</p>
				</div>
				<!-- 商家主帐号登出 -->
				<div>
					<h3>商家主帐号登出</h3>
					<p>api/doLogout</p>
					<h3>参数</h3>
					<p>无</p>
				</div>
				<!-- 初始化数据 -->
				<div>
					<h3>初始化数据</h3>
					<p>api/doInit</p>
					<h3>参数</h3>
					<p>update(option)</p>
				</div>
				<!-- 开台 -->
				<div>
					<h3>开台</h3>
					<p>api/openTable</p>
					<h3>参数</h3>
					<p>tableId(require)</p>
				</div>
				<!-- 离台 -->
				<div>
					<h3>离台</h3>
					<p>api/leaveTable</p>
					<h3>参数</h3>
					<p>orderId(require)</p>
				</div>
				<!-- 换台 -->
				<div>
					<h3>换台</h3>
					<p>api/changeTable</p>
					<h3>参数</h3>
					<p>orderId(require),tableId(require)</p>
				</div>
				<!-- 取得桌台状态 -->
				<div>
					<h3>取得桌台状态</h3>
					<p>api/getOrders</p>
					<h3>参数</h3>
					<p>无</p>
				</div>
				<!-- 取得订单详情 -->
				<div>
					<h3>取得订单详情</h3>
					<p>api/getOrderInfo</p>
					<h3>参数</h3>
					<p>orderId(require)</p>
				</div>

				<!-- 取得订单详情(根据tableId) -->
				<div>
					<h3>取得订单详情(根据tableId)</h3>
					<p>api/getOrderInfoByTableId</p>
					<h3>参数</h3>
					<p>tableId(require)</p>
				</div>

				<!-- 下单 -->
				<div>
					<h3>取得订单详情</h3>
					<p>api/openOrder</p>
					<h3>参数</h3>
					<p>orderId(require),orderDetails(require)</p>
				</div>
				<!-- 追单 -->
				<div>
					<h3>追单</h3>
					<p>api/addToOrder</p>
					<h3>参数</h3>
					<p>orderId(require),orderDetails(require)</p>
				</div>
				<!-- 结账 -->
				<div>
					<h3>结账</h3>
					<p>api/chargeOrder</p>
					<h3>参数</h3>
					<p>orderId(require)</p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>