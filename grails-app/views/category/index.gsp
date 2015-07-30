<%--
  Created by IntelliJ IDEA.
  User: jtoddington
  Date: 04/05/15
  Time: 13:25
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>

    <g:each in="${categories}" var="category">
        <g:render template="category" model="${[category: category]}" />
    </g:each>


    <%--

    <ul>
        <g:each in="${categories}" var="c">
            <li>${c.name}
                <ul>
                <g:each in="${c.children}" var="child">
                    <g:if test="${child != null}">
                        ${child.name}
                    </g:if>
                    <g:else>
                        it's null!
                    </g:else>
                </g:each>
                </ul>
            </li>
        </g:each>
    </ul>

    --%>

</body>
</html>