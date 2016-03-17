<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@taglib prefix="acl" uri="http://www.baozun.cn/acl"%>
<%@taglib prefix="url" uri="http://www.baozun.cn/url"%>
<c:set var="base" scope="request"><%=request.getContextPath()%></c:set>
<c:set var="pagebase" scope="request"><%=request.getContextPath()%></c:set>
<c:set var="staticbase" scope="request"><%=request.getContextPath()%></c:set>
<c:set var="imgbase" scope="request"><%=request.getContextPath()%></c:set>
<c:set var="title" scope="application"><spring:message code="platform.name" /></c:set>