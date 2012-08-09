window.ui =
  initBindings: ->
    console.log 'bindings'
    $('#buttonConnect').click -> do comm.connect
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




  initQuestions: ->
    $("#questionsWrapper").html('')
    for i in [0..5]
      questiondiv = $('#questionTemplate').clone()
      questiondiv.attr("id", "question#{i}")
      questiondiv.appendTo("#questionsWrapper")
      questiondiv.hide();
  init: ->
    do @initQuestions
    do @initBindings

