<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>桌台管理</title>
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
		$("#hiddenFloorId").val($("#floorId").val());
		$("#searchForm").submit();
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/eat/table/">桌台列表</a></li>
		<shiro:hasPermission name="eat:table:edit">
			<li><a href="${ctx}/eat/table/form">桌台添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="table"
		action="${ctx}/eat/table/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>归属公司：</label> <input type="hidden" id="hiddenCompanyId"
				name="room.floor.store.company.name" />
			<form:input path="" id="companyId" htmlEscape="false" maxlength="200"
				class="input-small" value="${table.room.floor.store.company.name}"
				disabled="true" />
			<label>归属楼层：</label> <input type="hidden" id="hiddenFloorId"
				name="room.floor.name" />
			<form:input path="" id="floorId" htmlEscape="false" maxlength="200"
				class="input-small" value="${table.room.floor.name}" disabled="true" />

		</div>
		<div style="margin-top: 8px;">
			<label>归属门店：</label> <input type="hidden" id="hiddenStoreId"
				name="room.floor.store.name" />
			<form:input path="" id="storeId" htmlEscape="false" maxlength="200"
				class="input-small" value="${table.room.floor.store.name}"
				disabled="true" />
			<label>归属房间：</label>
			<tags:treeselect id="room" name="room.id" value="${table.room.id}"
				labelName="room.name" labelValue="${table.room.name}"
				allowClear="true" title="房间" lastDataValueLabel="房间" expandAll="true"
				url="/eat/store/treeDataForEat?type=1&level=3"
				cssClass="input-small" notAllowSelectParent="true"
				secondLabelIds="companyId,storeId,floorId" />
			<label>桌台名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="50"
				class="input-small" />
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button"
				onClick="doSubmit()"
				value="查询" />
		</div>


	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>桌台名称</th>
				<th>归属公司</th>
				<th>归属门店</th>
				<th>归属楼层</th>
				<th>归属房间</th>
				<th>备注</th>
				<shiro:hasPermission name="eat:table:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="table">
				<tr>
					<td><a href="${ctx}/eat/table/form?id=${table.id}">${table.name}</a></td>
					<td>${table.room.floor.store.company.name}</td>
					<td>${table.room.floor.store.name}</td>
					<td>${table.room.floor.name}</td>
					<td>${table.room.name}</td>
					<td>${table.remarks}</td>
					<shiro:hasPermission name="eat:table:edit">
						<td><a href="${ctx}/eat/table/form?id=${table.id}">修改</a> <a
							href="${ctx}/eat/table/delete?id=${table.id}"
							onclick="return confirmx('确认要删除该桌台吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
