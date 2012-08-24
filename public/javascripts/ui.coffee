window.ui =
  initBindings: ->
    console.log 'bindings'

    $('#buttonConnect').click ->
      name = $.trim($('#inputPlayerName').val())
      if name == ""
        $('#inputPlayerName').val("Your name can't be blank!")
      else
        desiredPlayers = $('#inputDesiredPlayers').val()
        console.log desiredPlayers
        comm.connect name, desiredPlayers
        do $('#getStarted').slideUp
        do $('#currentRoom').slideDown
    $('#buttonConnectSettings').click ->
      do $('#getStartedMoreOptions').slideToggle
    $('#inputReady').click ->
      input = $ '#inputReady' 
      console.log input.attr('data-status')
      if input.attr('data-status') == "ready"
        # input.attr('data-status', "unready")
        do comm.unready
      else
        # console.log input.attr('data-status')
        # input.attr('data-status', "ready")
        do comm.ready
    $('#playerAnswer').keydown (event) ->
      if event.which==13 
        do event.preventDefault
        do comm.answer
        $('#playerAnswer').val('')

  updatePlayers: (players) ->
    # TODO (syu) this is buggy! when we skip player.id, we end up not populating all the player slots.
    localPlayer = null
    console.log "updatingPlayers"
    for player, i in players when player?
      if player.id == gameSettings.player.id
        localPlayer = player 
        gameSettings.player.score = player.score
        continue
      slotNumber = if localPlayer? then i - 1 else i
      if localPlayer?
        console.log "localPlayer exists,"
        console.log i
      else
        console.log "localPlayer nil,"
        console.log i
      console.log player, i, slotNumber
      playerDiv = $ "#playerSlot#{slotNumber}"
      playerDiv.removeAttr('data-empty')
      $('.playerName', playerDiv).text player.name
      $('.playerScore', playerDiv).text "#{player.score}"
      if player.ready
        playerDiv.attr('data-ready', true) 
      else
        playerDiv.removeAttr('data-ready')

    localPlayerDiv = $ "playerSlotLocal"
    if localPlayer.ready
      localPlayerDiv.attr('data-ready', true) 
    else
      localPlayerDiv.removeAttr('data-ready')
      # $('.playerScore', localPlayerDiv).text "#{localPlayer.score}"

  updateLocalPlayer: ->
    playerDiv = $ "#playerSlotLocal"
    player = gameSettings.player
    console.log "local player is", player
    $('.playerName', playerDiv).text player.name
    $('.playerScore', playerDiv).text "#{player.score}"
    playerDiv.removeAttr 'data-empty'

  updateQuestions: (problems) ->
    for problem, i in problems  when problem?
      # console.log problem
      questiondiv = $("#question#{i}")
      $("h3", questiondiv).text("#{problem.question}")
      questiondiv.show()
      # console.log questiondiv
  updateTimer: (timeInMs) ->
    $('#gameArea #timer').text(parseInt(timeInMs / 1000))
    secondsWarning = 15
    secondsRedAt = 5
    percOutOfTime = (secondsWarning*1000 - Math.min(secondsWarning*1000, timeInMs - secondsRedAt*1000)) / (secondsWarning*1000)
    console.log percOutOfTime
    color = Math.min( Math.pow(percOutOfTime, 3) * 255, 255)
    color = parseInt color
    console.log Math.min(secondsWarning*1000, timeInMs) 
    console.log color

    $('#gameArea #timer').css('color', "rgb(#{color},0,0)")


  initPlayers: ->
    for i in [0.. (gameSettings.maxPlayers - 1)]
      playerDiv = $('#playerSlotTemplate').clone()
      playerDiv.attr 'id', "playerSlot#{i}" 
      playerDiv.attr 'data-empty', "true" 
      playerDiv.appendTo '#playersList'
    localPlayerDiv = $('#playerSlotTemplate').clone()
    playerDiv.attr 'id', "playerSlotLocal" 
    playerDiv.prependTo '#playersList'

  initQuestions: ->
    $("#questionsWrapper").html('')
    for i in [0..5]
      questiondiv = $('#questionTemplate').clone()
      questiondiv.attr("id", "question#{i}")
      questiondiv.appendTo("#questionsWrapper")
      questiondiv.hide()
  instructions1: ->
    $('#instructions').text("You're now in the lobby. You'll need to wait until #{gameSettings.maxPlayers} join the game until you can start.")
  init: ->
    do @initQuestions
    do @initBindings

