<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Table tag" pageEncoding="UTF-8" %>
<%@attribute name="columns" type="java.util.List" required="false" %>
<%@attribute name="records" type="java.util.List" required="true" %>
<!-- table-content -->
<table class="table">
    <!-- table-heading -->
    <thead class="table-heading">
        <tr>
            <th class="checkbox">
                <form action="#">
                    <fieldset>
                        <input type="checkbox" class="checkbox"/>
                        <input type="submit" class="btn-send hidden"/>
                    </fieldset>
                </form>
            </th>

            <th class="number">
                <a class="header" href="#">Project #</a>
            </th>
            <c:forEach items="${columns}" var="column" varStatus="rowNumber">
                <th class="${column.type}"><a class="header" href="#" data-fieldName="${column.name}">${column.displayName}</a></th>
            </c:forEach>
        </tr>
    </thead>

    <tbody class="table-content">
        <c:forEach items="${records}" var="record" varStatus="rowNumber">
            <tr class="table-row">
                    <td>
                    <form action="#">
                            <fieldset>
                                <input type="checkbox"/>
                                <input type="submit" class="btn-send hidden"/>
                            </fieldset>
                        </form>
                    </td>
                    <td class="number">${(selectedPage -1)*10 + rowNumber.count}</td>
                    <c:forEach items="${columns}" var="column" varStatus="rowNumber">
                        <td class="table-cell ${column.type}">${record[column.name]}</td>
                    </c:forEach>
            </tr>
        </c:forEach>
    </tbody>
</table>
<ul class="pagination">
    <li><a href="#">&laquo;</a></li>
    <li><a href="#">1</a></li>
    <li><a href="#">2</a></li>
    <li><a href="#">3</a></li>
    <li><a href="#">4</a></li>
    <li><a href="#">5</a></li>
    <li><a href="#">&raquo;</a></li>
</ul>
