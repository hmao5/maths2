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
  gameSettings.setPlayerStatus(PLAYER_STATUS.CONNECTED)
  updates.begin()
  
ready = ->
  console.log 'ready'
  data = 
    round: window.lastUpdate.roundNum
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
    round: window.lastUpdate.roundNum
  ajaxSettings = 
    url: '/Ajax/unready'
    type: 'POST'
    success: unreadyCB
    data: data
  $.ajax ajaxSettings
  
unreadyCB = (response, status, jqXHR) ->
  console.log "unreadyCB called"
  
answer = ->
  ans = $('#playerAnswer').val()
  console.log 'answer', ans
  flag = false
  (flag = true) for pr in window.lastUpdate.activeProblems when (pr? and Number(ans)==pr.answer)  
  if not flag
  	return
  console.log 'sending answer to server', ans
  data = 
    ans: ans
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
  
PLAYER_STATUS = 
  UNCONNECTED: 'UNCONNECTED'
  CONNECTED: 'CONNECTED'
  READY: 'READY'
  DISCONNECTED: 'DISCONNECTED'
GAME_STATUS = 
  WAITING: 'WAITING'
  LOBBY: 'LOBBY'
  IN_GAME: 'IN_GAME'
  GAME_END: 'GAME_END'

$ -> 
  do uiInit
  do bindings
  window.gameSettings = 
    player: {}
    setPlayerName: (name) ->
      this.player.name = name
      $('#playerNameDisplay span').text(gameSettings.player.name)
    setPlayerID: (id) ->
      this.player.id = id
      $('#playerIDdisplay span').text(gameSettings.player.id)
    setGameStatus: (gameStatus) ->
      this.gameStatus = gameStatus
      $('#gameStatus span').text(gameSettings.gameStatus)
    setPlayerStatus: (playerStatus) ->
      this.playerStatus = playerStatus
      $('#playerStatus span').text(gameSettings.playerStatus)

  window.comm =
    connect: connect
    connectCB: connectCB
    ready: ready
    readyCB: readyCB
    unready: unready
    unreadyCB: unreadyCB
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
      #console.log 'getupdate response', status, response
      window.lastUpdate = response
      status = updates.gameStatus response 
      gameSettings.setGameStatus status
      
    
      if status == GAME_STATUS.WAITING
        true
      $("#playersList").html('')
      for player in response.players when player?
        $("#playersList").append("<li id='player#{player.id}'> </li>")
        $("#player#{player.id}").text("#{player.name}   #{player.score}")
        $("#player#{player.id}").addClass("ready") if player.ready
        $("#player#{player.id}").removeClass("ready") unless player.ready
      if status == GAME_STATUS.LOBBY
        $('#inputReady').removeAttr('disabled')
      if status == GAME_STATUS.IN_GAME
        $("#questionsWrapper").html('')
        for problem in response.activeProblems when problem?
          #$("#questionsWrapper").append("<li id='player#{player.id}'> #{player.name} </li>")
          questiondiv = $('#questionTemplate').clone()
          questiondiv.toggle()
          questiondiv.attr("id", "question#{problem.id}")
          questiondiv.appendTo("#questionsWrapper")
          $("h3", questiondiv).text("#{problem.question}")
          

        $('#inputReady').attr("disabled", true)

        
      # if status == waiting
      #    update player list
      # if status == lobby
      #    update player statuses
      # if status == in game
      # display active problems
      #  dispaly the score
      #  .. sync timer?
      
    begin: ->
      console.log "polling for updates"
      @timer = setInterval (=> do @getUpdate ), 1000
    clear: ->
      clear @timer
    gameStatus: (r) ->
      if !r.roundStarted and !r.gameStarted
        return GAME_STATUS.WAITING
      else if !r.roundStarted and r.gameStarted
        return GAME_STATUS.LOBBY
      else if r.roundStarted and r.gameStarted
        return GAME_STATUS.IN_GAME
      else if r.gameEnd
        return GAME_STATUS.GAME_END
