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
      input = $('#inputReady')
      console.log input.attr('data-status')
      if input.attr('data-status') == "ready"
        console.log input.attr('data-status')
        input.attr('data-status', "unready")
        do comm.unready
      else
        console.log input.attr('data-status')
        input.attr('data-status', "ready")
        do comm.ready
    $('#playerAnswer').keydown (event) ->
      if(event.which==13)
        do event.preventDefault
        do comm.answer
        $('#playerAnswer').val('')

  updateProblems: (problems) ->
    for problem, i in problems  when problem?
      console.log i
      console.log problem
      questiondiv = $("#question#{i}")
      $("h3", questiondiv).text("#{problem.question}")
      questiondiv.show()
      console.log questiondiv

  initQuestions: ->
    $("#questionsWrapper").html('')
    for i in [0..5]
      questiondiv = $('#questionTemplate').clone()
      questiondiv.attr("id", "question#{i}")
      questiondiv.appendTo("#questionsWrapper")
      questiondiv.hide();
  instructions1: ->
    $('#instructions').text("You're now in the lobby. You'll need to wait until #{gameSettings.maxPlayers} join the game until you can start.")
  init: ->
    do @initQuestions
    do @initBindings

