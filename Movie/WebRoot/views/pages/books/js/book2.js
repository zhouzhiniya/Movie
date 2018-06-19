var _url = '/Movie'
var seatids = new Array()

$(document).ready(function(){
	var index = layer.load();
	$.ajax({
		url: _url + "/seat/getSeatsByShowingId",
		type: 'post',
		data: {
			showing_id: $.cookie("showingid")
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;	//所有座位信息拼成的数组
				layer.close(index);
				var price = data[0].price;	//所有座位的价格相同
				$("#allprice").html(price);
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
				$("#lines").html("");
				for(var i=0; i<lines.length; i++){
					$("#lines").append('<span class="sits__indecator">'+lines[i]+'</span> ');
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
				$("#rows").html("");
				for(var i=1; i<=longestrows; i++){
					$("#rows").append('<span class="sits__indecator">'+i+'</span> ');
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
								$.ajax({
									url: _url + '/seat/getSeatByNameAndShowing',
									type: 'post',
									async: false,
									data: {
										showing_id: $.cookie("showingid"),
										seat: linename + '-' + onenum
									},
									success: function(resp){
										if(resp.resultCode == "30000"){
											var seatid = resp.data.seat_id;
											var state = resp.data.available;
											var price = resp.data.price;
											$.cookie("ticketprice",price);
											if(state == 0){
												$("#"+linename).append('<span class="sits__place sits-price--cheap" id="'+seatid+'" data-place="'+linename+onenum+'" data-price="'+price+'">'+linename+onenum+'</span>');
											}else{
												$("#"+linename).append('<span class="sits__place sits-state--not" id="'+seatid+'" data-place="'+linename+onenum+'" data-price="'+price+'">'+linename+onenum+'</span>');
											}
											
										}else{
											layer.msg(resp.resultDesc);
										}
									}
								})
							}
						}
						console.log(flag);
						//循环完判断flag决定是否画空白格子
						if(!flag){
							$("#"+linename).append('<span class="sits__not">1</span>');
						}
					}
				}
				var sum = 0;
				$('.sits-price--cheap').click(function (e) {
                    var place = $(this).attr('data-place');
                    var ticketPrice = $(this).attr('data-price');
                    var seatid = $(this).attr('id');

                    if(! $(e.target).hasClass('sits-state--your')){

                        if (! $(this).hasClass('sits-state--not') ) {
                            $(this).addClass('sits-state--your');

                            $('.checked-place').prepend('<span class="choosen-place '+place+'">'+ place +'</span>');
                            seatids.push(seatid);
                            sum += parseInt(ticketPrice);

                            $('.checked-result').text('￥'+sum);
                        }
                    }

                    else{
                        $(this).removeClass('sits-state--your');
                        var arrayindex = seatids.indexOf(seatid);
                        if(arrayindex > -1){
                        	seatids.splice(arrayindex,1);
                        }
                        $('.'+place+'').remove();

                        sum -= parseInt(ticketPrice);

                        $('.checked-result').text('￥'+sum)
                    }
                })
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
})

function pay(){
	if(seatids.length == 0){
		layer.msg("请选择座位！");
		return;
	}
	var allseat = "";
	for(var i=0; i<seatids.length; i++){
		if(i==(seatids.length-1)){
			allseat += seatids[i];
		}else{
			allseat += seatids[i] + ",";
		}
	}
	$.cookie("allseats",allseat);
	$.cookie("ticketnum",seatids.length);
	window.location.href = _url + "/views/pages/books/book3-buy.html";
}