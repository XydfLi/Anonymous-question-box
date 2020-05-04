$(document).ready(function(){
    //获取路径参数
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }

    //激活邮箱
    function actEmail(){
        let sign=getQueryString("sign");
        if((sign==null)||(sign=="")||(!sign)){
            return;
        }

        let account={
            mailbox:sign,
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/users/actEmail",
            type:"POST",
            cache:false,
            data:JSON.stringify(account),
            dataType:"json",
            crossDomain:true,
            async:false,
            xhrFields:{
                withCredentials:true
            },
            contentType:"application/json",
            success:function(result,status,xhr){
                if(result.infoCode==1000){
                    document.getElementById("actText").innerHTML="邮箱激活成功！";
                } else {
                    alert(result.info);
                    if((result.infoCode==1104)||(result.infoCode==1101)){
                        top.location.href="login.html"
                    }
                }
            },

            error:function(XMLHttpRequest, textStatus, errorThrown){
                if(XMLHttpRequest.status==403){
                    alert("您没有权限！")
                }
            }
        });
    }
    actEmail();

});