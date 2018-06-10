$(document).ready(function(){
	$("#ticketnum").html($.cookie("ticketnum"));
	$("#ticketprice").html($.cookie("ticketprice"));
	$("#allcost").html($.cookie("ticketnum")*$.cookie("ticketprice"));
})

function buy(){
	$.ajax({
		url: "/Movie/booking/addOneBooking",
		type: 'post',
		data: {
			showing_id: $.cookie("showingid"),
			seat_id: $.cookie("allseats")
		},
		success: function(resp){
			if(resp.resultCode == "30000"){
				layer.msg("购买成功！");
				$.cookie("bookingid",resp.data);
				window.location.href = "/Movie/views/pages/books/book-final.html";
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
}