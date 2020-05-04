$(document).ready(function(){

    let jwt=window.localStorage.getItem("jwt");

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

    //查询已拉黑提问
    $("#TipOffN").click(function(){
        if((!jwt)||(jwt==null)||(jwt=="")){
            alert("请先登录")
            top.location.href="admin_login.html";
        }

        $.ajax({
            url:"http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/1.0/action/tipOff",
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
                                <button class="tipOffA">封禁该用户</button>
                            </td>
                        </tr>`
                        $("#tipOffNTable").append(s);
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