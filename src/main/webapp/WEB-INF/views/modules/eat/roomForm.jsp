<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>房间管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/eat/room/">房间列表</a></li>
		<li class="active"><a href="${ctx}/eat/room/form?id=${room.id}">房间<shiro:hasPermission
					name="eat:room:edit">${not empty room.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="eat:room:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="room"
		action="${ctx}/eat/room/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
				<form:input path="" id="companyId" htmlEscape="false"
					maxlength="200" class="required" disabled="true"
					value="${room.floor.store.company.name}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属门店:</label>
			<div class="controls">
				<form:input path="" id="storeId" htmlEscape="false" maxlength="200"
					class="required" disabled="true" value="${room.floor.store.name}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属楼层:</label>
			<div class="controls">
				<tags:treeselect id="floor" name="floor.id" value="${room.floor.id}"
					labelName="floor.name" labelValue="${room.floor.name}"
					allowClear="true" title="楼层" lastDataValueLabel="楼层"
					url="/eat/store/treeDataForEat?type=1&level=2" cssClass="required" expandAll="true"
					notAllowSelectParent="true" secondLabelIds="companyId,storeId" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="200" class="input-xxlarge" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="eat:room:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
