$(function () {
    $.get(CONFIG.server + "/wxgzh/jssdkConfig", {
        url: encodeURIComponent(location.href.split('#')[0])
    }, function (resp) {
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: 'wxe5b3d72165c956c9', // 必填，公众号的唯一标识
            timestamp: resp.timestamp, // 必填，生成签名的时间戳
            nonceStr: resp.nonceStr, // 必填，生成签名的随机串
            signature: resp.signature,// 必填，签名
            jsApiList: ['getLocation', 'getLocalImgData', 'chooseImage', 'uploadImage', 'startRecord', 'stopRecord', 'onVoiceRecordEnd', 'uploadVoice'] // 必填，需要使用的JS接口列表
        });
    });

    wx.error(function (res) {
        console.log('something goes wrong...')
        console.log(res)
        $.get('/wxgzh/refreshConfig', function (resp) {
            alert(resp)
            window.location.href = "#t=" + new Date().getTime();
        })
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
    });
});

var CONFIG = {
    server:
        "http://koala.j2eeall.com"
}