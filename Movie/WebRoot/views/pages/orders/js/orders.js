function generateListHtml(img, movie, show_time, theater, auditorium, seat, purchase_time, token){
  var str = 
    '<div class="movie movie--preview movie--full"> ' + 
    '  <div class="col-sm-3 col-md-2 col-lg-2"> ' + 
    '     <div class="movie__images"> ' + 
    '       <img src="' + img +'"> ' + 
    '     </div> ' + 
    ' </div> ' + 
    ' <div class="col-sm-9 col-md-10 col-lg-10 movie__about">' + 
    '     <a href="movie-page-full.html" class="movie__title link--huge" id="movietitle">' + movie + '</a> ' + 
    '     <p class="movie__time" id="movieduration" style="margin-top:5px;">' + show_time + '</p> ' + 
    '     <p class="movie__option"><strong>影院： </strong>' + theater + '</p> ' + 
    '     <p class="movie__option"><strong>放映厅： </strong>' + auditorium + '</p> ' + 
    '     <p class="movie__option"><strong>座位： </strong>' + seat + '</p> ' + 
    '     <p class="movie__option"><strong>订票时间：  </strong>' + purchase_time + '</p> ' + 
    '     <p class="movie__option"><strong>取票号： </strong>' + token + '</p> ' + 
    ' </div> ' + 
    ' <div class="clearfix"></div> ' + 
    ' <!-- end time table--> ' + 
    '</div><br/>';
  return str;
}

var _url = '/Movie';

$.ajax({
  url: _url + "/booking/allBookings",
  type: 'post',
  success: function(resp){
    if(resp.resultCode == "30000")
    {
      if(resp.data.isLogin == 0)
      {
        //未登录
      }else{
        //已登录
        for(var i = 0; i < resp.data.length; i++){
          record = resp.data[i];
          $('#all-orders').append(generateListHtml(record.img, record.movie, record.show_time, record.theater, record.auditorium, record.seat, record.purchase_time, record.token));
        }
        
      } 
    }else{
      layer.msg(resp.resultDesc);
    } 
  }
});//ajax end