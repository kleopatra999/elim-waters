<%@tag description="Table tag" pageEncoding="UTF-8" %>
<%@attribute name="data" %>
<%@attribute name="columns" %>
<table>
    <thead>
    <tr>
        <c:forEach items="${columns}" var="column">
            <th>${columns[i].name}</th>
        </c:forEach>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="row" items="${data}">
        <tr>
            <c:forEach var="column" items="${row}">
                <td>
                    ${column}
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>