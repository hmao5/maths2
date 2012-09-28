window.utils = 
  playerIDtoHSLAstring:  (playerId, numPlayers) ->
    hue = (playerId*360/numPlayers) % 360
    "hsla(#{hue}, 100%, 50%, 1)"
