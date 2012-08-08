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

connectCB = (response, status, jqXHR) ->
  console.log "connectCB data:"
  console.log JSON.stringify(response)
  gameSettings.setPlayerID(response.id)
  gameSettings.setPlayerStatus(playerStatuses.CONNECTED)
  updates.begin()
  
ready = ->
  console.log 'ready'
  data = 
    round: 1
  ajaxSettings = 
    url: '/Ajax/ready'
    type: 'POST'
    success: readyCB
    data: data
  $.ajax ajaxSettings
  
readyCB = (response, status, jqXHR) ->
  console.log "readyCB called"
  
unready = ->
  console.log 'unready'
  data = 
    round: 1
  ajaxSettings = 
    url: '/Ajax/unready'
    type: 'POST'
    success: unreadyCB
    data: data
  $.ajax ajaxSettings
  
unreadyCB = (response, status, jqXHR) ->
  console.log "unreadyCB called"
  
answer = ->
  console.log 'answer'
  data = 
    ans: $('#playerAnswer').val()
  ajaxSettings = 
    url: '/Ajax/answer'
    type: 'POST'
    success: answerCB
    data: data
  $.ajax ajaxSettings
  
answerCB = (response, status, jqXHR) ->
  console.log "answerCB data:"
  console.log JSON.stringify(response)
  right = response.result=='RIGHT'
  console.log 'right: '+right
  
playerStatuses = 
  UNCONNECTED: 'UNCONNECTED'
  CONNECTED: 'CONNECTED'
  CONNECTED_AND_READY: 'CONNECTED_AND_READY'
  IN_GAME: 'IN_GAME'
  DISCONNECTED: 'DISCONNECTED'

$ -> 
  do bindings
  window.gameSettings = 
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

  window.comm =
    connect: connect
    connectCB: connectCB
    ready: ready
    readyCB: readyCB
    answer: answer
    answerCB: answerCB


  window.updates = 
    timer: {}
    getUpdate: ->
      ajaxSettings =
        url: '/Ajax/getUpdate'
        type: 'GET'
        success: @getUpdateCB
      $.ajax ajaxSettings
    getUpdateCB: (response, status, jqXHR) ->
      console.log 'getupdate response', status, JSON.stringify(response)
    begin: ->
      console.log "polling for updates"
      @timer = setInterval (=> do @getUpdate ), 1000
    clear: ->
      clear @timer
