$(document).ready(function(){

    let jwt=window.localStorage.getItem("jwt");
    let accountName=$("#account_name");
    let email=$("#mailbox");    

    //解除封禁
    $("#cancelBan").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="admin_login.html";
        }

        if(((accountName.val()==null)||(accountName.val()=="")||(!accountName.val()))&&
        ((email.val()==null)||(email.val()=="")||(!email.val()))){
            alert("请输入用户名或者邮箱！");
            return;
        }

        let url="";
        if((accountName.val()!=null)&&(accountName.val()!="")&&(accountName.val())){
            url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/ban/"+accountName.val()+"/1";
        } else {
            url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/ban/"+email.val()+"/2";
        }

        $.ajax({
            url:url,
            type:"POST",
            cache:false,
            // data:JSON.stringify(questionBox),
            // dataType:"json",
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
                    alert("解除封禁成功！");
                    location.reload();
                } else {
                    alert(result.info);
                    if((result.infoCode==1104)||(result.infoCode==1101)){
                        top.location.href="admin_login.html"
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

    //查询用户信息
    $("#qButton").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="admin_login.html";
        }

        if(((accountName.val()==null)||(accountName.val()=="")||(!accountName.val()))&&
        ((email.val()==null)||(email.val()=="")||(!email.val()))){
            alert("请输入用户名或者邮箱！");
            return;
        }

        subminQ(accountName.val(),email.val());

        $('#oneAccount').fadeOut(function(){
            $("#allAccount").fadeOut();
            $('#getOneAccount').fadeIn(function(){
                
            });
        });

    });    

    //查询信息
    function subminQ(accountName,email){
        let url="";
        if((accountName!=null)&&(accountName!="")&&(accountName)){
            url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/users/accountQInfo/"+accountName+"/1";
        } else {
            url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/users/accountQInfo/"+email+"/2";
        }

        $.ajax({
            url:url,
            type:"GET",
            cache:false,
            // data:JSON.stringify(QuestionBox),
            // dataType:"json",
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
                    let beQuestionedNum=result.data.beQuestionedNum;
                    let questionAnsNum=result.data.questionAnsNum;
                    let questionNum=result.data.questionNum;

                    for(let i=0;i<beQuestionedNum;i++){
                        let da=new Date(result.data.questionBoxesG[i].questionTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;

                        let s=
                        `<tr>
                            <td class="questionId">${result.data.questionBoxesG[i].questionId}</td>
                            <td class="questionContent">${result.data.questionBoxesG[i].questionContent}</td>
                            <td class="questionTime">${time}</td>
                        </tr>`
                        $("#beQuestionedNum").append(s);
                    }

                    for(let i=0;i<questionAnsNum;i++){
                        let da=new Date(result.data.answerBoxesY[i].answerTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;

                        let s=
                        `<tr>
                            <td class="questionId">${result.data.answerBoxesY[i].questionId}</td>
                            <td class="questionContent">${result.data.answerBoxesY[i].answerContent}</td>
                            <td class="questionTime">${time}</td>
                        </tr>`
                        $("#questionAnsNum").append(s);
                    }

                    for(let i=0;i<questionNum;i++){
                        let da=new Date(result.data.questionBoxes[i].questionTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;

                        let s=
                        `<tr>
                            <td class="questionId">${result.data.questionBoxes[i].questionId}</td>
                            <td class="questionContent">${result.data.questionBoxes[i].questionContent}</td>
                            <td class="questionTime">${time}</td>
                            <td class="operate">
                                <button class="tipOffA">封禁用户</button>
                            </td>
                        </tr>`
                        $("#questionNum").append(s);
                    }

                } else {
                    alert(result.info);
                    if((result.infoCode==1104)||(result.infoCode==1101)){
                        top.location.href="admin_login.html"
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
    }

    //封禁用户
    $(document).on('click','.tipOffA',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="admin_login.html";
        }

        //TODO
        let QuestionBox={
            questionContent:"自定义封禁理由"
        }

        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/ban/"+questionId+"/1";

        $.ajax({
            url:url,
            type:"PUT",
            cache:false,
            data:JSON.stringify(QuestionBox),
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
                    alert("封禁成功！");
                    location.reload();
                } else {
                    alert(result.info);
                    if((result.infoCode==1104)||(result.infoCode==1101)){
                        top.location.href="admin_login.html"
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

    //查询所有用户信息
    $("#AllAccount").click(function(){
        $('#getOneAccount').fadeOut(10);
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="admin_login.html";
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/users/accountInfo",
            type:"GET",
            cache:false,
            // data:JSON.stringify(questionBox),
            // dataType:"json",
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
                    let len=result.data.num;
                    for(let i=0;i<len;i++){
                        let iden=result.data.accountInfos[i].identity;
                        let ac,ban;
                        if(iden==3){
                            ac="N";
                            ban="N";
                        } else if(iden==4){
                            ac="Y";
                            ban="Y";
                        } else {
                            ac="Y";
                            ban="N";                            
                        }

                        let s=
                        `<tr>
                            <td class="accountName">${result.data.accountInfos[i].accountName}</td>
                            <td class="active">${ac}</td>
                            <td class="ban">${ban}</td>
                            <td class="beQuestionedNum">${result.data.accountInfos[i].beQuestionedNum}</td>
                            <td class="questionNum">${result.data.accountInfos[i].questionNum}</td>
                            <td class="questionAnsNum">${result.data.accountInfos[i].questionAnsNum}</td>
                        </tr>`
                        $("#allAccountTable").append(s);
                    }
                } else {
                    alert(result.info);
                    if((result.infoCode==1104)||(result.infoCode==1101)){
                        top.location.href="admin_login.html"
                    }
                }
            },

            error:function(XMLHttpRequest, textStatus, errorThrown){
                if(XMLHttpRequest.status==403){
                    alert("您没有权限！")
                }
            }
        });
        // return false;
    });


})