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
			}else{
				layer.msg(resp.resultDesc);
			}
		}
	})
})