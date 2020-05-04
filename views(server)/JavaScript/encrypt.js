//生成随机长度的字符串
function randomString(length){
    const expect=length;
    let str=Math.random().toString(36).substring(2);
    while(str.length<expect) {
      str+=Math.random().toString(36).substring(2);
    }
    return str.substring(0,expect);
}

//进行AES加密
function encrypt(word,randomAESKey){
    let key=CryptoJS.enc.Utf8.parse(randomAESKey);
    let srcs=CryptoJS.enc.Utf8.parse(word);
    let encrypted=CryptoJS.AES.encrypt(srcs,key,{mode:CryptoJS.mode.ECB,padding:CryptoJS.pad.Pkcs7});
    return encrypted.toString();
}

//利用RSA公钥进行加密
function encryptRSA(message,publicKey){
    let encrypt=new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    const txt=encrypt.encrypt(message);
    return txt;
}