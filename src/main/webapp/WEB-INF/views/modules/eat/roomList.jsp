<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>房间管理</title>
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
		<li class="active"><a href="${ctx}/eat/room/">房间列表</a></li>
		<shiro:hasPermission name="eat:room:edit">
			<li><a href="${ctx}/eat/room/form">房间添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="room"
		action="${ctx}/eat/room/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>归属公司：</label> <input type="hidden" id="hiddenCompanyId"
				name="floor.store.company.name" />
			<form:input path="" id="companyId" htmlEscape="false" maxlength="200"
				class="input-small" value="${room.floor.store.company.name}"
				disabled="true" />
			<label>归属楼层：</label>
			<tags:treeselect id="floor" name="floor.id" value="${room.floor.id}"
				labelName="floor.name" labelValue="${room.floor.name}"
				allowClear="true" title="楼层" lastDataValueLabel="楼层"
				url="/eat/store/treeDataForEat?type=1&level=2" expandAll="true"
				cssClass="input-small" notAllowSelectParent="true"
				secondLabelIds="companyId,storeId" />

		</div>
		<div style="margin-top: 8px;">
			<label>归属门店：</label> <input type="hidden" id="hiddenStoreId"
				name="floor.store.name" />
			<form:input path="" id="storeId" htmlEscape="false" maxlength="200"
				class="input-small" value="${room.floor.store.name}" disabled="true" />

			<label>房间名称：</label>
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
				<th>房间名称</th>
				<th>归属公司</th>
				<th>归属门店</th>
				<th>归属楼层</th>
				<th>备注</th>
				<shiro:hasPermission name="eat:room:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="room">
				<tr>
					<td><a href="${ctx}/eat/room/form?id=${room.id}">${room.name}</a></td>
					<td>${room.floor.store.company.name}</td>
					<td>${room.floor.store.name}</td>
					<td>${room.floor.name}</td>
					<td>${room.remarks}</td>
					<shiro:hasPermission name="eat:room:edit">
						<td><a href="${ctx}/eat/room/form?id=${room.id}">修改</a> <a
							href="${ctx}/eat/room/delete?id=${room.id}"
							onclick="return confirmx('确认要删除该房间吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
