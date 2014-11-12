<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>菜品管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function doSubmit() {
		$("#hiddenCompanyId").val($("#companyId").val());
		$("#hiddenStoreId").val($("#storeId").val());
		$("#searchForm").submit();
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/eat/dishes/">菜品列表</a></li>
		<shiro:hasPermission name="eat:dishes:edit">
			<li><a href="${ctx}/eat/dishes/form">菜品添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="dishes"
		action="${ctx}/eat/dishes/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>归属公司：</label> <input type="hidden" id="hiddenCompanyId"
				name="dcategory.store.company.name" />
			<form:input path="" id="companyId" htmlEscape="false" maxlength="200"
				class="input-small" value="${dishes.dcategory.store.company.name}"
				disabled="true" />
			<label>菜品类别：</label>
			<tags:treeselect id="dcategory" name="dcategory.id" value="${dishes.dcategory.id}"
				labelName="dcategory.name" labelValue="${dishes.dcategory.name}"
				allowClear="true" title="菜品类别" lastDataValueLabel="菜品类别"
				url="/eat/store/treeDataForEat?type=1&level=2&treeType=1" expandAll="true"
				cssClass="input-small" notAllowSelectParent="true"
				secondLabelIds="companyId,storeId" />

		</div>
		<div style="margin-top: 8px;">
			<label>归属门店：</label> <input type="hidden" id="hiddenStoreId"
				name="dcategory.store.name" />
			<form:input path="" id="storeId" htmlEscape="false" maxlength="200"
				class="input-small" value="${dishes.dcategory.store.name}"
				disabled="true" />

			<label>菜品名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="50"
				class="input-small" />
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button"
				onClick="doSubmit()" value="查询" />
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>菜品名称</th>
				<th>菜品类别</th>
				<th>菜品编码</th>
				<th>单价</th>
				<th>时价</th>
				<th>可折扣</th>
				<th>计入低消</th>
				<th>归属公司</th>
				<th>归属门店</th>
				<th>备注</th>
				<shiro:hasPermission name="eat:dishes:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="dishes">
				<tr>
					<td><a href="${ctx}/eat/dishes/form?id=${dishes.id}">${dishes.name}</a></td>
					<td>${dishes.dcategory.name}</td>
					<td>${dishes.nameCode}</td>
					<td>${dishes.price}元</td>
					<td>${dishes.isCurPrice eq '1'?'是':'否'}</td>
					<td>${dishes.isDiscount eq '1'?'是':'否'}</td>
					<td>${dishes.isMinChange eq '1'?'是':'否'}</td>
					<td>${dishes.dcategory.store.company.name}</td>
					<td>${dishes.dcategory.store.name}</td>
					<td>${dishes.remarks}</td>
					<shiro:hasPermission name="eat:dishes:edit">
						<td><a href="${ctx}/eat/dishes/form?id=${dishes.id}">修改</a> <a
							href="${ctx}/eat/dishes/delete?id=${dishes.id}"
							onclick="return confirmx('确认要删除该菜品吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
