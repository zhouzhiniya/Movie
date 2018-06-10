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
				$("#select-sort").html("");
				for(var i=0; i<data.length; i++){
					$("#select-sort").append("<option value='"+data[i]+"'>"+data[i]+"</option>");
				}
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})

	//时间选择器
	var today = new Date();
	var val = today.getFullYear() + "-" + (today.getMonth()+1) + "-" + today.getDate();
	laydate.render({
	  elem: '#date' ,//指定元素
	  value: val
	});

	//根据城市和时间获取所有场次
	getAllShowings();
})

$("#date").change(function(){
	getAllShowings();
})

$("#select-sort").change(function(){
	getAllShowings();
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