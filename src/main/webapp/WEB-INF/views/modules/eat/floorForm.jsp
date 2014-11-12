<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>楼层管理</title>
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
		<li><a href="${ctx}/eat/floor/">楼层列表</a></li>
		<li class="active"><a href="${ctx}/eat/floor/form?id=${floor.id}">楼层<shiro:hasPermission
					name="eat:floor:edit">${not empty floor.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="eat:floor:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="floor"
		action="${ctx}/eat/floor/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
				<form:input path="" id="companyId" htmlEscape="false"
					maxlength="200" class="required" disabled="true"
					value="${floor.store.company.name}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属门店:</label>
			<div class="controls">
				<tags:treeselect id="store" name="store.id"
					value="${floor.store.id}" labelName="store.name"
					labelValue="${floor.store.name}" allowClear="true" title="门店" lastDataValueLabel="门店"
					url="/eat/store/treeDataForEat?type=1&level=1" cssClass="required" expandAll="true"
					notAllowSelectParent="true" secondLabelIds="companyId" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼层名称:</label>
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
			<shiro:hasPermission name="eat:floor:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
