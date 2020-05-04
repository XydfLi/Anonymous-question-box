$(document).ready(function(){
    
    $("#register_form").validate({
        rules:{
            account_name:{
                required:true,
                maxlength:15
            },
            mailbox:{
                required:true,
                maxlength:25
            },
            pwd:{
                required:true,
                maxlength:20
            },
            repwd:{
                required:true,
                maxlength:20,
                equalTo:"#pwd"
            },
            code:{
                required:true,
                maxlength:6
            }
        },
        messages:{
            account_name:{
                required:"请输入用户名",
                maxlength:"用户名长度必须小于15"
            },
            mailbox:{
                required:"请输入邮箱",
                maxlength:"邮箱长度必须小于25"
            },
            pwd:{
                required:"请输入密码",
                maxlength:"密码长度必须小于20"
            },
            repwd:{
                required:"请再次输入密码",
                maxlength:"密码长度必须小于20",
                equalTo:"两次输入的密码必须一致"
            },
            code:{
                required:"请输入验证码",
                maxlength:"验证码长度必须小于6"
            }
        }
    });

    let account_name=$("#account_name");
    let pwd=$("#pwd");
    let repwd=$("#repwd");
    let auth_code=$("#code");
    let mailbox=$("#mailbox");

    let randomAESKey="";
    let pubKey="";

    //提交用户名登录表单
    $("#registerButton").click(function(){
        if((!account_name.val())||(account_name.val()==null)||(account_name.val()=="")||
        (!pwd.val())||(pwd.val()==null)||(pwd.val()=="")||
        (!auth_code.val())||(auth_code.val()==null)||(auth_code.val()=="")||
        (!repwd.val())||(repwd.val()==null)||(repwd.val()=="")||
        (!mailbox.val())||(mailbox.val()==null)||(mailbox.val()=="")){
            return;
        }

        if(pwd.val()!=repwd.val()){
            alert("两次密码必须相等");
            return;
        }

        if(!checkPassword(pwd.val())){
            alert("密码不合法");
            return;
        }

        if(onlyLetter(pwd.val())||onlyNumber(pwd.val())||onlyUnderline(pwd.val())){
            alert("密码必须包含字母、数字、下划线至少两种");
            return;
        }

        if(!checkEmail(mailbox.val())){
            alert("邮箱不合法");
            return;
        }

        if(!checkCode(auth_code.val())){
            alert("验证码不合法");
            return;
        }

        randomAESKey=randomString(16);//随机生成一个AES秘钥
        let account={
            accountName:account_name.val(),
            mailbox:mailbox.val(),
            password:encrypt(pwd.val(),randomAESKey)//AES加密秘钥
        }
        getRSAPublicKey();//获得RSA公钥
        let data={
            key:encryptRSA(randomAESKey,pubKey),//RSA公钥加密AES私钥
            data:account
        };

        $.ajax({
            url:`http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/users/register/${auth_code.val()}`,
            type:"POST",
            cache:false,
            data:JSON.stringify(data),
            dataType:"json",
            contentType:"application/json",
            crossDomain:true,
            async:false,
            xhrFields:{
                withCredentials:true
            },
            success:function(result,status,xhr){
                if(result.infoCode==1000){
                    alert("注册成功！");
                    $(location).attr("href","login.html");
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
        return false;
    });

    function getRSAPublicKey(){
        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/users/getKey",
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