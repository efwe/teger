class @Teger


  constructor: () ->
    socket = new SockJS('/temperature')
    @stompClient = Stomp.over(socket)
    @obelometer = new Obelometer()
    connect = () =>
      showOfficeTemperature = (message) =>
        @obelometer.showTemperature(JSON.parse(message.body))

      showOutsideTemperature = (message) =>
        @showOutsideTemperature(JSON.parse(message.body))

      @stompClient.subscribe '/topic/officeStream', showOfficeTemperature
      @stompClient.subscribe '/topic/outsideStream', showOutsideTemperature
    @stompClient.connect({}, connect)


  showOutsideTemperature: (message) ->
    #{"location":{"lat":null,"lon":null,"name":"office"},"temperature":{"temperatureUnit":"DEGREE_CELCIUS","value":24.125},"date":{"year":2016,"month":"JANUARY","dayOfMonth":19,"dayOfWeek":"TUESDAY","dayOfYear":19,"monthValue":1,"hour":18,"minute":31,"second":6,"nano":150000000,"chronology":{"id":"ISO","calendarType":"iso8601"}}}�    temp = message.temperature.value
    $('#outside-temperature').text(message.temperature.value)
