<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>类别管理</title>
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
		<li class="active"><a href="${ctx}/eat/dcategory/">类别列表</a></li>
		<shiro:hasPermission name="eat:dcategory:edit">
			<li><a href="${ctx}/eat/dcategory/form">类别添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="dcategory"
		action="${ctx}/eat/dcategory/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>归属公司：</label> <input type="hidden" id="hiddenCompanyId"
				name="store.company.name" />
			<form:input path="" id="companyId" htmlEscape="false" maxlength="200"
				class="input-small" value="${dcategory.store.company.name}"
				disabled="true" />
		</div>
		<div style="margin-top: 8px;">
			<label>归属门店：</label>
			<tags:treeselect id="store" name="store.id"
				value="${dcategory.store.id}" labelName="store.name"
				labelValue="${dcategory.store.name}" title="门店"
				lastDataValueLabel="门店"
				url="/eat/store/treeDataForEat?type=1&level=1"
				cssClass="input-small" allowClear="true" notAllowSelectParent="true"
				expandAll="true" secondLabelIds="companyId" />
			<label>菜品类别：</label>
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
				<th>菜品类别</th>
				<th>编码</th>
				<th>归属公司</th>
				<th>归属门店</th>
				<th>备注</th>
				<shiro:hasPermission name="eat:dcategory:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="dcategory">
				<tr>
					<td><a href="${ctx}/eat/dcategory/form?id=${dcategory.id}">${dcategory.name}</a></td>
					<td>${dcategory.code}</td>
					<td>${dcategory.store.company.name}</td>
					<td>${dcategory.store.name}</td>
					<td>${dcategory.remarks}</td>
					<shiro:hasPermission name="eat:dcategory:edit">
						<td><a href="${ctx}/eat/dcategory/form?id=${dcategory.id}">修改</a>
							<a href="${ctx}/eat/dcategory/delete?id=${dcategory.id}"
							onclick="return confirmx('确认要删除该类别吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
