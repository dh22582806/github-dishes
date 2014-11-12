<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>套餐管理</title>
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
		$("#searchForm").submit();
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/eat/packages/">套餐列表</a></li>
		<shiro:hasPermission name="eat:packages:edit">
			<li><a href="${ctx}/eat/packages/form">套餐添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="packages"
		action="${ctx}/eat/packages/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>归属公司：</label> <input type="hidden" id="hiddenCompanyId"
				name="store.company.name" />
			<form:input path="" id="companyId" htmlEscape="false" maxlength="200"
				class="input-small" value="${packages.store.company.name}"
				disabled="true" />
		</div>
		<div style="margin-top: 8px;">
			<label>归属门店：</label>
			<tags:treeselect id="store" name="store.id"
				value="${packages.store.id}" labelName="store.name"
				labelValue="${packages.store.name}" title="门店"
				lastDataValueLabel="门店"
				url="/eat/store/treeDataForEat?type=1&level=1"
				cssClass="input-small" allowClear="true" notAllowSelectParent="true"
				expandAll="true" secondLabelIds="companyId" />
			<label>套餐名称：</label>
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
				<th>套餐名称</th>
				<th>套餐编码</th>
				<th>单价</th>
				<th>可折扣</th>
				<th>归属公司</th>
				<th>归属门店</th>
				<th>备注</th>
				<shiro:hasPermission name="eat:packages:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="packages">
				<tr>
					<td><a href="${ctx}/eat/packages/form?id=${packages.id}">${packages.name}</a></td>
					<td>${packages.nameCode}</td>
					<td>${packages.price}元</td>
					<td>${dishes.isDiscount eq '1'?'是':'否'}</td>
					<td>${packages.store.company.name}</td>
					<td>${packages.store.name}</td>
					<td>${packages.remarks}</td>
					<shiro:hasPermission name="eat:packages:edit">
						<td><a href="${ctx}/eat/packages/form?id=${packages.id}">明细</a><a
							href="${ctx}/eat/packages/form?id=${packages.id}">修改</a> <a
							href="${ctx}/eat/packages/delete?id=${packages.id}"
							onclick="return confirmx('确认要删除该套餐吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
