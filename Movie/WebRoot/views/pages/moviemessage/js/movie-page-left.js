var _url = "/Movie"

$(document).ready(function(){
	//根据电影id获取所有信息
	var layerindex = layer.load();
	var movie_id = $.cookie("movie_id");
	$.ajax({
		url: _url + "/movie/getMovieInfoById",
		type: 'post',
		data: {
			movie_id: movie_id
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				$("#movietitle").html(data.title);
				$("#movieimg").attr("src",data.image);
				$("#movierate").html(data.douban_rating);
				$("#movietime").html(data.duration);
				var releasetime = data.release_date;
				$("#movieyear").html(releasetime.split("-")[0]);
				$("#movietype").html(data.type);
				$("#moviedate").html(releasetime);
				$("#director").html(data.director);
				$("#actor").html(data.actors);
				$("#moviecomment").html(data.comments_count);
				$("#moviecontent").html(data.summary);
				$("#movievideo").html('<video class="movie__media-item" controls="controls"><source src="'+data.video+'" type="video/mp4"></video>');
				layer.close(layerindex);
			}else{
				layer.msg(resp.resultDesc);
				layer.close(layerindex);
			}
		}
	})

	//获取所有的城市
	$.ajax({
		url: _url + '/theater/getAllCity',
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				//时间选择器
				var today = new Date();
				var val = today.getFullYear() + "-" + (today.getMonth()+1) + "-" + today.getDate();
				laydate.render({
				  elem: '#date' ,//指定元素
				  value: val
				});
				$("#date").change(function(){
					getAllShowings();
				})
				$("#select-sort").html("");
				for(var i=0; i<data.length; i++){
					$("#select-sort").append("<option value='"+data[i].city+"'>"+data[i].city+"</option>");
				}
				//根据城市和时间获取所有场次
				$.ajax({
					url: _url + "/theater/getTheaterByCityAndTime",
					type: 'post',
					data: {
						movie_id: $.cookie("movie_id"),
						city: data[0].city,
						time: val
					},
					success: function(resp){
						if(resp.resultCode == "30000"){
							var data = resp.data;
							if(data.length == 0){
								$("#allShowings").html("暂时没有排片哦~~~");
							}else{
								
							}
						}else{
							layer.msg(resp.resultDesc);
						}
					}
				})
				$("#select-sort").selectbox({
                    onChange: function (val, inst) {
                        $(inst.input[0]).children().each(function(item){
                            $(this).removeAttr('selected');
                        })
                        $(inst.input[0]).find('[value="'+val+'"]').attr('selected','selected');
                    	getAllShowings();
                    }

                });
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})

	//获取所有评论
	$.ajax({
		url: _url + '/movie/commentOfMovie',
		type: 'post',
		data: {
			movie_id: movie_id
		},
		success: function(resp){
			if (resp.resultCode == "30000"){
				$("#allcomments").html("")
				var data = resp.data;
				$("#commentnum").html(data.length);
				for(var i=0; i<data.length; i++){
					$("#allcomments").append('<div class="comment">'+
                                '<div class="comment__images">'+
                                    '<img src="http://placehold.it/50x50">'+
                                '</div>'+
                                '<a class="comment__author"><span class="social-used fa fa-facebook"></span>'+data[i].user_name+'</a>'+
                                '<p class="comment__date">'+data[i].created_at+'</p>'+
                                '<p class="comment__message">'+data[i].content+'</p>'+
                            '</div>')
				}
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})

	//获取豆瓣评论
	$.ajax({
		url: _url + '/movie/commentDouban',
		type: 'post',
		data: {
			movie_id: movie_id
		},
		success: function(resp){
			if (resp.resultCode == "30000"){
				$("#doubancomments").html("")
				var data = resp.data;
				$("#doubancommentnum").html(data.length);
				for(var i=0; i<data.length; i++){
					$("#doubancomments").append('<div class="comment">'+
                                '<div class="comment__images">'+
                                    '<img src="'+data[i].avatar+'">'+
                                '</div>'+
                                '<a class="comment__author"><span class="social-used fa fa-facebook"></span>'+data[i].user_name+'</a>'+
                                '<p class="comment__date">'+data[i].created_at+'</p>'+
                                '<p class="comment__message">'+data[i].content+'</p>'+
                            '</div>')
				}
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})

	
})


//根据城市和时间获取所有场次
function getAllShowings(){
	$.ajax({
		url: _url + "/theater/getTheaterByCityAndTime",
		type: 'post',
		data: {
			movie_id: $.cookie("movie_id"),
			city: $("#select-sort").val(),
			time: $("#date").val()
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				if(data.length == 0){
					$("#allShowings").html("暂时没有排片哦~~~");
				}else{

				}
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
}

//评论
function addcomment(){
	var content = $("#commentcontent").val();
	if(content == ""){
		layer.msg("请输入评论内容！");
		return;
	}
	$.ajax({
		url: _url + "/movie/addComment",
		type: 'post',
		data: {
			movie_id: $.cookie("movie_id"),
			content: content
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				layer.msg("评论成功！");
				window.href.reload();
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
}