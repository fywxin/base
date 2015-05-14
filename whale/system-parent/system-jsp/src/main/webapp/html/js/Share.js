//分享到微博，微信【http://www.jiathis.com/help/html/weixin-share-code】

Share.prototype.sharetosina = function(title,url,picurl)  {  
	var sharesinastring='http://v.t.sina.com.cn/share/share.php?title='+title+'&url='+url+'&content=utf-8&sourceUrl='+url+'&pic='+picurl;  
	window.open(sharesinastring,'newwindow','height=400,width=400,top=100,left=100');  
}

Share.prototype.sharetoqqzone = function(title,url,picurl) {  
	var shareqqzonestring='http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?summary='+title+'&url='+url+'&pics='+picurl;  
	window.open(shareqqzonestring,'newwindow','height=400,width=400,top=100,left=100');  
}  

//测试代码
