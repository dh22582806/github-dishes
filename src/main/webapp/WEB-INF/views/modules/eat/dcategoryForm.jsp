<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>类别管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
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
		<li><a href="${ctx}/eat/dcategory/">类别列表</a></li>
		<li class="active"><a
			href="${ctx}/eat/dcategory/form?id=${dcategory.id}">类别<shiro:hasPermission
					name="eat:dcategory:edit">${not empty dcategory.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="eat:dcategory:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="dcategory"
		action="${ctx}/eat/dcategory/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
				<form:input path="" id="companyId" htmlEscape="false"
					maxlength="200" class="required" disabled="true"
					value="${dcategory.store.company.name}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属门店:</label>
			<div class="controls">
				<tags:treeselect id="store" name="store.id"
					value="${dcategory.store.id}" labelName="store.name"
					labelValue="${dcategory.store.name}" allowClear="true" title="门店"
					lastDataValueLabel="门店"
					url="/eat/store/treeDataForEat?type=1&level=1" cssClass="required"
					expandAll="true" notAllowSelectParent="true"
					secondLabelIds="companyId" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">菜品类别:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">编码:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="5"
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
			<shiro:hasPermission name="eat:dcategory:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
