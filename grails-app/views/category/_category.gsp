<ul>
    <g:each in="${category.children}" var="child">
        <li>
            ${child.name}
            <g:render template="category" model="${[category: child]}" />
        </li>
    </g:each>
</ul>