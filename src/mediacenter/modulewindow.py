import config
import event as em
import osd

from gui.GUIObject      import *
from gui.Border         import *
from gui.Label          import *
from gui.AlertBox       import *
from gui.OptionBox      import *
from gui.LetterBoxGroup import *
from gui.Progressbar import *
from gui.ConfirmBox     import ConfirmBox

DEBUG = config.DEBUG
TRUE = 1
FALSE = 0

class ModuleSetup(PopupBox):
    """
    prog      the program to record
    left      x coordinate. Integer
    top       y coordinate. Integer
    width     Integer
    height    Integer
    context   Context in which the object is instanciated
    """
    
    def __init__(self, module = None, parent=None, left=None, top=None, width=500, height=350):
#        if context:
#            self.context = context
#        else:
#           context = 'module'

        self.moduleParameters = []
        
        self.numSets = []
        self.boolSets = []
        self.listSets = []
        
        PopupBox.__init__(self, text=_('Module Settings'), x=left, y=top, width=width, 
                          height=height)
        try:
            #TODO: DESCRIBE aufrufen, parameter abfragen
            c = get_controller()
            c.send_connection.send("" + module + " DESCRIBE")
            self.moduleParameters = marshall.unmarshall_list(c.send_connection.receive())
            
            #split up the params into their types
            """
            APPEND THE PARAMETERS!!
            """
            for param in self.moduleParameters:
                if param.type == 'NUMERIC':
                    self.numSets.append(NumericSetting())
                elif param.type == 'BOOL':
                    self.boolSets.append(BooleanSetting())
                elif param.type == 'LIST':
                    self.listSets.append(ListSetting())
            
        except:
            print "No Connection to controller"
            
        self.v_spacing = 15
        self.h_margin = 20
        self.v_margin = 20

        self.internal_h_align = Align.LEFT

        self.cancel = Button(_('CANCEL'))

        if not self.left: self.left = self.osd.width/2 - self.width/2
        if not self.top: self.top = self.osd.height/2 - self.height/2
            
        if module != None:
            self.accept = Button(_('Accept'))
            self.add_child(self.accept)
        else:
            self.empty = Label(_("No Module Specified!"), self, Align.LEFT)
            self.add_child(self.empty)

        self.add_child(self.cancel)
        for child in self.content.get_children():
            pass
        
        if len(self.numSets) > 0:
            self.numSets[0].setActive()
        elif len(self.boolSets) > 0:
            self.boolSets[0].setActive()
        elif len(self.listSets) > 0:
            self.listSets[0].setActive()
        else:
            self.cancel.toggle_selected()            

    def eventhandler(self, event, menuw=None):            
        if event == (em.INPUT_UP):
            if self.get_selected_child() == self.cancel:
                try:
                    self.save.toggle_selected()
                except:
                    pass      
                
            elif self.get_selected_child() == self.save:
                if len(self.listSets) > 0:
                    self.listSets[len(self.listSets) - 1].setActive()
                elif len(self.boolSets) > 0:
                    self.boolSets[len(self.boolSets) - 1].setActive()
                elif len(self.numSets) > 0:
                    self.numSets[len(self.numSets) - 1].setActive()
                else:
                    self.cancel.toggle_selected()            
            else:    
                self.get_selected_child().cont.setPrevActive()

            self.draw()
        elif event == (em.INPUT_DOWN):
            if self.get_selected_child() == self.cancel:
                pass
            elif self.get_selected_child() == self.save:
                self.cancel.toggle_selected()

            self.get_selected_child().cont.setPrevActive()
            self.draw() 
        elif event in (em.INPUT_LEFT, em.INPUT_RIGHT):
            self.get_selected_child().eventhandler(event, menuw)
            self.draw()
        elif event == (em.INPUT_ENTER):
            if self.get_selected_child() == self.cancel:
                self.destroy()
                return True
            elif self.get_selected_child() == self.accept:
                """
                WRITE BACK SETTINGS HERE
                """
                for setting in self.numSets:
                    pass
                for setting in self.boolSets:
                    pass
                for setting in self.listSets:
                    pass
            else:
                self.get_selected_child().toggle_box()
                self.draw()
        elif event == em.INPUT_EXIT:
            self.destroy()
            return True
        elif event in (em.MENU_PAGEDOWN, em.MENU_PAGEUP):
            return True
        else:
            return self.parent.eventhandler(event)

class BooleanSetting:
    def __init__(self, parentBox = None, iName = "Boolean"):
        self.name = iName
        self.parent = parentBox
        
        self.title = Label(_(self.name), self.parent, Align.LEFT)
        self.parent.add_child(self.title)
        #OPTIONBOX
        self.selector = OptBox('BOOL', self)
        self.selector.h_align = Align.NONE
      
        self.selector.add_item(text = "Selected", value = "True")
        self.selector.add_item(text = "Not selected", value = "False")
    
        self.selector.toggle_selected_index(0)
        self.parent.add_child(self.selector)

    def setActive(self):
        self.selector.toggle_selected()

    def setNextActive(self):
        index = self.parent.boolSets.index(self)
        length = len(self.parent.boolSets)
        if index < (length - 1):
            self.parent.boolSets[index + 1].setActive()
        elif index == (length - 1):
            self.parent.listSets[0].setActive()            
        
    def setPrevActive(self):
        index = self.parent.boolSets.index(self)
        length = len(self.parent.boolSets)
        if index > 0:
            self.parent.boolSets[index - 1].setActive()
        elif index == (0):
            self.parent.numSets[(len(self.parent.numSets - 1))].setActive()
        
class NumericSetting:
    def __init__(self, parentBox = None, iName = "Numeric", min = 1, max = 100):
        self.name = iName
        self.parent = parentBox
        self.max = max
        self.min = min
        self.selected = 0
        
        self.title = Label(_(self.name), self.parent, Align.LEFT)
        self.parent.add_child(self.title)
        
        self.listing = OptBox('NUMERIC', self)
        self.listing.h_align = Align.NONE
        #self.listing.add_item(text=_('ANY'), value='ANY')
        i = 0 
        while i < max:
            self.listing.add_item(text = i, value = i)
            i = i + 1
    
        self.listing.toggle_selected_index(self.selected)
        # setting the OptionBox's label to the current
        # value. It should be done by OptionBox when drawing
        #self.listing.change_item(None)
        self.parent.add_child(self.listing)    
        
        #self.slider = Progressbar(full = max)
        #self.parent.add_child(self.slider)
        
    def up(self):
        if self.selected != max:
            self.selected = self.selected + 1
            self.listing.toggle_selected_index(self.selected)

    def down(self):
        if self.selected != 0:
            self.selected = self.selected - 1
            self.listing.toggle_selected_index(self.selected)
    
    def setActive(self):
        self.listing.toggle_selected()

    def setNextActive(self):
        index = self.parent.numSets.index(self)
        length = len(self.parent.numSets)
        if index < (length - 1):
            self.parent.numSets[index + 1].setActive()
        elif index == (length - 1):
            self.parent.boolSets[0].setActive()
        
    def setPrevActive(self):
        index = self.parent.numSets.index(self)
        length = len(self.parent.numSets)
        if index > 0:
            self.parent.numSets[index - 1].setActive()
        elif index == (0):
            self.parent.listSets[(len(self.parent.listSets - 1))].setActive()


class ListSetting:
    def __init__(self, parentBox = None, iName = "List", iList = []):
        self.list = iList
        self.name = iName
        self.parent = parentBox
        
        self.title = Label(_(self.name), self.parent, Align.LEFT)
        self.parent.add_child(self.title)
        #OPTIONBOX
        self.listing = OptBox('LIST', self)
        self.listing.h_align = Align.NONE
        #self.listing.add_item(text=_('ANY'), value='ANY')
      
        for item in self.list:
            self.listing.add_item(text = item, value = "")
    
        self.listing.toggle_selected_index(0)
        # setting the OptionBox's label to the current
        # value. It should be done by OptionBox when drawing
        #self.listing.change_item(None)
        self.parent.add_child(self.listing)

    def setActive(self):
        self.listing.toggle_selected()

    def setNextActive(self):
        index = self.parent.listSets.index(self)
        length = len(self.parent.listSets)
        if index < (length - 1):
            self.parent.listSets[index + 1].setActive()
        elif index == (length - 1):
            self.parent.numSets[0].setActive()
        
    def setPrevActive(self):
        index = self.parent.listSets.index(self)
        length = len(self.parent.listSets)
        if index > 0:
            self.parent.listSets[index - 1].setActive()
        elif index == (0):
            self.parent.boolSets[(len(self.parent.boolSets - 1))].setActive()

        
class OptBox(OptionBox):
    def __init__(self, type = None, cont = None):
        self.type = type
        self.cont = cont
        OptionBox.__init__('ANY')
        
