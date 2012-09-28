
getMarginalSolvedProblems = (newActiveProblems) ->
  # numProblems = 3
  # newActiveProblems = _.compact(newActiveProblems)
  
  # for n in [0..(numProblems - newActiveProblems.length)]
  # console.log "derpenstein"
    # newActiveProblems.push


  newActiveProblems 
  oldActiveProblems = gameSettings.lastUpdate.activeProblems
  # console.log "old: ", oldActiveProblems
  # console.log "new: ", newActiveProblems
  oldIds =  _.map oldActiveProblems, (p) -> p?.id
  newIds =  _.map newActiveProblems, (p) -> p?.id
  solved = _.difference oldIds, newIds
  replacing = _.difference newIds, oldIds

  console.log "marginal solved problems", _.zip solved, replacing
  return _.zip solved, replacing

getReplacingProblems = (newActiveProblems) ->
  oldActiveProblems = gameSettings.lastUpdate.activeProblems
  oldIds =  _.map oldActiveProblems, (p) -> p?.id
  newIds =  _.map newActiveProblems, (p) -> p?.id
  difference = _.difference newIds, oldIds
  return difference

    

  

window.ui =
  initBindings: ->
    console.log 'bindings'

    $('#buttonConnect').click ->
      name = $.trim($('#inputPlayerName').val())
      if name == ""
        $('#inputPlayerName').val("Your name can't be blank!")
      else
        desiredPlayers = $('#inputDesiredPlayers').val()
        roundDuration = $('#inputRoundDuration').val()
        comm.connect name, desiredPlayers, roundDuration
        do $('#getStarted').slideUp
        do $('#currentRoom').slideDown
    $('#buttonConnectSettings').click ->
      do $('#getStartedMoreOptions').slideToggle
    $('#inputReady').click ->
      if gameSettings.player.ready == false
        gameSettings.player.ready = true
        do comm.ready
      else
        gameSettings.player.ready = false
        do comm.unready
    $('#playerAnswer').keydown (event) ->
      if event.which==13 
        do event.preventDefault
        do comm.answer
        $('#playerAnswer').val('')

  updatePlayers: (players) ->
    # TODO (syu) this is buggy! when we skip player.id, we end up not populating all the player slots.
    localPlayer = null
    for player, i in players when player?
      if player.id == gameSettings.player.id
        localPlayer = player 
        gameSettings.player.score = player.score
        continue
      slotNumber = if localPlayer? then i - 1 else i
      playerDiv = $ "#playerSlot#{slotNumber}"
      playerDiv.removeAttr('data-empty')
      $('.playerName', playerDiv).text player.name
      $('.playerScore', playerDiv).text "#{player.score}"
      if player.ready
        playerDiv.attr('data-ready', true) 
      else
        playerDiv.removeAttr('data-ready')

  updateLocalPlayer: ->
    playerDiv = $ "#playerSlotLocal"
    player = gameSettings.player
    $('.playerName', playerDiv).text player.name
    $('.playerScore', playerDiv).text "#{player.score}"
    playerDiv.removeAttr 'data-empty'
    if player.ready
      playerDiv.attr('data-ready', true) 
    else
      playerDiv.removeAttr('data-ready')

  onInGameQuestions: (problems) ->
    for i in [0..problems.length - 1]
      newProblem = problems[i]
      questiondiv = $("#question#{i}")
      questiondiv.attr('data-problemID', newProblem?.id)
      @renderQuestion(questiondiv, newProblem) 

  updateQuestions: (problems, allProblems) ->
    problemPairs =  getMarginalSolvedProblems problems

    for [solvedProblemID, newProblemID], i in problemPairs 
      solvedProblem = allProblems[solvedProblemID]
      newProblem = allProblems[newProblemID]
      questiondiv = $(".question[data-problemID=#{solvedProblem?.id}]")
      # console.log questiondiv, questiondiv.length
      #  questiondiv = $(".question").not("[data-problemID]").not("#questionTemplate").first() unless questiondiv.length

      questiondiv.attr('data-problemID', newProblem?.id)

      @renderQuestion(questiondiv, newProblem)

      console.log solvedProblem, newProblem, questiondiv


  renderQuestion: (questiondiv, newProblem) ->
    questiondiv.animate
      left: '+=50'
      opacity: 0.25
      , 200
      , ->
        console.log "questiondiv", questiondiv
        $("h3", questiondiv).text("#{newProblem?.question}")
        questiondiv.removeClass('solved')
        questiondiv.css('color', 'black')
        questiondiv.css('opacity', '1')
        questiondiv.css('left', '0')
    questiondiv.show()
     
  updateTimer: (timeInMs) ->
    timerText = if timeInMs == 0 then "Times up! Finish these questions!" else timeInMs/1000
    # console.log timeInMs
    $('#gameArea #timer').text(timerText)
    secondsWarning = 15
    secondsRedAt = 5
    percOutOfTime = (secondsWarning*1000 - Math.min(secondsWarning*1000, timeInMs - secondsRedAt*1000)) / (secondsWarning*1000)
    # console.log percOutOfTime
    color = Math.min( Math.pow(percOutOfTime, 3) * 255, 255)
    color = parseInt color
    # console.log Math.min(secondsWarning*1000, timeInMs) 
    # console.log color

    $('#gameArea #timer').css('color', "rgb(#{color},0,0)")

  cleanUpGameArea: () ->
    console.log 'cleanUpGameArea'
    $('#gameArea #timer').text('')
    for i in [0..2]
      questiondiv = $("#question#{i}")
      questiondiv.attr('data-problemID', 'null')
      @renderQuestion(questiondiv, {question: '...'} ) 

  renderStatus: (status) ->
    $('#gameStatus').html(status) 

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
