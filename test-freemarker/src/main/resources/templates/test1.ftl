<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!1111
<table>
    <tr>
        <td>编号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>日期</td>
        <td>朋友</td>
        <td>最好朋友</td>
    </tr>
    <#list ar as stu>
        <tr>
            <td>${stu_index+1}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            <td>${stu.birthday?string('yyyy-MM-dd HH:mm:ss')}</td>
            <td>
                <#if stu.friends??>
                    <#list stu.friends as f>
                            ${f.name!''}
                    </#list>
                <#else >
                    无朋友
                </#if>

            </td>
            <td>
                ${(stu.bestFriends.name)!'无朋友'}
            </td>

        </tr>
    </#list>
</table>


<#--第一种方式 直接map名称[‘key’].属性-->
姓名:${stumap['s1'].name}
<br/>
<#---->
<#list stumap?keys as key>
    ${key}:
    ${stumap[key].name}-----
</#list>
</body>
</html>