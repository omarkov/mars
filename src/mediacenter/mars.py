import sys
import traceback
import copy

import config
import menu
import skin
import osd
import rc
import directory
import re

import plugin
import plugins.rom_drives

import audio.player

from event import *
from item import Item

from plugins.shutdown import shutdown

from gui.OptionBox import *
from gui.Button import *

from video.videoitem import VideoItem
from audio.audioitem import AudioItem
from image.imageitem import ImageItem

from playlist import Playlist

from gui.PopupBox import PopupBox
from gui.Window import *
from gui.GUIObject import *
from gui.Border import *
from gui.Label import *
from gui.LetterBoxGroup import *
from gui.ConfirmBox import ConfirmBox

from net import controller
from net import parameter
from net import command
from net import marshall

import mars_controldaemon

#get the singletons so we get skin info and access the osd
skin = skin.get_singleton()
osd  = osd.get_singleton()
rc = rc.get_singleton()

AUDIO = 0
IMG = 1
VIDEO = 2

#CUSTOM FUNCTIONS
def getAvailableUsers():
    try:
        c = controller.get_controller()
        c.send_connection.send("CALL system getUsersInRoom")
        return marshall.unmarshall_list("".join(c.send_connection.receive().split('\n')))
    except:
        pass

#def getModules():
#    try:
#        c = controller.get_controller()
#        c.send_connection.send("CALL system getProfilesMC")
#        return marshall.unmarshall_list("".join(c.send_connection.receive().split('\n')))
#    except:
#        pass

def selectUser(username):
    
    print "Calling Userselection for user " + username
    try:
        c = controller.get_controller()
        c.send_connection.send("CALL system changeUserMC<<PARAMSEP>>\n" + username)
        c.send_connection.receive()
    except:
        pass

def setRepeat(repeat):
        global repeat_all
        global orig_repeat_all
        if repeat == False:
            rc.post_event(Event(OSD_MESSAGE, _( 'Repeat Profile' )))
        if repeat == True:
            rc.post_event(Event(OSD_MESSAGE, _( 'Repeat All' )))

        repeat_all = repeat
        orig_repeat_all = repeat
        return True

def enterRoom():
    print "EnterRoom"
    c = controller.get_controller()
    c.send_connection.send("CALL system getProfileNamesForActiveUser")
    tempList = marshall.unmarshall_list("".join(c.send_connection.receive().split('\n')))
    global profilelist
    profilelist = []

    for item in tempList:
       profilelist.append(ProfileType(item))

    return True

def exitRoom():
    print "ExitRoom"
    return True

def before_update():
    print "before update"
    rc.post_event(Event('stop_profile'))
    global repeat_all
    global orig_repeat_all
    repeat_all = orig_repeat_all
    return True

def update_handler():
    print "Update"
    
    global called_for_next
    called_for_next = False
    global audiolist
    global videolist
    global imagelist
    global audioPlayList
    global videoPlayList
    global imagePlayList
    global running
    global video
    audioItems = []
    videoItems = []
    imageItems = []
    
    for item in audiolist.get():
        print ">>>>ITEM audio: " + item.strip()
        item = item.strip()
        video = False
        try:
            audioItems.append(AudioItem(item, None))
        except:
            print "ERROR AUDIOITEMS APPEND"
            pass
    for item in videolist.get():
        video = True
        item = item.strip()
        print ">>>>ITEM video : " + item
        try:
            audioItems.append(VideoItem(item, None))
        except:
            print "ERROR VIDEOITEMS APPEND"
            pass
    for item in imagelist.get():
        item = item.strip()
        print ">>>>ITEM image : " + item
        imageItems.append(item)

    audioPlayList = Playlist("Musik", audioItems)
    #videoPlayList = Playlist("Videos",videoItems)
    print str(imagelist.get())
    imagePlayList = ImagePlaylist(None, "Bilder", imageItems)
    rc.post_event(Event('start_profile'))
    # self.currentProfile.startProfile()

    return True

def getNextProfile():
    global called_for_next
    global repeat_all
    if called_for_next == False and repeat_all == True:
        called_for_next = True
        
        print "Calling for next profile"
        try:
            c = controller.get_controller()
            c.send_connection.send("CALL system getNextProfileMC")
            c.send_connection.receive()
        except:
            pass
    else:
        print "DOUBLECALL"

class PluginInterface(plugin.MainMenuPlugin):
    def __init__(self):
        global profilelist
        global audiolist
        global videolist
        global imagelist
        global audioPlayList
        global videoPlayList
        global imagePlayList
        global repeat_all
        global running
        running = False
        repeat_all = False
        profilelist = []
        audiolist = []
        videolist = []
        imagelist = []
        audioPlayList = []
        videoPlayList = []
        imagePlayList = []
        
        """
        #making testlists
        
        imageItems = []
        imageItems.append("/home/mars/mars_fs/users/admin/personal/images/dsert.jpg")
        imageItems.append("/home/mars/mars_fs/users/admin/personal/images/mars-express-volcanoes.jpg")
        imageItems.append("/home/mars/mars_fs/users/admin/personal/images/southcan.jpg")
        imageItems.append("/home/mars/mars_fs/users/admin/personal/images/vlpan22.jpg")
        imagePlayList = ImagePlaylist(None, "Bilder", imageItems)
        audioItems = []
        audioItems.append(AudioItem("/home/mars/mars_fs/users/admin/personal/audio/send.mp3", None))
        audioPlayList = Playlist("Musik", audioItems)
        self.testprofile = ProfileType("test")
        profilelist.append(self.testprofile)
	"""
        c = controller.get_controller()

        audiolist = parameter.ListParameter("AudioPlaylist", "rw", 0, "audio")
        c.register_parameter(audiolist)
        videolist = parameter.ListParameter("VideoPlaylist", "rw", 0, "video")
        c.register_parameter(videolist)
        imagelist = parameter.ListParameter("ImagePlaylist", "rw", 0, "image")
        c.register_parameter(imagelist)


        c.register_command(command.Command("enterRoom", "BOOL", [], [], enterRoom))
        c.register_command(command.Command("exitRoom", "BOOL", [], [], exitRoom))
        c.register_command(command.Command("update", "BOOL", [], [], update_handler))
        c.register_command(command.Command("beforeUpdate", "BOOL", [], [], before_update))
        c.register_command(command.Command("setRepeat", "BOOL", ["BOOL"], ["repeatAll"], setRepeat))
        mars_controldaemon.ControlDaemon(c)

        self.currentProfile = ProfileType("CURRENT")
    
        c.signon()
#        global mars_plugin
#        mars_plugin = self
#        except:
#            print "Failed getting controller instance - check connection to server."
        
        self.profileMenu = Profiles(self)
        plugin.MainMenuPlugin.__init__(self)


        rc.post_event(Event('ready'))
        
    def eventhandler(self, event, menuw = None):
        global running
        global repeat_all
        print "plugin Class " + str(event)

        if event == 'start_profile':
            running = True
            self.currentProfile.startProfile()
        elif event == 'stop_profile':
            if running:
                self.currentProfile.stopProfile()
        elif event == 'shutdown':
			shutdown(exit=True)
        elif event == 'escape_pressed':
            repeat_all = False
            if running:
                self.currentProfile.stopProfile()
        elif event == 'playlist_end':
            if repeat_all == True:
                getNextProfile()
        elif event == VIDEO_END and repeat_all == True:
            getNextProfile()
#            menuw.show()
            menuw.back_one_menu()            
        elif event == 'ready':
            global MenuW
            MenuW = menuw
            c = controller.get_controller()
            c.send_connection.send("CALL system readyMC")
            marshall.unmarshall_bool(c.send_connection.receive())
 
    def items(self, parent):
        return [ self.profileMenu,
                 Tagusermenu(parent)]

class Profiles(Item):
    def __init__(self, plugin):
        Item.__init__(self)
#        self.parent = parent
        self.name = _('Profiles')
        self.image = None
        self.plugin = plugin
    
    def actions(self):
        return [(self.generate_menu_items, 'ProfilesSub')]

    def generate_menu_items(self, arg=None, menuw=None):
        sub_menus = []
        global profilelist
                
        for profile in profilelist:
            mi = profile
            mi.description = profile.getDesc()
            sub_menus.append(mi)
        
        sub_menu = menu.Menu(_("Profiles"), sub_menus)

        menuw.pushmenu(sub_menu)
        menuw.refresh()

                
class Settings(Item):
    def __init__(self, parent):
        Item.__init__(self,parent)
        self.parent = parent
        self.name = _('Settings')
        self.image = None
        
    def actions(self):
        return[(self.generate_menu_items, 'Settings Menu')]
        
    def generate_menu_items(self, arg = None, menuw = None):        
        modules = getModules()
        sub_menus = []
        for module in modules:
            #TODO: moduleigenschaften übernehmen
            mi = menu.MenuItem("Modulename", self.openModuleWindow("ModulName"))
            sub_menus.append(mi)

        sub_menu = menu.Menu(_("Settings"), sub_menus)

    def openModuleWindow(self, module = None):
        self.win = modulewindow.ModuleSetup(module)
        self.win.show()
        
class ChangeUser(Item):
    def __init__(self, parent):
        Item.__init__(self,parent)
        self.parent = parent
        self.name = _('Change User')
        self.image = None

    def actions(self):
        return [(self.generate_menu_items, 'ChangeUsersub')]
    
    def generate_menu_items(self, arg = None, menuw = None):
        sub_menus = []
        mi = Tagusermenu(self.parent)
        sub_menus.append(mi)
        
        mi = PDAUsermenu(self.parent)
        sub_menus.append(mi)
        
        sub_menu = menu.Menu(_("Change User") , sub_menus)

        menuw.pushmenu(sub_menu)
        menuw.refresh()
    
class PDAUsermenu(Item):
    def __init__(self, parent):
        Item.__init__(self,parent)
        self.parent = parent
        self.name = _("Via PDA")
        self.image = None

    def actions(self):
        return [(self.getPDAUser, 'PDAUsersub')]
                
    def getPDAUser(self, arg = None, menuw = None):
        pdapopup = PopupBox(text=_("Waiting for PDA..."))
        pdapopup.show()
            
class Tagusermenu(Item):
    def __init__(self, parent):
        Item.__init__(self, parent)
        self.parent = parent
        self.name = _("Change User")
        self.image = None

    def actions(self):
        return [(self.generate_menu_items, 'Tagusersub')]
                
    def generate_menu_items(self, arg = None, menuw = None):
        users = getAvailableUsers()
        usermenu = []
        for user in users:
            ui = UserType(user)
            usermenu.append(ui)
        
        user_menu = menu.Menu(_("Change User"), usermenu)

        menuw.pushmenu(user_menu)
        menuw.refresh()

class UserType(Item):
    def __init__(self, name):
        Item.__init__(self)
        self.name = name
       
    def actions(self):
        return[(self.selectUserItem, 'Select')]

    def selectUserItem(self, arg = None, menuw = None):
        selectUser(self.name)
 

class Viapda(Item):
    def __init__(self, parent):
        Item.__init__(self,parent)
        self.parent = parent
        self.name = _('Settings')
        self.image = None
        
#Type class for our profiles, holding the data needed to manage it
#provides profile convenient functions
class ProfileType(Item):
    def __init__(self, iName = "My Profile", iDesc = "No Description Available"):
        Item.__init__(self)
        self.medialist = []
        self.name = iName
        self.desc = iDesc
        self.menuw = None
        
    def actions(self):
        return [(self.selectProfile, 'Play')]

    def selectProfile(self, arg = None, menuw = None):
        global MenuW
        if menuw == None: menuw = MenuW
        
        c = controller.get_controller()
        c.send_connection.send("CALL system setProfileMC<<PARAMSEP>>\n" + self.name)
        marshall.unmarshall_bool(c.send_connection.receive())
            
        #self.startProfile()
    def browse(self, arg = None, menuw = None):
        global MenuW
        global audioPlayList
        self.medialist = audioPlayList.playlist
        if menuw == None: menuw = MenuW
        items = []
        for media in self.medialist:
            items.append(media)
            
        mediamenu = menu.Menu(self.name, items)
        menuw.pushmenu(mediamenu)
        return mediamenu
        
    def startProfile(self, menuw=None):
        global MenuW
        if menuw == None: menuw = MenuW
        # MenuW = menuw
        global audioPlayList
        global imagePlayList
        global running
        global orig_repeat_all
        repeat_all = orig_repeat_all
        
        running = True
        print "##STARTING PROFILE##"
        
        if len(audioPlayList.playlist) > 0:
            audioPlayList.play(None, self.menuw)
            
            if video == False:
                print "AUDIO"
                command = rc.key_event_mapper('DISPLAY')
                if command:
                    rc.post_event(command)
            else:
                print "VIDEO"
            
            if repeat_all == True:
                audioPlayList.repeat = False
            else:
                audioPlayList.repeat = True
        
        # menuw.goto_main_menu()
        
        if imagePlayList.hasItems():
            imagePlayList.browse(None, self.menuw)
            print "IMAGE"
            command = rc.key_event_mapper('SELECT')
            if command:
                rc.post_event(command)
        else:
            self.browse()

    def stopProfile(self, menuw=None):
        global MenuW
        if menuw == None: menuw = MenuW

        global audioPlayList
        global imagePlayList
        global running
        global repeat_all
        running = False
        print len(audioPlayList.playlist)
        if len(audioPlayList.playlist) > 0:
            print "###########STOPPING#########"
            print str(audioPlayList.playlist)
            audioPlayList.repeat = False
            audioPlayList.stop()

        if imagePlayList.hasItems():
            command = rc.key_event_mapper('DISPLAY')
            if command:
                rc.post_event(command)
        menuw.show()
        menuw.back_one_menu()
        #menuw.goto_main_menu()
        
    def eventhandler(self, event, menuw=None):
        """
        Handle specific events
        """
        global audioPlayList
        global videoPlayList
        global imagePlayList
        global repeat_all
        global MenuW
        print event
        if not menuw: menuw = MenuW

        if event == USER_END or event == STOP:
            running = False
            repeat_all = False
            
            self.stopProfile(menuw)
            audioPlayList.repeat = False
            audioPlayList.stop()
            #menuw.show()
            #menuw.back_one_menu()
            #self.stopProfile(menuw)
        elif event == 'start_profile':
            self.startProfile(menuw)
        elif event == 'stop_profile':
            self.stopProfile(menuw)
        elif event == MENU_SELECT:
            pass
#        else:
#            imagePlayList.eventhandler(event, menuw)
            
    def getName(self):
        return self.name
        
    def getDesc(self):
        return self.desc            

class ImagePlaylist(Item):
    def __init__(self, parent, iName = "Unnamed", iImagelist = [], iIntervall = 3):
        self.parent = parent
        self.name = iName
        self.imagelist = iImagelist
        self.intervall = iIntervall
        self.alreadybuilt = False
        self.currentImage = 0

        Item.__init__(self, parent)
        
    def appendImage(self, iImageItem):
        self.imagelist.append(iImageitem)
        
    def browse(self, arg = None, menuw = None):
        global MenuW
        if menuw == None: menuw = MenuW
        self.build()
        items = []
        for image in self.imagelist:
            items.append(image)
            
        imagemenu = menu.Menu(self.name, items)
        # 
        menuw.pushmenu(imagemenu)
        return imagemenu
        
    def build(self):
        if self.alreadybuilt: return

        mylist = self.imagelist
        self.imagelist = []
        
        for image in mylist:
            self.imagelist.append(ImagePlayitem(self, image, self.intervall))
            self.alreadybuilt = True
            
    def actions(self):
        #the actions for this class
        self.build()
#        items = [( self.browse, _('Browse Images') ), (self.view, _('View'))]
        items = [(self.view, _('View'))]
        
        return items
        
#    def startplayback(self):
#        if self.audiolist == []:
#            self.playback = False
#       else:
#            self.playback = True
#            self.audiolist.play()
#            gui = audio.player.get()
#            gui.hide()
        
#    def playnext(self):
#        if self.audiolist == []:
#            return
#        else:    
#            self.currentSong = (self.currentSong + 1) % len(self.audiolist)
#            self.audiolist[self.currentSong].play()
            
#         gui = audio.player.get()
#         gui.hide()
    
    def view(self, arg = None, menuw = None):        
        if not self.menuw: 
            self.menuw = menuw
        
        if len(self.imagelist) == 0:
            print String(_('Empty Imagelist'))
            return false
        try:
            self.imagelist[self.currentImage].view()
        except:
            print "Unknown Image"
            self.currentImage = (self.currentImage + 1) % len(self.imagelist)
            self.imagelist[self.currentImage].view()


    def hasItems(self):
        if len(self.imagelist) == 0:
            return False
        else:
            return True
        
    def eventhandler(self, event, menuw=None):
        """
        Handle imagelist specific events
        """
        global audioPlayList
        global videoPlayList
        global repeat_all
        global MenuW
        global running
        print 'ImageList ' + str(event)
        if not menuw: menuw = MenuW

        if event == USER_END or event == STOP:
            running = False
            audioPlayList.repeat = False
            repeat_all = False
            audioPlayList.stop()
            menuw.show()
            menuw.back_one_menu()
        elif event == 'escape_pressed':
            repeat_all = False
            if running:
                self.currentProfile.stopProfile()
        elif event == PLAY_END or event == PLAYLIST_NEXT:
            print repeat_all
            if imagePlayList.currentImage == len(imagePlayList.imagelist) - 1 and len(audioPlayList.playlist) == 0 and  orig_repeat_all == True :
                getNextProfile()
                menuw.show()
                menuw.back_one_menu()
            else:
                if len(imagePlayList.imagelist) != 0:
                    imagePlayList.currentImage = (imagePlayList.currentImage + 1) % len(imagePlayList.imagelist)
                    imagePlayList.imagelist[imagePlayList.currentImage].view(None, menuw)

        #elif event == MENU_SELECT:
            #self.currentImage = menuw.menu_items.index(menu.selected)
                    
class ImagePlayitem(ImageItem):
    def __init__(self, parent, iLocation = "", iDuration = 5):
        self.parent = parent
        
        filename = re.split('/', iLocation)
        filename = filename[len(filename) - 1]
        filename = (re.split('[\.]', filename))
        format = filename[len(filename) - 1]
        
        self.location = iLocation
        self.name = filename[0]
        self.duration = iDuration
        
        ImageItem.__init__(self, iLocation, parent, self.name, self.duration)
        
    def view(self, arg=None, menuw=None):            
        ImageItem.view(self, arg, menuw)
        
    def getLocation(self):
        return self.location
