connect = (name, desiredPlayers, roundDuration) ->
  gameSettings.setPlayerName name
  gameSettings.roundDuration = roundDuration
  data =
    playerName: gameSettings.player.name
    desiredPlayers: desiredPlayers
    roundDuration: roundDuration
  console.log 'connect', data
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
  gameSettings.maxPlayers = response.maxPlayers
  gameSettings.pollingInterval = parseInt($('#inputPollingInterval').val()) || 500
  gameSettings.player.ready = false
  console.log gameSettings
  do ui.initPlayers
  do ui.updateLocalPlayer
  do ui.instructions1
  updates.begin()

ready = ->
  console.log 'ready'
  data =
    round: gameSettings.lastUpdate?.roundNum
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
    round: gameSettings.lastUpdate?.roundNum
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
  answer_matches = false
  # validations
  (answer_matches = true) for problem in gameSettings.lastUpdate.activeProblems when (problem? and Number(ans) == problem.answer)
  return unless answer_matches
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
  right = response.correctAndFirst
  console.log 'right: '+right

PLAYER_STATUS =
  UNCONNECTED: 'UNCONNECTED'
  CONNECTED: 'CONNECTED'
  READY: 'READY'
  DISCONNECTED: 'DISCONNECTED'
GAME_STATUS =
  WAITING: 'Waiting for other players to join'
  LOBBY: 'In lobby'
  IN_GAME: 'In game'
  GAME_END: 'Game over'
  parse: (update) ->
    if update.gameEnd
      return GAME_STATUS.GAME_END
    else if !update.roundStarted and !update.gameStarted
      return GAME_STATUS.WAITING
    else if !update.roundStarted and update.gameStarted
      return GAME_STATUS.LOBBY
    else if update.roundStarted and update.gameStarted
      return GAME_STATUS.IN_GAME
  onWaiting: ->
    console.log "onWaiting!"
  onLobby: ->
    console.log "onLobby!"
    $('#inputReady').show();
  onInGame: ->
    console.log "onGame!"
    $('#gameArea').slideDown();
  onGameEnd: ->
    console.log "onEnd!"
    do updates.clear


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
  init: ->
    @timer = {}
    @begin =  ->
      console.log "polling for updates every #{gameSettings.pollingInterval}"
      @timer = setInterval (=> do @getUpdate ), gameSettings.pollingInterval
      
    @clear = ->
      clearInterval @timer
    @getUpdate = ->
      ajaxSettings =
        url: '/Ajax/getUpdate'
        type: 'GET'
        success: @getUpdateCB
        error: @clear
      $.ajax ajaxSettings

    @getUpdateCB = (update, updateStatus, jqXHR) =>
      console.log update
      status = GAME_STATUS.parse update
      @gameStatusCallbacks(status)
      ui.updatePlayers update.players
      do ui.updateLocalPlayer 
      if status == GAME_STATUS.IN_GAME
        ui.updateQuestions update.activeProblems, update.allProblems
        ui.updateTimer(update.roundTimerMillis)
      gameSettings.setGameStatus status
      gameSettings.lastUpdate = update


    @gameStatusCallbacks = (newStatus) ->
      oldStatus = gameSettings.gameStatus
      if newStatus != oldStatus
        do GAME_STATUS.onWaiting if newStatus == GAME_STATUS.WAITING
        do GAME_STATUS.onLobby if newStatus == GAME_STATUS.LOBBY
        do GAME_STATUS.onInGame if newStatus == GAME_STATUS.IN_GAME
        do GAME_STATUS.onGameEnd if newStatus == GAME_STATUS.GAME_END



$ ->
  do ui.init
  do updates.init
