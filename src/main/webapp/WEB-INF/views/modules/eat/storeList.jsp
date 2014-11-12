<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>门店管理</title>
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/eat/store/">门店列表</a></li>
		<shiro:hasPermission name="eat:store:edit">
			<li><a href="${ctx}/eat/store/form">门店添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="store"
		action="${ctx}/eat/store/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<div>
			<label>归属公司：</label>
			<tags:treeselect id="company" name="company.id"
				value="${store.company.id}" labelName="company.name"
				labelValue="${store.company.name}" title="公司" expandAll="true"
				url="/sys/office/treeData?type=1" cssClass="input-small"
				allowClear="true" />
			<label>门店名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="50"
				class="input-small" />
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>门店名称</th>
				<th>归属公司</th>
				<th>备注</th>
				<shiro:hasPermission name="eat:store:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="store">
				<tr>
					<td><a href="${ctx}/eat/store/form?id=${store.id}">${store.name}</a></td>
					<td>${store.company.name}</td>
					<td>${store.remarks}</td>
					<shiro:hasPermission name="eat:store:edit">
						<td><a href="${ctx}/eat/store/form?id=${store.id}">修改</a> <a
							href="${ctx}/eat/store/delete?id=${store.id}"
							onclick="return confirmx('确认要删除该门店吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
