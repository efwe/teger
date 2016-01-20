class @Obelometer

  constructor: () ->
    canvas = $('#obelometer')
    @dms = 50
    @index = 0
    @colorMap = {
      10: [0x0B0B61,0x0B0B61],
      11: [0x0B0B61,0x0B0B61],
      12: [0x0B0B61,0x0B0B61],
      13: [0x0B0B61,0x0B0B61],
      14: [0x0B0B61,0x0B0B61],
      15: [0x0B0B61,0x0B0B61],
      16: [0x0B0B61,0x0000FF],
      17: [0x0000FF,0x0040FF],
      18: [0x00BFFF,0x0080FF],
      19: [0x01DF3A,0x01DF01],
      20: [0x01DF3A,0x01DF01],
      21: [0x01DF3A,0x01DF01],
      22: [0x01DF3A,0x01DF01],
      23: [0x3ADF00,0x74DF00],
      24: [0xA5DF00,0xD7DF01],
      25: [0xDBA901,0xDF7401],
      26: [0xDF3A01,0xDF0101],
      27: [0xB43104,0xB40404],
      28: [0xB40404,0x8A0808],
      29: [0x61210B,0x610B0B],
      30: [0x61210B,0x610B0B],
      31: [0x61210B,0x610B0B],
      32: [0x61210B,0x610B0B],
      33: [0x61210B,0x610B0B],
      34: [0x61210B,0x610B0B],
      35: [0x61210B,0x610B0B],
      36: [0x61210B,0x610B0B]
    }
    point = new obelisk.Point(250, 450)
    @pixelView = new obelisk.PixelView(canvas, point)
    @renderGrid()
    console.log 'obelometer started'

  cube: (height,color) ->
    dimension = new obelisk.CubeDimension(@dms, @dms, height)
    color = new obelisk.CubeColor().getByHorizontalColor(color)
    # build cube with dimension and color instance
    cube = new obelisk.Cube(dimension, color, true)
    return cube

  renderGrid: () ->
    color = new obelisk.SideColor().getByInnerColor(obelisk.ColorPattern.GRAY);
    dimension = new obelisk.BrickDimension(@dms, @dms)
    brick = new obelisk.Brick(dimension, color)
    for  i in [0..3]
      for j in [0..4]
        p3dBrick = new obelisk.Point3D(i * (@dms - 2), j * (@dms - 2), 0)
        @pixelView.renderObject(brick, p3dBrick)

  reset: () ->
    canvas = $('#obelometer').get(0)
    context = canvas.getContext('2d')
    context.clearRect(0, 0, canvas.width, canvas.height)
    @index = 0
    @renderGrid()

  color: (temp) ->
    base = Math.floor(temp)
    if temp < 10
      colors = @colorMap[10]
    else if temp > 36
      colors = @colorMap[36]
    else
      colors = @colorMap[base]

    #console.log colors
    fraction = temp%1
    if(fraction < 0.5)
      return colors[0]
    else
      return colors[1]

  coords: () ->
    y = 0
    if @index > 0
      y = Math.floor(@index/4)
    x = @index%4
    #console.log @index+"=>["+x+","+y+"]"
    return [x,y]


  renderCube: (temp) ->
    if @index==20
      @reset()
    color = @color(temp)
    [x,y ] = @coords()
    point = new obelisk.Point3D(x*(@dms - 2), y*(@dms-2), 0)
    cube = @cube(Math.round(temp*10),color)
    @pixelView.renderObject(cube,point)
    @index++

  showTemperature: (message) ->
    #{"location":{"lat":null,"lon":null,"name":"office"},"temperature":{"temperatureUnit":"DEGREE_CELCIUS","value":24.125},"date":{"year":2016,"month":"JANUARY","dayOfMonth":19,"dayOfWeek":"TUESDAY","dayOfYear":19,"monthValue":1,"hour":18,"minute":31,"second":6,"nano":150000000,"chronology":{"id":"ISO","calendarType":"iso8601"}}}�    temp = message.temperature.value
    $('#temp-display').text(message.temperature.value)
    $('#time-display').text(message.date.dayOfYear+"/"+message.date.monthValue+"/"+message.date.year+"-"+message.date.hour+":"+message.date.minute+":"+message.date.second)
    @renderCube(message.temperature.value)
