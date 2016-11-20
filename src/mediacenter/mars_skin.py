"""
class Hurr(Item):
    def __init__(self, parent):
        Item.__init__(self, parent, skin_type='hurr')
        self.name = _('Hurr!')        reading = 1

        
    def actions(self):
        return [(self.show_hurr, 'hurr')]

    def show_hurr(self, arg=None, menuw=None):
        HurrHandler(arg, menuw)
 
class HurrHandler:
    def __init__(self, arg=None, menuw=None):
        self.arg = arg;
        self.menuw = menuw;
        
        #rc.app(self)

        skin.draw('hurr', self)

    #def eventhandler(self, event, menuw=None):
    #    return False

class HurrSkin(skin.Area):
    def __init__(self):
        skin.Area.__init__(self, 'content')

    def update_content(self):
        self.content  = self.calc_geometry(self.layout.content,  copy_object=True)
        stest = readFile()
        self.write_text(stest,
                        skin.get_font('medium0'),
                        self.content,
                        x = 250,
                        y = 200)


skin.register('hurr', ('screen', 'subtitle', 'title', 'plugin', HurrSkin()))

"""