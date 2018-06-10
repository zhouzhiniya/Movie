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
				var data = resp.data;
				layer.close(index);
				var seats = data.seat.split(",");
				var price = data.price;
				$.cookie("ticketprice",price);
				$("#allprice").html(price);
				//获取座位的所有行
				var lines = new Array();
				var lineslength = 0;
				for(var i=0; i<seats.length; i++){
					var seat = seats[i];
					var line = seat.split("-")[0];
					if(i==0){
						lines.push(line);
					}else{
						if(lines!=lines[lineslength]){
							lines.push(line);
							lineslength++;
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
					var rownum = 0;
					var linenumber = new Array();
					for(var j=0; j<seats.length; j++){
						var seat = seats[i];
						var line = seat.split("-")[0];
						var row = seat.split("-")[1];
						if(line == linename){
							rownum++;
							linenumber.push(row);
						}
					}
					allrows.push(rownum);
					linesnumbers[linename]=linenumber;
				}
				var longestrows = allrows[0];
				for(var i=1; i<allrows.length; i++){
					if(allrows[i]>longestrows){
						longestrows = allrows[i];
					}
				}
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
						for(var k=0; k<allnum.length; k++){
							var rowname = allnum[k];
							if(allnum[k] == j){
								//根据座位名称和showingid获取座位状态及id
								$.ajax({
									url: _url + '/seat/getSeatByNameAndShowing',
									type: 'post',
									data: {
										showing_id: $.cookie("showingid"),
										seat: lines[i] + '-' + allnum[k]
									},
									success: function(resp){
										if(resp.resultCode == "30000"){
											var seatid = resp.data.seat_id;
											var state = resp.data.seat_state;
											console.log(seatid);
											console.log(linename);
											if(state == 0){
												$("#"+linename).append('<span class="sits__place sits-price--cheap" id="'+seatid+'" data-place="'+linename+rowname+'" data-price="'+price+'">'+linename+rowname+'</span>');
											}else{
												$("#"+linename).append('<span class="sits-state sits-state--not" id="'+seatid+'" data-place="'+linename+rowname+'" data-price="'+price+'">'+linename+rowname+'</span>');
											}
											var sum = 0;
											$('.sits__place').click(function (e) {
							                    e.preventDefault();
							                    var place = $(this).attr('data-place');
							                    var ticketPrice = $(this).attr('data-price');
							                    var seatid = $(this).attr('id');

							                    if(! $(e.target).hasClass('sits-state--your')){

							                        if (! $(this).hasClass('sits-state--not') ) {
							                            $(this).addClass('sits-state--your');

							                            $('.checked-place').prepend('<span class="choosen-place '+place+'">'+ place +'</span>');
							                            seatids.push(seatid);
							                            sum += price;

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

							                        sum -= price;

							                        $('.checked-result').text('￥'+sum)
							                    }
							                })
										}else{
											layer.msg(resp.resultDesc);
										}
									}
								})
							}else{
								//空白格子
								
								$("#"+linename).append('<span class="sits-state sits-price--middle" id="'+seatid+'" data-place="'+linename+rowname+'" data-price="'+price+'">'+linename+rowname+'</span>');
										
							}
						}
					}
				}
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