function checkEmail(str){
    let reg=new RegExp("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")
    // let reg2=new RegExp("^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$");
    if(reg.test(str)){
        return true;
    } else {
        return false;
    }
}

function checkPassword(str){
    let reg=new RegExp("^[0-9a-zA-Z_]{1,}$");
    if(reg.test(str)){
        return true;
    } else {
        return false;
    }
}

function checkCode(str){
    let reg=new RegExp("^[A-Za-z0-9]+$");
    if(reg.test(str)){
        return true;
    } else {
        return false;
    }
}

function onlyLetter(str){
    let reg=new RegExp("^[A-Za-z]+$");
    if(reg.test(str)){
        return true;
    } else {
        return false;
    }
}

function onlyNumber(str){
    let reg=new RegExp("^[0-9]*$");
    if(reg.test(str)){
        return true;
    } else {
        return false;
    }
}

function onlyUnderline(str){
    let reg=new RegExp("^[_]*$");
    if(reg.test(str)){
        return true;
    } else {
        return false;
    }
}
