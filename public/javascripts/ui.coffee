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
    localPlayer = {}
    console.log "updatingPlayers"
    for player, i in players when player?
      console.log player, i
      if player.id == gameSettings.player.id
        localPlayer = player 
        continue
      playerDiv = $ "#playerSlot#{i}"
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
    $('.playerScore', localPlayerDiv).text "#{localPlayer.score}"

  updateLocalPlayer: ->
    playerDiv = $ "#playerSlotLocal"
    $('.playerName', playerDiv).text gameSettings.player.name
    playerDiv.removeAttr 'data-empty'

  updateQuestions: (problems) ->
    for problem, i in problems  when problem?
      # console.log i
      # console.log problem
      questiondiv = $("#question#{i}")
      $("h3", questiondiv).text("#{problem.question}")
      questiondiv.show()
      # console.log questiondiv

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

