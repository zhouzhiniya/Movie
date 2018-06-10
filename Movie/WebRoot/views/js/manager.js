$(document).ready(function(){
	$('#add-movie-div').find('.each-movie-pic').css('cursor','pointer');

	laydate.render({
  		elem: '.showing-time' ,
  		type: 'time'//指定元素
	});

	//没嵌代码的时候测试用的
	var result = 0;

	if(result == 0)
	{
		$('#do').hide();
		$('#no-do').show();
	}else{
		$('#do').show();
		$('#no-do').do();				
	}	

	//查看是否已经完成了今日排片
	$.ajax({
		type:"post",
		url:"",
		data:{

		},success:function(resp){

			var result = 0;

			if(result == 0)
			{
				$('#do').hide();
				$('#no-do').show();
			}else{
				$('#do').show();
				$('#no-do').do();				
			}	

		},error:function()
		{

		}

	});

});

	//tab js
	$('#this-week').click(function(){
		$('#this-week-tab').show(1000);
		$('#do-it-tab').hide(1000);
	});
	$('#do-it').click(function(){
		$('#this-week-tab').hide(1000);
		$('#do-it-tab').show(1000);		
	});

	var confirm_layer;

	$('body').on('click','#add-movie-btn',function(){
		confirm_layer = layer.open({
			type:1,
			title:'请选择排片电影',
			content:$('#add-movie-div'),
			area: ['1000px', '600px']
		});

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
	$('#confirm-btn').click(function(){

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
	$('#add-movie-div').find('.each-movie-pic').click(function(){



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
var growing_id = 0;
$('#add-showing').click(function(){
	growing_id ++;
	$('#showings').append(
		'<div class="showing-each">'+
    		'<select class="showing-movie" style="float: left;"><option value ="">电影</option></select>'+
    		'<select class="showing-room" style="float: left;"><option value ="">放映厅</option><option value ="volvo">Volvo</option></select>'+
    		'<input id="test' + growing_id + '" class="showing-time" type="text" style="float: left;" placeholder="开始时间">'+
    		'<input class="showing-price" type="text" style="float: left;" placeholder="票价" />'+
    		'<span class="delete-showing-icon"></span>'+
    		'<div style="clear: both;"></div>'+
    	'</div>'
	);

	refreshMovie();
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
	layer.confirm(
		'确定完成吗？',
		{title:'操作确认'},
		//成功
		function(index){

			layer.close(index);

			var movies = "";
			$('#showings').find('.showing-each').each(function(){

				//每个场次的字符串
				var each_showing = $(this).children('.showing-movie').val() + '-' + $(this).children('.showing-room').val() + '-' + 
				$(this).children('.showing-time').val() + '-' + $(this).children('.showing-price').val() + ',';

				movies += each_showing;
			});

			movies = movies.substring(0,movies.length - 1);
			alert(movies);

			//发送新的选择结果
			$.ajax({
				type:"post",
				data:{
					"movies":movies
				},
				url:"",
				success:function(resp)
				{
					layer.msg('已排完');
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