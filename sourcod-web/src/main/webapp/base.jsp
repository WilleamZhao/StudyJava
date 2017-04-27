<%--
  Created by IntelliJ IDEA.
  User: sourcod
  Date: 2017/4/27
  Time: 下午4:02
  To change this template use File | Settings | File Templates.

  base 页面
  文件不放在webapp下是为了方便查看
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>