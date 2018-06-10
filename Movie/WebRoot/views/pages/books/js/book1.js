var _url = '/Movie'
var movie_id = ""	//记录选择的电影
var showing_id = ""		//记录选择的场次

$(document).ready(function(){
	//获取所有电影
	$.ajax({
		url: _url + '/movie/getAllMovies',
		type: 'post',
		success: function(resp){
			if(resp.resultCode == '30000'){
				var data = resp.data;
				$("#allMovies").html("");
				for(var i=0; i<data.length; i++){
					$("#allMovies").append('<div class="swiper-slide" id="'+data[i].movie_id+'" data-film="'+data[i].title+'"> '+
                        '<div class="film-images">'+
                            '<img alt="" src="'+data[i].image+'">'+
                        '</div>'+
                        '<p class="choose-film__title">'+data[i].title+'</p>'+
                  '</div>');
				}
				var mySwiper = new Swiper('.swiper-container',{
                    slidesPerView:10,
                    loop:true
                  });

                $('.swiper-slide-active').css({'marginLeft':'-2px'});
                //media swipe visible slide
                //Onload detect
                    if ($(window).width() > 1930 ){
                         mySwiper.params.slidesPerView=13;
                         mySwiper.resizeFix();         
                    }else

                    if ($(window).width() >993 & $(window).width() <  1199  ){
                         mySwiper.params.slidesPerView=6;
                         mySwiper.resizeFix();         
                    }
                    else
                    if ($(window).width() >768 & $(window).width() <  992  ){
                         mySwiper.params.slidesPerView=5;
                         mySwiper.resizeFix();         
                    }

                    else
                    if ($(window).width() < 767 & $(window).width() > 481){
                         mySwiper.params.slidesPerView=4;
                         mySwiper.resizeFix();    
                    
                    } else
                     if ($(window).width() < 480){
                         mySwiper.params.slidesPerView=2;
                         mySwiper.resizeFix();    
                    }

                    else{
                        mySwiper.params.slidesPerView=10;
                        mySwiper.resizeFix();
                    }

                //Resize detect
                $(window).resize(function(){
                    if ($(window).width() > 1930 ){
                         mySwiper.params.slidesPerView=13;
                         mySwiper.reInit();          
                    }

                    if ($(window).width() >993 & $(window).width() <  1199  ){
                         mySwiper.params.slidesPerView=6;
                         mySwiper.reInit();          
                    }
                    else
                     if ($(window).width() >768 & $(window).width() <  992  ){
                         mySwiper.params.slidesPerView=5;
                         mySwiper.reInit();         
                    }

                    else
                    if ($(window).width() < 767 & $(window).width() > 481){
                         mySwiper.params.slidesPerView=4;
                          mySwiper.reInit();    
                    
                    } else
                     if ($(window).width() < 480){
                         mySwiper.params.slidesPerView=2;
                         mySwiper.reInit();   
                    }

                    else{
                        mySwiper.params.slidesPerView=10;
                        mySwiper.reInit();
                    }
                 });
                //choose film
                $('.film-images').click(function (e) {
                	 //visual iteractive for choose
                     $('.film-images').removeClass('film--choosed');
                     $(this).addClass('film--choosed');

                     //data element init
                     var chooseFilm = $(this).parent().attr('data-film');
                     $('.choose-indector--film').find('.choosen-area').text(chooseFilm);

                     //data element set
                     $('.choosen-movie').val(chooseFilm);
                     movie_id = $(this).parent().attr("id");

                })
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})

	//获取所有城市
	$.ajax({
		url: _url + '/theater/getAllCity',
		type: 'post',
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				$("#select-sort").html("");
				for(var i=0; i<data.length; i++){
					$("#select-sort").append('<option value="'+data[i]+'">'+data[i]+'</option>');
				}
				$("#select-sort").selectbox({
                    onChange: function (val, inst) {

                        $(inst.input[0]).children().each(function(item){
                            $(this).removeAttr('selected');
                        })
                        $(inst.input[0]).find('[value="'+val+'"]').attr('selected','selected');
                    }

                });
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

	getAllShowings();
})

//根据城市和时间获取所有场次
function getAllShowings(){
	$.ajax({
		url: _url + "/theater/getTheaterByCityAndTime",
		type: 'post',
		data: {
			movie_id: movie_id,
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