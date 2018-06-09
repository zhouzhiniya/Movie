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
})
