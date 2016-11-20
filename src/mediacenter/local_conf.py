import os

CONFIG_VERSION = 5.15

plugin.activate('mars.mars', level=1)
#plugin.activate('mars.mars_controldaemon', level=1)

#plugin.activate('weather', level=1)
plugin.activate('idlebar', level=1)
plugin.activate('idlebar.volume', level=1)
#plugin.activate('audio.fxmms', level=1)
#plugin.activate('audio.detachbar', level=1)

#plugin.activate('headlines', level=1)

plugin.remove('tv')
#plugin.remove('audio')
#plugin.remove('video')
#plugin.remove('image')

#DEBUG = 1
#CHILDAPP_DEBUG = 1

START_FULLSCREEN_X  = 0

MPLAYER_VERSION = 1.0
MPLAYER_VO_DEV = 'sdl'
MPLAYER_AO_DEV = 'alsa'
AUDIO_DEVICE = '/dev/dsp0'
DEV_MIXER = '/dev/mixer'
CONTROL_ALL_AUDIO = 1
MAJOR_AUDIO_CTRL = 'PCM'

USERHOME = os.path.expanduser("~")

AUDIO_ITEMS = [ ('Music', USERHOME) ]
VIDEO_ITEMS = [ ('Movies', USERHOME) ]
IMAGE_ITEMS = [ ('Images', USERHOME) ]

ROM_DRIVES = []
