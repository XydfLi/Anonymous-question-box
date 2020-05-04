$(document).ready(function(){

    let jwt=window.localStorage.getItem("jwt");
    let questionContent=$("#questionContent");
    let account_name=$("#account_name");


    let randomAESKey="";
    let pubKey="";

    let questionBoxState=false;

    //提交
    function subAns(url,cont){
        let AnswerBox={
            answerContent:cont
        }

        $.ajax({
            url:url,
            type:"PUT",
            cache:false,
            data:JSON.stringify(AnswerBox),
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
                    alert("提交成功！");
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
        $('#myans').fadeOut(10);
    }

    //回复提问
    $(document).on('click','.cAns',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }

        $('#questionN').fadeOut(function() {
            $('#myans').fadeIn()
        });

        $("#myansButton").click(function(){
            let cont=$("#myansContent").val();
            if(cont==null||cont==""||!cont){
                alert("回答内容不能为空！")
                return;
            }
            let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/"+questionId; 
            subAns(url,cont);
        });
    });

    //举报
    $(document).on('click','.tipQ',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }

        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/tipOff/"+questionId;

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
                    alert("举报成功！");
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

    //拉黑
    $(document).on('click','.blackQ',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }

        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/black/"+questionId;

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
                    alert("拉黑成功！");
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

    //取消删除
    $(document).on('click','.cancelQuesD',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }

        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/"+questionId+"/2";

        $.ajax({
            url:url,
            type:"PUT",
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
                    alert("取消删除成功！");
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

    //删除提问
    $(document).on('click','.quesD',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }

        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/"+questionId+"/1";

        $.ajax({
            url:url,
            type:"PUT",
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
                    alert("删除成功！");
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

    //取消拉黑
    $(document).on('click','.cancelBlack',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }

        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/black/"+questionId;

        $.ajax({
            url:url,
            type:"PUT",
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
                    alert("取消拉黑成功！");
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

    //查询已拉黑提问
    $("#BlackQ").click(function(){
        $('#ansQuesDetail').fadeOut(10);
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }
        questionBoxIsOpen();
        if(!questionBoxState){
            alert("请先打开提问箱");
            return;
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/black",
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
                    // if(len==0){
                    //     alert("您没有一条提问收到回复！");
                    //     return;
                    // }
                    for(let i=0;i<len;i++){
                        let da=new Date(result.data.questions[i].questionTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;
                        let s=
                        `<tr>
                            <td class="questionId">${result.data.questions[i].questionId}</td>
                            <td class="questionContent">${result.data.questions[i].questionContent}</td>
                            <td class="questionTime">${time}</td>
                            <td class="operate">
                                <button class="cancelBlack">取消拉黑</button>
                            </td>
                        </tr>`
                        $("#blackQTable").append(s);
                    }
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
        // return false;
    });

    //查询已回复提问
    $("#QuestionY").click(function(){
        $('#ansQuesDetail').fadeOut(10);
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }
        questionBoxIsOpen();
        if(!questionBoxState){
            alert("请先打开提问箱");
            return;
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/Y",
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
                    // if(len==0){
                    //     alert("您没有一条提问收到回复！");
                    //     return;
                    // }
                    for(let i=0;i<len;i++){
                        let da=new Date(result.data.answers[i].answerTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;
                        let s=
                        `<tr>
                            <td class="questionId">${result.data.answers[i].questionId}</td>
                            <td class="answerContent">${result.data.answers[i].answerContent}</td>
                            <td class="answerTime">${time}</td>
                            <td class="operate">
                                <button class="quesD">删除</button>
                                <button class="tipQ">举报</button>
                                <button class="blackQ">拉黑</button>
                            </td>
                        </tr>`
                        $("#questionYTable").append(s);
                    }
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
        // return false;
    });

    //查询未回复提问
    $("#QuestionN").click(function(){
        $('#ansQuesDetail').fadeOut(10);
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }
        questionBoxIsOpen();
        if(!questionBoxState){
            alert("请先打开提问箱");
            return;
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/N",
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
                    // if(len==0){
                    //     alert("您没有一条提问收到回复！");
                    //     return;
                    // }
                    for(let i=0;i<len;i++){
                        let da=new Date(result.data.questions[i].questionTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;
                        let s=
                        `<tr>
                            <td class="questionId">${result.data.questions[i].questionId}</td>
                            <td class="questionContent">${result.data.questions[i].questionContent}</td>
                            <td class="questionTime">${time}</td>
                            <td class="operate">
                                <button class="cAns">回复</button>
                                <button class="quesD">删除</button>
                                <button class="tipQ">举报</button>
                                <button class="blackQ">拉黑</button>
                            </td>
                        </tr>`
                        $("#questionNTable").append(s);
                    }
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
        // return false;
    });

    //查询已删除提问
    $("#QuestionD").click(function(){
        $('#ansQuesDetail').fadeOut(10);
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }
        questionBoxIsOpen();
        if(!questionBoxState){
            alert("请先打开提问箱");
            return;
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/D",
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
                    // if(len==0){
                    //     alert("您没有一条提问收到回复！");
                    //     return;
                    // }
                    for(let i=0;i<len;i++){
                        let da=new Date(result.data.answers[i].answerTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;
                        let s=
                        `<tr>
                            <td class="questionId">${result.data.answers[i].questionId}</td>
                            <td class="answerContent">${result.data.answers[i].answerContent}</td>
                            <td class="answerTime">${time}</td>
                            <td class="operate">
                                <button class="cancelQuesD">取消删除</button>
                            </td>
                        </tr>`
                        $("#questionDTable").append(s);
                    }
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
        // return false;
    });

    //提问详情
    $(document).on('click','.oneRow',function(){
        let questionId=$(this).parent().parent().find('td').eq(0).text();
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }
        let anws;
        let anwsLen;
        let url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/"+questionId;
        $.ajax({
            url:url,
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
                    anws=result.data.answers;
                    anwsLen=result.data.num;
                    let len=result.data.num;
                    // if(len==0){
                    //     alert("您没有一条提问收到回复！");
                    //     return;
                    // }
                    for(let i=0;i<len;i++){
                        let da=new Date(result.data.answers[i].answerTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;
                        let s=
                        `<tr>
                            <td class="accountName">${result.data.answers[i].accountName}</td>
                            <td class="answerContent">${result.data.answers[i].answerContent}</td>
                            <td class="answerTime">${time}</td>
                        </tr>`
                        $("#ansQuesDetailTable").append(s);
                    }
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
        $('#ansQues').fadeOut(function() {
            $('#ansQuesDetail').fadeIn()
        });
    });

    //有了回复的提问
    $("#ansQuestion").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html";
        }
        questionBoxIsOpen();
        if(!questionBoxState){
            alert("请先打开提问箱");
            return;
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/questionBox/question/ans",
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
                    if(len==0){
                        alert("您没有一条提问收到回复！");
                        return;
                    }
                    for(let i=0;i<len;i++){
                        let da=new Date(result.data.questions[i].questionTime);
                        let y = da.getFullYear();
                        let m = (da.getMonth() + 1).toString().padStart(2, "0");
                        let d = da.getDate().toString().padStart(2, "0");
                        let time = y+"-"+m+"-"+d;
                        let s=
                        `<tr>
                            <td class="questionId">${result.data.questions[i].questionId}</td>
                            <td class="questionContent">${result.data.questions[i].questionContent}</td>
                            <td class="questionTime">${time}</td>
                            <td class="operate">
                                <button class="oneRow">详情</button>
                            </td>
                        </tr>`
                        $("#ansQuestionTable").append(s);
                    }
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
        $('#ansQuesDetail').fadeOut(10,function() {
            $('#ansQues').fadeIn(10)
        });
        // return false;
    });


    //发起提问
    $("#createQuestionButton").click(function(){
        $('#ansQuesDetail').fadeOut(10);
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="login.html"
        }
        let flag=false;
        let obj=document.getElementsByName("optradio");
        for(let i=0;i<obj.length;i++){
            if(obj[i].checked){
                if(i==0){
                    flag=true;
                }
            }
        }

        if((!questionContent.val())||(questionContent.val()==null)||(questionContent.val()=="")){
            alert("请输入提问内容");
            return;
        }

        if(flag&&((!account_name.val())||(account_name.val()==null)||(account_name.val()==""))){
            alert("请输入回答者用户名");
            return;
        }

        let questionBox={
            questionContent:questionContent.val()
        }

        let questionId=-1;

        //创建提问
        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/questionBox/question",
            type:"POST",
            cache:false,
            data:JSON.stringify(questionBox),
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
                    console.log("提问创建成功！");
                    questionId=result.data;
                    console.log(questionId);
                    //没有操作
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
        //创建回答
        createAnswer(flag,account_name.val(),questionId);

        return false;
    });

    //创建回答
    function createAnswer(flag,accountName,questionId){
        $('#ansQuesDetail').fadeOut(10);
        if(flag&&((!accountName)||(accountName==null)||(accountName==""))){
            alert("请输入回答者用户名");
            return false;
        }

        let url="";
        if(flag){
            url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/"+questionId+"/1";
        } else {
            url="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/answerBox/answer/"+questionId+"/0";
        }

        let account={
            accountName:accountName
        }

        $.ajax({
            url:url,
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
                    alert("提问创建成功！");
                    //没有操作
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
    }

    function questionBoxIsOpen(){
        $('#ansQuesDetail').fadeOut(10);
        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/questionBox/questionBoxState",
            type:"GET",
            dataType:"json",
            cache:false,
            crossDomain:true,
            async:false,
            beforeSend:function(xhr){
                xhr.setRequestHeader("jwt",jwt);
            },

            success: function(result) {
                if(result.info == "OK") {
                    if(result.data==1){
                        questionBoxState=true;
                        console.log("开启");
                    } else {
                        questionBoxState=false;
                        console.log("关闭");
                    }
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