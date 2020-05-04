$(document).ready(function(){

    $("#register_form").validate({
        rules:{
            mailbox:{
                required:true,
                maxlength:25
            }
        },
        messages:{
            mailbox:{
                required:"请输入邮箱",
                maxlength:"邮箱长度必须小于25"
            }
        }
    });

    let jwt=window.localStorage.getItem("jwt")
    let mailbox=$("#mailbox");

    let randomAESKey="";
    let pubKey="";

    //修改邮箱
    $("#submitMailboxButton").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录");
            window.location.href="login.html";
        }

        if((!mailbox.val())||(mailbox.val()==null)||(mailbox.val()=="")){
            return;
        }
        if(!checkEmail(mailbox.val())){
            alert("邮箱不合法");
            return;
        }

        let account={
            mailbox:mailbox.val()
        }

        $.ajax({
            url:"http://localhost:8080/1.0/users/passwordUri",
            type:"POST",
            cache:false,
            data:JSON.stringify(account),
            dataType:"json",
            crossDomain:true,
            async:false,
            xhrFields:{
                withCredentials:true
            },
            beforeSend:function(xhr){
                xhr.setRequestHeader("jwt",jwt);
            },
            contentType:"application/json",
            success:function(result,status,xhr){
                if(result.infoCode==1000){
                    alert("已发送链接到您的邮箱！");
                    // top.location.href="login.html"
                } else {
                    alert(result.info);
                    if((result.infoCode==1104)||(result.infoCode==1101)){
                        window.location.href="login.html";
                    }
                }
            },

            error:function(XMLHttpRequest, textStatus, errorThrown){
                window.location.href="login.html";////////////////////////////////非法jwt可能的错误
                if(XMLHttpRequest.status==403){
                    alert("您没有权限！")
                    return;
                }
                alert("请先登录")
                        
            }
        });
        return false;
    });

    function getRSAPublicKey(){
        $.ajax({
            url:"http://localhost:8080/1.0/users/getKey",
            type:"GET",
            dataType:"json",
            cache:false,
            crossDomain:true,
            async:false,

            success: function(result) {
                if(result.info == "OK") {
                    pubKey=result.data;
                } else {
                    alert(result.info);
                }
            },

            error:function(XMLHttpRequest, textStatus, errorThrown){
                if(XMLHttpRequest.status==403){
                    alert("您没有权限！")
                }
            }
        });
    }


});