<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>
<h2>회원 등록</h2>

<div>
    <form name="frm">
        <sec:csrfInput />
        아이디:<input type="text" name="username">
        비밀번호:<input type="text" name="password">
        이름:<input type="text" name="name">
        이메일:<input type="text" name="email">
    </form>
    <button type="button" data-btn="btnCreate">등록</button>
</div>

<script>
    $(function () {
        $("button[data-btn='btnCreate']").on("click", function () {
            $.ajax({
                url: "/user",
                type: "POST",
                data: $("form[name=frm]").serialize(),
                dataType: "json",
                success: function (response) {
                    console.log(response.data);
                    alert(response.message);
                    location.href = "/user";
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseJSON.message);
                    $(xhr.responseJSON.errors || []).each(function (i, error) {
                        alert(error.errorField + ": " + error.errorMessage);
                    });
                }
            })
        })
    })
</script>

</body>
</html>
