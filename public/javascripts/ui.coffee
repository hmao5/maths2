window.bindings = ->
  console.log 'bindings'
  $('#buttonConnect').click -> do comm.connect
  $('#inputReady').click ->
    input = $('#inputReady')
    if input.is(':checked') 
      input.removeClass('unready')
      input.addClass('ready')
      do comm.ready
    else
      input.addClass('unready')
      input.removeClass('ready')
      do comm.unready

window.uiInit = ->
  $('#inputReady').attr("disabled", true)

