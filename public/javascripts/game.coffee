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
    round: window.lastUpdate?.roundNum
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
    round: window.lastUpdate?.roundNum
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
  (answer_matches = true) for problem in window.lastUpdate.activeProblems when (problem? and Number(ans) == problem.answer)
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
  WAITING: 'WAITING'
  LOBBY: 'LOBBY'
  IN_GAME: 'IN_GAME'
  GAME_END: 'GAME_END'
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
  onLobby: ->
    $('#inputReady').toggle();
  onInGame: ->
    $('#inputReady').attr("disabled", true)
    $('#gameArea').toggle();
  onGameEnd: ->
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
    @getUpdate = ->
      ajaxSettings =
        url: '/Ajax/getUpdate'
        type: 'GET'
        success: @getUpdateCB
        error: @clear
      $.ajax ajaxSettings
    @getUpdateCB = (update, updateStatus, jqXHR) =>
      status = GAME_STATUS.parse update
      @gameStatusCallbacks(status)
      # ....TODO do we need this
      gameSettings.setGameStatus status


      if status==GAME_STATUS.GAME_END
        do @clear

      # TODO(syu) don't clear the html (fix scrolling problem)
      $("#playersList").html('')
      for player in update.players when player?
        $("#playersList").append("<li id='player#{player.id}'> </li>")
        $("#player#{player.id}").text("#{player.name}   #{player.score}")
        $("#player#{player.id}").addClass("ready") if player.ready
        $("#player#{player.id}").removeClass("ready") unless player.ready
      if status == GAME_STATUS.LOBBY
        $("#questionsWrapper").html('')
        $('#inputReady').removeAttr('disabled')
      if status == GAME_STATUS.IN_GAME
        $("#questionsWrapper").html('')
        for problem in update.activeProblems when problem?
          #$("#questionsWrapper").append("<li id='player#{player.id}'> #{player.name} </li>")
          questiondiv = $('#questionTemplate').clone()
          questiondiv.toggle()
          questiondiv.attr("id", "question#{problem.id}")
          questiondiv.appendTo("#questionsWrapper")
          $("h3", questiondiv).text("#{problem.question}")
      window.lastUpdate = update
    @gameStatusCallbacks = (newStatus) ->
      oldStatus = gameSettings.gameStatus
      if newStatus != oldStatus
        do GAME_STATUS.onWaiting if newStatus == GAME_STATUS.WAITING
        do GAME_STATUS.onLobby if newStatus == GAME_STATUS.LOBBY
        do GAME_STATUS.onInGame if newStatus == GAME_STATUS.IN_GAME
        do GAME_STATUS.onGameEnd if newStatus == GAME_STATUS.GAME_END
    @begin =  ->
      console.log "polling for updates"
      @timer = setInterval (=> do @getUpdate ), 2000
    @clear = ->
      clearInterval @timer

$ ->
  do ui.init
  do updates.init
