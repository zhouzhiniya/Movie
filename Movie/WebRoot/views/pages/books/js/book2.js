var _url = '/Movie'

$(document).ready(function(){
	$.ajax({
		url: _url + "/seat/getSeatsByShowingId",
		type: 'post',
		data: {
			showing_id: $.cookie("showing_id")
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				var seats = data.seat.split(",");
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
					for(var j=1; j<=longestrows; j++){
						for(var k=0; k<allnum.length; k++){
							if(allnum[k] == j){
								$("#"+line[i]).append('<span class="sits__place sits-price--cheap" data-place="'+lines[i]+allnum[k]+'" data-price="">'+lines[i]+allnum[k]+'</span>');
							}else{
								$("#"+line[i]).append('<span class="sits__place sits-price--middle" data-place="'+lines[i]+allnum[k]+'" data-price="">'+lines[i]+allnum[k]+'</span>');
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