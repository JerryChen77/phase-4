<%--
  Created by IntelliJ IDEA.
  User: duo
  Date: 2019/7/2
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>支付页面</title>
</head>
<body>

<h1>请扫码支付</h1>
    <img src="/wxpay/doPay"/>
</body>
<script type="text/javascript" src="http://cdn-hangzhou.goeasy.io/goeasy.js"></script>
<script type="text/javascript">
    var goEasy = new GoEasy({
        appkey: 'BC-13372467cbde47cb8fa70538dbab4755'
    });
    //GoEasy-OTP可以对appkey进行有效保护，详情请参考：GoEasy-Reference

    goEasy.subscribe({
        channel:'Java1902',
        onMessage: function(message){
            // alert('收到：'+message.content);
            if(message.content=='success'){
                console.log(essage.content);
                location.href='./pay_suc.jsp';
            }

        }
    });


</script>
</html>
