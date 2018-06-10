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
				$("#allprice").html($.cookie("ticketnum")*$.cookie("ticketprice"));
				$(".ticket__movie").html(data.title);
				$(".ticket__place").html(data.seat);
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
})