$(document).ready(function(){
	
	$('#add-movie-div').find('.each-movie-pic').css('cursor','pointer');

	laydate.render({
  		elem: '.showing-time' ,
  		type: 'time'//指定元素
	});

	//时间选择器
	var today = new Date();
	var val = today.getFullYear() + "/" + (today.getMonth()+1) + "/" + today.getDate();
	laydate.render({
	  elem: '#showtime' ,//指定元素
	  value: val,
	  min: val,
	  format: 'yyyy/MM/dd'
	});
	
//	$("#login-form").show();
//	$("#register-form").hide();
//	
//	$('#login-btn').hide();
//	$('#alreay').show();
//	
//	$('#no-login').hide();
//	
//	$('#menu-list').show();
//	$('#this-week-tab').hide();
//	$('#do-it-tab').show();
//	$('#room-setting-tab').hide();

	$.ajax({
		url: _url + "/theater/checkLogin",
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30000")
			{
				if(resp.data.isLogin == 0)
				{
					//未登录
					$('#login-btn').show();
					$("#user-login").show();
					$('#already-login').hide();
					
					$('#no-login').show();
					
					$('#menu-list').hide();
					$('#this-week-tab').hide();
					$('#do-it-tab').hide();
					$('#room-setting-tab').hide();
				}else{
					//已登录
					$('#login-btn').hide();
					$("#user-login").hide();
					$('#alreay-login').show();
					$('#theater-name').text(resp.data.theaterName);
					
					$('#no-login').hide();
					
					$('#menu-list').show();
					$('#this-week-tab').show();
					$('#do-it-tab').hide();
					$('#room-setting-tab').hide();
					
				}	
			}else{
				layer.msg(resp.resultDesc);
			}	
		}
	});//ajax end
	
	
		

});
var _url = '/Movie';

var growing_id = 0;
var allAuditoriumInfoName = new Array();
var allAuditoriumInfoId = new Array();



	//tab js==
	$('#this-week').click(function(){
		$('#this-week-tab').show(1000);
		$('#do-it-tab').hide(1000);
		$('#room-setting-tab').hide(1000);
	});
	$('#do-it').click(function(){
		
		$('#this-week-tab').hide(1000);
		$('#room-setting-tab').hide(1000);
		$('#do-it-tab').show(1000);	
		$("#showings").html("");

		$('#do-it-tab').find('.each-choose-movie').each(function(){
			$(this).remove();
		})
		growing_id = 0;
		
		
		//问今天排了没
		$.ajax({
			type:"post",
			url:_url + "/showing/ifHasShowing",
			data:{
				day:0
			},success:function(resp)
			{
				if(resp.resultCode == "30000")
				{
					//清空数组
					allAuditoriumInfoName = [];
					allAuditoriumInfoId = [];

					$.ajax({
						type:"post",
						url:_url + "/theater/getAuditorium",
						data:{

						},success:function(resp)
						{
							if(resp.resultCode == "30000")
							{
								for(var i = 0;i < resp.data.length;i++)
								{
									allAuditoriumInfoName.push(resp.data[i].auditorium);
									allAuditoriumInfoId.push(resp.data[i].auditorium_id);
								}	
								
							}else{
								//layer.msg(resp.resultDesc);
							}	
						},error:function()
						{
							//layer.msg('服务器错误，请重试');
						}
					});
					//如果今天没排片
					if(resp.data.haveShowing == 0)
					{
						$("#do").html("今天暂无排片");
						$("#do").css("color","#fff").css("margin-left","40px");
						//ajax end
						
					}else{//如果今天排了片子			
									
						var result = resp.data.showinginfo;
						$('#do').find('.el__data').children('.each-movie').html('');
						$('.each-movie-session').html("");
						
						if(result.length == 0)
						{
							$(this).find('.el__data').children('.each-movie').text('今天暂无排片');
						}
									
						//循环添加电影
						for(var i = 0;i < result.length;i++)
						{
							$('#do').find('.el__data').children('.each-movie').append(
				            	'<div class="movie-base" movie-id="' + result[i].movie_id + '" movie-day="0">' + 
				        			'<span class="movie-name">' + result[i].title + '</span>'+
				        		'</div>	'
							);
						}	
						
					}	
					
				}else{
					layer.msg(resp.resultDesc);
				}	
			},error:function()
			{
				//layer.msg('服务器错误，请重试');
			}
		});
	});
	$('#room-setting').click(function(){
		$('#this-week-tab').hide(1000);
		$('#room-setting-tab').show(1000);
		$('#do-it-tab').hide(1000);		
		
		//是否设置过影厅
		$('#already-have-rooms').html('');
		var layindex = layer.load();
		$.ajax({
			type:"post",
			url:_url + "/theater/getAuditorium",
			data:{

			},success:function(resp)
			{
				if(resp.resultCode == "30000")
				{
					if(resp.data.length == 0)
					{
						$('#have-no-rooms').show();
						$('#already-have-rooms').hide();
						layer.close(layindex);
					}else{
						$('#have-no-rooms').hide();
						$('#already-have-rooms').show();
						for(var i = 0;i < resp.data.length;i++)
						{
							$('#already-have-rooms').append('<div class="each-rooms" room-id="' + resp.data[i].auditorium_id + '"><span id="auditoriumname" onclick="showRoom('+resp.data[i].auditorium_id+')">' + resp.data[i].auditorium + '</span><img onclick="deleteRoom('+resp.data[i].auditorium_id+')" src="/Movie/views/images/delete.png" style="width:25px;height:25px;position:absolute;right:-12px;top:-12px;cursor:pointer;z-index:1000"></div>');
						}	
						
						$('#already-have-rooms').append('<div style="clear: both;"></div>');
						layer.close(layindex);
					}
					
				}else{
					layer.msg(resp.resultDesc);
				}	
			},error:function()
			{
				//layer.msg('服务器错误，请重试');
			}
		});
		
		
	});

	function deleteRoom(roomid){
		layer.confirm('确定删除该影厅吗？',function(index){
			$.ajax({
				url: _url + '/theater/deleteAuditorium',
				type: 'post',
				data: {
					auditorium_id: roomid
				},
				success: function(resp){
					if(resp.resultCode == "30000"){
						layer.msg("删除成功！");
						layer.close(index);
						if(resp.data.length == 0)
						{
							$('#have-no-rooms').show();
							$('#already-have-rooms').hide();
							layer.close(layindex);
						}else{
							$('#have-no-rooms').hide();
							$('#already-have-rooms').show();
							$('#already-have-rooms').html("");
							for(var i = 0;i < resp.data.length;i++)
							{
								$('#already-have-rooms').append('<div class="each-rooms" room-id="' + resp.data[i].auditorium_id + '"><span id="auditoriumname" onclick="showRoom('+resp.data[i].auditorium_id+')">' + resp.data[i].auditorium + '</span><img onclick="deleteRoom('+resp.data[i].auditorium_id+')" src="/Movie/views/images/delete.png" style="width:25px;height:25px;position:absolute;right:-12px;top:-12px;cursor:pointer;z-index:1000"></div>');
							}	
							
							$('#already-have-rooms').append('<div style="clear: both;"></div>');
						}
					}
				}
			})
		})
	}
	
	
	//================================================== this-week ==============================================================
	//每次点击手风琴
	$(".el:not(.s--active)").click(function(){
		var day = $(this).attr('day-data');
		var result = "";
		
		$.ajax({
			type:"post",
			url:_url + "/showing/ifHasShowing",
			data:{
				day:day
			},success:function(resp)
			{
				if(resp.resultCode == "30000")
				{
					if(resp.data.haveShowing == "0"){
						$(".s--active").find('.el__data').children('.each-movie').text('该天暂无排片');
						$('.each-movie-session').html("");
					}else{
						result = resp.data.showinginfo;	
						$('.each-movie-session').html('');
						$(".s--active").find('.el__data').children('.each-movie').html('');
						$('.each-movie-session').html("");
						if(result.length == 0)
						{
							$(".s--active").find('.el__data').children('.each-movie').text('该天暂无排片');
						}
						
						//循环添加电影
						for(var i = 0;i < result.length;i++)
						{
							$(".s--active").find('.el__data').children('.each-movie').append(
				            	'<div class="movie-base" movie-id="' + result[i].movie_id + '" movie-day="' + day + '">' + 
				        			'<span class="movie-name">' + result[i].title + '</span>'+
				        		'</div>	'
							);
						}
					}
				}else{
					layer.msg(resp.resultDesc);
				}	
			},error:function()
			{
				//layer.msg('服务器错误，请重试');
			}
		});

		
			
	});
	



	
	//点击电影
	$('body').on('click','.movie-base',function(){
		
		var day = $(this).attr('movie-day');
		var id = $(this).attr('movie-id');
		
		$('.each-movie-session').html('');
		
		$.ajax({
			type:"post",
			url:_url + "/showing/getShowingInfoByMovieId",
			data:{
				day:day,
				id:id
			},success:function(resp)
			{
				if(resp.resultCode == "30000")
				{
					//请求成功
					$('.each-movie-session').html("");
					for(var k = 0;k < resp.data.length;k++)
					{
						$('.each-movie-session').append(
				            	'<span class="movie-session">'+
			            			'<span class="session-time">开场时间：' + resp.data[k].show_time + '</span> '+
			            			'<span class="session-place">影厅：' + resp.data[k].auditorium + '</span>'+
				        			'<span class="movie-duration"><img src="/Movie/views/images/delete.png" style="width:20px;cursor:pointer" onclick="deleteShow('+resp.data[k].showing_id+')"></span>'+
			            		'</span>'			
						);	
					}	
			
					
				}else{
					layer.msg(resp.resultDesc);
				}	
			},error:function()
			{
				//layer.msg('服务器错误，请重试');
			}
		});
		
	});

	function deleteShow(showing_id){
		layer.confirm('确定删除该排片？',function(){
			$.ajax({
				url: _url + '/showing/deleteShowing',
				type: 'post',
				data: {
					showing_id: showing_id
				},
				success: function(resp){
					if(resp.resultCode == "30000"){
						layer.msg("删除成功！");
						layer.closeAll();
						$("#do-it").click();
					}else{
						layer.msg(resp.resultDesc);
						layer.closeAll();
					}
				}
			})
		})
	}
	
	//==================================================== today-do ====================================================
	var confirm_layer;

	$('body').on('click','#add-movie-btn',function(){
		confirm_layer = layer.open({
			type:1,
			title:'请选择排片电影',
			content:$('#add-movie-div'),
			area: ['1000px', '600px']
		});
		
		//打开之后要请求所有电影列表
		$('#add-movie-div').children('.choose-movie-list').html('');
		var layindex = layer.load();
		
		$.ajax({
			type:"post",
			url:_url + "/movie/getAllMovies",
			data:{

			},success:function(resp)
			{
				if(resp.resultCode == "30000")
				{
					for(var i = 0;i < resp.data.length;i++)
					{
						var name = resp.data[i].title;
						var id = resp.data[i].movie_id;
						var image = resp.data[i].image;
						
						$('#add-movie-div').children('.choose-movie-list').append(
				        	'<div class="each-choose-movie" movie-id="' + id + '" movie-confirm="" movie-pic-url="' + image + '">'+
			        			'<div class="each-movie-pic" style="background-image: url(' + "'" + image +"'" + ')">'+
			        				'<div class="movie-add-btn-not"></div>'+
			        				'<div class="movie-add-btn-confirm" style="display: none;"></div>'+
			        				'<div class="each-movie-name">' + name + '</div>'+
			        			'</div>'+	
			        		'</div>'
						);
					}	
					$('#add-movie-div').children('.choose-movie-list').append('<div style="clear:both;"></div><div id="confirm-btn" class="btn btn-md btn--warning btn--book "> 确定</div>');
			
				}else{
					layer.msg(resp.resultDesc);
				}	
			},error:function()
			{
				//layer.msg('服务器错误，请重试');
			}
		});//ajax end
		layer.close(layindex);
	
		//刷新选中状态
		$('#add-movie-div').find('.each-choose-movie').each(function(){
			var this_movie_id = $(this).attr('movie-id');
			var this_div = $(this);

			this_div.find('.movie-add-btn-not').show();
			this_div.find('.movie-add-btn-confirm').hide();

			$('#do-it-tab').find('.each-choose-movie').each(function(){

				if($(this).attr('movie-id') == this_movie_id)
				{

					this_div.find('.movie-add-btn-not').hide();
					this_div.find('.movie-add-btn-confirm').show();
				}	
			});
		});

	});


	//确定添加电影
	$('body').on('click','#confirm-btn',function(){

		//每次添加都先清空
		$('#do-it-tab').find('.choose-movie-list').html('');

		$('#add-movie-div').find('.each-movie-pic').each(function(){
			if($(this).parent().attr('moive-confirm') == 1)
			{
				var img_url = $(this).parent().attr('movie-pic-url');
				var movie_name = $(this).find('.each-movie-name').text();
				var moive_id = $(this).parent().attr('movie-id');

				$('#do-it-tab').find('.choose-movie-list').append(
					'<div class="each-choose-movie" movie-id="' + moive_id + '" movie-confirm="" >'+
	        			'<div class="each-movie-pic" style="background-image:url(' + "'" + img_url + "'" + ')">'+
	        				'<div class="movie-delete-btn"></div>'+
	        				'<div class="each-movie-name">' + movie_name + '</div>'+
	        			'</div>'+
	        		'</div>'
				);
			}	
		});	

		$('#do-it-tab').find('.choose-movie-list').append('<img id="add-movie-btn" src="../../images/add.png" style="cursor: pointer;">');	

		//刷新电影列表
		refreshMovie();

		layer.close(confirm_layer);
	});

//添加电影
	$('body').on('click','#add-movie-div .each-movie-pic',function(){



		//选中状态
		if($(this).find('.movie-add-btn-not').css('display') == 'none')
		{
			$(this).find('.movie-add-btn-not').show();
			$(this).find('.movie-add-btn-confirm').hide();

			$(this).parent().attr('moive-confirm','0');
		}
		else{
			$(this).find('.movie-add-btn-not').hide();
			$(this).find('.movie-add-btn-confirm').show();

			$(this).parent().attr('moive-confirm','1');
		}	

	});

//取消已经选择的电影
$('body').on('click','.movie-delete-btn',function(){

	var this_btn = $(this);

	layer.confirm(
		'确定取消该影片吗？',
		{title:'操作确认'},
		//成功
		function(index){
			this_btn.parent().parent().remove();
			layer.close(index);

			//刷新电影列表
			refreshMovie();

			//发送新的选择结果
			$.ajax({

			});
		},
		//取消
		function(index){
			layer.close(index);
		}
		);
});

//添加场次
// var add_showing_layer;
// $('#add-showing').click(function(){
// 	add_showing_layer = layer.open({
// 		type:1,
// 		title:'添加场次',
// 		content:$('#add-showing-div'),
// 		area: ['1000px', '300px']
// 	});

// });

$('#add-showing').click(function(){	
	
	var AuditorSelectTag = "";
	for(var j = 0;j < allAuditoriumInfoName.length;j++)
	{
		AuditorSelectTag += '<option value="' + allAuditoriumInfoId[j] + '">' + allAuditoriumInfoName[j] +'</option>' 
	}

	//alert(AuditorSelectTag);
	
	
	if(growing_id == 0){
		growing_id ++;
		$('#showings').append(
			'<div class="showing-each">'+
	    		'<select id="showingmovie'+growing_id+'" class="showing-movie login__input" style="float: left;width:unset;margin-right:10px;color:#000"><option value ="">电影</option></select>'+
	    		'<select id="showingroom'+growing_id+'" class="showing-room login__input" style="float: left;width:unset;margin-right:10px;color:#000"><option value ="">放映厅</option>' + AuditorSelectTag + '</select>'+
	    		'<input id="test' + growing_id + '" class="showing-time login__input" type="text" style="float: left;width:unset;margin-right:10px;color:#000" placeholder="开始时间">'+
	    		'<input id="showingprice'+growing_id+'" class="showing-price login__input" type="text" style="float: left;width:unset;margin-right:10px;color:#000" placeholder="票价" />'+
	    		'<span class="delete-showing-icon"></span>'+
	    		'<div style="clear: both;"></div>'+
	    	'</div>'
		);
	}else{
		if($("#showingmovie"+growing_id).val() == "" || $("#showingroom"+growing_id).val() == "" || $("#test"+growing_id).val() == "" || $("#showingprice"+growing_id).val() == ""){
			layer.msg("请将信息填写完整！");
			return;
		}
		growing_id ++;
		$('#showings').append(
			'<div class="showing-each">'+
	    		'<select id="showingmovie'+growing_id+'" class="showing-movie login__input" style="float: left;width:unset;margin-right:10px;color:#000"><option value ="">电影</option></select>'+
	    		'<select id="showingroom'+growing_id+'" class="showing-room login__input" style="float: left;width:unset;margin-right:10px;color:#000"><option value ="">放映厅</option>' + AuditorSelectTag + '</select>'+
	    		'<input id="test' + growing_id + '" class="showing-time login__input" type="text" style="float: left;width:unset;margin-right:10px;color:#000" placeholder="开始时间">'+
	    		'<input id="showingprice'+growing_id+'" class="showing-price login__input" type="text" style="float: left;width:unset;margin-right:10px;color:#000" placeholder="票价" />'+
	    		'<span class="delete-showing-icon"></span>'+
	    		'<div style="clear: both;"></div>'+
	    	'</div>'
		);
	}
	

	// refreshMovie();
	$('#showingmovie'+growing_id).html('<option value ="">电影</option>');

	$('#do-it-tab').find('.each-choose-movie').each(function(){
		var id_value = $(this).attr('movie-id');
		var name = $(this).find('.each-movie-name').text();

		$('#showingmovie'+growing_id).append('<option value ="' + id_value + '">' + name + '</option>');
	});
	var id = "#test" + growing_id ;

	laydate.render({
  		elem: id,
  		type: 'time'//指定元素
	});

});

$('body').on('click','.delete-showing-icon',function(){
	var this_btn = $(this);

	layer.confirm(
		'确定取消该场次吗？',
		{title:'操作确认'},
		//成功
		function(index){
			this_btn.parent().remove();
			layer.close(index);

			//发送新的选择结果
			$.ajax({

			});
		},
		//取消
		function(index){
			layer.close(index);
		}
		);
});

$('#complete').click(function(){
	var showdate = $("#showtime").val();
	if($('#do-it-tab').find('.each-choose-movie').html()==undefined){
		layer.msg("请选择排片电影！");
		return;
	}
	if(growing_id == 0){
		layer.msg("请添加场次！");
		return;
	}
	layer.confirm(
		'确定完成吗？',
		{title:'操作确认'},
		//成功
		function(index){

			layer.close(index);

			var movies = "";
			var empty = false;
			$('#showings').find('.showing-each').each(function(){
				if($(this).children('.showing-movie').val() == ''){
				layer.msg('请将信息填写完整！');
				empty = true;
				}
		        if($(this).children('.showing-room').val() == ''){
		          layer.msg('请将信息填写完整！');
		          empty = true;
		        }
		        if($(this).children('.showing-time').val() == ''){
		          layer.msg('请将信息填写完整！');
		          empty = true;
		        }
		        if($(this).children('.showing-price').val() == ''){
		          layer.msg('请将信息填写完整！');
		          empty = true;
		        }

				//每个场次的字符串
				var each_showing = $(this).children('.showing-movie').val() + '-' + $(this).children('.showing-room').val() + '-' + showdate + '-' +
				$(this).children('.showing-time').val() + '-' + $(this).children('.showing-price').val() + ',';

				movies += each_showing;
			});
			if(empty == true){
			  return;
			}

			movies = movies.substring(0,movies.length - 1);
			//alert(movies);

			//发送新的选择结果
			$.ajax({
				type:"post",
				data:{
					"movies":movies
				},
				url:_url + "/theater/addShowing",
				success:function(resp)
				{
					if(resp.resultCode == "30000")
					{
						layer.msg('已排完');
						$('#do-it').click();
					}else{
						layer.msg(resp.resultDesc);
					}	
				},
				error:function()
				{
					layer.msg('服务器错误，请重试');
				}
			});
		},
		//取消
		function(index){
			layer.close(index);
		}
	);
});

function refreshMovie(){

	$('.showing-movie').html('<option value ="">电影</option>');

	$('#do-it-tab').find('.each-choose-movie').each(function(){
		var id_value = $(this).attr('movie-id');
		var name = $(this).find('.each-movie-name').text();

		$('.showing-movie').append('<option value ="' + id_value + '">' + name + '</option>');
	});
}


//room-setting======================================================================================================================
var add_room_layer;
$('#add-room').click(function(){
	add_room_layer = layer.open({
		type:1,
		title:'添加影厅',
		content:$('#add-room-div'),
		area: ['100%', '100%']	
	});
	$('.this-room-name').val("");
	$("#choose-row").val("");
	$("#choose-column").val("");
	$("#sit-pic").html('<aside class="sits__line">'+
            '</aside>'+
            '<footer class="sits__number">'+
            '</footer>');
});

function drawSits(row,column,div){

	div.html(
            '<aside class="sits__line">'+
            '</aside>'+
            '<footer class="sits__number">'+
            '</footer>'
	);

	//alert(row + ',' +column);

	var char = "";

	for(var i = 1;i <= row;i++)
	{
		switch(i)
		{
			case 1 : char = 'A'; break;
			case 2 : char = 'B'; break;
			case 3 : char = 'C'; break;
			case 4 : char = 'D'; break;
			case 5 : char = 'E'; break;
			case 6 : char = 'F'; break;
			case 7 : char = 'G'; break;
			case 8 : char = 'H'; break;
			case 9 : char = 'I'; break;
			case 10 : char = 'J'; break;
			case 11 : char = 'K'; break;
			case 12 : char = 'L'; break;
			case 13 : char = 'M'; break;
			case 14 : char = 'N'; break;
			case 15 : char = 'O'; break;
			case 16 : char = 'P'; break;
			case 17 : char = 'Q'; break;
			case 18 : char = 'R'; break;
			case 19 : char = 'S'; break;
			case 20 : char = 'T'; break;
			case 21 : char = 'U'; break;
			case 22 : char = 'V'; break;
			case 23 : char = 'W'; break;
			case 24 : char = 'X'; break;
			case 25 : char = 'Y'; break;
			case 26 : char = 'Z'; break;


		}

		div.children('.sits__line').append('<span class="sits__indecator">' + char + '</span>');
		div.children('.sits__line').after('<div class="sits__row">');

	}	

	var newChar = "";
	for(var m = 1;m <= row;m++)
	{
		switch(m)
		{
			case 1 : newChar = 'A'; break;
			case 2 : newChar = 'B'; break;
			case 3 : newChar = 'C'; break;
			case 4 : newChar = 'D'; break;
			case 5 : newChar = 'E'; break;
			case 6 : newChar = 'F'; break;
			case 7 : newChar = 'G'; break;
			case 8 : newChar = 'H'; break;
			case 9 : newChar = 'I'; break;
			case 10 : newChar = 'J'; break;
			case 11 : newChar = 'K'; break;
			case 12 : newChar = 'L'; break;
			case 13 : newChar = 'M'; break;
			case 14 : newChar = 'N'; break;
			case 15 : newChar = 'O'; break;
			case 16 : newChar = 'P'; break;
			case 17 : newChar = 'Q'; break;
			case 18 : newChar = 'R'; break;
			case 19 : newChar = 'S'; break;
			case 20 : newChar = 'T'; break;
			case 21 : newChar = 'U'; break;
			case 22 : newChar = 'V'; break;
			case 23 : newChar = 'W'; break;
			case 24 : newChar = 'X'; break;
			case 25 : newChar = 'Y'; break;
			case 26 : newChar = 'Z'; break;
		}

		for(var j = 1;j <= column;j++)
		{	
			div.children('.sits__row').eq(m - 1).append('<span class="sits__place sits-price--middle sits-state--your" data-place="' + newChar + '-' + j + '" data-price="30">' + newChar + j + '</span>');
		}

	}



	for(var k = 1;k <= column;k++)
	{
		div.children('.sits__number').append('<span class="sits__indecator">' + k +'</span>');
	}

	init_BookingTwo();

}

//查看座位
function showDrawSits(row,column,div){

	div.html(
            '<aside class="sits__line">'+
            '</aside>'+
            '<footer class="sits__number">'+
            '</footer>'
	);

	//alert(row + ',' +column);

	var char = "";

	for(var i = 1;i <= row;i++)
	{
		switch(i)
		{
			case 1 : char = 'A'; break;
			case 2 : char = 'B'; break;
			case 3 : char = 'C'; break;
			case 4 : char = 'D'; break;
			case 5 : char = 'E'; break;
			case 6 : char = 'F'; break;
			case 7 : char = 'G'; break;
			case 8 : char = 'H'; break;
			case 9 : char = 'I'; break;
			case 10 : char = 'J'; break;
			case 11 : char = 'K'; break;
			case 12 : char = 'L'; break;
			case 13 : char = 'M'; break;
			case 14 : char = 'N'; break;
			case 15 : char = 'O'; break;
			case 16 : char = 'P'; break;
			case 17 : char = 'Q'; break;
			case 18 : char = 'R'; break;
			case 19 : char = 'S'; break;
			case 20 : char = 'T'; break;
			case 21 : char = 'U'; break;
			case 22 : char = 'V'; break;
			case 23 : char = 'W'; break;
			case 24 : char = 'X'; break;
			case 25 : char = 'Y'; break;
			case 26 : char = 'Z'; break;


		}

		div.children('.sits__line').append('<span class="sits__indecator">' + char + '</span>');
		div.children('.sits__line').after('<div class="sits__row">');

	}	

	var newChar = "";
	for(var m = 1;m <= row;m++)
	{
		switch(m)
		{
			case 1 : newChar = 'A'; break;
			case 2 : newChar = 'B'; break;
			case 3 : newChar = 'C'; break;
			case 4 : newChar = 'D'; break;
			case 5 : newChar = 'E'; break;
			case 6 : newChar = 'F'; break;
			case 7 : newChar = 'G'; break;
			case 8 : newChar = 'H'; break;
			case 9 : newChar = 'I'; break;
			case 10 : newChar = 'J'; break;
			case 11 : newChar = 'K'; break;
			case 12 : newChar = 'L'; break;
			case 13 : newChar = 'M'; break;
			case 14 : newChar = 'N'; break;
			case 15 : newChar = 'O'; break;
			case 16 : newChar = 'P'; break;
			case 17 : newChar = 'Q'; break;
			case 18 : newChar = 'R'; break;
			case 19 : newChar = 'S'; break;
			case 20 : newChar = 'T'; break;
			case 21 : newChar = 'U'; break;
			case 22 : newChar = 'V'; break;
			case 23 : newChar = 'W'; break;
			case 24 : newChar = 'X'; break;
			case 25 : newChar = 'Y'; break;
			case 26 : newChar = 'Z'; break;
		}

		for(var j = 1;j <= column;j++)
		{	
			div.children('.sits__row').eq(m - 1).append('<span class="sits__place sits-state--not" data-place="' + newChar + '-' + j + '" data-price="30">' + newChar + j + '</span>');
		}

	}



	for(var k = 1;k <= column;k++)
	{
		div.children('.sits__number').append('<span class="sits__indecator">' + k +'</span>');
	}

}

$('#choose-row').change(function(){
	drawSits($('#choose-row').val(),$('#choose-column').val(),$('#sit-pic'));
});

$('#choose-column').change(function(){

	drawSits($('#choose-row').val(),$('#choose-column').val(),$('#sit-pic'));
});


$('#confirm-add-room-btn').click(function(){
	layer.confirm(
		'确定添加该影厅？',
		{title:'操作确认'},
		//成功
		function(index){
			
			layer.close(index);
			
			var sits = "";
			var room_name = $('.this-room-name').val();
			
			$('.sits-state--your').each(function(){
				sits += $(this).attr('data-place') + ',';
			});
			
			sits = sits.substring(0,sits.length - 1);
			if(room_name == ""){
				layer.msg("请输入影厅名称！");
				return;
			}
			if(sits == ""){
				layer.msg("请设置影厅座位！");
				return;
			}
			

			//确定添加影厅
			$.ajax({
				type:"post",
				data:{
					seats:sits,
					name:room_name
				},
				url:_url + "/theater/addAuditorium",
				success:function(resp)
				{
					if(resp.resultCode == "30000")
					{
						layer.msg('添加成功！');
						$('#room-setting').click();
						layer.close(add_room_layer);
					}else{
						layer.msg(resp.resultDesc);
					}	
				},
				error:function()
				{
					layer.msg('服务器错误，请重试');
				}
			});


		},
		//取消
		function(index){
			layer.close(index);
		}
		);


});

//======================================= look room start ================================
var look_room_layer;
function showRoom(roomId){
	look_room_layer = layer.open({
		type:1,
		title:'查看影厅信息',
		content:$('#look-room-div'),
		area: ['100%', '100%']		
	});

	$('#look-room-name').text($(this).text());
	var roomid = roomId;
	$.ajax({
		type:"post",
		url:_url + "/seat/getSeatsByAuditoriumId",
		data:{
			roomId:roomid
		},success:function(resp){
			// var seats = resp.data;
			// var row = new Array();
			// var column = new Array();
			// for(var i=0; i<data.length; i++){

			// }

			// var row = resp.data;
			// var column = resp.data;

			// $('#look-row').text(row);
			// $('#look-column').text(column);

			// showDrawSits(3,3,$('#look-sit-pic'));
			var data = resp.data;
			//获取座位的所有行
			var lines = new Array();
			var lineslength = 0;
			for(var i=0; i<data.length; i++){
				var seat = data[i].seat;
				var line = seat.split("-")[0];	//行的标志，A、B、C...
				if(i == 0){
					lines.push(line);
				}else{
					if(lines[lineslength] != line){
						lines.push(line);
						lineslength ++ ;
					}
				}
			}
			$(".sits__line").html("");
			for(var i=0; i<lines.length; i++){
				$(".sits__line").append('<span class="sits__indecator">'+lines[i]+'</span> ');
			}
			//获取所有行中最长有几列及每行对应的数字
			var allrows = new Array();
			var linesnumbers = {};
			for(var i=0; i<lines.length; i++){
				var linename = lines[i];
				var linenumber = new Array();
				for(var j=0; j<data.length; j++){
					var seat = data[j].seat;
					var line = seat.split("-")[0];
					var row = seat.split("-")[1];
					if(line == linename){
						linenumber.push(row);
					}
				}
				allrows.push(linenumber[linenumber.length-1]);
				linesnumbers[linename]=linenumber;
			}
			console.log(allrows);
			var longestrows = allrows[0];
			for(var i=1; i<allrows.length; i++){
				if(allrows[i]>longestrows){
					longestrows = allrows[i];
				}
			}
			console.log(longestrows);
			$(".sits__number").html("");
			for(var i=1; i<=longestrows; i++){
				$(".sits__number").append('<span class="sits__indecator">'+i+'</span> ');
			}
			//填充所有座位
			$("#allseats").html("");
			for(var i=0; i<lines.length; i++){
				$("#allseats").append('<div class="sits__row" id="'+lines[i]+'"></div>');
				var allnum = linesnumbers[lines[i]];
				var linename = lines[i];
				for(var j=1; j<=longestrows; j++){
					var flag = false;	//判断是否已经在该位置画过格子
					for(var k=0; k<allnum.length; k++){
						var onenum = allnum[k];
						if(onenum == j){
							//根据座位名称和showingid获取座位状态及id
							flag = true;
							$("#"+linename).append('<span class="sits__place sits-state--not">1</span>');
						}
					}
					//循环完判断flag决定是否画空白格子
					if(!flag){
						$("#"+linename).append('<span class="sits__not">1</span>');
					}
				}
			}


		},error:function(){
			layer.msg('服务器错误，请重试');
		}
	});

}

// ======================================================== login in & register =================================================
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
	var account = $("#login-account").val();
	var password = $("#login-password").val();
	if(account == "" || password == "" ){
		layer.msg("请将信息填写完整！");
		return;
	}
	$.ajax({
		url: _url + "/theater/login",
		type: 'post',
		data: {
			account: account,
			password: password
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				
				window.location.reload();
				
//				//登录成功
//				
//				
//				$('#login-btn').hide();
//				$('#alreay').show();
//				//$('#theater-name').text(resp.data.theaterName);
//				
//				$('#no-login').hide();
//				
//				$('#menu-list').show();
//				$('#this-week-tab').show();
//				$('#do-it-tab').hide();
//				$('#room-setting-tab').hide();
//				
//				//关闭登录框
//		        $('.overlay').removeClass('open').addClass('close');
//
//		        setTimeout(function(){
//		            $('.overlay').removeClass('close');}, 500);
				
				
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	});
}

//注册
function register(){
	var theater = $('#theatername').val();
	var account = $("#account").val();
	var password = $("#registerpassword").val();
	var passwordagain = $("#passwordagain").val();
	var phone = $("#mobile").val();
	var email = $("#email").val();
	var city = $("#city").val();
	var address = $("#address").val();

	if(account == ""  || password == "" || passwordagain == "" || phone == "" || email == "" || city == ""|| address == ""|| theater == ""){
		layer.msg("用户名、密码、手机号、邮箱及性别不能为空！");
		return;
	}
	if(password != passwordagain){
		layer.msg("两次输入的密码不同！");
		return;
	}

	$.ajax({
		url: _url + "/theater/register",
		data: {
			theater:theater,
			account: account,
			password: password,
			phone: phone,
			email: email,
			city: city,
			address: address
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
	});
}

$('#log-out').click(function(){
	$.ajax({
		url: _url + "/theater/exit",
		data: {

		},
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30000")
			{
				
			}	
		}
	});
	window.location.reload();
	
});
//====================== Date Function=========================


