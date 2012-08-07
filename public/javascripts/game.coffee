bindings = ->
  console.log 'bindings'
  $('#buttonConnect').click -> do connect
  
connect = ->
  console.log 'connect'
  gameSettings.player.name = $('#inputPlayerName').val()
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
  console.log "connectCB data: #{data}"
  gameSettings.player.id = response.id


gameSettings = {}
$ -> 
  do bindings
  gameSettings = 
    player: {}

