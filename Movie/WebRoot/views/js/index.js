var _url = '/Movie'

$(document).ready(function(){
	//判断是否登录
	var layerindex = layer.load();
	$("#login-form").show();
	$("#register-form").hide();
	$.ajax({
		url: _url + "/user/checkLogin",
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30002"){
				//未登录
				$(".haslogin").hide();
				$(".nologin").show();
				//获取评分前6的电影信息
				$.ajax({
					url: _url + '/movie/getHigherGradeMovies',
					type: 'post',
					success: function(resp){
						if(resp.resultCode == '30000'){
							var data = resp.data;
							$("#firsttitle").html("今&nbsp;日&nbsp;最&nbsp;佳");
							$("#gradeMovie").html("");
							for(var i=0; i<data.length; i++){
								var type = data[i].type.split(",");
								var movietype = "";
								for(var j=0; j<type.length; j++){
									if(j == type.length-1){
										movietype += type[j];
									}else{
										movietype += type[j] + " | ";
									}
								}
								$("#gradeMovie").append('<div class="movie-beta__item second--item">'+
			                         '<img alt="" src="'+data[i].image+'">'+
			                         '<span class="best-rate">'+data[i].douban_rating+'</span>'+
									 '<ul class="movie-beta__info">'+
			                             '<li><span class="best-voted">今日'+data[i].collect_count+'人已观看</span></li>'+
			                             '<li>'+
			                                '<p class="movie__time">'+data[i].duration+'</p>'+
			                                '<p>'+movietype+'</p>'+
			                                '<p>'+data[i].comments_count+' 条评论</p>'+
			                             '</li>'+
			                             '<li class="last-block">'+
			                                 '<a class="slide__link" onclick="moreMovie('+data[i].movie_id+')">更多</a>'+
			                             '</li>'+
			                         '</ul>'+
			                     '</div>');
							}
						}else{
							layer.msg(resp.resultDesc);
						}
					}
				})
			}else if(resp.resultCode == "30007"){
				//已登录
				$(".haslogin").show();
				$(".nologin").hide();
				//获取推荐的电影
				$.ajax({
					url: _url + '/movie/recommend',
					type: 'post',
					success: function(resp){
						if (resp.resultCode == "30000"){
							var data = resp.data;
							$("#firsttitle").html("推&nbsp;荐&nbsp;电&nbsp;影");
							$("#gradeMovie").html("");
							for(var i=0; i<data.length; i++){
								var type = data[i].type.split(",");
								var movietype = "";
								for(var j=0; j<type.length; j++){
									if(j == type.length-1){
										movietype += type[j];
									}else{
										movietype += type[j] + " | ";
									}
								}
								$("#gradeMovie").append('<div class="movie-beta__item second--item">'+
			                         '<img alt="" src="'+data[i].image+'">'+
			                         '<span class="best-rate">'+data[i].douban_rating+'</span>'+
									 '<ul class="movie-beta__info">'+
			                             '<li><span class="best-voted">今日'+data[i].collect_count+'人已观看</span></li>'+
			                             '<li>'+
			                                '<p class="movie__time">'+data[i].duration+'</p>'+
			                                '<p>'+movietype+'</p>'+
			                                '<p>'+data[i].comments_count+' 条评论</p>'+
			                             '</li>'+
			                             '<li class="last-block">'+
			                                 '<a class="slide__link" onclick="moreMovie('+data[i].movie_id+')">更多</a>'+
			                             '</li>'+
			                         '</ul>'+
			                     '</div>');
							}
						}else{
							//获取评分前6的电影信息
							$.ajax({
								url: _url + '/movie/getHigherGradeMovies',
								type: 'post',
								success: function(resp){
									if(resp.resultCode == '30000'){
										var data = resp.data;
										$("#firsttitle").html("今&nbsp;日&nbsp;最&nbsp;佳");
										$("#gradeMovie").html("");
										for(var i=0; i<data.length; i++){
											var type = data[i].type.split(",");
											var movietype = "";
											for(var j=0; j<type.length; j++){
												if(j == type.length-1){
													movietype += type[j];
												}else{
													movietype += type[j] + " | ";
												}
											}
											$("#gradeMovie").append('<div class="movie-beta__item second--item">'+
						                         '<img alt="" src="'+data[i].image+'">'+
						                         '<span class="best-rate">'+data[i].douban_rating+'</span>'+
												 '<ul class="movie-beta__info">'+
						                             '<li><span class="best-voted">今日'+data[i].collect_count+'人已观看</span></li>'+
						                             '<li>'+
						                                '<p class="movie__time">'+data[i].duration+'</p>'+
						                                '<p>'+movietype+'</p>'+
						                                '<p>'+data[i].comments_count+' 条评论</p>'+
						                             '</li>'+
						                             '<li class="last-block">'+
						                                 '<a class="slide__link" onclick="moreMovie('+data[i].movie_id+')">更多</a>'+
						                             '</li>'+
						                         '</ul>'+
						                     '</div>');
										}
									}else{
										layer.msg(resp.resultDesc);
									}
								}
							})
						}
					}
				})
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})

	
	//获取所有电影
	$.ajax({
		url: _url + '/movie/getAllMovies',
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				$("#allMovies").html("");
				for(var i=0; i<data.length; i++){
					var type = data[i].type.split(",");
					var movietype = "";
					for(var j=0; j<type.length; j++){
						if(j == type.length-1){
							movietype += type[j];
						}else{
							movietype += type[j] + " | ";
						}
					}
					var forecast = data[i].forecast_rating;
					if(forecast == null){
						forecast = 0;
					}else{
						var str = forecast + "";
						forecast = str.substring(0,str.indexOf('.') + 3);
					}
					if((i%4) == 0 || ((i-1)%4) == 0){
						$("#allMovies").append('<div class="movie movie--test movie--test--light movie--test--left">'+
                                '<div class="movie__images">'+
                                    '<a class="movie-beta__link" onclick="moreMovie('+data[i].movie_id+')">'+
                                    '<img alt="" src="'+data[i].image+'">'+
                                    '</a>'+
                                '</div>'+
                                '<div class="movie__info">'+
                                    '<a class="movie__title" onclick="moreMovie('+data[i].movie_id+')">'+data[i].title+'</a>'+
									'<p class="movie__time">'+data[i].duration+'</p>'+
                                    '<p class="movie__option">'+movietype+'</p>'+ 
                                    '<div class="movie__rate">'+
                                        '<div class="score"></div>'+
                                        '<span style="position: absolute;right: 30px;top: -30px;font-size: 14px;color: #ffd564;">豆瓣评分</span>'+
                                        '<span class="movie__rating" style="top:-8px">'+data[i].douban_rating+'</span>'+
                                        '<span style="position: absolute;top: -50px;font-size: 18px;color: #fe505a;">AI 预测评分</span>'+
                                        '<span class="movie__rating" style="right: unset;width: 60px;height: 60px;border-radius: 50%;top: -20px;line-height: 35px;background: #fe505a;color: #fff;">'+forecast+'</span>'+
                                    '</div>'+               
                                '</div>'+
                            '</div>');
					}else if(((i-2)%4) == 0 || ((i-3)%4) == 0){
						$("#allMovies").append('<div class="movie movie--test movie--test--dark movie--test--right">'+
                                '<div class="movie__images">'+
                                    '<a onclick="moreMovie('+data[i].movie_id+')" class="movie-beta__link">'+
                                    '<img alt="" src="'+data[i].image+'">'+
                                    '</a>'+
                                '</div>'+
                                '<div class="movie__info">'+
                                    '<a onclick="moreMovie('+data[i].movie_id+')" class="movie__title">'+data[i].title+'</a>'+
									'<p class="movie__time">'+data[i].duration+'</p>'+
                                    '<p class="movie__option">'+movietype+'</p>'+ 
                                    '<div class="movie__rate">'+
                                        '<div class="score"></div>'+
                                        '<span style="position: absolute;right: 30px;top: -30px;font-size: 14px;color: #ffd564;">豆瓣评分</span>'+
                                        '<span class="movie__rating" style="top:-8px">'+data[i].douban_rating+'</span>'+
                                        '<span style="position: absolute;top: -50px;font-size: 18px;color: #fe505a;">AI 预测评分</span>'+
                                        '<span class="movie__rating" style="right: unset;width: 60px;height: 60px;border-radius: 50%;top: -20px;line-height: 35px;background: #fe505a;color: #fff;">'+forecast+'</span>'+
                                    '</div>'+               
                                '</div>'+
                            '</div>');
					}
					
				}
				//获取最新电影
				$.ajax({
					url: _url + "/movie/getRecentMovies",
					type: 'post',
					success: function(resp){
						if(resp.resultCode == "30000"){
							var data = resp.data;
							$("#recentMovies").html("");
							for(var i=0; i<data.length; i++){
								$("#recentMovies").append('<div class="col-sm-4 similar-wrap col--remove">'+
			                    '<div class="post post--preview post--preview--wide">'+
			                        '<div class="post__image">'+
			                            '<img alt="" src="'+data[i].image+'">'+
			                            '<div class="social social--position social--hide">'+
			                                '<span class="social__name">Share:</span>'+
			                                '<a href="#" class="social__variant social--first fa fa-facebook"></a>'+
			                                '<a href="#" class="social__variant social--second fa fa-twitter"></a>'+
			                                '<a href="#" class="social__variant social--third fa fa-vk"></a>'+
			                            '</div>'+
			                        '</div>'+
			                        '<p class="post__date">'+data[i].release_date+'</p>'+
			                        '<a href="/Movie/views/pages/moviemessage/single-page-left.html" class="post__title">'+data[i].title+'</a>'+
			                        '<a href="/Movie/views/pages/moviemessage/single-page-left.html" class="btn read-more post--btn">read more</a>'+
			                    '</div>'+
			                '</div>')
							}
							layer.close(layerindex);
						}else{
							layer.msg(resp.resultDesc);
							layer.close(layerindex);
						}
					}
				})
			}else{
				layer.msg(resp.resultDesc);
				layer.close(layerindex);
			}
		}
	})

	

	
})

//点击今日最佳中的更多
function moreMovie(movieid){
	$.cookie("movie_id",movieid);
	window.location.href = "/Movie/views/pages/moviemessage/movie-page-left.html";
}

//搜索电影
function searchMovie(key){
	var keywords = $(".select__field").val();
	if(keywords == ""){
		layer.msg("请填写搜索条件！");
		return;
	}
	var data = null;
	if(key == "movie_type"){
		data = {
			movie_type: keywords
		}
	}else if(key == "actor"){
		data = {
			actor: keywords
		}
	}else if(key == "director"){
		data = {
			director: keywords
		}
	}
	var layerindex = layer.load();
	$.ajax({
		url: _url + '/movie/searchMovie',
		data: data,
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				$("#allMovies").html("");
				if(data.length >0){
					for(var i=0; i<data.length; i++){
						var type = data[i].type.split(",");
						var movietype = "";
						for(var j=0; j<type.length; j++){
							if(j == type.length-1){
								movietype += type[j];
							}else{
								movietype += type[j] + " | ";
							}
						}
						if((i%4) == 0 || ((i-1)%4) == 0){
							$("#allMovies").append('<div class="movie movie--test movie--test--light movie--test--left">'+
	                                '<div class="movie__images">'+
	                                    '<a class="movie-beta__link" onclick="moreMovie('+data[i].movie_id+')">'+
	                                    '<img alt="" src="'+data[i].image+'">'+
	                                    '</a>'+
	                                '</div>'+
	                                '<div class="movie__info">'+
	                                    '<a class="movie__title" onclick="moreMovie('+data[i].movie_id+')">'+data[i].title+'</a>'+
										'<p class="movie__time">'+data[i].duration+'</p>'+
	                                    '<p class="movie__option">'+movietype+'</p>'+ 
	                                    '<div class="movie__rate">'+
	                                        '<div class="score"></div>'+
	                                        '<span class="movie__rating">'+data[i].douban_rating+'</span>'+
	                                    '</div>'+               
	                                '</div>'+
	                            '</div>');
						}else if(((i-2)%4) == 0 || ((i-3)%4) == 0){
							$("#allMovies").append('<div class="movie movie--test movie--test--dark movie--test--right">'+
	                                '<div class="movie__images">'+
	                                    '<a onclick="moreMovie('+data[i].movie_id+')" class="movie-beta__link">'+
	                                    '<img alt="" src="'+data[i].image+'">'+
	                                    '</a>'+
	                                '</div>'+
	                                '<div class="movie__info">'+
	                                    '<a onclick="moreMovie('+data[i].movie_id+')" class="movie__title">'+data[i].title+'</a>'+
										'<p class="movie__time">'+data[i].duration+'</p>'+
	                                    '<p class="movie__option">'+movietype+'</p>'+ 
	                                    '<div class="movie__rate">'+
	                                        '<div class="score"></div>'+
	                                        '<span class="movie__rating">'+data[i].douban_rating+'</span>'+
	                                    '</div>'+               
	                                '</div>'+
	                            '</div>');
						}
						
					}
				}else{
					$("#allMovies").html("暂无对应电影哦~~~");
				}
				layer.close(layerindex);
			}else{
				layer.msg(resp.resultDesc);
				layer.close(layerindex);
			}
		}
	})
}

//显示注册框
function gotoregister(){
	$("#login-form").hide();
	$("#register-form").show();
}

//显示登录框
function gotologin(){
	$("#login-form").show();
	$("#register-form").hide();
}

//登录
function login(){
	var account = $("#account").val();
	var password = $("#password").val();
	if(account == "" || password == ""){
		layer.msg("请将信息填写完整！");
		return;
	}
	$.ajax({
		url: _url + "/user/login",
		type: 'post',
		data: {
			username: account,
			password: password
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				window.location.reload();
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
}

//注册
function register(){
	var username = $("#username").val();
	var password = $("#registerpassword").val();
	var passwordagain = $("#passwordagain").val();
	var mobile = $("#mobile").val();
	var email = $("#email").val();
	var gender = $("#gender").val();
	var birthday = $("#birthday").val();

	if(username == ""  || password == "" || passwordagain == "" || mobile == "" || email == "" || gender == ""){
		layer.msg("用户名、密码、手机号、邮箱及性别不能为空！");
		return;
	}
	if(password != passwordagain){
		layer.msg("两次输入的密码不同！");
		return;
	}

	$.ajax({
		url: _url + "/user/register",
		data: {
			username: username,
			password: password,
			mobile: mobile,
			email: email,
			gender: gender,
			birthday: birthday
		},
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30000"){
				layer.msg("注册成功！去登录");
				$("#login-form").show();
				$("#register-form").hide();
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
}