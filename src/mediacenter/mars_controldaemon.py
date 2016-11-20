import sys
import rc
import osd
import plugin
from event import *
from net import *

rc = rc.get_singleton()

class ControlDaemon(plugin.DaemonPlugin):
    # Functions
    def __init__(self,c):
        c.register_command(command.Command("osd", "BOOL", ["STRING"], ["message"], self.control_osd))
        c.register_command(command.Command("remoteUp", "BOOL", [], [], self.control_up))
        c.register_command(command.Command("remoteDown", "BOOL", [], [], self.control_down))
        c.register_command(command.Command("remoteLeft", "BOOL", [], [], self.control_left))
        c.register_command(command.Command("remoteRight", "BOOL", [], [], self.control_right))
        c.register_command(command.Command("remoteOK", "BOOL", [], [], self.control_enter))
        c.register_command(command.Command("remoteStopProfile", "BOOL", [], [], self.control_StopProfile))
        c.register_command(command.Command("remoteESC", "BOOL", [], [], self.control_esc))
        c.register_command(command.Command("volumeUp", "BOOL", [], [], self.control_VolumeUp))
        c.register_command(command.Command("volumeDown", "BOOL", [], [], self.control_VolumeDown))
        c.register_command(command.Command("toggleMute", "BOOL", [], [], self.control_ToggleMute))
        c.register_command(command.Command("shutdown", "BOOL", [], [], self.control_shutdown))
        plugin.DaemonPlugin.__init__(self)
    
    #Control funtions
    def control_osd(self,msg):
        rc.post_event(Event(OSD_MESSAGE, _( msg )))
        return True
 
    def control_up(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote Up' )))
        command = rc.key_event_mapper('UP')
        if command:
            rc.post_event(command)
        return True
            
    def control_down(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote Down' )))
        command = rc.key_event_mapper('DOWN')
        if command:
            rc.post_event(command)
        return True
    
    def control_left(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote Left' )))
        command = rc.key_event_mapper('LEFT')
        if command:
            rc.post_event(command)
        return True
    
    def control_right(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote Right' )))
        command = rc.key_event_mapper('RIGHT')
        if command:
            rc.post_event(command)
        return True
    
    def control_esc(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote ESC' )))
        command = rc.key_event_mapper('EXIT')
        if command:
            rc.post_event(command)
        rc.post_event('escape_pressed')
        return True

    def control_shutdown(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote SHUTDOWN' )))
        rc.post_event(Event('shutdown'))
        return True

    def control_enter(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote OK' )))
        command = rc.key_event_mapper('SELECT')
        if command:
            rc.post_event(command)
        return True
    
    def control_StopProfile(self):
        rc.post_event(Event('stop_profile'))
        return True
    

    def control_VolumeUp(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote Volume Up' )))
        command = rc.key_event_mapper('VOL+')
        if command:
            rc.post_event(command)

        return True

    def control_VolumeDown(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote Volume Down' )))
        command = rc.key_event_mapper('VOL-')
        if command:
            rc.post_event(command)
        return True

    def control_ToggleMute(self):
        rc.post_event(Event(OSD_MESSAGE, _( 'Remote Volume Mute Toggle' )))
        command = rc.key_event_mapper(key.K_RETURN)
        if command:
            rc.post_event(command)
        return True

    def shutdown(self):
        pass
