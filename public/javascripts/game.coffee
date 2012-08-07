bindings = ->
  console.log 'bindings'
  $('#buttonConnect').click -> do connect
  
connect = ->
  console.log 'connect'
  gameSettings.setPlayerName $('#inputPlayerName').val()
  data = 
    playerName: gameSettings.player.name
  ajaxSettings = 
    url: '/Ajax/connect'
    type: 'POST'
    success: connectCB
    data: data
  $.ajax ajaxSettings
  console.log ajaxSettings
  console.log gameSettings

connectCB = (response, status, jqXHR) ->
  console.log "connectCB data:"
  console.log response
  gameSettings.setPlayerID(response.id)
  gameSettings.setPlayerStatus(playerStatuses.CONNECTED)
  updates.begin()
  
gameSettings =  {}
updates = {}
  
playerStatuses = 
  UNCONNECTED: 'UNCONNECTED'
  CONNECTED: 'CONNECTED'
  CONNECTED_AND_READY: 'CONNECTED_AND_READY'
  IN_GAME: 'IN_GAME'
  DISCONNECTED: 'DISCONNECTED'

$ -> 
  do bindings
  gameSettings = 
    player: {}
    setPlayerName: (name) ->
      this.player.name = name
      $('#playerNameDisplay span').text(gameSettings.player.name)
    setPlayerID: (id) ->
      this.player.id = id
      $('#playerIDdisplay span').text(gameSettings.player.id)
    setPlayerStatus: (status) ->
      this.status = status
      $('#gameStatus span').text(gameSettings.status)

  updates = 
    timer: {}
    getUpdate: ->
      ajaxSettings =
        url: '/Ajax/getUpdate'
        type: 'GET'
        success: @getUpdateCB
      $.ajax ajaxSettings
    getUpdateCB: (response, status, jqXHR) ->
      console.log 'getupdate response', status, response
    begin: ->
      console.log "polling for updates"
      @timer = setInterval (=> do @getUpdate ), 1000
    clear: ->
      clear @timer



