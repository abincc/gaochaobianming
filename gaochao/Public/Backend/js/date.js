$(function(){

  var d = new Date()
  var weekArr = ["日","一","二","三","四","五","六"]
  var haveYear = d.getFullYear()
  var haveMonth = d.getMonth()+1
  var y = haveYear, m = haveMonth

  // console.log(d.getDay(), 33);

  function selectDate(selectYear, selectMonth){
    for(var i=1970;i<=haveYear;i++){
      var $option = $('<option value="'+i+'">'+i+'</option>')
      $(selectYear).append($option).val(y)
    }
    for(var i=1;i<=12;i++){
      var $option = $('<option value="'+i+'">'+i+'</option>')
      $(selectMonth).append($option).val(m)
    }
  }

  function creatWeek(selector){
    $.each(weekArr, function(){
      $(selector).append('<li>'+this+'</li>')
    })
  }

  function getDays(year, month){
    if (month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
      return 31
    }else if (year%4==0 && month==2) {
      return 29
    }else if (year%4 !=0 && month==2) {
      return 28
    }else if(month==4||month==6||month==9||month==11){
      return 30
    }
  }

  function dayStart(year,month){
    var tmpDate = new Date(year+'/'+month+'/'+1);
	   return (tmpDate.getDay());
  }

  function createDays(year,month,days, selector){
    var len = dayStart(year, month)*13.8+'%'
    for(var i=0;i<days;i++){
      var $p=$('<p>已</p>')
      if (i==0) {
        var $li = $('<li class="dayli" style="margin-left: '+len+'">'+(i+1)+'</li>')
      }else {
        var $li = $('<li>'+(i+1)+'</li>')
      }
      $li.append($p)
      $(selector).append($li)
    }
  }


  function totalSwith(year,month){
    selectDate('.selectYear', '.selectMonth')
    creatWeek('.week')
    createDays(year,month, getDays(year,month), '.days')
  }

  totalSwith(y, m)

  $('.selectYear').change(function(event){
    $('.week').html('')
    $('.days').html('')
    y = event.target.value
    totalSwith(y, m)
  })
  $('.selectMonth').change(function(event){
    $('.week').html('')
    $('.days').html('')
    m = event.target.value
    totalSwith(y, m)
  })

})
