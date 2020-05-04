$(document).ready(function(){
    
    $("#register_form").validate({
        rules:{
            oldPwd:{
                required:true,
                maxlength:20
            },
            newPwd:{
                required:true,
                maxlength:20
            },
            repwd:{
                required:true,
                maxlength:20,
                equalTo:"#newPwd"
            }
        },
        messages:{
            oldPwd:{
                required:"请输入原密码",
                maxlength:"密码长度必须小于20"
            },
            newPwd:{
                required:"请输入新密码",
                maxlength:"密码长度必须小于20"
            },
            repwd:{
                required:"请再次输入密码",
                maxlength:"密码长度必须小于20",
                equalTo:"两次输入的密码必须一致"
            }
        }
    });

    let oldPwd=$("#oldPwd")
    let newPwd=$("#newPwd")
    let repwd=$("#repwd");
    // let jwt=window.localStorage.getItem("jwt")

    let randomAESKey="";
    let pubKey="";

    //提交用户名登录表单
    $("#submitPasswordButton").click(function(){
        // if((!jwt)||(jwt==null)||(jwt=="")){
        //     alert("请先登录")
        //     top.location.href="login.html"
        // }
        let sign=getQueryString("sign");
        if((sign==null)||(sign=="")||(!sign)||
        (oldPwd.val()==null)||(oldPwd.val()=="")||(!oldPwd.val())||
        (newPwd.val()==null)||(newPwd.val()=="")||(!newPwd.val())||
        (repwd.val()==null)||(repwd.val()=="")||(!repwd.val())){
            return;
        }

        if(newPwd.val()!=repwd.val()){
            alert("两次密码必须相等");
            return;
        }

        if(!checkPassword(newPwd.val())){
            alert("密码不合法");
            return;
        }

        if(onlyLetter(newPwd.val())||onlyNumber(newPwd.val())||onlyUnderline(newPwd.val())){
            alert("密码必须包含字母、数字、下划线至少两种");
            return;
        }

        randomAESKey=randomString(16);//随机生成一个AES秘钥
        let account={
            accountName:encrypt(oldPwd.val(),randomAESKey),//AES加密秘钥
            mailbox:sign,
            password:encrypt(newPwd.val(),randomAESKey)//AES加密秘钥
        }
        getRSAPublicKey();//获得RSA公钥
        let data={
            key:encryptRSA(randomAESKey,pubKey),//RSA公钥加密AES私钥
            data:account
        };

        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/users/password";

        // alert(data)
        // alert(url)
        $.ajax({
            url:url,
            type:"PUT",
            cache:false,
            data:JSON.stringify(data),
            dataType:"json",
            contentType:"application/json",
            crossDomain:true,
            async:false,
            xhrFields:{
                withCredentials:true
            },
            // beforeSend:function(xhr){
            //     xhr.setRequestHeader("jwt",jwt);
            // },
            success:function(result,status,xhr){
                if(result.infoCode==1000){
                    alert("密码修改成功！");
                    $(location).attr("href","login.html");
                } else {
                    alert(result.info);
                    if((result.infoCode==1104)||(result.infoCode==1101)){
                        top.location.href="login.html"
                    }
                }
            },

            error:function(XMLHttpRequest, textStatus, errorThrown){
                if(XMLHttpRequest.status==500){
                    alert("签名非法！")
                }
                if(XMLHttpRequest.status==403){
                    alert("您没有权限！")
                }
            }
        });
        return false;
    });

    //获取路径参数
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }

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