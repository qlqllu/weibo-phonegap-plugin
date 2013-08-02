window.weiboLogin = function(info, cb, errbk){
	if(info.scope === undefined){
		scope = "email,direct_messages_read,direct_messages_write," +
		"friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
		"follow_app_official_microblog"
	}
	cordova.exec(function(data){
		$.post('https://api.weibo.com/oauth2/access_token', {
			client_id: info.appKey,
			client_secret: info.appSecret,
			grant_type: 'authorization_code',
			code: data.code,
			redirect_uri: info.redirectUrl
		}, function(ret){
			if(cb){
				cb(ret)
			}
		});
	}, function(err) {
    	if(errbk){
    		errbk(err);
    	}
    }, "Weibo", "login", [info.appKey, info.redirectUrl, info.scope]);
};