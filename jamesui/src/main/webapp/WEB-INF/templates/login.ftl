<html>
<head>
    <style type="text/css">
        body {
            height: 100%;
        }

        body > table {
            width: 100%;
            height: 100%;
            background-color: #f5f5f5;
        }

        body > table > tbody > tr > td
        {
            text-align: center;
        }

        form > table
        {
            margin-left:auto;
            margin-right:auto;
        }

        .error
        {
            font-weight: bold;
            color: red;
        }
    </style>
</head>

<body>
<table>
    <tr>
        <td>
            <h1>Welcome to James UI</h1>
            <form method="post" action="/jamesui/j_spring_security_check">
                <table>
                    <tr>
                        <td>Username</td>
                        <td><input type="text" name="j_username"/></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="j_password"/></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td><input type="submit" value="Login"></td>
                    </tr>
                </table>
            </form>

            <#if isError?? && isError?string == "true">
                <div class="error">User or Password not valid</div>
            </#if>

        </td>
    </tr>
</table>
</body>
</html>