window.bindings = ->
  console.log 'bindings'
  $('#buttonConnect').click -> do comm.connect
