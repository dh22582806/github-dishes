<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>楼层管理</title>
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
		<li class="active"><a href="${ctx}/eat/floor/">楼层列表</a></li>
		<shiro:hasPermission name="eat:floor:edit">
			<li><a href="${ctx}/eat/floor/form">楼层添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="floor"
		action="${ctx}/eat/floor/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>归属公司：</label> <input type="hidden" id="hiddenCompanyId"
				name="store.company.name" />
			<form:input path="" id="companyId" htmlEscape="false" maxlength="200"
				class="input-small" value="${floor.store.company.name}" disabled="true" />
		</div>
		<div style="margin-top:8px;">
			<label>归属门店：</label>
			<tags:treeselect id="store" name="store.id" value="${floor.store.id}"
				labelName="store.name" labelValue="${floor.store.name}" title="门店"  lastDataValueLabel="门店"
				url="/eat/store/treeDataForEat?type=1&level=1" cssClass="input-small" expandAll="true"
				allowClear="true" notAllowSelectParent="true"
				secondLabelIds="companyId" />
			<label>楼层名称：</label>
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
				<th>楼层名称</th>
				<th>归属公司</th>
				<th>归属门店</th>
				<th>备注</th>
				<shiro:hasPermission name="eat:floor:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="floor">
				<tr>
					<td><a href="${ctx}/eat/floor/form?id=${floor.id}">${floor.name}</a></td>
					<td>${floor.store.company.name}</td>
					<td>${floor.store.name}</td>
					<td>${floor.remarks}</td>
					<shiro:hasPermission name="eat:floor:edit">
						<td><a href="${ctx}/eat/floor/form?id=${floor.id}">修改</a> <a
							href="${ctx}/eat/floor/delete?id=${floor.id}"
							onclick="return confirmx('确认要删除该楼层吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
