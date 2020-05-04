$(document).ready(function(){

    //选择用户名登录按钮
    $("#accountNameButton").click(function(){
        let login=document.getElementsByClassName("login_con");
        login[0].classList.remove("hidden");
        login[0].classList.add("show");
        login[1].classList.remove("show");
        login[1].classList.add("hidden");
        let tags=document.getElementsByClassName("top_tag");
        tags[0].classList.add("active");
        tags[1].classList.remove("active");
    });

    //选择邮箱登录按钮
    $("#mailboxButton").click(function(){
        let login=document.getElementsByClassName("login_con");
        login[0].classList.remove("show");
        login[0].classList.add("hidden");
        login[1].classList.remove("hidden");
        login[1].classList.add("show");
        let tags=document.getElementsByClassName("top_tag");
        tags[1].classList.add("active");
        tags[0].classList.remove("active");
    });

    let account_name=$("#account_name");
    let pwd=$("#pwd");
    let auth_code=$("#auth_code");
    let em_pwd=$("#em_pwd");
    let em_auth_code=$("#em_auth_code");
    let mailbox=$("#mailbox");

    let randomAESKey="";
    let pubKey="";

    //提交用户名登录表单
    $("#ac_login").click(function(){
        if((!account_name.val())||(account_name.val()==null)||(account_name.val()=="")||
        (!pwd.val())||(pwd.val()==null)||(pwd.val()=="")||
        (!auth_code.val())||(auth_code.val()==null)||(auth_code.val()=="")){
            return;
        }

        // if(account_name.val()!="admin"){//这个页面不允许非管理员登录
        //     alert("用户名非法");
        //     return;
        // }

        if(!checkPassword(pwd.val())){
            alert("密码不合法");
            return;
        }

        if(!checkCode(auth_code.val())){
            alert("验证码不合法");
            return;
        }

        randomAESKey=randomString(16);//随机生成一个AES秘钥
        let account={
            accountName:account_name.val(),
            password:encrypt(pwd.val(),randomAESKey)//AES加密秘钥
        }
        getRSAPublicKey();//获得RSA公钥
        let data={
            key:encryptRSA(randomAESKey,pubKey),//RSA公钥加密AES私钥
            data:account
        };

        $.ajax({
            url:`http://localhost:8080/1.0/users/login/${auth_code.val()}/2`,
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
                    alert("用户名登录成功");
                    window.localStorage.setItem("jwt",xhr.getResponseHeader("jwt"));
                    $(location).attr("href","admin_home.html");
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

    //提交邮箱登录表单
    $("#em_login").click(function(){
        if((!mailbox.val())||(mailbox.val()==null)||(mailbox.val()=="")||
        (!em_pwd.val())||(em_pwd.val()==null)||(em_pwd.val()=="")||
        (!em_auth_code.val())||(em_auth_code.val()==null)||(em_auth_code.val()=="")){
            return;
        }

        if(!checkEmail(mailbox.val())){
            alert("邮箱不合法");
            return;
        }

        if(!checkPassword(em_pwd.val())){
            alert("密码不合法");
            return;
        }

        if(!checkCode(em_auth_code.val())){
            alert("验证码不合法");
            return;
        }

        randomAESKey=randomString(16);//随机生成一个AES秘钥
        let account={
            mailbox:mailbox.val(),
            password:encrypt(em_pwd.val(),randomAESKey)//AES加密秘钥
        }
        getRSAPublicKey();//获得RSA公钥
        let data={
            key:encryptRSA(randomAESKey,pubKey),//RSA公钥加密AES私钥
            data:account
        };

        $.ajax({
            url:`http://localhost:8080/1.0/users/login/${em_auth_code.val()}/2`,
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
                    alert("邮箱登录成功");
                    window.localStorage.setItem("jwt",xhr.getResponseHeader("jwt"));
                    $(location).attr("href","admin_home.html");
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

        //不加这一行就不能跳转页面
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