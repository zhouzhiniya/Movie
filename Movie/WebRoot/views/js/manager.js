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

var _url = '/Movie';


	//tab js
	$('#this-week').click(function(){
		$('#this-week-tab').show(1000);
		$('#do-it-tab').hide(1000);
		$('#room-setting-tab').hide(1000);
	});
	$('#do-it').click(function(){
		$('#this-week-tab').hide(1000);
		$('#room-setting-tab').hide(1000);
		$('#do-it-tab').show(1000);		
	});
	$('#room-setting').click(function(){
		$('#this-week-tab').hide(1000);
		$('#room-setting-tab').show(1000);
		$('#do-it-tab').hide(1000);		
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


//room-setting======================================================================================================================
var add_room_layer;
$('#add-room').click(function(){
	add_room_layer = layer.open({
		type:1,
		title:'添加影厅',
		content:$('#add-room-div'),
		area: ['100%', '100%']		
	});
});

function drawSits(row,column,div){

	div.html(
            '<aside class="sits__line">'+
            '</aside>'+
            '<footer class="sits__number">'+
            '</footer>'
	);

	// alert(row + ',' +column);

	var char;

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

	var newChar;
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
			$('.sits__row').eq(m - 1).append('<span class="sits__place sits-price--middle" data-place="' + newChar + '-' + j + '" data-price="30">' + newChar + j + '</span>');
		}

	}



	for(var k = 1;k <= column;k++)
	{
		div.children('.sits__number').append('<span class="sits__indecator">' + k +'</span>');
	}

	init_BookingTwo();

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
			layer.close(add_room_layer);

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

//======================================= look room start ================================
var look_room_layer;
$('.each-rooms').click(function(){
	look_room_layer = layer.open({
		type:1,
		title:'查看影厅信息',
		content:$('#look-room-div'),
		area: ['100%', '100%']		
	});

	$('#look-room-name').text($(this).text());
	$.ajax({
		type:"post",
		url:"",
		data:{
			roomId:""
		},success:function(resp){

			var row = resp.data;
			var column = resp.data;

			$('#look-row').text(row);
			$('#look-column').text(column);



		},error:function(){
			layer.msg('服务器错误，请重试');
		}
	});

	drawSits(6,7,$('#look-sit-pic'));//应该放到ajax 的 success
});

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