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
        do comm.ready
      else
        console.log input.attr('data-status')
        input.attr('data-status', "ready")
        do comm.unready
    $('#playerAnswer').keydown (event) ->
      if(event.which==13)
        do event.preventDefault
        do comm.answer
        $('#playerAnswer').val('')
  uiInit: ->
    $('#inputReady').attr("disabled", true)

