$(document).ready(function(){
	$.ajax({
		url : "/Movie/booking/getBookingInfoByBookid",
		type: 'post',
		data: {
			booking_id: $.cookie("bookingid")
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				var data = resp.data;
				$(".ticket__number").html(data.token);
				$("#time").html(data.show_time);
				$(".ticket__cinema").html(data.theater);
				$(".ticket__auditorium").html(data.auditorium);
				$("#allprice").html($.cookie("ticketnum")*$.cookie("ticketprice"));
				$(".ticket__movie").html(data.title);
				var seats = data.seat_id.split(",");
				var allseat = "";
				for(var i=0; i<seats.length; i++){
					$.ajax({
						url: "/Movie/seat/getSeat",
						type: 'post',
						data: {
							seat_id: seats[i]
						},
						success: function(resp){
							if(resultCode.resultCode == "30000"){
								allseat += resp.data.seat + ",";
							}else{
								layer.msg(resp.resultDesc);
							}
						}
					})
				}
				$(".ticket__place").html(allseat);
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
})