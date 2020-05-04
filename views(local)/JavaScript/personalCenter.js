$(document).ready(function(){
    
    $("#updateAccountName").validate({
        rules:{
            account_name:{
                required:true,
                maxlength:15
            }
        },
        messages:{
            account_name:{
                required:"请输入用户名",
                maxlength:"用户名长度必须小于15"
            }
        }
    });

    $("#updataMailbox").validate({
        rules:{
            mailbox:{
                required:true,
                maxlength:25
            },
            pwd:{
                required:true,
                maxlength:20
            }
        },
        messages:{
            mailbox:{
                required:"请输入邮箱",
                maxlength:"邮箱长度必须小于25"
            },
            pwd:{
                required:"请输入密码",
                maxlength:"密码长度必须小于20"
            }
        }
    });

    $("#activeAccount").validate({
        rules:{
            mailbox:{
                required:true,
                maxlength:25
            }
        },
        messages:{
            mailbox:{
                required:"请输入需要激活邮箱",
                maxlength:"邮箱长度必须小于25"
            }
        }
    });

    let jwt=window.localStorage.getItem("jwt")
    let account_name=$("#account_name");
    let mailbox=$("#mailbox");
    let pwd=$("#pwd");
    let actMailbox=$("#actMailbox");

    let randomAESKey="";
    let pubKey="";

    let accountName="visitor";

    $("#goUpdatePassword").click(function(){
        top.location.href="updatePassword.html";
    });

    // $("#goActiveAccount").click(function(){
    //     top.location.href="activeAccount.html";
    // });

    //激活邮箱
    $("#activeAccountButton").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html"
        }
        if((!actMailbox.val())||(actMailbox==null)||(actMailbox=="")){
            return;
        }

        if(!checkEmail(actMailbox.val())){
            alert("邮箱不合法");
            return;
        }

        let account={
            mailbox:actMailbox.val()
        }

        $.ajax({
            url:"http://localhost:8080/1.0/users/emailUri",
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
                    alert("已发送激活链接到您的邮箱！");
                    location.reload();
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
        return false;
    });

    //获得用户名
    function getAccountName(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            document.getElementById("acNa").innerHTML="visitor";
            return;
        }

        $.ajax({
            url:"http://localhost:8080/1.0/users/getAccountName",
            type:"GET",
            cache:false,
            // data:JSON.stringify(data),
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
                    accountName=result.data;
                    document.getElementById("acNa").innerHTML=result.data;
                    // location.reload();
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
    getAccountName();

    //获得头像
    function getAvatar(){
        let el=document.getElementById("myAv");
        if(accountName!="visitor"){
            el.src="http://localhost:8080/1.0/users/getAvatar/"+accountName;
        }

        var myimg,oldwidth,oldheight;
        var maxwidth=200;
        var maxheight=150;
        var imgs = document.getElementById('myIm').getElementsByTagName('img');

        for(i=0;i<imgs.length;i++){
            myimg = imgs[i];

            if(myimg.width > myimg.height)
            {
                if(myimg.width > maxwidth)
                {
                    oldwidth = myimg.width;
                    myimg.height = myimg.height * (maxwidth/oldwidth);
                    myimg.width = maxwidth;
                }
            }else{
                if(myimg.height > maxheight)
                {
                    oldheight = myimg.height;
                    myimg.width = myimg.width * (maxheight/oldheight);
                    myimg.height = maxheight;
                }
            }
        }
    }
    getAvatar();

    //修改用户名
    $("#updateAccountNameButton").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html"
        }
        if((!account_name.val())||(account_name==null)||(account_name=="")){
            return;
        }

        let account={
            accountName:account_name.val(),
        }
        let data={
            key:null,
            data:account
        };

        $.ajax({
            url:"http://localhost:8080/1.0/users/account",
            type:"PUT",
            cache:false,
            data:JSON.stringify(data),
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
                    alert("用户名修改成功！");
                    top.location.href="login.html"
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
        return false;
    });

    //修改邮箱
    $("#updataMailboxButton").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html"
        }

        if((!mailbox.val())||(mailbox.val()==null)||(mailbox.val()=="")||
        (!pwd.val())||(pwd.val()==null)||(pwd.val()=="")){
            return;
        }

        if(!checkEmail(mailbox.val())){
            alert("邮箱不合法");
            return;
        }

        randomAESKey=randomString(16);//随机生成一个AES秘钥
        let account={
            mailbox:mailbox.val(),
            password:encrypt(pwd.val(),randomAESKey)//AES加密秘钥
        }
        getRSAPublicKey();//获得RSA公钥
        let data={
            key:encryptRSA(randomAESKey,pubKey),//RSA公钥加密AES私钥
            data:account
        };

        $.ajax({
            url:"http://localhost:8080/1.0/users/account",
            type:"PUT",
            cache:false,
            data:JSON.stringify(data),
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
                    alert("邮箱修改成功！");
                    top.location.href="login.html"
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
        return false;
    });

    //开启提问箱
    $("#openQ").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html"
        }

        $.ajax({
            url:"http://localhost:8080/1.0/users/questionBoxStatus/1",
            type:"PUT",
            cache:false,
            // data:JSON.stringify(data),
            dataType:"json",
            crossDomain:true,
            async:false,
            xhrFields:{
                withCredentials:true
            },
            beforeSend:function(xhr){
                xhr.setRequestHeader("jwt",jwt);
            },
            // contentType:"application/json",
            success:function(result,status,xhr){
                if(result.infoCode==1000){
                    alert("提问箱打开！");
                    location.reload();
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
        return false;
    });

    //关闭提问箱
    $("#closeQ").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html"
        }

        $.ajax({
            url:"http://localhost:8080/1.0/users/questionBoxStatus/0",
            type:"PUT",
            cache:false,
            // data:JSON.stringify(data),
            dataType:"json",
            crossDomain:true,
            async:false,
            xhrFields:{
                withCredentials:true
            },
            beforeSend:function(xhr){
                xhr.setRequestHeader("jwt",jwt);
            },
            // contentType:"application/json",
            success:function(result,status,xhr){
                if(result.infoCode==1000){
                    alert("提问箱关闭！");
                    location.reload();
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
        return false;
    });

    //上传头像
    $("#upload").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html"
        }

        let form_data = new FormData();
        form_data.append("file", file);

        let formData=new FormData();
        let img_file=document.getElementById("file");
        let fileObj=img_file.files[0];

        if(fileObj==undefined){
            return;
        }

        let point=fileObj.name.lastIndexOf(".");
        let file_type = fileObj.name.substr(point);

        if((file_type!=".jpg")&&(file_type!=".png")&&(file_type!=".JPG")&&(file_type!=".PNG")) {
            alert("文件格式错误！");
            return;
        }        

        formData.append("file",fileObj);

        url="http://localhost:8080/1.0/users/avatar";
        $.ajax({
            url: url,
            type: "POST",
            data: formData,
            async:false,
            contentType: false,
            processData: false,

            beforeSend:function(xhr){
                xhr.setRequestHeader("jwt",jwt);
            },
            success:function(result,status,xhr){
                if(result.infoCode==1000){
                    alert("上传成功！");
                    location.reload();
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