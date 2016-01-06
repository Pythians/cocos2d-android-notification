
local MainScene = class("MainScene", cc.load("mvc").ViewBase)
local platformTools=require("app.components.platform.PlatformHelper")


local function getLocalNotificationNum()
    local num = platformTools.getLocalNotificationNum()
    printf("%d",num)
end

local function pushLocalNotification()
    local push={
        key="soldierInfo",
        time=10,
        action="立刻征战!",
        content="军队训练完毕,准备开战!",
        title="最新军情",
        sound="", --默认声音
        badgeNum=1,
    }
    platformTools.pushLocalNotification(push)
end

local function cancelNotification()
    local key = "soldierInfo"
    print(platformTools.cancelNotification(key))
end

local function cleanAllLocalNotifications()
    platformTools.cleanAllLocalNotifications()
end

local function getLocalNotification()
    dump(platformTools.getLocalNotification())
end

function MainScene:onCreate()
    -- add background image
    cc.LayerColor:create(cc.c4b(145,0,125,205))
        :move(display.left_bottom)
        :addTo(self)

    local menu = cc.Menu:create()
    menu:move(display.left_bottom)
    self:addChild(menu)

    local menuitem = cc.MenuItemFont:create('Add a notice')
    menuitem:move(display.width / 4, display.height * 3 / 4)
    menuitem:setFontSizeObj(64)
    menuitem:onClicked(pushLocalNotification)

    local menuclear = cc.MenuItemFont:create('clear a notice')
    menuclear:move(display.width / 4, display.cy)
    menuclear:setFontSizeObj(64)
    menuclear:onClicked(cancelNotification)

    local clearall = cc.MenuItemFont:create('clear all notice')
    clearall:move(display.width / 4, display.cy / 2)
    clearall:setFontSizeObj(64)
    clearall:onClicked(cleanAllLocalNotifications)

    local getall = cc.MenuItemFont:create('get all notice')
    getall:move(display.width * 3 / 4, display.height * 3 / 4)
    getall:setFontSizeObj(64)
    getall:onClicked(getLocalNotification)

    local getnum = cc.MenuItemFont:create('get notice num')
    getnum:move(display.width * 3 / 4, display.cy)
    getnum:setFontSizeObj(64)
    getnum:onClicked(getLocalNotificationNum)

    menu:addChild(menuitem)
    menu:addChild(menuclear)
    menu:addChild(clearall)
    menu:addChild(getall)
    menu:addChild(getnum)

end

return MainScene
