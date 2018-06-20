$(document).ready(function(){
	var layindex = layer.load();
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
							if(resp.resultCode == "30000"){
								allseat += resp.data.seat + ",";
								$(".ticket__place").html(allseat);
							}else{
								layer.msg(resp.resultDesc);
							}
						}
					})
				}
				var content = toUtf8("取票号:"+data.token+"电影名称:"+data.title+"放映时间:"+data.show_time);
				$("#code").qrcode(content);
				layer.close(layindex);
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
})

function toUtf8(str) {    
    var out, i, len, c;    
    out = "";    
    len = str.length;    
    for(i = 0; i < len; i++) {    
        c = str.charCodeAt(i);    
        if ((c >= 0x0001) && (c <= 0x007F)) {    
            out += str.charAt(i);    
        } else if (c > 0x07FF) {    
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));    
            out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));    
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));    
        } else {    
            out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));    
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));    
        }    
    }    
    return out;    
}