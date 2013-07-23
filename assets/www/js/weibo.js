window.weiboLogin = function(appKey, redirectUrl){
	cordova.exec(function(data){
		alert('ok:' + JSON.stringify(data));
		$.post('https://api.weibo.com/oauth2/access_token', {
			client_id: appKey,
			client_secret: 'a1b82daae1063c6c1d09d706df1f8da2',
			grant_type: 'authorization_code',
			code: data.code,
			redirect_uri: redirectUrl
		}, function(info){
			alert(info);
		});
	}, function(err) {
    	alert('error:' + JSON.stringify(err))
    }, "Weibo", "login", [appKey, redirectUrl]);
};